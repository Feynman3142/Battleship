package battleship;

import exceptions.GameException;

public class Cell {

    private Symbol symbol;
    private Ship occupyingShip;

    public Cell() {
        this.setCell(Symbol.FOG);
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setCell(Symbol symbol) {
        if (Symbol.SHIP.equals(symbol)) {
            throw new GameException(String.format(
                    "Cannot assign %s symbol (%c) to cell. Please use the setCell(Ship occupyingShip) method instead ",
                    Symbol.SHIP.label, Symbol.SHIP.value));
        } else if (Symbol.FOG.equals(symbol)) {
            this.occupyingShip = null;
        }
        this.symbol = symbol;
    }

    public void setCell(Ship occupyingShip) {
        this.symbol = Symbol.SHIP;
        this.occupyingShip = occupyingShip;
    }

    public Ship getOccupyingShip() {
        return occupyingShip;
    }

}
