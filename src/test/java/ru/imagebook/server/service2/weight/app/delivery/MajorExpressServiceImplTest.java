package ru.imagebook.server.service2.weight.app.delivery;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ru.imagebook.server.service2.app.delivery.MajorExpressService;
import ru.imagebook.server.service2.app.delivery.MajorExpressServiceImpl;
import ru.imagebook.shared.model.app.MajorData;

/**
 * @author alexander.ov
 * @since 2014-09-11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MajorExpressServiceImplTest.Config.class})
public class MajorExpressServiceImplTest {

    @Autowired
    private MajorExpressService majorExpressService;

    @Test
    public void getCityCodeTest() {
        assertThat(majorExpressService.getCityCode("Баскатовка"), is("6335"));
    }

    @Test
    public void getCostAndTimeTest() {
        MajorData data = majorExpressService.getCostAndTime("Белорецк", 500);
        assertThat(data.getCost(), is(2379));
        assertThat(data.getTime(), is(5));

        data = majorExpressService.getCostAndTime("Мурманск", 20000);
        assertThat(data.getCost(), is(4699));
        assertThat(data.getTime(), is(2));

        data = majorExpressService.getCostAndTime("Тверь", 840);
        assertThat(data.getCost(), is(714));
        assertThat(data.getTime(), is(1));
    }

    @Configuration
    @ImportResource("file:src/main/webapp/WEB-INF/spring/config.xml")
    protected static class Config {
        @Bean(initMethod = "init")
        public MajorExpressService majorExpressService() {
            return new MajorExpressServiceImpl();
        }
    }
}
