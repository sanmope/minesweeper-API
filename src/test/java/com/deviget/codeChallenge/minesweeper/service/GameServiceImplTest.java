package com.deviget.codeChallenge.minesweeper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import com.deviget.codeChallenge.minesweeper.exception.GameException;
import com.deviget.codeChallenge.minesweeper.model.Cell;
import com.deviget.codeChallenge.minesweeper.model.Game;
import com.deviget.codeChallenge.minesweeper.model.GameResponse;
import com.deviget.codeChallenge.minesweeper.model.GridRequest;
import com.deviget.codeChallenge.minesweeper.model.MarkRequest;
import com.deviget.codeChallenge.minesweeper.model.MarkType;
import com.deviget.codeChallenge.minesweeper.model.State;
import com.deviget.codeChallenge.minesweeper.model.StepRequest;
import com.deviget.codeChallenge.minesweeper.repository.GameRepository;
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

        String userName = "testName";
        GridRequest request = new GridRequest();
        request.setColumns(4);      
        request.setRows(4);
        request.setName(userName);

        when(gameRepository.findGameByUserNameAndState(request.getName(), State.ACTIVE)).thenReturn(
            Optional.of(new Game()));
        
        assertThrows(GameException.class,()-> gameService.createGame(request));
    }

    @Test
    public void testCreateGameSuccessfullyCreated(){
        String userName = "userName";
        GridRequest request = new GridRequest();
        request.setColumns(4);      
        request.setRows(4);
        request.setMines(4);
        request.setName(userName);
    

        GameResponse response = gameService.createGame(request);
        assertEquals(State.ACTIVE, response.getState());
        assertEquals(userName, response.getUserName());
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
    public void testSetMarkAlreadyRevealed(){

        String userName = "testName";
        String markType = MarkType.RED_FLAG.name();

        MarkRequest request = new MarkRequest();
        request.setRow(0);
        request.setColumn(0);

        Cell[][] grid = new Cell[2][2];

        grid[0][0] = Cell.builder()
            .revealed(true)
            .row(0)
            .col(0)
            .build();

        Game game = Game.builder()
            .mines(3)
            .state(State.ACTIVE)
            .TimeTracker(LocalDateTime.now())
            .grid(grid)
            .userName(userName)
            .build();

        when(gameRepository.findGameByUserNameAndState(userName, State.ACTIVE)).thenReturn(
            Optional.of(game));

        assertThrows(GameException.class, () -> gameService.setMark(userName, markType, request));

    }

    @Test
    public void testGetGame(){

        String username = "Test1";

        Game game = Game.builder()
            .mines(3)
            .state(State.ACTIVE)
            .TimeTracker(LocalDateTime.now())
            .grid(new Cell[3][3])
            .userName(username)
            .build();

        GameResponse gameResponse = GameResponse.builder()
            .timeTracker(game.getTimeTracker())
            .userName(game.getUserName())
            .grid(game.getGrid())
            .build();

        when(gameRepository.findGameByUserNameAndState(username, State.ACTIVE)).thenReturn(
            Optional.of(game));
        
        when(modelMapper.map(game,GameResponse.class)).thenReturn(
            GameResponse.builder()
            .timeTracker(game.getTimeTracker())
            .userName(game.getUserName())
            .grid(game.getGrid())
            .build());

        assertNotNull(gameService.getGame(username));
        assertEquals(gameResponse, gameService.getGame(username));

    }

    @Test
    public void testStepOnGameFinshedWonAllCellsRevealed(){
        String userName = "testName";

        StepRequest request = StepRequest.builder()
            .row(0)
            .column(0)
            .build();

        Cell[][] grid = new Cell[2][2];

        //create a grid with all cells revealed
        for (int i=0; i < 2; i++){
            for (int j=0; j < 2 ; j++){
                grid[i][j] = Cell.builder()
                .revealed(true)
                .row(0)
                .col(0)
                .build();
            }
        }
        grid[0][0].setRevealed(false);

        Game game = Game.builder()
            .mines(0)
            .state(State.ACTIVE)
            .TimeTracker(LocalDateTime.now())
            .grid(grid)
            .userName(userName)
            .build();
        

        when(gameRepository.findGameByUserNameAndState(userName, State.ACTIVE)).thenReturn(
            Optional.of(game));

        when(modelMapper.map(game,GameResponse.class)).thenReturn(
            GameResponse.builder()
            .timeTracker(game.getTimeTracker())
            .userName(game.getUserName())
            .grid(game.getGrid())
            .state(State.WON)
            .build());
        
        assertEquals(State.WON,gameService.stepOn(userName, request).getState());
    }

    @Test
    public void testStepOnGameFinshedWonAllCellsExploded(){
        String userName = "testName";

        StepRequest request = StepRequest.builder()
            .row(0)
            .column(0)
            .build();

        Cell[][] grid = new Cell[2][2];

        //create a grid with all cells revealed
        for (int i=0; i < 2; i++){
            for (int j=0; j < 2 ; j++){
                grid[i][j] = Cell.builder()
                .revealed(true)
                .row(0)
                .col(0)
                .build();
            }
        }
        grid[0][0].setRevealed(false);

        Game game = Game.builder()
            .mines(0)
            .state(State.ACTIVE)
            .TimeTracker(LocalDateTime.now())
            .grid(grid)
            .userName(userName)
            .build();
        

        when(gameRepository.findGameByUserNameAndState(userName, State.ACTIVE)).thenReturn(
            Optional.of(game));

        when(modelMapper.map(game,GameResponse.class)).thenReturn(
            GameResponse.builder()
            .timeTracker(game.getTimeTracker())
            .userName(game.getUserName())
            .grid(game.getGrid())
            .state(State.WON)
            .build());
        
        assertEquals(State.EXPLODED,gameService.stepOn(userName, request).getState());
    }

    @Test
    public void testGetTimeTrackerSecondsSpentInGame(){
        
        String userName = "Test1";
        Long diffInSeconds = 50L;

        Game game = Game.builder()
            .mines(3)
            .state(State.ACTIVE)
            .TimeTracker(LocalDateTime.now().minusSeconds(diffInSeconds))
            .grid(new Cell[3][3])
            .userName(userName)
            .build();

        GameResponse gameResponse = GameResponse.builder()
            .timeTracker(game.getTimeTracker())
            .userName(game.getUserName())
            .grid(game.getGrid())
            .build();

        when(gameRepository.findGameByUserNameAndState(userName, State.ACTIVE)).thenReturn(
            Optional.of(game));
        
        when(modelMapper.map(game,GameResponse.class)).thenReturn(
            GameResponse.builder()
            .timeTracker(game.getTimeTracker())
            .userName(game.getUserName())
            .grid(game.getGrid())
            .build());

        assertTrue(diffInSeconds < gameService.getTimeTracker(userName));

    }
    
}
