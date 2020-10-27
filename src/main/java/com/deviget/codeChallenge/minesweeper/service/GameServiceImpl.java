package com.deviget.codeChallenge.minesweeper.service;

import java.util.Optional;
import java.util.Random;

import com.deviget.codeChallenge.minesweeper.GameException;
import com.deviget.codeChallenge.minesweeper.model.Cell;
import com.deviget.codeChallenge.minesweeper.model.Game;
import com.deviget.codeChallenge.minesweeper.model.GameBean;
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
    public GameBean getGame(String userName) {

        Optional<Game> game = gameRepository.findGameByUserNameAndState(userName, State.ACTIVE);
        return game.map(gameMap -> modelMapper.map(gameMap, GameBean.class)).get();

    }

    @Override
    public GameBean createGame(GridRequest request) {
        
        if(gameRepository.findGameByUserNameAndState(request.getName(), State.ACTIVE).isPresent()){
            throw new GameException(String.format("The user [%s] has already created a game and is cative  ",request.getName()));
        }

        matrixGrid = initializeGrid(request);

        return null;
    }

    private Cell[][] initializeGrid(GridRequest request) {
        Cell[][] matrixGridCells = new Cell[request.getRows()][request.getColumns()];

        //Creating the Grid with it corresponding Cells.
        for(int row=0; row < request.getRows()-1; row++){
            for (int col=0; col <request.getColumns()-1; col++){
                matrixGridCells[row][col] = new Cell();
            }
        }

        //Assigning the mines randomly
        int minesLeft = request.getMines();
        Random r = new Random();
        
        int minesAdded = 0;
        while (minesAdded <= minesLeft - 1){
            int x = r.nextInt(request.getRows());
            int y = r.nextInt(request.getColumns());
            if (matrixGridCells[x][y].isMine()){
                matrixGridCells[x][y].setMine(true);
                minesLeft --;
            }
        }

        

        return matrixGridCells;
    }

}
