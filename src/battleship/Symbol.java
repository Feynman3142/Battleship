package battleship;

public enum Symbol {

    FOG("Fog", '~'),
    HIT("Hit", 'X'),
    MISS("Miss", 'M'),
    SHIP("Ship", 'O');

    public final String label;
    public final char value;

    Symbol(String label, char value) {
        this.label = label;
        this.value = value;
    }

}
