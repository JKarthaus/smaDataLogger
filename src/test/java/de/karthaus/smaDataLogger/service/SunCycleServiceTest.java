package de.karthaus.smaDataLogger.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.karthaus.smaDataLogger.config.SmaDataLoggerConfig;

public class SunCycleServiceTest {

	SunCycleService sunCycleservice;
	SmaDataLoggerConfig smaDataLoggerConfig;

	@Before
	public void setup() {
		smaDataLoggerConfig = new SmaDataLoggerConfig();
		smaDataLoggerConfig.setLatitude("50.933449");
		smaDataLoggerConfig.setLongitude("7.522828");
	}

	@Test
	public void testSunCycleService() {
		sunCycleservice = new SunCycleService(smaDataLoggerConfig);
		sunCycleservice.isSunUp();
	}

}
