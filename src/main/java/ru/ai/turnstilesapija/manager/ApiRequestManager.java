package ru.ai.turnstilesapija.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ai.turnstilesapija.models.ApiResponse;
import ru.ai.turnstilesapija.models.RequestContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ApiRequestManager {
    private final Map<String, RequestContext<ApiResponse>> pending = new ConcurrentHashMap<>();


}
