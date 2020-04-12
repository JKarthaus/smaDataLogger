package de.karthaus.smaDataLogger.service;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = InfluxService.class)
@RunWith(SpringRunner.class)
public class InfluxServiceTest extends TestCase {

    @Autowired
    InfluxService influxService;

    public void setUp() throws Exception {
        super.setUp();
    }



    @Test
    public void testWriteDatapoint() {
        influxService.WriteDatapoint();
    }
}