package ru.ai.turnstilesapija.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TermCfg {
    @Value("${app.first-device-sn}")
    public String firstDeviceSN;
    @Value("${app.second-device-sn}")
    public String secondDeviceSN;

    @Value("${app.first-device-secret}")
    public String firstDeviceSecret;
    @Value("${app.second-device-secret}")
    public String secondDeviceSecret;

    @Value("${app.config.port}")
    public int configPort;
}
