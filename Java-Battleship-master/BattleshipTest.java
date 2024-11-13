import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.lang.reflect.Method;
import java.util.InputMismatchException;
import java.lang.reflect.Field;
import java.util.Random;


public class BattleshipTest {

    private Player player;
    private Player computer;
    private Player user;
    private ByteArrayOutputStream outContent;
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    // Test Randomizer for controlled computer moves
    static class TestRandomizer extends Randomizer {
        private static int[] values;
        private static int index = 0;

        public static void setNextValues(int... vals) {
            values = vals;
            index = 0;
        }

        public static int nextInt(int min, int max) {
            if (values == null || index >= values.length) {
                return min;
            }
            return values[index++];
        }

        public static void reset() {
            values = null;
            index = 0;
        }
    }

    @BeforeEach
    void setUp() {
        player = new Player();
        computer = new Player();
        user = new Player();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        TestRandomizer.reset();
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private void simulateUserInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Battleship.reader = new Scanner(System.in);
    }

    @Test
    void testCompleteGameFlow() {
        String input = String.join("\n",
                // Ship placements (row, col, direction)
                "A", "1", "0",
                "B", "2", "0",
                "C", "3", "0",
                "D", "4", "0",
                "E", "5", "0",
                "",  // End player setup

                // User guesses (row, col)
                "A", "1",  // Correct guess
                "Z", "1",  // Invalid row
                "A", "2",  // Correct guess
                "B", "2",  // Correct guess
                "", "", "",  // Computer moves
                "", "", ""   // Game continuation
        );

        simulateUserInput(input);

        assertThrows(InputMismatchException.class, () -> {
            Battleship.main(new String[]{});
        }, "Expected InputMismatchException to be thrown.");

        String output = outContent.toString();
        assertTrue(output.contains("JAVA BATTLESHIP"), "Game title should appear");
        assertTrue(output.contains("Player SETUP"), "Player setup should complete");
        assertTrue(output.contains("Computer SETUP"), "Computer setup should complete");
    }



    @Test
    void testShipPlacementPhase() {
        String input = String.join("\n",
                // Valid ship placements
                "A", "1", "0",
                "B", "2", "0",
                "C", "3", "0",
                "D", "4", "0",
                "E", "5", "0",
                "", "" // For computer setup
        );

        simulateUserInput(input);

        Player testPlayer = new Player();
        assertDoesNotThrow(() -> {
            Battleship.setup(testPlayer);
        });

        assertEquals(0, testPlayer.numOfShipsLeft(), "All ships should be placed");
    }

    @Test
    void testUserGuessPhase() {
        Player userPlayer = new Player();
        Player computer = new Player();
        // Place a ship on computer's board for testing
        computer.chooseShipLocation(computer.ships[0], 0, 0, 0);

        String input = "A\n1\n";
        simulateUserInput(input);

        String result = Battleship.askForGuess(userPlayer, computer);
        assertTrue(result.contains("HIT") || result.contains("MISS"));
    }

    @Test
    void testComputerGuessPhase() {
        Player computer = new Player();
        Player userPlayer = new Player();
        // Place a ship on user's board
        userPlayer.chooseShipLocation(userPlayer.ships[0], 0, 0, 0);

        // Simulate enter key presses for computer's turn
        String input = "\n\n";
        simulateUserInput(input);

        assertDoesNotThrow(() -> {
            Battleship.compMakeGuess(computer, userPlayer);
        });
    }

    @Test
    void testGameInitialization() {
        String input = String.join("\n",
                // Ship placements
                "A", "1", "0",
                "B", "2", "0",
                "C", "3", "0",
                "D", "4", "0",
                "E", "5", "0",
                "", "" // Computer setup newlines
        );

        simulateUserInput(input);

        assertDoesNotThrow(() -> {
            Player userPlayer = new Player();
            Battleship.setup(userPlayer);
            assertTrue(userPlayer.ships[0].isLocationSet(), "First ship should be placed");
            assertTrue(userPlayer.ships[4].isLocationSet(), "Last ship should be placed");
        });
    }

    @Test
    void testGameEndConditions() {
        Player testPlayer = new Player();
        Player testComputer = new Player();

        // Set up a nearly-won game state
        for (int i = 0; i < Grid.NUM_ROWS - 1; i++) {
            for (int j = 0; j < Grid.NUM_COLS - 1; j++) {
                testPlayer.playerGrid.markHit(i, j);
            }
        }

        assertTrue(testPlayer.playerGrid.hasLost(), "Game should end when all ships are hit");
    }

