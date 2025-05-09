package ru.imagebook.server.service2.admin;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ru.imagebook.client.admin.service.ZoneService;
import ru.imagebook.server.repository2.admin.CountryRepository;
import ru.imagebook.server.repository2.admin.ZoneRepository;
import ru.imagebook.server.service.FileConfig;
import ru.imagebook.server.tools.CountryUtility;
import ru.imagebook.shared.model.Country;
import ru.imagebook.shared.model.Zone;
import ru.imagebook.shared.service.admin.delivery.FileTypeErrorMessage;
import ru.imagebook.shared.service.admin.delivery.ParseErrorMessage;
import ru.imagebook.shared.service.admin.delivery.ProgressParseFileMessage;
import ru.imagebook.shared.service.admin.delivery.ProgressUploadFileMessage;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.client.oo.Encoding;
import ru.minogin.core.server.hibernate.Dehibernate;
import ru.minogin.core.server.push.PushService;
import ru.minogin.oo.server.OOClient;
import ru.minogin.oo.server.spreadsheet.Sheet;
import ru.minogin.oo.server.spreadsheet.SpreadsheetDoc;

public class ZoneServiceImpl implements ZoneService, ZoneServerService{
	private final ZoneRepository repository;
	private final CountryRepository countryRepository;
	private final FileConfig fileConfig;
	private final OOClient ooClient;
	private final PushService pushService;


	@Autowired
	public ZoneServiceImpl(ZoneRepository repository, CountryRepository countryRepository,
			OOClient ooClient, FileConfig fileConfig, PushService pushService){
		this.repository = repository;
		this.countryRepository = countryRepository;
		this.ooClient = ooClient;
		this.fileConfig = fileConfig;
		this.pushService = pushService;
	}

	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public void addZone(Zone zone) {
		repository.saveZone(zone);
	}

	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public void updateZone(Zone modified) {
		Zone zone = repository.getZone(modified.getId());
		zone.setZip(modified.getZip());
		zone.setCountry(modified.getCountry());
		zone.setRegion(modified.getRegion());
		zone.setDistrict(modified.getDistrict());
		zone.setCity(modified.getCity());
	}

	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public void deleteZones(List<Integer> ids) {
		repository.deleteZones(ids);

	}

	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	@Dehibernate
	public List<Zone> loadZones(int offset, int limit) {
		return repository.loadZones(offset, limit);
	}

	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public long getZonesCount() {
		return repository.getZonesCount();
	}

	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	@Dehibernate
	public List<Country> loadCountries() {
		return CountryUtility.PopDefaultUp(countryRepository.loadCountries());
	}

	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public void uploadAndParseFile(MultipartFile multipartFile){
		String fileName = fileConfig.getTempPath() + File.separator + multipartFile.getOriginalFilename();
		pushService.sendToAuthenticatedUser(new ProgressUploadFileMessage(1));
		uploadFile(multipartFile, fileName);
		pushService.sendToAuthenticatedUser(new ProgressUploadFileMessage(100));
		repository.deleteAllZones();
		SpreadsheetDoc doc = ooClient.openSpreadsheetDoc(fileName, Encoding.UTF8);
		try {
			Sheet sheet = doc.getSheet(0);
			int rowsCount = sheet.getRowsCount();
			for (int i = 1; i < rowsCount; i++) {
				try {
					parseZone(sheet, i);
					double percent = (i * 100) / (rowsCount - 1);
					pushService.sendToAuthenticatedUser(new ProgressParseFileMessage(percent));
				} catch (ZoneParseException e){
					pushService.sendToAuthenticatedUser(new ParseErrorMessage(e.getRowId(), e.getFieldId()));
				}
			}
		} catch (NullPointerException e) {
			pushService.sendToAuthenticatedUser(new FileTypeErrorMessage());
			throw e;
		}
	}

	private void uploadFile(MultipartFile file, String fileName) {
		try {
			InputStream is = file.getInputStream();
			OutputStream os = new BufferedOutputStream(new FileOutputStream(fileName));
			IOUtils.copy(is, os);
			is.close();
			os.close();
		}catch (IOException e) {
			Exceptions.rethrow(e);
		}
	}

	private void parseZone(Sheet sheet, int rowIndex) {
		int j = 0;

		String zip = sheet.getText(j++, rowIndex);
		if (zip.isEmpty()) {
			throw new ZoneParseException(rowIndex, j);
		}

		String countryName = sheet.getText(j++, rowIndex);
		if (countryName.isEmpty()) {
			throw new ZoneParseException(rowIndex, j);
		}

		Country country = countryRepository.getCountryByName(countryName);
		if (country == null) {
			country = new Country();
			country.setName(countryName);
			countryRepository.saveCountry(country);
		}

		Zone zone = new Zone();
		zone.setCountry(country);
		zone.setZip(zip);
		//zone.setRegion(sheet.getText(j++, rowIndex));
		zone.setDistrict(sheet.getText(j++, rowIndex));

		String city = sheet.getText(j++, rowIndex);
		if (city.isEmpty()) {
			throw new ZoneParseException(rowIndex, j);
		}

		zone.setCity(city);
		repository.saveZone(zone);
	}

}
