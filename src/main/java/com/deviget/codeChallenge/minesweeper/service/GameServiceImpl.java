package com.deviget.codeChallenge.minesweeper.service;

import java.util.Optional;
import java.util.Random;

import com.deviget.codeChallenge.minesweeper.GameException;
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


import org.modelmapper.ModelMapper;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ModelMapper modelMapper;

    private Cell[][] matrixGrid;

    @Override
    public GameResponse getGame(String userName) {

        Optional<Game> game = gameRepository.findGameByUserNameAndState(userName, State.ACTIVE);
        return game.map(gameMap -> modelMapper.map(gameMap, GameResponse.class)).get();

    }

    @Override
    public GameResponse createGame(GridRequest request) {

        if (gameRepository.findGameByUserNameAndState(request.getName(), State.ACTIVE).isPresent()) {
            throw new GameException(
                    String.format("The user [%s] has already created a game and is cative", request.getName()));
        }

        matrixGrid = initializeGrid(request);

        Game newGame = new Game(matrixGrid, request.getName());
        gameRepository.save(newGame);

        return GameResponse.builder().userName(newGame.getUserName()).grid(newGame.getGrid())
                .state(newGame.getState()).build();
    }

    @Override
    public GameResponse setMark(String userName, String markType, MarkRequest request) {

        Optional<Game> game = gameRepository.findGameByUserNameAndState(userName, State.ACTIVE);

        if (!game.isPresent()) {
            throw new GameException(String.format("The user [%s] has no active game to play with", userName));
        }

        if (game.get().getGrid()[request.getRow()][request.getColumn()].isRevealed()) {
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
        Optional<Game> game = gameRepository.findGameByUserNameAndState(userName, State.ACTIVE);
        if (!game.isPresent()) {
            throw new GameException(String.format("The user [%s] has no active game to play with", userName));
        }

        Cell[][] grid = game.get().getGrid();

        if (revealCell(grid,request.getRow(),request.getColumn()) == -1) {
            game.get().setState(State.EXPLODED);
        }else if (hasWon(grid)){
            game.get().setState(State.WON);
        }

        return modelMapper.map(gameRepository.save(game.get()), GameResponse.class);
    }



    private Cell[][] initializeGrid(GridRequest request) {
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
        int minesLeft = request.getMines();
        Random r = new Random();

        int minesAdded = 0;
        while (minesAdded <= minesLeft - 1) {
            int x = r.nextInt(width);
            int y = r.nextInt(height);
            if (!matrixGridCells[x][y].isMine()) {
                matrixGridCells[x][y].setMine(true);
                minesLeft--;
                incrementMinesArroundForNeighborCells(matrixGridCells, x, y);
            }
        }

        return matrixGridCells;
    }

    private void incrementMinesArroundForNeighborCells(Cell[][] matrixGridCells, int x, int y) {
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
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (matrixGridCells[row][col].isRevealed()){
                    cellRevealed ++;
                }
            }
        }
        return cellRevealed;
    }

    private boolean hasWon (Cell[][] matrixGridCells){
        if (countCellRevealed(matrixGridCells) == (matrixGridCells.length*matrixGridCells[0].length)){
            return true;
        }else{
            return false;
        }
    }



    private int revealCell (Cell[][] matrixGridCells,int x, int y){
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
