package exceptions;

public class InvalidCoordinatesException extends GameException {

    public InvalidCoordinatesException(String coord, int numRows, int numCols) {
        super(String.format(
                "Error: Coordinates (given: %s) are outside the range of the board (%d x %d grid)",
                coord, numRows, numCols));
    }

}
