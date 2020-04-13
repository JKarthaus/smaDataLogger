package de.karthaus.smaDataLogger.service;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

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

    private InfluxDB influxDB;

    @PostConstruct
    public void init() {
        log.info("Try to connect to InfluxDB Server {} with userName:{}", server, username);
        influxDB = InfluxDBFactory.connect(server, username, password);
        influxDB.query(new Query("CREATE DATABASE " + DATABASE_NAME));
        influxDB.setDatabase(DATABASE_NAME);
    }

    /**
     * @param tag
     * @param value
     */
    public void WriteDatapoint(String tag, Double value) {
        log.debug("Store value:{} to Tag:{} in InfluxDB at Server:{}", value, tag, server);
        influxDB.write(Point.measurement("smaMeasurement")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .tag("dataType", tag)
                .addField("value", value)
                .build());
    }

}
