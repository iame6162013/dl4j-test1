package org.nd4j.linalg.api.parallel.tasks.cpu;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.Op;
import org.nd4j.linalg.api.parallel.tasks.BaseTask;
import org.nd4j.linalg.api.parallel.tasks.Task;
import org.nd4j.linalg.api.parallel.tasks.TaskExecutorProvider;
import org.nd4j.linalg.api.parallel.tasks.TaskFactoryProvider;

import java.util.List;
import java.util.concurrent.Future;

public abstract class AbstractCPUTask<V> extends BaseTask<V> {
    protected final int threshold;
    protected int n;
    protected int offsetX;
    protected int offsetY;
    protected int offsetZ;
    protected int incrX;
    protected int incrY;
    protected int incrZ;

    protected boolean doTensorFirst;
    protected int tensorIdx;
    protected int tensorDim;


    protected Future<V> future;

    public AbstractCPUTask(int threshold, int n, int offsetX, int offsetY, int offsetZ, int incrX, int incrY, int incrZ){
        this.threshold = threshold;
        this.n = n;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.incrX = incrX;
        this.incrY = incrY;
        this.incrZ = incrZ;
        doTensorFirst = false;
    }

    public AbstractCPUTask(Op op, int threshold){
        this.threshold = threshold;
        this.n = op.x().length();
        this.offsetX = op.x().offset();
        this.offsetY = (op.y() != null ? op.y().offset() : 0);
        this.offsetZ = (op.z() != null ? op.z().offset() : 0);
        this.incrX = op.x().elementWiseStride();
        this.incrY = (op.y() != null ? op.y().elementWiseStride() : 0);
        this.incrZ = (op.z() != null ? op.z().elementWiseStride() : 0);
        doTensorFirst = false;
    }

    /** Constructor for doing a 1d tensor along dimension first */
    public AbstractCPUTask(Op op, int threshold, int tadIdx, int tadDim){
        doTensorFirst = true;
        this.threshold = threshold;
        this.tensorIdx = tadIdx;
        this.tensorDim = tadDim;
    }

    protected void doTensorFirst(Op op){
        INDArray x = op.x();
        INDArray y = op.y();
        INDArray z = op.z();
        INDArray tadx = x.tensorAlongDimension(tensorIdx,tensorDim);
        this.n = tadx.length();
        offsetX = tadx.offset();
        incrX = tadx.elementWiseStride();
        if(y==null){
            offsetY = 0;
            incrY = 0;
        } else if(y==x){
            offsetY = offsetX;
            incrY = incrX;
        } else {
            INDArray tady = y.tensorAlongDimension(tensorIdx,tensorDim);
            offsetY = tady.offset();
            incrY = tady.elementWiseStride();
        }

        if(z==null) {
            offsetZ = 0;
            incrZ = 0;
        } else if(z==x) {
            offsetZ = offsetX;
            incrZ = incrX;
        } else if(z==y){
            offsetZ = offsetY;
            incrZ = incrY;
        } else {
            INDArray tadz = z.tensorAlongDimension(tensorIdx,tensorDim);
            offsetZ = tadz.offset();
            incrZ = tadz.elementWiseStride();
        }
    }

    @Override
    public void invokeAsync(){
        this.future = TaskExecutorProvider.getTaskExecutor().executeAsync(this);
    }
}
