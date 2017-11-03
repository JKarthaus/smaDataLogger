package de.karthaus.smaDataLogger.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import de.karthaus.smaDataLogger.config.SmaDataLoggerConfig;
import de.karthaus.smaDataLogger.service.SmaLoggerService;

@Configuration
@IntegrationComponentScan
public class MqttConnector {

	private static final Logger log = LoggerFactory.getLogger(MqttConnector.class);

	@Autowired
	SmaDataLoggerConfig smaDataLoggerConfig;

	@Bean
	MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
		log.info("Init Client Factory to Broker URL {}", smaDataLoggerConfig.getMqttBrokerURL());
		clientFactory.setServerURIs(smaDataLoggerConfig.getMqttBrokerURL());
		// if (smaDataLoggerConfig.getMqttBrokerUsename() != null) {
		// clientFactory.setUserName(smaDataLoggerConfig.getMqttBrokerUsename());
		// }
		// if (smaDataLoggerConfig.getMqttBrokerPasssword() != null) {
		// clientFactory.setPassword(smaDataLoggerConfig.getMqttBrokerPasssword());
		// }
		return clientFactory;
	}

	@Bean
	MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel")
	public MessageHandler mqttOutbound() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("smaMqttConnector", mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic(smaDataLoggerConfig.getMqttBrokerTopic());
		return messageHandler;
	}

	@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
	public interface MqttBrokerConnector {
		void sendToMqtt(String payload);
	}

}
