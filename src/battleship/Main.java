package battleship;

import exceptions.GameException;

import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static Board board = null;
    static int stageNumber = 0;
    static int shipNumber = 0;
    static boolean shouldEndGame = false;
    static boolean isPlayer1Turn = true;

    public static void main(String[] args) {

        while(!shouldEndGame) {
            switch(Stage.values()[stageNumber]) {
                case CREATE_BOARD:
                    board = Board.createBoard(10, 10);
                    System.out.println("Player 1, place your ships on the game field");
                    board.displayGrid(true, isPlayer1Turn);
                    ++stageNumber;
                    break;
                case PLACE_SHIPS:
                    Ship ship = Ship.values()[shipNumber];
                    System.out.printf("Enter the coordinates of the %s (%d cells):\n", ship.label, ship.size);
                    String[] coords = scanner.nextLine().split("\\s+");
                    try {
                        board.placeShip(coords[0], coords[1], ship, isPlayer1Turn);
                        board.displayGrid(true, isPlayer1Turn);
                        ++shipNumber;
                        if (shipNumber == Ship.values().length) {
                            if (isPlayer1Turn) {
                                isPlayer1Turn = false;
                                shipNumber = 0;
                                waitForNextPlayer();
                                System.out.println("Player 2, place your ships on the game field");
                                board.displayGrid(true, isPlayer1Turn);
                            } else {
                                isPlayer1Turn = true;
                                waitForNextPlayer();
                                ++stageNumber;
                            }
                        }
                    } catch (GameException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case TAKE_SHOTS:
                    board.displayGrid(isPlayer1Turn);
                    System.out.printf("%s, it's your turn\n", isPlayer1Turn ? "Player 1" : "Player 2");
                    String coord = scanner.nextLine().trim();
                    try {
                        shouldEndGame = board.takeShot(coord, isPlayer1Turn);
                        if (!shouldEndGame) {
                            waitForNextPlayer();
                            isPlayer1Turn = !isPlayer1Turn;
                        }
                    } catch (GameException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
            }
        }
    }

    private static void waitForNextPlayer() {
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
    }
}
