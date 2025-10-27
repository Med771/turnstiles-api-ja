package ru.ai.turnstilesapija.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class StatusCnt {
    @GetMapping(path = "test")
    public ResponseEntity<HttpStatus> test() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping(path = "taskStatus?taskId={taskId}")
//    public ResponseEntity<HttpStatus> taskStatus(@PathVariable String taskId) {
//        // TODO: check process
//        Optional<Boolean> isCompleted = Optional.of(Boolean.TRUE);
//
//        if (isCompleted.isPresent()) {
//            if (isCompleted.get()) {
//                return new ResponseEntity<>(HttpStatus.OK);
//            }
//
//            return new ResponseEntity<>(HttpStatus.ACCEPTED);
//        }
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(HttpStatus.NOT_FOUND);
//    }
}
