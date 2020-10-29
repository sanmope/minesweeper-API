package com.deviget.codeChallenge.minesweeper.model;

import java.io.Serializable;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cell implements Serializable {

    private boolean revealed;
	private int minesAround;
	private boolean mine;
	private boolean redFlag;
    private boolean questionMark;

    public Cell(boolean mine){
        this.mine = mine;
    }
    
}