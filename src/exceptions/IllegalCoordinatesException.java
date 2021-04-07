package exceptions;

public class IllegalCoordinatesException extends GameException {

    public IllegalCoordinatesException(String coord, String legalCoordRegex) {
        super(String.format(
                "Error: Coordinates (given: %s) must match regex %s\nValid examples: a9, B10, c1, D4",
                coord, legalCoordRegex));
    }

}
