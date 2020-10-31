package com.deviget.codeChallenge.minesweeper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.deviget.codeChallenge.minesweeper.exception.GameException;
import com.deviget.codeChallenge.minesweeper.model.Cell;
import com.deviget.codeChallenge.minesweeper.model.Game;
import com.deviget.codeChallenge.minesweeper.model.GameResponse;
import com.deviget.codeChallenge.minesweeper.model.GridRequest;
import com.deviget.codeChallenge.minesweeper.model.State;
import com.deviget.codeChallenge.minesweeper.repository.GameRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class GameServiceImplTest {
    private GameServiceImpl gameService;
    private GameRepository gameRepository;
    private ModelMapper modelMapper;
    
    @BeforeEach
    public void setup(){
        gameService = new GameServiceImpl();
        gameRepository = mock(GameRepository.class);
        gameService.setGameRepository(gameRepository);
        modelMapper = mock(ModelMapper.class);
        gameService.setModelMapper(modelMapper);
    }

    @Test
    public void testCreateGameAlreadyCreated(){

        GridRequest request = new GridRequest();
        request.setColumns(4);      
        request.setRows(4);
        request.setName("testName");

        when(gameRepository.findGameByUserNameAndState(request.getName(), State.ACTIVE)).thenReturn(Optional.of(new Game()));
        assertThrows(GameException.class,()-> gameService.createGame(request));

    }

    @Test
    public void testCreateGameSuccessfullyCreated(){

        GridRequest request = new GridRequest();
        request.setColumns(4);      
        request.setRows(4);
        request.setMines(4);
        request.setName("testName");
    

        GameResponse response = gameService.createGame(request);
        assertEquals(State.ACTIVE, response.getState());
        assertEquals("testName", response.getUserName());
        assertEquals(request.getRows(), response.getGrid().length); 
        assertEquals(request.getColumns(), response.getGrid()[0].length);

        Cell[][] gridMatrix = response.getGrid();
        
        int minesCounter = 0;
        for (int i=0; i<response.getGrid().length;i++){
            for (int j=0; j<response.getGrid()[0].length; j++){
                if(gridMatrix[i][j].isMine()){
                    minesCounter++;
                }
            }    
        }
        assertEquals(request.getMines(), minesCounter);

    }

    @Test
    public void testCreateGameOversizedNotAllowed(){

        GridRequest request = new GridRequest();
        request.setColumns(90);      
        request.setRows(90);
        request.setName("testName");

        when(gameRepository.findGameByUserNameAndState(request.getName(), State.ACTIVE)).thenReturn(Optional.of(new Game()));
        assertThrows(GameException.class,()-> gameService.createGame(request));


    }
    
}
