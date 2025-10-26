package ru.ai.turnstilesapija.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PersionPosition {
    GUEST("guest"),
    STUDENT("student"),
    TEACHER("teacher"),
    ADMIN("admin");

    @Getter
    private final String position;
}
