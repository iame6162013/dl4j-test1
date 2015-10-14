package org.nd4j.linalg.api.iterator;

import org.junit.Test;
import org.nd4j.linalg.BaseNd4jTest;
import org.nd4j.linalg.api.iter.NdIndexIterator;
import org.nd4j.linalg.factory.Nd4jBackend;

import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * @author Adam Gibson
 */
public class NDIndexIteratorTest extends BaseNd4jTest {
    public NDIndexIteratorTest() {
    }

    public NDIndexIteratorTest(String name) {
        super(name);
    }

    public NDIndexIteratorTest(String name, Nd4jBackend backend) {
        super(name, backend);
    }

    public NDIndexIteratorTest(Nd4jBackend backend) {
        super(backend);
    }

    @Test
    public void testIterate() {
        Iterator<int[]> shapeIter = new NdIndexIterator(2,2);
        int[][] possibleSolutions = new int[][]{
                {0,0},
                {0,1},
                {1,0},
                {1,1},
        };

        for(int i = 0; i < 4; i++) {
            assertArrayEquals(possibleSolutions[i],shapeIter.next());
        }


    }

    @Override
    public char ordering() {
        return 'f';
    }
}
