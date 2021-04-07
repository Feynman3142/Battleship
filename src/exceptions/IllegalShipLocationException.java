package exceptions;

public class IllegalShipLocationException extends GameException {

    public IllegalShipLocationException(String startCoord, String endCoord) {
        super(String.format(
                "Error: Illegal coordinates (given: %s %s). Ship must be placed horizontally or vertically",
                startCoord, endCoord));
    }
}
