package com.deviget.codeChallenge.minesweeper.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/minesweeper/v1")
public class GameController{
    
    @GetMapping(value="/game/hello")
	public ResponseEntity<String> hello()  {
        return new ResponseEntity<>("hello World",HttpStatus.OK);
        
    }

}