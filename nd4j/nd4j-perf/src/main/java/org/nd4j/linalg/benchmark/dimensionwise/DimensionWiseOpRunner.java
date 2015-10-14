package org.nd4j.linalg.benchmark.dimensionwise;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.benchmark.api.OpRunner;
import org.nd4j.linalg.factory.Nd4j;

/**
 * @author Adam Gibson
 */
public class DimensionWiseOpRunner implements OpRunner {
    INDArray arr = Nd4j.create(1000000);
    INDArray dup = arr.dup();


    @Override
    public void runOp() {
        arr.mean(0);
    }
}
