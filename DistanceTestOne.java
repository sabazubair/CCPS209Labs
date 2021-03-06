import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.*;
import java.util.zip.CRC32;

public class DistanceTestOne {

    private static final int SEED = 123456;

    @Test public void testExtractSquares() {
        CRC32 check = new CRC32();
        for(int n = 0; n < 1_000_000; n++) {
            int sp = Distance.extractSquares(n);
            assertEquals(0, n % (sp*sp));
            int a = n / (sp*sp); // Integer division truncates
            assertEquals(n, sp*sp*a);
            check.update(sp);
        }
        assertEquals(2907073306L, check.getValue());
    }

    // To clarify the expected behaviour of toString, here are some test cases
    // that demonstrate how the toString method is supposed to behave.
    
    private static int[][][] testCases = {
        { {61, 3} }, // each term is {root, coefficient}, so this means 3Sqrt[61]
        { {1003, -42} },
        { {1, -1}, {2, 3}, {10, -1}, {17, 2}  },
        { {5, -1}, {3, -1}, {2, -1} }, 
        { {99, 2}, {999, 2}, {9999, 2} },
        { {123, 3}, {127, 5}, {3, 18} }, 
        { {5, 1}, {10, 1}, {15, 1}, {20, 1}, {25, 1}, {30, 1} },
        { {2, -1}, {3, -1}, {5, -1}, {7, -1}, {11, -1}, {13, -1} },
        // Terms whose roots are equal after extracting the square must be combined.
        { {2, -1}, {4, -1}, {8, -1}, {16, -1}, {32, -1}, {64, -1}, {128, -1} },
        { {10*10, 5}, {100*100, -5}, {1000*1000, 5} }
    };
    
    private static String[] expected = {
        "3Sqrt[61]",
        "-42Sqrt[1003]",
        "-1 + 3Sqrt[2] - Sqrt[10] + 2Sqrt[17]",
        "-Sqrt[2] - Sqrt[3] - Sqrt[5]",
        "6Sqrt[11] + 6Sqrt[111] + 6Sqrt[1111]",
        "18Sqrt[3] + 3Sqrt[123] + 5Sqrt[127]",
        "5 + 3Sqrt[5] + Sqrt[10] + Sqrt[15] + Sqrt[30]",
        "-Sqrt[2] - Sqrt[3] - Sqrt[5] - Sqrt[7] - Sqrt[11] - Sqrt[13]",
        "-14 - 15Sqrt[2]",
        "4550"
    };
    
    @Test public void testToString() {
        int i = 0;
        for(int[][] testCase: testCases) {
            TreeMap<Integer, Integer> coeff = new TreeMap<>();
            for(int[] co: testCase) {
                coeff.put(co[0], co[1]);
            }
            Distance d = new Distance(coeff);
            //System.out.println("\"" + d + "\"");
            assertEquals(expected[i], d.toString());
            i++;
        }
        
    }
    
    @Test public void testConstruction() {
        Random rng = new Random(SEED);
        CRC32 check = new CRC32();
        for(int i = 0; i < 10_000; i++) {
            int whole = rng.nextInt(3 * (i + 2));
            if(rng.nextBoolean()) { whole = -whole; }
            int base = rng.nextInt(3 * (i + 2)) + 1;
            Distance d = new Distance(whole, base);
            String rep = d.toString();
            // if(i % 50 == 0 && i < 1000) { 
                // System.out.println(whole + " " + base + " : <" + rep + ">"); 
            // }
            check.update(rep.getBytes());
        }
        assertEquals(4065287689L, check.getValue());
    }    
}