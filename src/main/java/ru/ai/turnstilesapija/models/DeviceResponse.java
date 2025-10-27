package ru.ai.turnstilesapija.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceResponse {
    private String cmd;
    private String seqId;
    private String code;
}