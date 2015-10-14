package org.nd4j.linalg.api.blas.params;

import org.junit.Test;
import org.nd4j.linalg.BaseNd4jTest;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.factory.Nd4jBackend;

/**
 * @author Adam Gibson
 */
public class ParamsTestsF extends BaseNd4jTest {

    public ParamsTestsF() {
    }

    public ParamsTestsF(String name) {
        super(name);
    }

    public ParamsTestsF(String name, Nd4jBackend backend) {
        super(name, backend);
    }

    public ParamsTestsF(Nd4jBackend backend) {
        super(backend);
    }

    @Test
    public void testGemm() {
        INDArray a = Nd4j.create(2, 2);
        INDArray b = Nd4j.create(2,3);
        INDArray c = Nd4j.create(2,3);
        GemmParams params = new GemmParams(a,b,c);
        assertEquals(a.rows(),params.getM());
        assertEquals(b.columns(),params.getN());
        assertEquals(a.columns(),params.getK());
        assertEquals(a.rows(),params.getLda());
        assertEquals(b.rows(),params.getLdb());
        assertEquals(c.rows(),params.getLdc());
    }

    @Override
    public char ordering() {
        return 'f';
    }
}
