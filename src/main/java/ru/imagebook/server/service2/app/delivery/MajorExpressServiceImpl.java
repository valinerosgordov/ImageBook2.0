package ru.imagebook.server.service2.app.delivery;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.imagebook.shared.model.app.MajorData;
import ru.minogin.core.server.http.Response;
import ru.minogin.core.server.http.XHttpClient;
import ru.minogin.util.shared.exceptions.Exceptions;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static ru.minogin.util.shared.math.MathUtil.roundMoney;


public class MajorExpressServiceImpl implements MajorExpressService {
    private static final Logger LOG = Logger.getLogger(MajorExpressServiceImpl.class);

    //private static final int TIMEOUT_SEC = 20;
    private static final double COST_MULTIPLIER = 1.2;

    @Autowired
    private DeliveryConfig deliveryConfig;

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    private volatile Map<String, String> cities = new HashMap<String, String>();

    public void init() {
        try {
            readLock.lock();
            if (cities.isEmpty()) {
                loadMajorCities();
            }
        } catch (Exception ex) {
            LOG.error("Failed to load Major cities data, msg:" + ex.getMessage(), ex);
        } finally {
            readLock.unlock();
        }
    }

    private void loadMajorCities() {
        try {
            readLock.unlock();
            writeLock.lock();
            if (!cities.isEmpty()) {
                return;
            }
            LOG.info(String.format("Loading Major cities data [%s, %s, %s]", deliveryConfig.getMajorClientName(),
                    deliveryConfig.getMajorCityName(), deliveryConfig.getMajorWbCost()));

            final Map<String, String> loadedCities = new HashMap<String, String>();

            XHttpClient client = new XHttpClient();
            //client.setTimeoutSec(TIMEOUT_SEC);

            String xml = IOUtils.toString(getClass().getResourceAsStream("city.xml"));
            Response response = client.postXml(deliveryConfig.getMajorCitysUrl(), xml);
            String result = response.getContent();

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            ByteArrayInputStream is = new ByteArrayInputStream(result.getBytes("UTF-8"));
            DefaultHandler defaultHandler = new DefaultHandler() {
                private boolean cityCode;
                private boolean cityRusName;
                private String code;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes)
                        throws SAXException {
                    if (qName.equals("CityCode"))
                        cityCode = true;
                    else if (qName.equals("CityRusName"))
                        cityRusName = true;
                }

                public void characters(char ch[], int start, int length) throws SAXException {
                    if (cityCode) {
                        code = new String(ch, start, length);
                        cityCode = false;
                    }

                    if (cityRusName) {
                        String name = new String(ch, start, length);
                        loadedCities.put(name, code);
                        cityRusName = false;
                    }
                }
            };
            saxParser.parse(is, defaultHandler);
            cities = Collections.unmodifiableMap(loadedCities);
            LOG.info("Loading Major cities data completed, loaded=" + cities.size());
        } catch (Exception ex) {
            Exceptions.rethrow(ex);
        } finally {
            readLock.lock();
            writeLock.unlock();
        }
    }

    @Override
    public Set<String> getCities() {
        try {
            readLock.lock();
            if (cities.isEmpty()) {
                loadMajorCities();
            }
            return cities.keySet();
        } finally {
            readLock.unlock();
        }
    }

    private Map<String, String> getMajorCities() {
        try {
            readLock.lock();
            if (cities.isEmpty()) {
                loadMajorCities();
            }
            return cities;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public String getCityCode(String cityMajorName) {
        return getMajorCities().get(cityMajorName);
    }

    @Override
    public MajorData getCostAndTime(String cityMajorName, int weightG) {
        try {
            XHttpClient client = new XHttpClient();
            //client.setTimeoutSec(TIMEOUT_SEC);

            String xml = IOUtils.toString(getClass().getResourceAsStream("cost.xml"));
            xml = xml.replace("${clientName}", deliveryConfig.getMajorClientName());

            String origCityCode = getCityCode(deliveryConfig.getMajorCityName());
            xml = xml.replace("${origCityCode}", origCityCode);

            String destCityCode = getCityCode(cityMajorName);
            xml = xml.replace("${destCityCode}", destCityCode);
            xml = xml.replace("${weight}", (((double) weightG) / 1000) + "");
            xml = xml.replace("${wbCost}", deliveryConfig.getMajorWbCost() + "");
            Response response = client.postXml(deliveryConfig.getMajorCalcUrl(), xml);
            String result = response.getContent();

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            ByteArrayInputStream is = new ByteArrayInputStream(result.getBytes("UTF-8"));

            final MajorData data = new MajorData();
            DefaultHandler defaultHandler = new DefaultHandler() {
                private boolean fTariff;
                private boolean fDelivtime;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes)
                        throws SAXException {
                    if (qName.equals("fTariff"))
                        fTariff = true;
                    else if (qName.equals("fDelivtime"))
                        fDelivtime = true;
                }

                public void characters(char ch[], int start, int length) throws SAXException {
                    if (fTariff) {
                        Double cost = new Double(new String(ch, start, length));
                        data.setCost(roundMoney(cost * COST_MULTIPLIER));
                        fTariff = false;
                    }

                    if (fDelivtime) {
                        data.setTime(new Integer(new String(ch, start, length)));
                        fDelivtime = false;
                    }
                }
            };
            saxParser.parse(is, defaultHandler);

            return data;
        } catch (Exception ex) {
            LOG.error("Failed to get cost and time by cityName=" + cityMajorName + ", w=" + weightG, ex);
            return Exceptions.rethrow(ex);
        }
    }
}
