package de.karthaus.smaDataLogger.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import de.karthaus.smaDataLogger.worker.SmaWorker;

@Component
public class SmaDataLoggerConfig {

	private static final Logger log = LoggerFactory.getLogger(SmaDataLoggerConfig.class);

	@Value("${mqtt.broker.url}")
	private String mqttBrokerURL;

	@Value("${mqtt.broker.password:#{null}}")
	private String mqttBrokerPasssword;

	@Value("${mqtt.broker.username:#{null}}")
	private String mqttBrokerUsename;

	@Value("${mqtt.broker.topic}")
	private String mqttBrokerTopic;

	@Value("${location.longitude}")
	private String longitude;

	@Value("${location.latitude}")
	private String latitude;

	@PostConstruct
	public void init() {
		log.info("SmaDataLoggerConfig initialised");
	}

	public String getMqttBrokerURL() {
		return mqttBrokerURL;
	}

	public void setMqttBrokerURL(String mqttBrokerURL) {
		this.mqttBrokerURL = mqttBrokerURL;
	}

	public String getMqttBrokerPasssword() {
		return mqttBrokerPasssword;
	}

	public void setMqttBrokerPasssword(String mqttBrokerPasssword) {
		this.mqttBrokerPasssword = mqttBrokerPasssword;
	}

	public String getMqttBrokerUsename() {
		return mqttBrokerUsename;
	}

	public void setMqttBrokerUsename(String mqttBrokerUsename) {
		this.mqttBrokerUsename = mqttBrokerUsename;
	}

	public String getMqttBrokerTopic() {
		return mqttBrokerTopic;
	}

	public void setMqttBrokerTopic(String mqttBrokerTopic) {
		this.mqttBrokerTopic = mqttBrokerTopic;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

}
