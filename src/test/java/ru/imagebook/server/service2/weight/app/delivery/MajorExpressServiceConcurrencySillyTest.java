package ru.imagebook.server.service2.weight.app.delivery;

import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import ru.imagebook.server.service2.app.delivery.MajorExpressService;
import ru.imagebook.server.service2.app.delivery.MajorExpressServiceImpl;

/**
 * @author Sergey Boykov
 */
@ContextConfiguration(classes = MajorExpressServiceConcurrencySillyTest.Config.class)
@Test(groups = {"integration"})
public class MajorExpressServiceConcurrencySillyTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MajorExpressService majorExpressService;

    private AtomicInteger properlyInitCount = new AtomicInteger(0);
    private AtomicInteger count = new AtomicInteger(0);

    private static final int NUM_THREADS = 50;

    @Test(threadPoolSize = NUM_THREADS, invocationCount = NUM_THREADS)
    public void shouldProperlyInitialize() {
        count.incrementAndGet();
        if (!majorExpressService.getCities().isEmpty()) {
            properlyInitCount.incrementAndGet();
        }
    }

    @Test(dependsOnMethods = {"shouldProperlyInitialize"})
    public void checkCount() {
        System.out.println("properlyInitCount=" + properlyInitCount.get());
        System.out.println("count=" + properlyInitCount.get());
        assertTrue(properlyInitCount.get() == NUM_THREADS);
    }

    @Configuration
    @ImportResource("file:src/main/webapp/WEB-INF/spring/config.xml")
    protected static class Config {
        @Bean(initMethod = "init")
        public MajorExpressService majorExpressServiceImpl() {
            return new MajorExpressServiceImpl();
        }
    }
}