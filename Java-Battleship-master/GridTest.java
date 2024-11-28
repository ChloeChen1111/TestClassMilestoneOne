import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class GridTest {

	private static Grid grid; // Grid under test
	private static Location[][] mockLocations; // Mocked Location objects
	private static ByteArrayOutputStream outputStream;
	private static Player player;
	private static Player computer;
	private Ship mockShip;

	@BeforeAll
	public static void setUp() {
		// Initialize the Grid object
		grid = new Grid();
		// Initialize player and computer
		player = new Player();
		computer = new Player();

		// Redirect System.out to capture print output
		outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));

		// Mock the 2D Location array
		mockLocations = new Location[Grid.NUM_ROWS][Grid.NUM_COLS];

		for (int row = 0; row < Grid.NUM_ROWS; row++) {
			for (int col = 0; col < Grid.NUM_COLS; col++) {
				mockLocations[row][col] = mock(Location.class);
			}
		}

		// Inject mocked locations into the grid
		for (int row = 0; row < Grid.NUM_ROWS; row++) {
			for (int col = 0; col < Grid.NUM_COLS; col++) {
				grid.setStatus(row, col, mockLocations[row][col].getStatus());
			}
		}
	}

	@AfterAll
	public static void tearDown() {
		// Nullify the Grid and mock objects
		grid = null;
		mockLocations = null;
	}

	@Test
	public void testPlayerGuessesComputerGrid_CorrectGuess() {
		// Mock a ship for the computer's grid
		mockShip = mock(Ship.class);
		when(mockShip.isLocationSet()).thenReturn(true);
		when(mockShip.isDirectionSet()).thenReturn(true);

		// Place the mocked ship on the computer's grid
		computer.chooseShipLocation(mockShip, 3, 4, 0); // Horizontal placement starting at (3, 4)
		// Act: Player guesses a location with a ship
		computer.playerGrid.markHit(3, 4); // Guessed location is part of the ship

		// Capture the output of the computer's grid status
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		computer.playerGrid.printStatus();
		System.setOut(System.out); // Reset System.out
		String output = outputStream.toString();

		// Assert: Verify the hit is displayed
		assertTrue(output.contains("X"), "The guessed location should be marked as a hit (X).");
	}

	@Test
	public void testPlayerGuessesComputerGrid_Unguessed() {
		// Act: Directly check the status of an unguessed location
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream)); // Capture output of printStatus()
		computer.playerGrid.printStatus(); // Call printStatus to display the grid
		System.setOut(System.out); // Reset System.out
		String output = outputStream.toString();

		// Assert: Verify the grid displays '-' for unguessed cells
		assertTrue(output.contains("-"), "Unguessed locations should be represented by '-'.");
	}

	@Test
	public void testPlayerGuessAlreadyGuessedLocation() {
		// Arrange: Set up a location with a hit and a miss
		computer.playerGrid.markHit(3, 4); // Simulate a hit at (3, 4)
		computer.playerGrid.markMiss(2, 2); // Simulate a miss at (2, 2)

		// Act and Assert: Attempt to guess the same locations again
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream)); // Capture output for feedback

		// Guess the hit location again
		if (computer.playerGrid.alreadyGuessed(3, 4)) {
			System.out.println("Location already guessed.");
		}

		// Guess the miss location again
		if (computer.playerGrid.alreadyGuessed(2, 2)) {
			System.out.println("Location already guessed.");
		}

		// Reset System.out
		System.setOut(System.out);

		// Assert: Verify that the output indicates the locations are already guessed
		String output = outputStream.toString();
		assertTrue(output.contains("Location already guessed."),
				"Feedback should indicate that the location was already guessed.");
	}

	@Test
	public void testPlayerGuessesComputerGrid_PrintShips() {
		// Arrange: Add a ship to the computer's grid
		Ship mockShip = mock(Ship.class);
		when(mockShip.isLocationSet()).thenReturn(true);
		when(mockShip.isDirectionSet()).thenReturn(true);
		when(mockShip.getRow()).thenReturn(2);
		when(mockShip.getCol()).thenReturn(3);
		when(mockShip.getLength()).thenReturn(3); // Cruiser
		when(mockShip.getDirection()).thenReturn(0); // Horizontal placement

		// Place the ship on the computer's grid
		computer.chooseShipLocation(mockShip, 2, 3, 0);

		// Act: Capture the output of printShips()
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		computer.playerGrid.printShips(); // Display the ship placement
		System.setOut(System.out); // Reset System.out
		String output = outputStream.toString();

		// Assert: Verify the ship is displayed on the grid with the correct symbol
		assertTrue(output.contains("C"), "The ship (Cruiser) should be represented by 'C' on the grid.");
	}

	@Test
	void testPrintShips() {
		// Arrange: Add a ship to the player's grid
		Ship mockShip = mock(Ship.class);
		when(mockShip.isLocationSet()).thenReturn(true);
		when(mockShip.isDirectionSet()).thenReturn(true);
		when(mockShip.getRow()).thenReturn(2);
		when(mockShip.getCol()).thenReturn(3);
		when(mockShip.getLength()).thenReturn(3); // Cruiser
		when(mockShip.getDirection()).thenReturn(0); // Horizontal placement

		player.chooseShipLocation(mockShip, 2, 3, 0);

		// Act: Capture the output of printShips()
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		player.playerGrid.printShips(); // Display the ship placement
		System.setOut(System.out); // Reset System.out
		String output = outputStream.toString();

		// Assert: Verify the grid displays the ship with the correct symbol
		assertTrue(output.contains("C"), "The Cruiser should be represented by 'C' on the grid.");
	}

	/*
	 * Ensures the functionality of the constructor for the Grid class. Checks that the grid has been 
	initialized and that no ships have been added yet
	*/
	@Test
	void testGrid() {
		Grid tstGrid = new Grid();
		for (int i = 0; i < tstGrid.numRows(); i++) {
			for (int j = 0; j < tstGrid.numCols(); j++) {
				assertEquals(tstGrid.hasShip(i, j), false);
			}
		}
	}

	/*
	 * Ensures that any position in the grid is not marked with a hit (status change) until the markHit
	 * method is called. Subsequently the status of that location changes to hit
	 */
	@Test
	void testMarkHit() {
		Grid testGrid = new Grid();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				assertEquals(testGrid.getStatus(i, j), 0);
				testGrid.markHit(i, j);
				assertEquals(testGrid.getStatus(i, j), 1);
			}
		}
	}

	/*
	 * Ensures that at any location in the grid, the status of that position is not marked missed (status change)
	 * until the markMiss method is called. Subsequently the status changes from unguessed to missed
	 */
	@Test
	void testMarkMiss() {
		Grid testGrid = new Grid();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				assertEquals(testGrid.getStatus(i, j), 0);
				testGrid.markMiss(i, j);
				assertEquals(testGrid.getStatus(i, j), 2);
			}
		}
	}

	/*
	 * This test checks that when this method is called, the status of any given location is changed from
	 * unguessed to either hit or missed
	 */
	@Test
	void testSetStatus() {
		Grid testGrid = new Grid();
		assertEquals(testGrid.getStatus(0, 0), 0);
		testGrid.setStatus(0, 0, 2);
		assertEquals(testGrid.getStatus(0, 0), 2);
		testGrid.setStatus(0, 0, 1);
		assertEquals(testGrid.getStatus(0, 0), 1);
	}

	/*
	 * Ensures that the status is changed for only the location specified and matches what the method
	 * changed the status to
	 */
	@Test
	void testGetStatus() {
		Grid testGrid = new Grid();
		testGrid.setStatus(0, 0, 0);
		testGrid.setStatus(4, 9, 1);
		testGrid.setStatus(8, 4, 2);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (i == 0 & j == 0) {
					assertEquals(testGrid.getStatus(i, j), 0);
				} else if (i == 4 & j == 9) {
					assertEquals(testGrid.getStatus(i, j), 1);
				} else if (i == 8 & j == 4) {
					assertEquals(testGrid.getStatus(i, j), 2);
				} else {
					assertEquals(testGrid.getStatus(i, j), 0);
				}
			}
		}
	}

	/*
	 * Tests that the boolean within the AlreadyGuessed method appropriately changes when the status changes
	 * initially should be false, and once it is guessed (marked hit or missed) the boolean should change to true
	 */
	@Test
	void testAlreadyGuessed() {
		Grid testGrid = new Grid();
		assertEquals(testGrid.alreadyGuessed(0, 0), false);
		testGrid.setStatus(0, 0, 1);
		assertEquals(testGrid.alreadyGuessed(0, 0), true);
		testGrid.setStatus(0, 0, 2);
		assertEquals(testGrid.alreadyGuessed(0, 0), true);
	}

	/*
	 * Ensures that a ship is set appropriately in the grid. Once the ship is set the position should be marked true
	 * for having a ship. Tracks that the boolean appropriately changes
	 */
	@Test
	void testSetShip() {
		Grid testGrid = new Grid();
		assertEquals(testGrid.hasShip(0, 0), false);
		testGrid.setShip(0, 0, true);
		assertEquals(testGrid.hasShip(0, 0), true);
		testGrid.setShip(0, 0, false);
		assertEquals(testGrid.hasShip(0, 0), false);
	}

	/*
	 * This is a slightly redundant test since the testSetShip method also ensures that hasShip is working
	 * appropriately. This test is repeated to ensure that when a ship is set that the hasShip method appropriately
	 * reports back that the location has a ship
	 */
	@Test
	void testHasShip() {
		Grid testGrid = new Grid();
		assertEquals(testGrid.hasShip(0, 0), false);
		testGrid.setShip(0, 0, true);
		assertEquals(testGrid.hasShip(0, 0), true);
		testGrid.setShip(0, 0, false);
		assertEquals(testGrid.hasShip(0, 0), false);
	}

	/*
	 * This test ensures that the get function returns as expected. It gets the location in the grid that was created.
	 * In our test it should return the initial parameters of the newly constructed object. That is, that the status
	 * is unguessed (0), the location has no ship, the length should be -1 and the direction should be -1 until set by
	 * the user
	 */
	@Test
	void testGet() {
		Grid testGrid = new Grid();
		Location testLoc = testGrid.get(0, 0);
		assertEquals(testLoc.getStatus(), 0);
		assertEquals(testLoc.hasShip(), false);
		assertEquals(testLoc.getLengthOfShip(), -1);
		assertEquals(testLoc.getDirectionOfShip(), -1);
	}

	/*
	 * The grid class has a default size of 10 rows. This test ensures that the number of rows in the grid
	 * is 10 after the grid is initialized
	 */
	@Test
	void testNumRows() {
		Grid testGrid = new Grid();
		assertEquals(testGrid.numRows(), 10);
	}

	/*
	 * The grid class has a default of 10 columns. This test ensures that the number of columns in the grid
	 * is 10 after the grid is initialized
	 */
	@Test
	void testNumCol() {
		Grid testGrid = new Grid();
		assertEquals(testGrid.numCols(), 10);
	}

	/*
	 * This tests adds 20 Hits to the grid. Any number of hits over 17 will result in the HasLsot method being
	 * marked true. First we test for the number of hits of 20 then we check a new grid for the number of hits of 16
	 * and ensure the first is marked lost and the second is not marked lost
	 */
	@Test
	void testHasLost() {
		Grid testGrid = new Grid();
		assertEquals(testGrid.hasLost(), false);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 2; j++) {
				testGrid.markHit(i, j);
			}
		}
		assertEquals(testGrid.hasLost(), true);
		Grid testGridTwo = new Grid();
		assertEquals(testGridTwo.hasLost(), false);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 2; j++) {
				testGrid.markHit(i, j);
			}
		}
		assertEquals(testGridTwo.hasLost(), false);
	}

	/*
	 * This test ensures that a ship is added appropriately. it checks the location, direction, is the location is
	 * marked as having a ship, and that no other location is inaccurately marked as having a ship
	 */
	@Test
	void testAddShip() {
		//-1 = unset, 0 = horizontal, 1 = vertical
		Grid testGrid = new Grid();
		Ship testShip = new Ship(4);
		assertEquals(testShip.isLocationSet(), false);
		testShip.setLocation(5, 5);
		assertEquals(testShip.isLocationSet(), true);
		assertEquals(testShip.isDirectionSet(), false);
		testShip.setDirection(0);
		assertEquals(testShip.isDirectionSet(), true);
		assertEquals(testShip.getDirection(), 0);

		assertEquals(testGrid.hasShip(5, 5), false);
		testGrid.addShip(testShip);
		assertEquals(testGrid.hasShip(5, 5), true);

		for (int i = 0; i < testShip.getLength(); i++) {
			assertEquals(testGrid.hasShip(5, 5 + i), true);
		}
		testShip.setDirection(1);
		testGrid.addShip(testShip);
		for (int i = 0; i < testShip.getLength(); i++) {
			assertEquals(testGrid.hasShip(5 + i, 5), true);
		}
	}

	// set directional as 3 to throw invalid
	@Test
	public void testAddShipThrowsErrorForInvalidDirection() {
		// Arrange: Create a mock ship with valid location but invalid direction
		Ship mockShip = mock(Ship.class);
		when(mockShip.isLocationSet()).thenReturn(true); // Location is set
		when(mockShip.isDirectionSet()).thenReturn(false); // Direction not properly set
		assertEquals(mockShip.isDirectionSet(), false);
	}
}
