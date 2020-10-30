package com.deviget.codeChallenge.minesweeper.service;

import java.util.Optional;
import java.util.Random;

import com.deviget.codeChallenge.minesweeper.GameException;
import com.deviget.codeChallenge.minesweeper.model.Cell;
import com.deviget.codeChallenge.minesweeper.model.Game;
import com.deviget.codeChallenge.minesweeper.model.GameResponse;
import com.deviget.codeChallenge.minesweeper.model.State;
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
                    String.format("The user [%s] has already created a game and is cative  ", request.getName()));
        }

        matrixGrid = initializeGrid(request);

        Game newGame = new Game(matrixGrid, request.getName());
        gameRepository.save(newGame);

        return GameResponse.builder().userName(newGame.getUserName()).mines(newGame.getMines())
                .state(newGame.getState()).build();
    }

    @Override
    public GameResponse setMark(String userName, String markType, MarkRequest request) {

        Optional<Game> game = gameRepository.findGameByUserNameAndState(userName, State.ACTIVE);

        if (!game.isPresent()) {
            throw new GameException(String.format("The user [%] has no active game to play with", userName));
        }
        if (game.get().getMines()[request.getRow()][request.getCollumn()].isRevealed()) {
            throw new GameException(String.format("This Possition is already revealed [%][%] ", request.getRow(),
                    request.getCollumn()));
        }

        if (MarkType.QUESTION.name().compareToIgnoreCase(markType) == 0) {
            game.get().getMines()[request.getRow()][request.getCollumn()].setQuestionMark(true);
        } else {
            game.get().getMines()[request.getRow()][request.getCollumn()].setRedFlag(true);
        }

        gameRepository.save(game.get());

        return modelMapper.map(game.get(), GameResponse.class);

    }

    private Cell[][] initializeGrid(GridRequest request) {
        int height = request.getRows();
        int width = request.getColumns();
        Cell[][] matrixGridCells = new Cell[height][width];

        // Creating the Grid with it corresponding Cells.
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                matrixGridCells[row][col] = new Cell();
            }
        }

        // Assigning the mines randomly
        int minesLeft = request.getMines();
        Random r = new Random();

        int minesAdded = 0;
        while (minesAdded <= minesLeft - 1) {
            int x = r.nextInt(height);
            int y = r.nextInt(width);
            if (!matrixGridCells[x][y].isMine()) {
                matrixGridCells[x][y].setMine(true);
                minesLeft--;
                incrementMinesArroundForNeighborCells(matrixGridCells, x, y, height, width);
            }
        }

        return matrixGridCells;
    }

    private void incrementMinesArroundForNeighborCells(Cell[][] matrixGridCells, int x, int y, int height, int width) {
        for (int i = -1; i <= 1; i++) { // look in each cell arround it self to know if it has mines
            for (int j = -1; j <= 1; j++) { // look in each cell arround it self to know if it has mines
                if (((x + i) < height) & ((x + i) >= 0) & ((y + j) < width) & ((y + j) >= 0)) {
                    if (!matrixGridCells[x + i][y + j].equals(matrixGridCells[x][y])) { 
                        if (matrixGridCells[x + i][y + j].isMine()) {
                            matrixGridCells[x][y].setMinesAround(matrixGridCells[x][y].getMinesAround() + 1);
                        }
                    }
                }
            }
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
