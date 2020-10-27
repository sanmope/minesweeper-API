package com.deviget.codeChallenge.minesweeper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.deviget.codeChallenge.minesweeper.service.GameService;


@RestController
@RequestMapping("/challenge/deviget/minesweeper/v1")
public class GameController{
    
    @Autowired
    private GameService gameService;

    @GetMapping(value="/game/hello")
	public ResponseEntity hello() {
        try{ 
            return ResponseEntity.status(HttpStatus.OK).body("hello buddy");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no hello");
        }
        
    }
    @GetMapping(value="/game/{userName}")
	public ResponseEntity getGame(@PathVariable String userName) {
        try{ 
            return ResponseEntity.ok(gameService.getGame(userName));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("no username");
        }
        
    }

}