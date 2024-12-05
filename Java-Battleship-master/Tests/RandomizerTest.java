package Tests;

import Src.Randomizer;
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
        assertNotNull(randomizer);
    }

    @Test
    public void testGetInstance() {
        // Test when instance is null
        assertNull(Randomizer.theInstance);
        Random instance1 = Randomizer.getInstance();
        assertNotNull(instance1);

        // Test when instance exists
        Random instance2 = Randomizer.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void testNextBoolean() {
        // Test both cases by fixing Random's output
        Randomizer.theInstance = new Random() {
            @Override
            public boolean nextBoolean() {
                return true;
            }
        };
        assertTrue(Randomizer.nextBoolean());

        Randomizer.theInstance = new Random() {
            @Override
            public boolean nextBoolean() {
                return false;
            }
        };
        assertFalse(Randomizer.nextBoolean());
    }

    @Test
    public void testNextBooleanProbability() {
        // Test probability < threshold (true case)
        Randomizer.theInstance = new Random() {
            @Override
            public double nextDouble() {
                return 0.4;
            }
        };
        assertTrue(Randomizer.nextBoolean(0.5));

        // Test probability >= threshold (false case)
        Randomizer.theInstance = new Random() {
            @Override
            public double nextDouble() {
                return 0.6;
            }
        };
        assertFalse(Randomizer.nextBoolean(0.5));
    }

    @Test
    public void testNextInt() {
        // Test positive and negative values
        Randomizer.theInstance = new Random() {
            @Override
            public int nextInt() {
                return 42;
            }
        };
        assertEquals(42, Randomizer.nextInt());

        Randomizer.theInstance = new Random() {
            @Override
            public int nextInt() {
                return -42;
            }
        };
        assertEquals(-42, Randomizer.nextInt());
    }

    @Test
    public void testNextIntBound() {
        // Test bound cases
        Randomizer.theInstance = new Random() {
            @Override
            public int nextInt(int bound) {
                return 0; // minimum value
            }
        };
        assertEquals(0, Randomizer.nextInt(10));

        Randomizer.theInstance = new Random() {
            @Override
            public int nextInt(int bound) {
                return bound - 1; // maximum value
            }
        };
        assertEquals(9, Randomizer.nextInt(10));
    }

    @Test
    public void testNextIntRange() {
        // Test min value
        Randomizer.theInstance = new Random() {
            @Override
            public int nextInt(int bound) {
                return 0;
            }
        };
        assertEquals(5, Randomizer.nextInt(5, 10));

        // Test max value
        Randomizer.theInstance = new Random() {
            @Override
            public int nextInt(int bound) {
                return bound - 1;
            }
        };
        assertEquals(10, Randomizer.nextInt(5, 10));
    }

    @Test
    public void testNextDouble() {
        // Test minimum value (0.0)
        Randomizer.theInstance = new Random() {
            @Override
            public double nextDouble() {
                return 0.0;
            }
        };
        assertEquals(0.0, Randomizer.nextDouble(), 0.0001);

        // Test maximum value (just under 1.0)
        Randomizer.theInstance = new Random() {
            @Override
            public double nextDouble() {
                return 0.99999;
            }
        };
        assertEquals(0.99999, Randomizer.nextDouble(), 0.0001);
    }

    @Test
    public void testNextDoubleRange() {
        // Test minimum value
        Randomizer.theInstance = new Random() {
            @Override
            public double nextDouble() {
                return 0.0;
            }
        };
        assertEquals(1.0, Randomizer.nextDouble(1.0, 2.0), 0.0001);

        // Test maximum value
        Randomizer.theInstance = new Random() {
            @Override
            public double nextDouble() {
                return 0.99999;
            }
        };
        assertEquals(1.99999, Randomizer.nextDouble(1.0, 2.0), 0.0001);
    }
}