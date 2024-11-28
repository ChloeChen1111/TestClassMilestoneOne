import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PlayerTest{
    private static Player player;
    private static Ship mockShip, mockShip1, mockShip2 ,mockShip3, mockShip4, mockShip5, mockShip6;
    private static Grid mockGrid, mockPlayerGrid, mockOppGrid;

    @BeforeAll
    static void setUp() throws Exception {
        try{
            // Mock general Ship and Grid classes for testing
            mockShip = mock(Ship.class);
            mockGrid = mock(Grid.class);

            //mock individual ship for testing
            mockShip1 = mock(Ship.class);
            mockShip2 = mock(Ship.class);
            mockShip3 = mock(Ship.class);
            mockShip4 = mock(Ship.class);
            mockShip5 = mock(Ship.class);

            mockPlayerGrid = mock(Grid.class); // Ensure player grid is mocked
            mockOppGrid = mock(Grid.class); // Ensure opponent grid is mocked

            // Initialize the player object
            player = new Player();

            // Inject mocked Grids for testing purposes
            // represents the player’s grid where ships are placed.
            player.playerGrid = mockPlayerGrid;
            // represents the opponent’s grid where the player makes guesses during gameplay.
            player.oppGrid = mockOppGrid;

            // Inject mocked Ships (as if they were initialized with different lengths)
            player.ships = new Ship[]{mockShip1, mockShip2, mockShip3, mockShip4, mockShip5};
            setAllShipsLocationAndDirection();

        } catch (Exception e) {
            // If there's an issue, throw an exception that will prevent the tests from running
            throw new Exception("Setup failed: " + e.getMessage(), e);
        }
    }

    // Helper method to set all ships with location and direction
    private static void setAllShipsLocationAndDirection() {

        for (Ship ship : player.ships) {
            when(ship.isLocationSet()).thenReturn(true);
            when(ship.isDirectionSet()).thenReturn(true);
        }
        assertEquals(0, player.numOfShipsLeft(), "All ships are set, so 0 ships should be left.");

    }


    @Test
    public void testNumOfShipsLeft_NoShipsSet() {
        for (Ship ship : player.ships) {
            when(ship.isLocationSet()).thenReturn(false);
            when(ship.isDirectionSet()).thenReturn(false);
        }
        assertEquals(5, player.numOfShipsLeft(), "No ships are set, so all 5 ships should be left.");
    }

    @AfterAll
    static void tearDown() throws Exception {
        try{
            // Nullify all objects to release memory and clean up resources
            player = null;;
            mockPlayerGrid = null;
            mockOppGrid = null;
        } catch (Exception e) {
            // If there's an issue, throw an exception that will prevent the tests from running
            throw new Exception("TearDown failed: " + e.getMessage(), e);
        }
    }

    /*
    *
    */
    @Test
    public void testAddShips(){
        player.addShips();
        for(int i = 0; i < player.ships.length; i++){
            mockPlayerGrid.addShip(player.ships[i]);
        }
    }

    // Test to check if all ship has been set
    @Test
    public void testNumOfShipsLeft_AllShipsSet() {
        setAllShipsLocationAndDirection();
        // Since all ships are set, no ships should be left to place
        assertEquals(0, player.numOfShipsLeft(), "All ships are set, so 0 ships should be left.");
    }

    @Test
    public void testNumOfShipsLeft_SomeShipsSet() throws Exception {
        // reset logic for testing purposes
        tearDown();
        // set up once again
        setUp();

        // Set the behavior for mockShip1
        mockShip1.setLocation(1, 1);  // Simulate setting the location
        mockShip1.setDirection(0);    // Simulate setting the direction
        // First, stub the behavior of isLocationSet and isDirectionSet
        when(mockShip1.isLocationSet()).thenReturn(true);
        when(mockShip1.isDirectionSet()).thenReturn(true);
        if(mockShip1.isLocationSet() && mockShip1.isDirectionSet()){
            // Call numOfShips for ship1, since it has been set -> expected 0
            assertEquals(0, player.numOfShipsLeft(), "1 ships are set, so 0 ships should be left.");
        }

        // Set the behavior for mockShip2
        mockShip2.setLocation(5, 8);
        mockShip2.setDirection(1);
        if(mockShip2.isLocationSet() && mockShip2.isDirectionSet()){
            // Call numOfShips for ship1, since it has been set -> expected 0
            assertEquals(0, player.numOfShipsLeft(), "1 ships are set, so 0 ships should be left.");
        }


        // Set the behavior for mockShip3 (location and direction not set)
        when(mockShip3.isLocationSet()).thenReturn(false);
        when(mockShip3.isDirectionSet()).thenReturn(false);

        // Set the behavior for mockShip4
        mockShip4.setLocation(3, 3);
        mockShip4.setDirection(2);
        if(mockShip4.isLocationSet() && mockShip4.isDirectionSet()){
            // Call numOfShips for ship1, since it has been set -> expected 0
            assertEquals(1, player.numOfShipsLeft(), "1/2 total 2 ships are set, so 1 ships should be left.");
        }


        // Set the behavior for mockShip5 (location and direction not set)
        when(mockShip5.isLocationSet()).thenReturn(false);
        when(mockShip5.isDirectionSet()).thenReturn(false);

        // Verify that the setters were called
        verify(mockShip1).setLocation(1, 1);
        verify(mockShip1).setDirection(0);
        verify(mockShip2).setLocation(5, 8);
        verify(mockShip2).setDirection(1);
        verify(mockShip4).setLocation(3, 3);
        verify(mockShip4).setDirection(2);

        // Call numOfShipsLeft()
        assertEquals(2, player.numOfShipsLeft(), "3 ships are set, so 2 ships should be left.");
    }

    // this test is intentionally to catch error
    @Test
    public void testNumOfShipsLeft_ThrowsErrorInSetup() throws Exception {
        player = null;
        // We expect this test to fail because setUp() will not initialize the player object
        assertThrows(Exception.class, () -> {
            player.numOfShipsLeft();  // This will throw a NullPointerException due to player being null
        });
        // reset everything
        tearDown();
        setUp();
    }

    /* These following testcases follow a decision table to make sure all edge cases player and grid has been successfully tested
     TC1: Success (L+D = True, S = True)
     TC2: Failure (L+D = True, S = False)
     TC3: Failure (L+D = False, S = True) - NOT A POSSIBLE TESTCASE because if location is not set, then how come space is occupied
     TC4: Failure (L+D = False, S = False)

    */

    //TC1
    @Test
    public void testChooseShipLocation_Success() throws Exception {
        when(mockShip.isLocationSet()).thenReturn(true);
        when(mockShip.isDirectionSet()).thenReturn(true);
        when(mockPlayerGrid.hasShip(anyInt(), anyInt())).thenReturn(true);

        player.chooseShipLocation(mockShip, 3, 4, 1);

        verify(mockShip).setLocation(3, 4);
        verify(mockShip).setDirection(1);
    }

    //TC2
    @Test
    public void testChooseShipLocation_NoSpaceAvailable() {
        // Test Case 2: Location and direction set, no space available
        when(mockShip.isLocationSet()).thenReturn(true);
        when(mockShip.isDirectionSet()).thenReturn(true);
        when(mockPlayerGrid.hasShip(anyInt(), anyInt())).thenReturn(false);

        player.chooseShipLocation(mockShip, 3, 4, 1);

        verify(mockShip).setLocation(3, 4);
        verify(mockShip).setDirection(1);
        verify(mockGrid, never()).addShip(mockShip);  // Ship not added since no space is available
    }

    //TC4: Location is set, but direction is not set, and no space is available. The ship cannot be placed.
    @Test
    public void testChooseShipLocation_DirectionNotSet_NoSpaceAvailable() throws Exception {
        // Test Case 3: Location set, direction not set
        when(mockShip.isLocationSet()).thenReturn(false);
        when(mockShip.isDirectionSet()).thenReturn(false);
        when(mockPlayerGrid.hasShip(anyInt(), anyInt())).thenReturn(false);

        player.chooseShipLocation(mockShip, 3, 4, 1);
        // since the player choose location, so the set has been change
        if(mockShip.isLocationSet() && mockShip.isDirectionSet()){
            // Verify that the ship was not added to the grid
            verify(mockPlayerGrid, never()).addShip(mockShip);  // Ship not added since no space is available
        }
    }

}
