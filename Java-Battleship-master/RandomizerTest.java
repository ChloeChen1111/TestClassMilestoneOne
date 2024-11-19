import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.Random;

public class RandomizerTest {

    @Before
    public void setUp() {
        Randomizer.theInstance = null;
    }

    @Test
    public void testConstructor() {
        Randomizer randomizer = new Randomizer();
        assertNotNull("Constructor should create instance", randomizer);
    }

    @Test
    public void testGetInstance() {
        assertNull("Initial instance should be null", Randomizer.theInstance);
        Random instance1 = Randomizer.getInstance();
        assertNotNull("First getInstance call should create instance", instance1);
        Random instance2 = Randomizer.getInstance();
        assertSame("getInstance should return the same instance", instance1, instance2);
    }

    @Test
    public void testNextBoolean() {
        boolean foundTrue = false;
        boolean foundFalse = false;

        for (int i = 0; i < 100 && (!foundTrue || !foundFalse); i++) {
            boolean result = Randomizer.nextBoolean();
            if (result) {
                foundTrue = true;
            } else {
                foundFalse = true;
            }
        }

        assertTrue("nextBoolean should eventually return true", foundTrue);
        assertTrue("nextBoolean should eventually return false", foundFalse);
    }

    @Test
    public void testNextBooleanWithProbability() {
        // Test extreme cases
        assertTrue("probability 1.0 should return true", Randomizer.nextBoolean(1.0));
        assertFalse("probability 0.0 should return false", Randomizer.nextBoolean(0.0));

        // Test middle probability
        int trueCount = 0;
        int trials = 1000;

        for (int i = 0; i < trials; i++) {
            if (Randomizer.nextBoolean(0.5)) {
                trueCount++;
            }
        }

        double ratio = (double) trueCount / trials;
        assertTrue("Ratio should be roughly 0.5", ratio >= 0.4 && ratio <= 0.6);
    }

    @Test
    public void testNextInt() {
        int first = Randomizer.nextInt();
        boolean foundDifferent = false;

        for (int i = 0; i < 1000; i++) {
            int next = Randomizer.nextInt();
            if (next != first) {
                foundDifferent = true;
                break;
            }
        }

        assertTrue("nextInt should return different values", foundDifferent);
    }

    @Test
    public void testNextIntWithBound() {
        int bound = 10;
        boolean foundZero = false;
        boolean foundNine = false;

        for (int i = 0; i < 1000 && !(foundZero && foundNine); i++) {
            int result = Randomizer.nextInt(bound);
            assertTrue("Result should be >= 0", result >= 0);
            assertTrue("Result should be < bound", result < bound);

            if (result == 0) foundZero = true;
            if (result == bound - 1) foundNine = true;
        }

        assertTrue("Should find minimum value (0)", foundZero);
        assertTrue("Should find maximum value (9)", foundNine);
    }

    @Test
    public void testNextIntWithRange() {
        int min = 5;
        int max = 10;
        boolean foundMin = false;
        boolean foundMax = false;

        for (int i = 0; i < 1000 && !(foundMin && foundMax); i++) {
            int result = Randomizer.nextInt(min, max);
            assertTrue(result >= min);
            assertTrue(result <= max);

            if (result == min) foundMin = true;
            if (result == max) foundMax = true;
        }

        assertTrue("Should find minimum value", foundMin);
        assertTrue("Should find maximum value", foundMax);
    }

    @Test
    public void testNextDouble() {
        boolean foundLow = false;
        boolean foundHigh = false;

        for (int i = 0; i < 1000 && !(foundLow && foundHigh); i++) {
            double result = Randomizer.nextDouble();
            assertTrue("Result should be >= 0.0", result >= 0.0);
            assertTrue("Result should be < 1.0", result < 1.0);

            if (result < 0.1) foundLow = true;
            if (result > 0.9) foundHigh = true;
        }

        assertTrue("Should find low values", foundLow);
        assertTrue("Should find high values", foundHigh);
    }

    @Test
    public void testNextDoubleWithRange() {
        double min = 1.0;
        double max = 2.0;
        boolean foundNearMin = false;
        boolean foundNearMax = false;

        for (int i = 0; i < 1000 && !(foundNearMin && foundNearMax); i++) {
            double result = Randomizer.nextDouble(min, max);
            assertTrue("Result should be >= min", result >= min);
            assertTrue("Result should be <= max", result <= max);

            if (result < min + 0.1) foundNearMin = true;
            if (result > max - 0.1) foundNearMax = true;
        }

        assertTrue("Should find values near minimum", foundNearMin);
        assertTrue("Should find values near maximum", foundNearMax);
    }

    @Test
    public void testNextDoubleWithEqualMinMax() {
        double value = 5.0;
        double result = Randomizer.nextDouble(value, value);
        assertEquals(value, result, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithZeroBound() {
        Randomizer.nextInt(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeBound() {
        Randomizer.nextInt(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithInvalidRange() {
        Randomizer.nextInt(10, 5);
    }
}