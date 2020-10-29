package com.deviget.codeChallenge.minesweeper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import com.deviget.codeChallenge.minesweeper.GameException;
import com.deviget.codeChallenge.minesweeper.model.GridRequest;
import com.deviget.codeChallenge.minesweeper.model.MarkRequest;
import com.deviget.codeChallenge.minesweeper.service.GameService;


@RestController
@RequestMapping("/challenge/deviget/minesweeper/v1")
public class GameController{
    
    @Autowired
    private GameService gameService;


    @PostMapping(value = "/game", consumes="application/json") 
    public ResponseEntity createGame(@Valid @RequestBody GridRequest request) { 

        try {
            if(request.getMines() < request.getColumns()*request.getRows()){
                ResponseEntity.status(HttpStatus.CONFLICT).body("the number of mines is greater than the number of cells. Please reduce the ammount.");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(gameService.createGame(request));
        } catch(GameException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

        }
    }

    @GetMapping(value="/game/{userName}")
	public ResponseEntity getGame(@PathVariable String userName) {
        try{ 
            return ResponseEntity.ok(gameService.getGame(userName));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no username");
        }
        
    }

    @PutMapping(value = "/game/{userName}/mark/{markType}", consumes="application/json")
    public ResponseEntity setMark(@PathVariable String userName, @PathVariable String markType, @Valid @RequestBody MarkRequest request) {
        try {     
            return ResponseEntity.ok(gameService.setMark(userName,markType,request));
        } catch(GameException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());   
        }

    }

}