package de.karthaus.smaDataLogger.service;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

import de.karthaus.smaDataLogger.config.SmaDataLoggerConfig;

@Component
public class SunCycleService {

	private static final Logger log = LoggerFactory.getLogger(SunCycleService.class);

	private Location location;

	private SunriseSunsetCalculator calculator;

	private Calendar sunrise;
	private Calendar sunset;

	/**
	 * 
	 * @param smaDataLoggerConfig
	 */
	public SunCycleService(SmaDataLoggerConfig smaDataLoggerConfig) {
		location = new Location(smaDataLoggerConfig.getLatitude(), smaDataLoggerConfig.getLongitude());
		calculator = new SunriseSunsetCalculator(location, TimeZone.getDefault());
	}

	/**
	 * 
	 */
	@PostConstruct
	private void calculateCycle() {
		sunrise = calculator.getOfficialSunriseCalendarForDate(Calendar.getInstance());
		sunset = calculator.getOfficialSunsetCalendarForDate(Calendar.getInstance());
		log.info("Calculated sunrise to : {} sunset to {}", DateFormat.getTimeInstance().format(sunrise), DateFormat.getTimeInstance().format(sunset));
	}

	/**
	 * 
	 * @return
	 */
	public boolean isSunUp() {
		if (Calendar.getInstance().after(sunrise) && Calendar.getInstance().before(sunset)) {
			log.debug("Sun is up");
			return true;
		}
		log.debug("Sun is down");
		return false;
	}

	/**
	 * 
	 */
	@Scheduled(cron = "0 0 2 * * *")
	private void cycleSheduler() {
		calculateCycle();
	}
}
