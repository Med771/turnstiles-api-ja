package ru.ai.turnstilesapija.models;

import lombok.Getter;

import java.util.concurrent.CompletableFuture;

@Getter
public class RequestContext<T> {
    private final String seqId;
    private final CompletableFuture<T> future;

    public RequestContext(String seqId) {
        this.seqId = seqId;
        this.future = new CompletableFuture<>();
    }
}