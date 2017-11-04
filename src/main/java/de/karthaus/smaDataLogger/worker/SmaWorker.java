package de.karthaus.smaDataLogger.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.karthaus.smaDataLogger.mqtt.MqttConnector.MqttBrokerConnector;
import de.karthaus.smaDataLogger.service.SmaLoggerService;
import de.karthaus.smaDataLogger.service.SunCycleService;

@Component
public class SmaWorker {

	private static final Logger log = LoggerFactory.getLogger(SmaWorker.class);

	@Autowired
	SmaLoggerService smaLoggerService;

	@Autowired
	SunCycleService sunCycleService;

	MqttBrokerConnector deviceGateway;

	public SmaWorker(MqttBrokerConnector deviceGateway) {
		this.deviceGateway = deviceGateway;
	}

	@Scheduled(fixedDelay = (8 * 1000))
	private void getData() {
		double pac = 0;

		if (sunCycleService.isSunUp()) {
			if (smaLoggerService.isConnected()) {
				pac = smaLoggerService.getPac();
				log.debug("Pac Value:{}", pac);
			} else {
				log.debug("Sma Not Connected Push 0");
			}
		} else {
			log.debug("Sun is down - Push 0");
		}
		deviceGateway.sendToMqtt("" + pac);

	}

	@Scheduled(fixedDelay = (10 * 60 * 1000))
	private void checkConnected() {
		if (!smaLoggerService.isConnected()) {
			log.info("Try to Connect to device...");
			smaLoggerService.connect();
			if (!smaLoggerService.isConnected()) {
				log.info("SMA is not connected.");
			}
		}
	}

}