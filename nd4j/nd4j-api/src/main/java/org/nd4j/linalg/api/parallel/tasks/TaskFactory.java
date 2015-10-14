package org.nd4j.linalg.api.parallel.tasks;

import org.apache.commons.math3.util.Pair;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.*;

/** A TaskFactory provides Task objects for each type of Op
 */
public interface TaskFactory {

    Task<Void> getTransformAction(TransformOp op);

    Task<Void> getTransformAction(TransformOp op, int... dimension );

    Task<Void> getScalarAction( ScalarOp op);

    Task<Double> getAccumulationTask( Accumulation op );

    Task<INDArray> getAccumulationTask( Accumulation op, int... dimension );

    Task<Pair<Double,Integer>> getIndexAccumulationTask( IndexAccumulation op );

    Task<INDArray> getIndexAccumulationTask( IndexAccumulation op, int... dimension );

    Task<Void> getVectorOpAction(VectorOp op);

}
