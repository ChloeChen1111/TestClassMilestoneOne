package Tests;

import Src.Ship;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testShipConstructor() {
		Ship testShip = new Ship(3);
		assertEquals(-1, testShip.getDirection());
		assertEquals(3, testShip.getLength());
		assertEquals(-1, testShip.getRow());
		assertEquals(-1, testShip.getCol());
	}

	@Test
	void testIsLocationSet() {
		Ship testShip = new Ship(2);
		assertFalse(testShip.isLocationSet());
		testShip.setLocation(0, 0);
		assertTrue(testShip.isLocationSet());
	}

	@Test
	void testIsDirectionSet() {
		Ship testShip = new Ship(2);
		assertFalse(testShip.isDirectionSet());
		testShip.setDirection(1);
		assertTrue(testShip.isDirectionSet());
		assertThrows(IllegalArgumentException.class, () -> testShip.setDirection(2));
		assertThrows(IllegalArgumentException.class, () -> testShip.setDirection(-2));
	}

	@Test
	void testSetLocation() {
		Ship testShip = new Ship(2);
		testShip.setLocation(2, 3);
		assertEquals(2, testShip.getRow());
		assertEquals(3, testShip.getCol());
	}

	@Test
	void testSetDirection() {
		Ship testShip = new Ship(2);
		testShip.setDirection(Ship.HORIZONTAL);
		assertEquals(Ship.HORIZONTAL, testShip.getDirection());
		testShip.setDirection(Ship.VERTICAL);
		assertEquals(Ship.VERTICAL, testShip.getDirection());
		testShip.setDirection(Ship.UNSET);
		assertEquals(Ship.UNSET, testShip.getDirection());
	}

	@Test
	void testGetters() {
		Ship testShip = new Ship(4);
		assertEquals(-1, testShip.getRow());
		assertEquals(-1, testShip.getCol());
		assertEquals(4, testShip.getLength());
		assertEquals(-1, testShip.getDirection());
	}

	@Test
	void testToString() {
		Ship testShip = new Ship(4);
		assertEquals("Src.Ship: -1, -1 with length 4 and direction UNSET", testShip.toString());

		testShip.setLocation(5, 7);
		assertEquals("Src.Ship: 5, 7 with length 4 and direction UNSET", testShip.toString());

		testShip.setDirection(Ship.HORIZONTAL);
		assertEquals("Src.Ship: 5, 7 with length 4 and direction HORIZONTAL", testShip.toString());

		testShip.setDirection(Ship.VERTICAL);
		assertEquals("Src.Ship: 5, 7 with length 4 and direction VERTICAL", testShip.toString());
	}

	@Test
	void testSetDirectionInvalid() {
		Ship testShip = new Ship(3);
		assertThrows(IllegalArgumentException.class, () -> testShip.setDirection(99));
	}

	@Test
	void testDirectionToStringVertical() {
		Ship testShip = new Ship(4);
		testShip.setDirection(Ship.VERTICAL);
		assertEquals("Src.Ship: -1, -1 with length 4 and direction VERTICAL", testShip.toString());
	}

}
