package Tests;

import Src.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class RegressionTest {
    private Grid grid;
    private Player player;
    private Player computer;
    private Battleship battleship;
    private Ship mockShip1,mockShip2,mockShip3;
    private Random randomizer;

    @BeforeEach
    void setup() {

        //initialize classes
        grid = mock(Grid.class);
        player = mock(Player.class);
        computer = new Player();
        battleship = new Battleship();
        randomizer = new Random();
        mockShip1 = mock(Ship.class);
        mockShip2 = mock(Ship.class);
        mockShip3 = mock(Ship.class);

        // initialize logics
        player.ships = new Ship[]{mockShip1, mockShip2, mockShip3};
    }

    @Test
    void testValidShipPlacementOnGrid() {
        mockShip1.setLocation(0, 0); // A1
        mockShip1.setDirection(0); // HORIZONTAL
        grid.addShip(mockShip1);

        when(mockShip1.isLocationSet()).thenReturn(true);
        when(mockShip1.isDirectionSet()).thenReturn(true);
        if(mockShip1.isLocationSet() && mockShip1.isDirectionSet()){
            // Call numOfShips for ship1, since it has been set -> expected 0
            Assertions.assertEquals(0, player.numOfShipsLeft(), "1 ships are set, so 0 ships should be left.");
        }
        verify(mockShip1).setLocation(0, 0);
        verify(mockShip1).setDirection(0);
    }

    @Test
    public void testOverlappingPlacement() {
        // Place the first ship on the grid
        mockShip1.setLocation(0, 0); // A1
        mockShip1.setDirection(0); // Horizontal
        grid.addShip(mockShip1);

        // Place a second ship that overlaps with the first
        when(mockShip2.isLocationSet()).thenReturn(false); // Simulating invalid location
        when(mockShip2.isDirectionSet()).thenReturn(true);
        mockShip2.setLocation(0, 0); // Same location as the first ship
        mockShip2.setDirection(1); // Horizontal

        // Act
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Redirect System.out to capture printed output
        grid.addShip(mockShip2); // Add the overlapping ship

        // Assert
        // Verify that the second ship's location was not successfully set
        verify(mockShip2, never()).isLocationSet();
    }

    @Test
    public void testValidGuess() {
        // Arrange: Mock a grid and place a ship
        Grid testGrid = new Grid();
        Ship mockShip = mock(Ship.class);

        when(mockShip.isLocationSet()).thenReturn(true);
        when(mockShip.isDirectionSet()).thenReturn(true);
        when(mockShip.getRow()).thenReturn(3);
        when(mockShip.getCol()).thenReturn(4);
        when(mockShip.getLength()).thenReturn(3); // Cruiser

        testGrid.addShip(mockShip);

        // Act: Mark a hit on the ship's location
        testGrid.markHit(3, 4);

        // Assert: Verify the location is marked as a hit
        assertEquals(1, testGrid.getStatus(3, 4), "Src.Location should be marked as a hit.");
    }

    @Test
    public void testInvalidGuessInput() {
        // Arrange
        Grid testGrid = new Grid();

        // Act & Assert: Expect an ArrayIndexOutOfBoundsException for an invalid guess
        ArrayIndexOutOfBoundsException exception = assertThrows(
                ArrayIndexOutOfBoundsException.class,
                () -> testGrid.markHit(10, 10), // Out-of-bounds location
                "Expected an exception for invalid guess input."
        );
        // No specific message validation since ArrayIndexOutOfBoundsException does not have a custom message
    }

    // TC5. Valid Coordinate Conversion
    @Test
    public void testValidCoordinateConversion() {
        // Arrange
        Grid grid = new Grid();

        // Helper function to convert coordinates
        String input = "A1"; // A1 corresponds to (0,0)
        int row = grid.switchCounterToIntegerForArray(input.charAt(0)); // Convert 'A' to 0
        int col = Integer.parseInt(input.substring(1)) - 1; // Convert '1' to 0

        // Act & Assert
        assertEquals(0, row, "Row for 'A1' should be 0.");
        assertEquals(0, col, "Column for 'A1' should be 0.");
    }


    // TC6. Invalid Coordinate Conversion
    @Test
    public void testInvalidCoordinateConversion() {
        // Arrange
        Grid grid = new Grid();
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> grid.switchCounterToIntegerForArray(91), // '[' ASCII value
                "Expected an exception for invalid coordinate conversion."
        );
        assertEquals("ERROR OCCURED IN SWITCH", exception.getMessage());
    }

    //TC7:Valid Direction Setting
    @Test
    public void testValidDirectionSetting() {
        // Arrange
        mockShip3.setDirection(0);
        when(mockShip3.isDirectionSet()).thenReturn(true);
        verify(mockShip3).setDirection(0);
    }


    //TC8. Out-of-Bounds Src.Ship Placement
    @Test
    public void testOutOfBoundsPlacement() {
        // Arrange
        Ship mockShip = mock(Ship.class);

        when(mockShip.isLocationSet()).thenReturn(true);
        when(mockShip.isDirectionSet()).thenReturn(true);
        when(mockShip.getRow()).thenReturn(8);
        when(mockShip.getCol()).thenReturn(8);
        when(mockShip.getLength()).thenReturn(4);

        Grid grid = new Grid();

        // Act & Assert
        ArrayIndexOutOfBoundsException exception = assertThrows(
                ArrayIndexOutOfBoundsException.class,
                () -> grid.addShip(mockShip),
                "Expected an exception for out-of-bounds placement."
        );
        assertEquals("Index 10 out of bounds for length 10", exception.getMessage());
    }


    // TC9. Not Win Condition
    @Test
    public void testNotLostCondition() {
        // Arrange
        Grid grid = new Grid();

        // Simulate marking fewer than 17 hits (e.g., 16 hits)
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 2; j++) {
                grid.markHit(i, j);
            }
        }

        // Act
        boolean hasLost = grid.hasLost();

        // Assert
        assertFalse(hasLost, "Src.Player should not lose if less than 17 hits are made.");
    }

    // TC10. Win Condition
    @Test
    public void testWinCondition() {
        // Arrange
        Grid grid = new Grid();

        // Mark 17 hits
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 2; j++) {
                grid.markHit(i, j);
            }
        }

        // Act
        boolean hasLost = grid.hasLost();

        // Assert
        assertTrue(hasLost, "Src.Player should lose when all ships are sunk.");
    }

    //TC 11: Random Value Generation
    @Test
    public void testRandomValueGeneration() {
        // Arrange
        int min = 1;
        int max = 10;

        // Act
        int randomValue = Randomizer.nextInt(min, max);

        // Assert
        assertTrue(randomValue >= min && randomValue <= max,
                "Random value should be within the range " + min + " to " + max + ".");
    }


}
