package exceptions;

import battleship.Ship;

public class InvalidShipSizeException extends GameException {

    public InvalidShipSizeException(int invalidSize, Ship ship) {
        super(String.format(
                "Error: Invalid number of cells for %s (given: %d). %s must occupy %d cells",
                ship.label, invalidSize, ship.label, ship.size));
    }

}
