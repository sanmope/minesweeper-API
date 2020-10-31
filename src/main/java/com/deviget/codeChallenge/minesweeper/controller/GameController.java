package com.deviget.codeChallenge.minesweeper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import com.deviget.codeChallenge.minesweeper.exception.GameException;
import com.deviget.codeChallenge.minesweeper.model.GameResponse;
import com.deviget.codeChallenge.minesweeper.model.GridRequest;
import com.deviget.codeChallenge.minesweeper.model.MarkRequest;
import com.deviget.codeChallenge.minesweeper.model.State;
import com.deviget.codeChallenge.minesweeper.model.StepRequest;
import com.deviget.codeChallenge.minesweeper.service.GameService;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("/challenge/deviget/minesweeper/v1")
public class GameController{
    
    @Autowired
    private GameService gameService;


    @PostMapping(value = "/game", consumes="application/json") 
    public ResponseEntity createGame(@Valid @RequestBody GridRequest request) { 

        try {
            if(request.getMines() < request.getColumns()*request.getRows()){
                log.error("[GameController: createGame]: the number of mines is greater than the number of cells. Please reduce the ammount.");
                ResponseEntity.status(HttpStatus.CONFLICT).body("the number of mines is greater than the number of cells. Please reduce the ammount.");
            }
            log.info("[GameController: createGame] Game Created for user {}",request.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(gameService.createGame(request));
        } catch(com.deviget.codeChallenge.minesweeper.exception.GameException e) {
            log.error("[GameController: createGame]: Error: %s", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

        }
    }

    @GetMapping(value="/game/{userName}")
	public ResponseEntity getGame(@PathVariable String userName) {
        try{
            log.info("[GameController: getGame] Game returned for user {}",gameService.getGame(userName)); 
            return ResponseEntity.ok(gameService.getGame(userName));
        } catch (Exception e){
            log.error("[GameController: getGame]: Error: %s", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        
    }

    @GetMapping(value="/game/{userName}/timetracker")
	public ResponseEntity getTimeTracker(@PathVariable String userName) {
        try{
            log.info("[GameController: getTimeTracker] getting time"); 
            return ResponseEntity.ok(gameService.getTimeTracker(userName));
        } catch (Exception e){
            log.error("[GameController: getTimeTracker]: Error: %s", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        
    }

    @PutMapping(value = "/game/{userName}/mark/{markType}", consumes="application/json")
    public ResponseEntity setMark(@PathVariable String userName, @PathVariable String markType, @Valid @RequestBody MarkRequest request) {
        try {  
            log.info("[GameController: mark] cell {}{} marked with {}",request.getRow(),request.getRow(),markType);   
            return ResponseEntity.ok(gameService.setMark(userName,markType,request));
        } catch(GameException e){
            log.error("[GameController: mark]: Error: %s", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());   
        }

    }

    @PutMapping(value = "/game/{userName}/step")
    public ResponseEntity stepOn(@PathVariable String userName, @Valid @RequestBody StepRequest request) {
        try {
            GameResponse response = gameService.stepOn(userName,request);   
            if (response.getState() == State.EXPLODED){
                log.info("[GameController: stepOn] cell {}{}:  {}",request.getRow(),request.getRow(),State.EXPLODED);   
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(String.format("[%s] You Stepped into a mine! - Game Over!!", userName));    
            }else if (response.getState() == State.WON){
                log.info("[GameController: stepOn] cell {}{}:  Game {}",request.getRow(),request.getRow(),State.WON);   
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(String.format("[%s] You Won the Game! Congratulations!!", userName));    
            }else{
                log.info("[GameController: stepOn] cell {}{} revealed",request.getRow(),request.getRow());   
                return ResponseEntity.ok(gameService.stepOn(userName,request));
            }
        } catch (GameException e){
            log.error("[GameController: stepOn]: Error: %s", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  
        }
    }
}