package exceptions;

public class InvalidShipLocationException extends GameException {

    public InvalidShipLocationException(String startCoord, String endCoord) {
        super(String.format(
                "Error: Invalid ship location (given: %s %s). Ship must not cross or be next to any other ship",
                startCoord, endCoord));
    }
}
