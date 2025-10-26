package ru.ai.turnstilesapija.dto;

import ru.ai.turnstilesapija.models.PersionPosition;

public record AddPersonReq(
        String name,
        PersionPosition position,
        long validTimeBegin,
        long validTimeEnd,
        String baseImg64
) {
}
