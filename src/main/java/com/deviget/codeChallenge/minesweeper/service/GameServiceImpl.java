package com.deviget.codeChallenge.minesweeper.service;

import java.util.Optional;
import java.util.Random;

import com.deviget.codeChallenge.minesweeper.GameException;
import com.deviget.codeChallenge.minesweeper.model.Cell;
import com.deviget.codeChallenge.minesweeper.model.Game;
import com.deviget.codeChallenge.minesweeper.model.GameResponse;
import com.deviget.codeChallenge.minesweeper.model.State;
import com.deviget.codeChallenge.minesweeper.model.GridRequest;
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
        
        if(gameRepository.findGameByUserNameAndState(request.getName(), State.ACTIVE).isPresent()){
            throw new GameException(String.format("The user [%s] has already created a game and is cative  ",request.getName()));
        }

        matrixGrid = initializeGrid(request);

        Game newGame = new Game(matrixGrid,request.getName());
        gameRepository.save(newGame);

        return GameResponse.builder()
                .userName(newGame.getUserName())
                .mines(newGame.getMines())
                .state(newGame.getState())
                .build();
    }

    private Cell[][] initializeGrid(GridRequest request) {
        int height = request.getRows()-1;
        int width  = request.getColumns()-1;  
        Cell[][] matrixGridCells = new Cell[height][width];

        //Creating the Grid with it corresponding Cells.
        for(int row=0; row < height; row++){
            for (int col=0; col < width; col++){
                matrixGridCells[row][col] = new Cell();
            }
        }

        //Assigning the mines randomly
        int minesLeft = request.getMines();
        Random r = new Random();
        
        int minesAdded = 0;
        while (minesAdded <= minesLeft - 1){
            int x = r.nextInt(height);
            int y = r.nextInt(width);
            if (!matrixGridCells[x][y].isMine()){
                matrixGridCells[x][y].setMine(true);
                minesLeft --;
            }
        }

        //Count how many mines it has arround 
        for(int row=0; row < height; row++){ // loop through grid
            for (int col=0; col < width; col++){ // loop through grid
                for (int i=-1; i <= 1; i++){ //look in each cell arround it self to know if it has mines
                    for (int j=-1; j <= 1; j++){ //look in each cell arround it self to know if it has mines
                        if (((row+i) <= height) & ((row+i) >=0) & ((col+j) <= width) & ((col+j) >=0) & (i != 0) & (j != 0) ){
                            if (matrixGridCells[row+i][col+j].isMine()){
                                matrixGridCells[row][col].setMinesAround(matrixGridCells[row][col].getMinesAround() + 1);
                            }
                        }
                    }   
                }
            }
        }

        return matrixGridCells;
    }

}
