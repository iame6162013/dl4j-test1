/*
 *
 *  * Copyright 2015 Skymind,Inc.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 *
 */

package org.nd4j.linalg.api.ops;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ndarray.LinearViewNDArray;

/**
 * Base op. An op involves iterating over 2 buffers (x,y)  up to n elements
 * and applying a transform or accumulating a result.
 *
 * @author Adam Gibson
 */
public abstract class BaseOp implements Op {

    protected INDArray x, y, z;
    protected int n;
    protected int numProcessed;
    protected Object[] extraArgs;
    protected boolean passThrough;

    public BaseOp() {
    }

    @Override
    public boolean isPassThrough() {
        return passThrough;
    }

    @Override
    public void setX(INDArray x) {
        if(x == null)
            throw new IllegalArgumentException("X must not be null");
        this.x = x;
        numProcessed = 0;
        this.n = x.length();
    }

    @Override
    public void setZ(INDArray z) {
        if(z == null)
            throw new IllegalArgumentException("Z must not be null");
        this.z = z;
        numProcessed = 0;
        this.n = z.length();
    }

    @Override
    public void setY(INDArray y) {
        if(y == null)
            throw new IllegalArgumentException("Y must not be null");
        this.y = y;
        numProcessed = 0;
        this.n = y.length();
    }

    /**
     * Specify an alternative result array
     *
     * @param x the input
     * @param z the output array
     */
    public BaseOp(INDArray x, INDArray z) {
        this(x, z, x.length());
    }

    /**
     * Specify an alternative output array
     *
     * @param x the input
     * @param z the output
     * @param n the number of elements to iterate on
     */
    public BaseOp(INDArray x, INDArray z, int n) {
        this(x, null, z, n);
    }


    public BaseOp(INDArray x, INDArray y, INDArray z, int n) {
        ensureProperVectors(x,y,z);
        this.n = n;
        init(x, y, z, n);
    }

    protected void ensureProperVectors(INDArray x,INDArray y,INDArray z) {
        this.x = x;
        if(x.offset() > 0 && !(x instanceof LinearViewNDArray) && x.length() < x.data().length()) {
            this.x = x.linearView();
        }

        this.y = y;
        if(y != null && y.offset() > 0 && !(y instanceof LinearViewNDArray) && y.majorStride() > y.elementStride()) {
            this.y = y.linearView();
        }

        this.z = z;
        if(z.offset() > 0 && !(x instanceof LinearViewNDArray) && z.majorStride() > z.elementStride()) {
            this.z = z.linearView();
        }

    }


    /**
     * An op for one ndarray
     *
     * @param x the ndarray
     */
    public BaseOp(INDArray x) {
        this(x, null, x, x.length());
    }

    @Override
    public Object[] extraArgs() {
        return extraArgs;
    }

    @Override
    public INDArray x() {
        return x;
    }

    @Override
    public INDArray y() {
        return y;
    }


    @Override
    public INDArray z() {
        return z;
    }

    @Override
    public int n() {
        return n;
    }


    @Override
    public void init(INDArray x, INDArray y, INDArray z, int n) {


    }

    @Override
    public int numProcessed() {
        return numProcessed;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public void exec() {
        //no-op
    }

    @Override
    public void exec(int... dimensions) {
        //no-op
    }
}
