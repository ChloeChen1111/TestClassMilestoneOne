import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ShipMutantTest {
    private Ship originalShip;
    private ShipMutant mutantShip;

    @BeforeEach
    void setUp() {
        // Create both original and mutant ships for comparison
        originalShip = new Ship(3);
        mutantShip = new ShipMutant(3);

        // Set same initial values for both
        originalShip.setLocation(2, 4);
        mutantShip.setLocation(2, 4);
        originalShip.setDirection(Ship.VERTICAL);
        mutantShip.setDirection(Ship.VERTICAL);
    }

    @Test
    void testGetRowMutant() {
        try {
            // Compare mutant behavior with original
            assertEquals(originalShip.getRow(), mutantShip.getRow(),
                    "Mutant detected: getRow() returning incorrect value");
        } catch (AssertionError e) {
            System.err.println("getRow() mutation detected! Original value: " +
                    originalShip.getRow() + ", Mutant value: " + mutantShip.getRow());
            throw e;
        }
    }

    @Test
    void testGetColMutant() {
        try {
            // Compare mutant behavior with original
            assertEquals(originalShip.getCol(), mutantShip.getCol(),
                    "Mutant detected: getCol() returning incorrect value");
        } catch (AssertionError e) {
            System.err.println("getCol() mutation detected! Original value: " +
                    originalShip.getCol() + ", Mutant value: " + mutantShip.getCol());
            throw e;
        }
    }

    @Test
    void testGetLengthMutant() {
        try {
            // Compare mutant behavior with original
            assertEquals(originalShip.getLength(), mutantShip.getLength(),
                    "Mutant detected: getLength() returning incorrect value");
        } catch (AssertionError e) {
            System.err.println("getLength() mutation detected! Original value: " +
                    originalShip.getLength() + ", Mutant value: " + mutantShip.getLength());
            throw e;
        }
    }

    @Test
    void testGetDirectionMutant() {
        try {
            // Compare mutant behavior with original
            assertEquals(originalShip.getDirection(), mutantShip.getDirection(),
                    "Mutant detected: getDirection() returning incorrect value");
        } catch (AssertionError e) {
            System.err.println("getDirection() mutation detected! Original value: " +
                    originalShip.getDirection() + ", Mutant value: " + mutantShip.getDirection());
            throw e;
        }
    }

    // Test edge cases for each getter
    @Test
    void testGetRowEdgeCases() {
        mutantShip.setLocation(-1, 4); // Testing with negative row
        try {
            assertEquals(-1, mutantShip.getRow(),
                    "Mutant detected: getRow() handling negative values incorrectly");
        } catch (AssertionError e) {
            System.err.println("getRow() mutation for negative values detected! Expected: -1, Got: "
                    + mutantShip.getRow());
            throw e;
        }
    }

    @Test
    void testGetColEdgeCases() {
        mutantShip.setLocation(2, -1); // Testing with negative column
        try {
            assertEquals(-1, mutantShip.getCol(),
                    "Mutant detected: getCol() handling negative values incorrectly");
        } catch (AssertionError e) {
            System.err.println("getCol() mutation for negative values detected! Expected: -1, Got: "
                    + mutantShip.getCol());
            throw e;
        }
    }

    @Test
    void testGetLengthEdgeCases() {
        Ship testShip = new Ship(1); // Testing with minimum length
        ShipMutant testMutant = new ShipMutant(1);
        try {
            assertEquals(testShip.getLength(), testMutant.getLength(),
                    "Mutant detected: getLength() handling minimum length incorrectly");
        } catch (AssertionError e) {
            System.err.println("getLength() mutation for minimum length detected! Expected: "
                    + testShip.getLength() + ", Got: " + testMutant.getLength());
            throw e;
        }
    }

    @Test
    void testGetDirectionEdgeCases() {
        mutantShip.setDirection(Ship.UNSET); // Testing with UNSET direction
        try {
            assertEquals(Ship.UNSET, mutantShip.getDirection(),
                    "Mutant detected: getDirection() handling UNSET value incorrectly");
        } catch (AssertionError e) {
            System.err.println("getDirection() mutation for UNSET value detected! Expected: "
                    + Ship.UNSET + ", Got: " + mutantShip.getDirection());
            throw e;
        }
    }

    // Keep the utility methods
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @AfterEach
    void tearDown() throws Exception {
    }
}