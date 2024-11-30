import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    private Location location;

    @BeforeEach
    void setUp() {
        location = new Location();
    }

    // Test default constructor and initial values
    @Test
    void testLocationConstructor() {
        assertEquals(Location.UNGUESSED, location.getStatus());
        assertFalse(location.hasShip());
        assertEquals(-1, location.getLengthOfShip());
        assertEquals(-1, location.getDirectionOfShip());
    }

    // Test checkHit method
    @Test
    void testCheckHit() {
        assertFalse(location.checkHit());  // Initially should be false
        location.markHit();
        assertTrue(location.checkHit());   // Should be true after marking hit
    }

    // Test checkMiss method
    @Test
    void testCheckMiss() {
        assertFalse(location.checkMiss());  // Initially should be false
        location.markMiss();
        assertTrue(location.checkMiss());   // Should be true after marking miss
    }

    // Test isUnguessed method
    @Test
    void testIsUnguessed() {
        assertTrue(location.isUnguessed());  // Initially should be true
        location.markHit();
        assertFalse(location.isUnguessed()); // Should be false after marking hit
        location.setStatus(Location.UNGUESSED);  // Reset to UNGUESSED
        assertTrue(location.isUnguessed());  // Should be true again
    }

    // Test markHit method
    @Test
    void testMarkHit() {
        location.markHit();
        assertEquals(Location.HIT, location.getStatus());
        assertTrue(location.checkHit());
    }

    // Test markMiss method
    @Test
    void testMarkMiss() {
        location.markMiss();
        assertEquals(Location.MISSED, location.getStatus());
        assertTrue(location.checkMiss());
    }

    // Test hasShip and setShip methods
    @Test
    void testSetHasShip() {
        assertFalse(location.hasShip());  // Initially false
        location.setShip(true);
        assertTrue(location.hasShip());
        location.setShip(false);
        assertFalse(location.hasShip());
    }

    // Test setStatus and getStatus methods
    @Test
    void testSetStatus() {
        location.setStatus(Location.HIT);
        assertEquals(Location.HIT, location.getStatus());

        location.setStatus(Location.MISSED);
        assertEquals(Location.MISSED, location.getStatus());

        location.setStatus(Location.UNGUESSED);
        assertEquals(Location.UNGUESSED, location.getStatus());
    }

    // Test getLengthOfShip and setLengthOfShip methods
    @Test
    void testGetAndSetLengthOfShip() {
        assertEquals(-1, location.getLengthOfShip());  // Initially -1
        location.setLengthOfShip(4);
        assertEquals(4, location.getLengthOfShip());
    }
    @Test
    void testGetAndSetDirectionOfShip() {
        // Initially, directionOfShip should be -1
        assertEquals(-1, location.getDirectionOfShip());

        // Set a new direction value and verify it
        location.setDirectionOfShip(2);
        assertEquals(2, location.getDirectionOfShip());

        // Set another direction value and verify it
        location.setDirectionOfShip(4);
        assertEquals(4, location.getDirectionOfShip());
    }

}
