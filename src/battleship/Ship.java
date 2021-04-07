package battleship;

public enum Ship {

    AIRCRAFT_CARRIER("Aircraft Carrier",5),
    BATTLESHIP("Battleship", 4),
    SUBMARINE("Submarine", 3),
    CRUISER("Cruiser", 3),
    DESTROYER("Destroyer", 2);

    public final String label;
    public final int size;

    Ship(String label, int size) {
        this.label = label;
        this.size = size;
    }

}
