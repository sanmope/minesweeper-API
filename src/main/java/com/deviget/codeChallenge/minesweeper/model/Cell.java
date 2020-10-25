import com.deviget.codeChallenge.minesweeper.model;

@Data
public class Cell {

    private boolean revealed;
	private int minesAround;
	private boolean mine;
	private boolean redFlag;
    private boolean questionMark;

    public Cell(boolean mine){
        this.mine = mine;
    }
    
}