    @Test
    public void testInvalidInputHandling() {
        String input = String.join("\n",
                "Z",      // Invalid row
                "A",      // Correct row
                "1",      // Correct column
                "0",      // Direction
                "B",      // Valid row
                "11",     // Invalid column
                "2",      // Correct column
                "0",      // Direction
                "C", "3", "0",
                "D", "4", "0",
                "E", "5", "0",
                "", ""    // Computer setup continuation
        );

        simulateUserInput(input);

        assertThrows(InputMismatchException.class, () -> {
            Player userPlayer = new Player();
            Battleship.setup(userPlayer);
        }, "Expected InputMismatchException to be thrown.");
    }











    @Test
    public void testSetupComputerAllShipsPlaced() throws Exception {
        Method setupComputer = Battleship.class.getDeclaredMethod("setupComputer", Player.class);
        setupComputer.setAccessible(true);  // Allow access to the private method

        setupComputer.invoke(null, computer);  // Invoke the private static method

        // Validate all ships are placed
        for (Ship ship : computer.ships) {
            assertTrue(ship.isLocationSet(), "Each ship should have a valid location.");
            assertTrue(ship.isDirectionSet(), "Each ship should have a valid direction.");
        }

        assertEquals(0, computer.numOfShipsLeft(), "All ships should be placed.");
    }

