package org.nd4j.linalg.api.blas;

import org.junit.Test;
import org.nd4j.linalg.BaseNd4jTest;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.factory.Nd4jBackend;

/**
 * @author Adam Gibson
 */
public class Level1Test extends BaseNd4jTest {
    public Level1Test() {
    }

    public Level1Test(String name) {
        super(name);
    }

    public Level1Test(String name, Nd4jBackend backend) {
        super(name, backend);
    }

    public Level1Test(Nd4jBackend backend) {
        super(backend);
    }

    @Test
    public void testDot() {
        INDArray vec1 = Nd4j.create(new float[]{1, 2, 3, 4});
        INDArray vec2 = Nd4j.create(new float[]{1, 2, 3, 4});
        assertEquals(30, Nd4j.getBlasWrapper().dot(vec1, vec2), 1e-1);

        INDArray matrix = Nd4j.linspace(1, 4, 4).reshape(2, 2);
        INDArray row = matrix.getRow(1);
        double dot = Nd4j.getBlasWrapper().dot(row, row);
        assertEquals(20,dot, 1e-1);

    }

    @Test
    public void testAxpy() {
        INDArray matrix = Nd4j.linspace(1,4,4).reshape(2,2);
        INDArray row = matrix.getRow(1);
        Nd4j.getBlasWrapper().level1().axpy(row.length(),1.0,row,row);
        assertEquals(getFailureMessage(),Nd4j.create(new double[]{4,8}),row);

    }

    @Override
    public char ordering() {
        return 'f';
    }
}
