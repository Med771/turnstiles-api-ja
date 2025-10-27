package ru.ai.turnstilesapija.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ai.turnstilesapija.dto.AddPersonReq;
import ru.ai.turnstilesapija.dto.AddPersonRes;
import ru.ai.turnstilesapija.services.PersonServ;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PersonCnt {
    private final PersonServ personServ;

    @PostMapping(path = "addPerson")
    public ResponseEntity<AddPersonRes> addPerson(@RequestBody AddPersonReq addPersonReq) {
        try {
            return  ResponseEntity.status(HttpStatus.CREATED).body(personServ.addPerson(addPersonReq));
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping(path= "addBatchPerson")
//    public ResponseEntity<HttpStatus> addBatchPersons(@RequestBody List<AddPersonReq> addBatchPersonReq) {
//        try {
//            personServ.addBatchPersons(addBatchPersonReq);
//
//            return new ResponseEntity<>(HttpStatus.CREATED);
//        }
//        catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
}
