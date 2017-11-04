package de.karthaus.smaDataLogger.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import de.karthaus.smaDataLogger.mqtt.MqttConnector.MqttBrokerConnector;
import de.karthaus.smaDataLogger.worker.SmaWorker;
import de.michaeldenk.yasdi4j.YasdiChannel;
import de.michaeldenk.yasdi4j.YasdiDevice;
import de.michaeldenk.yasdi4j.YasdiDeviceEvent;
import de.michaeldenk.yasdi4j.YasdiDeviceListener;
import de.michaeldenk.yasdi4j.YasdiDriver;
import de.michaeldenk.yasdi4j.YasdiMaster;

@Component
public class SmaLoggerService {

	private static final Logger log = LoggerFactory.getLogger(SmaLoggerService.class);

	final String yasdiIni = "yasdi.ini"; // YASDI ini file
	final int nrDevices = 1; // the number of devices in the plant
	final int maxValueAge = 5; // seconds; see YASDI manual
	final DecimalFormat df = new DecimalFormat("0.00");

	private boolean isConnected;
	private YasdiDevice[] devices;
	private YasdiMaster master;
	private YasdiDriver[] drivers;

	@PostConstruct
	private void init() {
		isConnected = false;
		/* get and initialize the master */
		master = YasdiMaster.getInstance();
		try {
			master.initialize("yasdi.ini");
			/* register a listener for device events */
			master.addDeviceListener(new YasdiDeviceListener() {
				public void deviceAdded(YasdiDeviceEvent e) {
					log.info("Device {} added.", e.getDevice().getName());
				}

				public void deviceRemoved(YasdiDeviceEvent e) {
					log.info("Device {} removed.", e.getDevice().getName());
				}
			});
			connect();
		} catch (IOException e1) {
			log.error(e1.getMessage());
			isConnected = false;
		}
	}

	/**
	 * 
	 * @return
	 */

	public void connect() {
		/* get available drivers and set all drivers online */
		drivers = master.getDrivers();
		for (YasdiDriver d : drivers) {
			try {
				master.setDriverOnline(d);
			} catch (IOException e1) {
				log.error("Set Driver Online failed for {}->{}", d.getName(), e1.getMessage());
			}
		}
		/* detect and get devices */
		try {
			master.detectDevices(nrDevices);
		} catch (IOException e1) {
			log.error("Detect Devices failed {}", e1.getMessage());
			isConnected = false;
			return;
		}
		devices = master.getDevices();
		if (devices.length > 0) {
			log.info("Found {} Devices", devices.length);
			isConnected = true;
		} else {
			log.error("Found NO Devices");
			isConnected = false;
		}
	}

	/**
	 * 
	 * @return
	 */
	public double getPac() {
		double result = 0;
		if (devices.length == 0) {
			log.error("Devices lost...");
			isConnected = false;
		}
		for (YasdiDevice device : devices) {
			log.debug("Try to get Pac from Device {}", device.getName());
			for (YasdiChannel yasdiChannel : device.getSpotValueChannels()) {
				/* request current channel value */
				try {
					log.debug("YasdiChannel: {} updateValue", yasdiChannel.getName());
					yasdiChannel.updateValue(maxValueAge);
					if (yasdiChannel.getName().equalsIgnoreCase("Pac")) {
						result = yasdiChannel.getValue();
					}
				} catch (IOException e) {
					log.error(e.getMessage());
					isConnected = false;
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	@PreDestroy
	public boolean disconnect() {
		/* clean up */
		/* set drivers offline */
		for (YasdiDriver d : drivers) {
			master.setDriverOffline(d);
		}
		log.info("Shutting Down the SMA Connection...");
		/* free the master resources */
		master.shutdown();
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
		return true;
	}

	public boolean isConnected() {
		return isConnected;
	}

}
