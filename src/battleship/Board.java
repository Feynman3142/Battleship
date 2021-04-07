package battleship;

import exceptions.*;

import java.util.HashMap;
import java.util.Map;

public class Board {

    public static final String LEGAL_COORD = "[A-Za-z][1-9]0?";

    private final int numRows;
    private final int numCols;

    private final Cell[][] player1Grid;
    private final Cell[][] player2Grid;

    private final Map<Ship, Integer> player1ShipsHealth = new HashMap<>();
    private final Map<Ship, Integer> player2ShipsHealth = new HashMap<>();

    private int player1TotalHealth;
    private int player2TotalHealth;

    private Board(int numRows, int numCols) {

        this.numRows = numRows;
        this.numCols = numCols;

        this.player1Grid = new Cell[numRows][numCols];
        this.player2Grid = new Cell[numRows][numCols];
        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                this.player1Grid[row][col] = new Cell();
                this.player2Grid[row][col] = new Cell();
            }
        }

        this.player1TotalHealth = 0;
        this.player2TotalHealth = 0;
    }

    public static Board createBoard(int numRows, int numCols) {

        if (numRows != 10 && numCols != 10) {
            throw new IllegalBoardDimensionsException(numRows, numCols);
        }

        return new Board(numRows, numCols);
    }

    public Coord parseCoord(String coord) {
        if (!coord.matches(LEGAL_COORD)) {
            throw new IllegalCoordinatesException(coord, LEGAL_COORD);
        }
        int row = coord.toLowerCase().charAt(0) - 'a';
        int col = Integer.parseInt(coord.substring(1)) - 1;
        if (row >= this.numRows || col >= this.numCols) {
            throw new InvalidCoordinatesException(coord, this.numRows, this.numCols);
        }
        return new Coord(row, col);
    }

    public void placeShip(String startCoordString, String endCoordString, Ship ship, boolean isPlayer1Turn) {

        Cell[][] grid = isPlayer1Turn ? this.player1Grid : this.player2Grid;

        // Things to check:
        // 1. Valid coordinates (can be plotted on the board)
        Coord startCoord = this.parseCoord(startCoordString);
        Coord endCoord = this.parseCoord(endCoordString);

        int startRow = startCoord.getX();
        int startCol = startCoord.getY();
        int endRow = endCoord.getX();
        int endCol = endCoord.getY();

        // 2. Coordinates lie on the same horizontal / vertical line
        boolean isHorizontal = startRow == endRow;
        boolean isVertical = startCol == endCol;
        // Coordinates must either share the same row or same column but not both
        // (that means the coordinates are the same) and not neither (neither same row nor same column)
        if (isHorizontal == isVertical) {
            throw new IllegalShipLocationException(startCoordString, endCoordString);
        }

        if (isHorizontal && startCol > endCol) {
            int temp = startCol;
            startCol = endCol;
            endCol = temp;
        } else if (startRow > endRow) {
            int temp = startRow;
            startRow = endRow;
            endRow = temp;
        }

        // 3. Coordinates correspond to right size of ship
        // If the boat is horizontal, starting and ending columns will differ
        // Id the boat is vertical, starting and ending rows will differ
        int actualShipSize = isHorizontal ? endCol - startCol + 1 : endRow - startRow + 1;
        if (actualShipSize != ship.size) {
            throw new InvalidShipSizeException(actualShipSize, ship);
        }

        // 4. Placing the ship on these coordinates do not put it in contact with any other ship already plotted
        // First loop through all the cells of the ship to be placed
        for (int row = startRow; row <= endRow; ++row) {
            for (int col = startCol; col <= endCol; ++col) {
                // Ensure that the cell or any neighbour of the cell that is 1 cell away
                // (top, bottom, left, right, top-right, top-left, bottom-right, bottom-left)
                // is not occupied by another ship
                for (int checkRow = row - 1; checkRow >= 0 && checkRow < this.numRows && checkRow <= row + 1; ++checkRow) {
                    for (int checkCol = startCol - 1; checkCol >= 0 && checkCol < this.numCols && checkCol <= col + 1; ++checkCol) {
                        if (Symbol.SHIP.equals(grid[checkRow][checkCol].getSymbol())) {
                            throw new InvalidShipLocationException(startCoordString, endCoordString);
                        }
                    }
                }
            }
        }

        for (int row = startRow; row <= endRow; ++row) {
            for (int col = startCol; col <= endCol; ++col) {
                grid[row][col].setCell(ship);
            }
        }

        if (isPlayer1Turn) {
            this.player1ShipsHealth.put(ship, ship.size);
            this.player1TotalHealth += ship.size;
        } else {
            this.player2ShipsHealth.put(ship, ship.size);
            this.player2TotalHealth += ship.size;
        }

    }

    private boolean isGameOver() {
        return this.player1TotalHealth == 0 || this.player2TotalHealth == 0;
    }

    public boolean takeShot(String coordString, boolean isPlayer1Turn) {

        Cell[][] grid = isPlayer1Turn ? this.player2Grid : this.player1Grid;

        Coord coord = this.parseCoord(coordString);
        int row = coord.getX();
        int col = coord.getY();

        int newShipHealth = -1;

        switch (grid[row][col].getSymbol()) {
            case SHIP:
                grid[row][col].setCell(Symbol.HIT);
                Ship occupyingShip = grid[row][col].getOccupyingShip();
                if (isPlayer1Turn) {
                    newShipHealth = this.player2ShipsHealth.get(occupyingShip) - 1;
                    this.player2ShipsHealth.put(occupyingShip, newShipHealth);
                    --this.player2TotalHealth;
                } else {
                    newShipHealth = this.player1ShipsHealth.get(occupyingShip) - 1;
                    this.player1ShipsHealth.put(occupyingShip, newShipHealth);
                    --this.player1TotalHealth;
                }
            case HIT:
                this.displayGrid(false);
                int totalHealth = isPlayer1Turn ? this.player2TotalHealth : this.player1TotalHealth;
                System.out.println(totalHealth == 0 ? "You sank the last ship. You won. Congratulations!" :
                        newShipHealth == 0 ? "You sank a ship!" : "You hit a ship!");
                break;
            case FOG:
            case MISS:
                grid[row][col].setCell(Symbol.MISS);
                this.displayGrid(false);
                System.out.println("You missed");
                break;
            default:
                break;
        }

        return isGameOver();

    }

    public void displayGrid(boolean isPlayer1Turn) {
        this.displayGrid(false, !isPlayer1Turn);
        for (int i = 0; i < 2 * this.numCols; ++i) {
            System.out.print("-");
        }
        System.out.println();
        this.displayGrid(true, isPlayer1Turn);
    }

    public void displayGrid(boolean displayShips, boolean isPlayer1Turn) {

        Cell[][] grid = isPlayer1Turn ? this.player1Grid : this.player2Grid;

        System.out.print("  ");
        for (int i = 1; i <= this.numCols; ++i) {
            System.out.print(i + " ");
        }
        System.out.println();

        char rowHeader = 'A';

        for (int row = 0; row < this.numRows; ++row) {
            System.out.print(rowHeader + " ");
            for (int col = 0; col < this.numCols; ++col) {
                // display the actual cell only if "displayShips" flag is true
                // (display everything!) or if the cell isn't an un-hit SHIP cell
                System.out.print(displayShips || !Symbol.SHIP.equals(grid[row][col].getSymbol()) ?
                        grid[row][col].getSymbol().value + " " : Symbol.FOG.value + " ");
            }
            System.out.println();
            ++rowHeader;
        }

    }
}
