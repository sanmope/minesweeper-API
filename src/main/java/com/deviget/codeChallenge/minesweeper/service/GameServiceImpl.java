package com.deviget.codeChallenge.minesweeper.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

import com.deviget.codeChallenge.minesweeper.exception.GameException;
import com.deviget.codeChallenge.minesweeper.model.Cell;
import com.deviget.codeChallenge.minesweeper.model.Game;
import com.deviget.codeChallenge.minesweeper.model.GameResponse;
import com.deviget.codeChallenge.minesweeper.model.State;
import com.deviget.codeChallenge.minesweeper.model.StepRequest;
import com.deviget.codeChallenge.minesweeper.model.GridRequest;
import com.deviget.codeChallenge.minesweeper.model.MarkRequest;
import com.deviget.codeChallenge.minesweeper.model.MarkType;
import com.deviget.codeChallenge.minesweeper.repository.GameRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

import org.modelmapper.ModelMapper;
@Log4j2
@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ModelMapper modelMapper;

    private Cell[][] matrixGrid;

    @Override
    public GameResponse getGame(String userName) {

        log.info("[GameService: getGame] Searching for active game for user {}",userName);
        Optional<Game> game = gameRepository.findGameByUserNameAndState(userName, State.ACTIVE);
        return game.map(gameMap -> modelMapper.map(gameMap, GameResponse.class)).get();

    }

    @Override
    public GameResponse createGame(GridRequest request) {

        log.info("[GameService: createGame] Searching for active game for user {} before creation",request.getName());
        if (gameRepository.findGameByUserNameAndState(request.getName(), State.ACTIVE).isPresent()) {
            log.error("[GameService: createGame] The user {} has already created a game and is cative", request.getName());
            throw new GameException(
                    String.format("The user [%s] has already created a game and is cative", request.getName()));
        }

        matrixGrid = initializeGrid(request);

        Game newGame = new Game(matrixGrid, request.getName());
        newGame.setMines(request.getMines());
        newGame.setTimeTracker(LocalDateTime.now());
        gameRepository.save(newGame);

        log.info("[GameService: createGame] Game in Active State for user {} found",request.getName());
        return GameResponse.builder().userName(newGame.getUserName()).grid(newGame.getGrid())
                .state(newGame.getState()).timeTracker(newGame.getTimeTracker()).build();
    }

    @Override
    public GameResponse setMark(String userName, String markType, MarkRequest request) {

        Optional<Game> game = gameRepository.findGameByUserNameAndState(userName, State.ACTIVE);
        log.info("[GameService: setMark] Seting mark {} for user {} in his active game",markType,game.get().getUserName());
        if (!game.isPresent()) {
            log.error("[GameService: setMark] Seting mark {} failed for user {}. No Active Game",markType,game.get().getUserName());
            throw new GameException(String.format("The user {} has no active game to play with", userName));
        }

        if (game.get().getGrid()[request.getRow()][request.getColumn()].isRevealed()) {
            log.error("[GameService: setMark] Seting mark {} for user {}: This Possition is already revealed [%][%] ",markType,game.get().getUserName(), request.getRow(),
            request.getColumn());
            throw new GameException(String.format("This Possition is already revealed [%][%] ", request.getRow(),
                    request.getColumn()));
        }

        if (MarkType.QUESTION.name().compareToIgnoreCase(markType) == 0) {
            game.get().getGrid()[request.getRow()][request.getColumn()].setQuestionMark(true);
        } else {
            game.get().getGrid()[request.getRow()][request.getColumn()].setRedFlag(true);
        }

        gameRepository.save(game.get());

        return modelMapper.map(game.get(), GameResponse.class);

    }

    public GameResponse stepOn(String userName, StepRequest request){
        
        log.info("[GameService: stepOn] stepOn {}{}. Looking for active Game",request.getRow(),request.getColumn());
        Optional<Game> game = gameRepository.findGameByUserNameAndState(userName, State.ACTIVE);
        if (!game.isPresent()) {
            log.error("[GameService: stepOn] stepOn {}{} failed for user {}. No Active Game",request.getRow(),request.getColumn(),game.get().getUserName());
            throw new GameException(String.format("The user [%s] has no active game to play with", userName));
        }

        Cell[][] grid = game.get().getGrid();

        if (revealCell(grid,request.getRow(),request.getColumn()) == -1) {
            log.info("[GameService: stepOn] stepOn {}{} for user {}. Mine Explded",request.getRow(),request.getColumn(),game.get().getUserName());
            game.get().setState(State.EXPLODED);
        }else if (hasWon(grid,game.get().getMines())){
            log.info("[GameService: stepOn] stepOn {}{} for user {}. User Won the Game!!",request.getRow(),request.getColumn(),game.get().getUserName());
            game.get().setState(State.WON);
        }

        return modelMapper.map(gameRepository.save(game.get()), GameResponse.class);
    }


    
    public Long getTimeTracker(String userName) {
        
        log.info("[GameService: getTimeTracker] Looking for active Game");
        Optional<Game> game = gameRepository.findGameByUserNameAndState(userName, State.ACTIVE);
        if (!game.isPresent()) {
            log.error("[GameService: getTimeTracker] failed for user {}. No Active Game",userName);
            throw new GameException(String.format("The user [%s] has no active game to play with", userName));
        }else{
            return ( ChronoUnit.SECONDS.between(game.get().getTimeTracker(),LocalDateTime.now()));
        }
    }

    private Cell[][] initializeGrid(GridRequest request) {
        log.info("[GameService: initializeGrid]: creating Grid for the game");
        int height = request.getRows();
        int width = request.getColumns(); 
        Cell[][] matrixGridCells = new Cell[height][width];
        
        // Creating the Grid with it corresponding Cells.
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                matrixGridCells[row][col] = new Cell();
                matrixGridCells[row][col].setRow(row);
                matrixGridCells[row][col].setCol(col);
            }
        }

        // Assigning the mines randomly
        log.info("[GameService: initializeGrid]: Assigning mines Ramdomly");
        int minesLeft = request.getMines();
        Random r = new Random();

        int minesAdded = 0;
        while (minesAdded <= minesLeft - 1) {
            int y = r.nextInt(width);
            int x = r.nextInt(height);
            if (!matrixGridCells[x][y].isMine()) {
                matrixGridCells[x][y].setMine(true);
                minesLeft--;
                incrementMinesArroundForNeighborCells(matrixGridCells, x, y);
            }
        }

        return matrixGridCells;
    }

    private void incrementMinesArroundForNeighborCells(Cell[][] matrixGridCells, int x, int y) {
        log.info("[GameService: incrementMinesArroundForNeighborCells]: incrementing mines arround for neighbor cells.");
        int height = matrixGridCells.length;
        int width = matrixGridCells[0].length;
        for (int i = -1; i <= 1; i++) { // look in each cell arround it self to know if it has mines
            for (int j = -1; j <= 1; j++) { // look in each cell arround it self to know if it has mines
                if (((x + i) < height) & ((x + i) >= 0) & ((y + j) < width) & ((y + j) >= 0)) {
                    if (!matrixGridCells[x + i][y + j].equals(matrixGridCells[x][y])) {
                        matrixGridCells[x+i][y+j].setMinesAround(matrixGridCells[x+i][y+j].getMinesAround() + 1);
                    }
                }
            }
        }
    }

    private void uncoverEmptyCells (Cell[][] matrixGridCells, int row, int col){
        int height = matrixGridCells.length;
        int width = matrixGridCells[0].length;

        log.info("[GameService: uncoverEmptyCells]: Uncovering empty cells.");
        if (row < 0 || row > width || col < 0 || col > height){
            return;
        }

        if (matrixGridCells[row][col].getMinesAround() == 0 && !matrixGridCells[row][col].isRevealed()){
            matrixGridCells[row][col].setRevealed(true);
            for (int i = -1; i <= 1; i++) { // look in each cell arround it self to know if it has mines
                for (int j = -1; j <= 1; j++) { // look in each cell arround it self to know if it has mines
                    if (((row + i) < height) & ((row + i) >= 0) & ((col + j) < width) & ((col + j) >= 0)) {
                        if (!matrixGridCells[row + i][col + j].equals(matrixGridCells[row][col])) { //if this is not me
                            uncoverEmptyCells(matrixGridCells, row + i, col + j);
                        }
                    }
                }   
            }   
        }else {
            return;
        }

    }

    private int countCellRevealed (Cell[][] matrixGridCells){
        int height = matrixGridCells.length;
        int width = matrixGridCells[0].length;
        int cellRevealed = 0;
        
        log.info("[GameService: countCellRevealed]: Returning amount of mines already revealed.");
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (matrixGridCells[row][col].isRevealed()){
                    cellRevealed ++;
                }
            }
        }
        return cellRevealed;
    }

    private boolean hasWon (Cell[][] matrixGridCells, int mines){
        log.info("[GameService: hasWon]: Validating if user already won the game.");
        if (countCellRevealed(matrixGridCells) == (matrixGridCells.length*matrixGridCells[0].length - mines)){
            return true;
        }else{
            return false;
        }
    }



    private int revealCell (Cell[][] matrixGridCells,int x, int y){
        
        log.info("[GameService: revealCell]: Setpping into the cell.");
        if (matrixGridCells[x][y].isMine()){
            return -1;
        }else{
            uncoverEmptyCells(matrixGridCells,x,y);
            return countCellRevealed(matrixGridCells);
        }
    }


    //Methods used for Testing purposes.
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    
    public void setGameRepository(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }


}
