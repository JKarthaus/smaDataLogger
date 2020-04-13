package de.karthaus.smaDataLogger.service;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@ConditionalOnProperty(
        value = "influx.db.enabled",
        havingValue = "true",
        matchIfMissing = false)
public class InfluxService {

    private static final Logger log = LoggerFactory.getLogger(InfluxService.class);

    @Value("${influx.db.server:'http://127.0.0.1:8086'}")
    private String server;

    @Value("${influx.db.username:username}")
    private String username;

    @Value("${influx.db.password:password}")
    private String password;

    private final static String DATABASE_NAME = "smaDataLogger";

    @PostConstruct
    public void init() {
        log.info("Try to connect to InfluxDB Server {} with userName:{}", server, username);
        InfluxDB influxDB = InfluxDBFactory.connect(server, username, password);

        influxDB.query(new Query("CREATE DATABASE " + DATABASE_NAME));
        influxDB.setDatabase(DATABASE_NAME);
    }

    public void WriteDatapoint() {

    }

}