    @Test
    public void testNoOverlappingShips() throws Exception {
        Method setupComputer = Battleship.class.getDeclaredMethod("setupComputer", Player.class);
        setupComputer.setAccessible(true);

        setupComputer.invoke(null, computer);

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (computer.playerGrid.hasShip(row, col)) {
                    assertTrue(verifySingleOccupancy(row, col), "There should be no overlapping ships.");
                }
            }
        }
    }

    @Test
    public void testShipsWithinBounds() throws Exception {
        Method setupComputer = Battleship.class.getDeclaredMethod("setupComputer", Player.class);
        setupComputer.setAccessible(true);

        setupComputer.invoke(null, computer);

        for (Ship ship : computer.ships) {
            int startRow = ship.getRow();
            int startCol = ship.getCol();
            int length = ship.getLength();
            int direction = ship.getDirection();

            if (direction == Ship.HORIZONTAL) {
                assertTrue(startCol + length <= 10, "Horizontal ship should fit within the grid.");
            } else if (direction == Ship.VERTICAL) {
                assertTrue(startRow + length <= 10, "Vertical ship should fit within the grid.");
            }
        }
    }

    private boolean verifySingleOccupancy(int row, int col) {
        int count = 0;
        for (Ship ship : computer.ships) {
            if (ship.isLocationSet() && covers(ship, row, col)) {
                count++;
            }
        }
        return count <= 1;
    }

    private boolean covers(Ship ship, int row, int col) {
        int startRow = ship.getRow();
        int startCol = ship.getCol();
        int length = ship.getLength();
        int direction = ship.getDirection();

        if (direction == Ship.HORIZONTAL) {
            return row == startRow && col >= startCol && col < startCol + length;
        } else if (direction == Ship.VERTICAL) {
            return col == startCol && row >= startRow && row < startRow + length;
        }
        return false;
    }


    @Test
    public void testPlayerShipsCorrectlyPlaced() {
        // Simulate user input for 5 ships
        String simulatedInput =
                "A\n1\n0\n" +  // Ship 1: A1, horizontal
                        "B\n2\n1\n" +  // Ship 2: B2, vertical
                        "C\n3\n0\n" +  // Ship 3: C3, horizontal
                        "D\n4\n1\n" +  // Ship 4: D4, vertical
                        "E\n5\n0\n";   // Ship 5: E5, horizontal

        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        try {
            Battleship.reader = new Scanner(System.in);  // Reinitialize Scanner with simulated input

            // Call the setup method
            Battleship.setup(player);

            // Validate that all ships have been placed
            int numShipsPlaced = 0;
            for (Ship ship : player.ships) {
                assertTrue(ship.isLocationSet() && ship.isDirectionSet(),
                        "Each ship must have a valid location and direction.");
                numShipsPlaced++;
            }

            assertEquals(5, numShipsPlaced, "All 5 ships should be placed.");

        } finally {
            System.setIn(originalIn);  // Restore original System.in
        }
    }

    @Test
    public void testShipGridIntegrity() {
        // Simulate user input for placing 5 ships
        String simulatedInput =
                "A\n1\n0\n" +  // Ship 1: A1, horizontal
                        "B\n2\n1\n" +  // Ship 2: B2, vertical
                        "C\n3\n0\n" +  // Ship 3: C3, horizontal
                        "D\n4\n1\n" +  // Ship 4: D4, vertical
                        "E\n5\n0\n";   // Ship 5: E5, horizontal

        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        try {
            Battleship.reader = new Scanner(System.in);  // Reinitialize Scanner for simulated input

            // Call the setup method to simulate manual ship placement
            Battleship.setup(player);

            // Validate that no ship is out of grid bounds
            for (Ship ship : player.ships) {
                int row = ship.getRow();
                int col = ship.getCol();
                assertTrue(row >= 0 && row < 10, "Ship's row should be within grid bounds.");
                assertTrue(col >= 0 && col < 10, "Ship's column should be within grid bounds.");
            }

        } finally {
            System.setIn(originalIn);  // Restore original System.in
        }
    }





        @Test
    public void testAskForGuess_Hit() {
        // Arrange
        Player userPlayer = new Player();
        Player opponent = new Player();

        // Place a ship at a known location on the opponent's grid (for testing a hit)
        opponent.playerGrid.setShip(4, 5, true); // Place a ship at (4,5) which is "E6"

        // Simulate user input for the guess: "E" for row, "6" for column
        String simulatedInput = "E\n6\n";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Ensure Scanner reads from the new InputStream
        Battleship.reader = new Scanner(System.in);  // Reset the scanner to read from the simulated input

        // Act
        String result = Battleship.askForGuess(userPlayer, opponent);

        // Assert
        assertEquals("** USER HIT AT E6 **", result, "Expected a hit at E6");

        // Cleanup
        System.setIn(originalIn); // Restore the original System.in
    }

    @Test
    public void testAskForGuess_Miss() {
        // Arrange
        Player userPlayer = new Player();
        Player opponent = new Player();

        // Ensure no ship is present at (2, 3) for testing a miss
        // "C4" is the input: row "C" and column "4"
        String simulatedInput = "C\n4\n";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Ensure Scanner reads from the new InputStream
        Battleship.reader = new Scanner(System.in);  // Reset the scanner to read from the simulated input

        // Act
        String result = Battleship.askForGuess(userPlayer, opponent);

        // Assert
        assertEquals("** USER MISS AT C4 **", result, "Expected a miss at C4");

        // Cleanup
        System.setIn(originalIn); // Restore the original System.in
    }




    @Test
    public void testConvertLetterToInt() {
        assertEquals(0, Battleship.convertLetterToInt("A"));
        assertEquals(1, Battleship.convertLetterToInt("B"));
        assertEquals(2, Battleship.convertLetterToInt("C"));
        assertEquals(3, Battleship.convertLetterToInt("D"));
        assertEquals(4, Battleship.convertLetterToInt("E"));
        assertEquals(5, Battleship.convertLetterToInt("F"));
        assertEquals(6, Battleship.convertLetterToInt("G"));
        assertEquals(7, Battleship.convertLetterToInt("H"));
        assertEquals(8, Battleship.convertLetterToInt("I"));
        assertEquals(-1, Battleship.convertLetterToInt("Z"));
        assertEquals(9, Battleship.convertLetterToInt("J"));

    }

    @Test
    public void testConvertIntToLetter() {
        assertEquals("A", Battleship.convertIntToLetter(0));
        assertEquals("B", Battleship.convertIntToLetter(1));
        assertEquals("C", Battleship.convertIntToLetter(2));
        assertEquals("D", Battleship.convertIntToLetter(3));
        assertEquals("E", Battleship.convertIntToLetter(4));
        assertEquals("F", Battleship.convertIntToLetter(5));
        assertEquals("G", Battleship.convertIntToLetter(6));
        assertEquals("H", Battleship.convertIntToLetter(7));
        assertEquals("I", Battleship.convertIntToLetter(8));
        assertEquals("J", Battleship.convertIntToLetter(9));
        assertEquals("Z", Battleship.convertIntToLetter(10)); // Invalid case
    }

    @Test
    public void testConvertUserColToProCol() {
        assertEquals(0, Battleship.convertUserColToProCol(1));
        assertEquals(1, Battleship.convertUserColToProCol(2));
        assertEquals(2, Battleship.convertUserColToProCol(3));
        assertEquals(3, Battleship.convertUserColToProCol(4));
        assertEquals(4, Battleship.convertUserColToProCol(5));
        assertEquals(5, Battleship.convertUserColToProCol(6));
        assertEquals(6, Battleship.convertUserColToProCol(7));
        assertEquals(7, Battleship.convertUserColToProCol(8));
        assertEquals(8, Battleship.convertUserColToProCol(9));
        assertEquals(9, Battleship.convertUserColToProCol(10));
        assertEquals(-1, Battleship.convertUserColToProCol(11)); // Invalid case
    }

    @Test
    public void testConvertCompColToRegular() {
        assertEquals(1, Battleship.convertCompColToRegular(0));
        assertEquals(2, Battleship.convertCompColToRegular(1));
        assertEquals(3, Battleship.convertCompColToRegular(2));
        assertEquals(4, Battleship.convertCompColToRegular(3));
        assertEquals(5, Battleship.convertCompColToRegular(4));
        assertEquals(6, Battleship.convertCompColToRegular(5));
        assertEquals(7, Battleship.convertCompColToRegular(6));
        assertEquals(8, Battleship.convertCompColToRegular(7));
        assertEquals(9, Battleship.convertCompColToRegular(8));
        assertEquals(10, Battleship.convertCompColToRegular(9));
        assertEquals(-1, Battleship.convertCompColToRegular(10));
    }

    @Test
    public void testHasErrorsHorizontal() {
        Player player = new Player();
        Ship ship = new Ship(4);
        player.ships[0] = ship;
        assertFalse(Battleship.hasErrors(0, 0, 0, player, 0));
        assertTrue(Battleship.hasErrors(0, 7, 0, player, 0));
    }
    @Test
    public void testOverlapHorizontal() {
        Player player = new Player();
        Ship ship1 = new Ship(4); // Ship of length 4
        player.ships[0] = ship1;

        // Place the first ship at (0,0) horizontally
        player.ships[0].setLocation(0, 0);
        player.ships[0].setDirection(0); // Horizontal
        player.playerGrid.addShip(player.ships[0]);

        // Place another ship that overlaps at (0, 2)
        Ship ship2 = new Ship(3); // Another ship of length 3
        player.ships[1] = ship2;

        // This should return true due to overlap at (0, 2)
        assertTrue(Battleship.hasErrors(0, 2, 0, player, 1)); // Overlap at (0,2)
    }

    @Test
    public void testHasErrorsVertical() {
        Player player = new Player();
        Ship ship = new Ship(4);
        player.ships[0] = ship;
        assertFalse(Battleship.hasErrors(0, 0, 1, player, 0));
        assertTrue(Battleship.hasErrors(7, 0, 1, player, 0));
    }

    @Test
    public void testOverlapVertical() {
        Player player = new Player();
        Ship ship1 = new Ship(4); // Ship of length 4
        player.ships[0] = ship1;

        // Place the first ship at (0,0) vertically
        player.ships[0].setLocation(0, 0);
        player.ships[0].setDirection(1); // Vertical
        player.playerGrid.addShip(player.ships[0]);

        // Place another ship that overlaps at (2, 0)
        Ship ship2 = new Ship(3); // Another ship of length 3
        player.ships[1] = ship2;

        // This should return true due to overlap at (2, 0)
        assertTrue(Battleship.hasErrors(2, 0, 1, player, 1)); // Overlap at (2,0)
    }

    @Test
    public void testOverlapHorizontalComp() {
        Player computer = new Player();
        Ship ship1 = new Ship(4); // Ship of length 4
        computer.ships[0] = ship1;

        // Place the first ship horizontally at (0, 0)
        computer.ships[0].setLocation(0, 0);
        computer.ships[0].setDirection(0); // Horizontal
        computer.playerGrid.addShip(computer.ships[0]);

        // Now try placing another ship horizontally starting at (0, 2), which should overlap
        Ship ship2 = new Ship(3); // Another ship of length 3
        computer.ships[1] = ship2;

        assertTrue(Battleship.hasErrorsComp(0, 2, 0, computer, 1)); // Overlap, should return true
    }

    @Test
    public void testVerticalOverlap() {
        Player player = new Player();
        Ship ship1 = new Ship(4); // Ship of length 4
        player.ships[0] = ship1;

        // Place the first ship vertically at (0, 0)
        player.ships[0].setLocation(0, 0);
        player.ships[0].setDirection(1); // Vertical direction
        player.playerGrid.addShip(player.ships[0]);

        // Now place a second ship vertically starting at (2, 0), which should overlap
        Ship ship2 = new Ship(3); // Another ship of length 3
        player.ships[1] = ship2;

        // Check if the new ship overlaps with the first one (it should)
        assertTrue(Battleship.hasErrorsComp(2, 0, 1, player, 1)); // Overlap, should return true
    }
    @Test
    public void testVerticalShipOutOfBounds() {
        Player player = new Player();
        Ship ship = new Ship(4); // Ship of length 4
        player.ships[0] = ship;

        // Attempt to place a ship vertically starting at row 8, which should go out of bounds
        // Length + row = 4 + 8 = 12, which exceeds the grid boundary (10).
        assertTrue(Battleship.hasErrorsComp(8, 0, 1, player, 0)); // Ship goes out of bounds
    }

    @Test
    public void testHasErrorsComp() {
        Player player = new Player();
        Ship ship = new Ship(3); // Ship of length 3
        player.ships[0] = ship;

        // Test no errors, valid placement
        assertFalse(Battleship.hasErrorsComp(0, 0, 0, player, 0)); // No errors, ship fits horizontally

        // Test errors, ship out of bounds horizontally
        assertTrue(Battleship.hasErrorsComp(0, 8, 0, player, 0)); // Ship doesn't fit horizontally
    }


}

