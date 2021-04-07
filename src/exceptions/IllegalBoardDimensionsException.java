package exceptions;

public class IllegalBoardDimensionsException extends GameException {

    public IllegalBoardDimensionsException(
            int illegalNumRows,
            int illegalNumCols) {
        super(String.format(
                "Error: The board must have 10 rows (given: %d) and 10 columns (given: %d)",
                illegalNumRows, illegalNumCols));
    }

}
