package com.deviget.codeChallenge.minesweeper.model;

import java.io.Serializable;
import java.util.Objects;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cell implements Serializable {

    private boolean revealed;
	private int minesAround;
	private boolean mine;
	private boolean redFlag;
    private boolean questionMark;
    private int row;
    private int col;

    public Cell(boolean mine){
        this.mine = mine;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Cell)) {
            return false;
        }
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row,col);
    }
}