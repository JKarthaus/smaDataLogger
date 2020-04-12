package de.karthaus.smaDataLogger.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        value = "influx.db.enabled",
        havingValue = "true",
        matchIfMissing = false)
public class InfluxService {

    @Value("${infux.db.server}")
    private String server;

    @Value("${infux.db.username}")
    private String username;

    @Value("${infux.db.password}")
    private String password;


    public void WriteDatapoint() {

    }

}
