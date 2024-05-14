package org.palpalmans.ollive_back.domain.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<?> getTest(){
        System.out.println("work");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "에러 메시지");
    }
}