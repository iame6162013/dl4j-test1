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

package org.nd4j.linalg.api.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.nd4j.linalg.api.complex.IComplexDouble;
import org.nd4j.linalg.api.complex.IComplexFloat;
import org.nd4j.linalg.api.complex.IComplexNumber;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.util.ArrayUtil;

import java.io.*;
import java.lang.ref.WeakReference;
import java.nio.*;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.*;

/**
 * Base class for a data buffer
 * handling basic byte operations among other things.
 *
 * @author Adam Gibson
 */
public abstract class BaseDataBuffer implements DataBuffer {

    protected int length;
    protected int elementSize;
    protected transient ByteBuf dataBuffer;
    protected Collection<String> referencing = Collections.synchronizedSet(new HashSet<String>());
    protected transient WeakReference<DataBuffer> ref;
    protected boolean isPersist = false;
    protected AllocationMode allocationMode;
    protected double[] doubleData;
    protected int[] intData;
    protected float[] floatData;

    /**
     *
     * @param buf
     * @param length
     */
    protected BaseDataBuffer(ByteBuf buf,int length) {
        allocationMode = Nd4j.alloc;
        this.dataBuffer = buf;
        this.length = length;
    }

    /**
     *
     * @param data
     * @param copy
     */
    public BaseDataBuffer(float[] data, boolean copy) {
        allocationMode = Nd4j.alloc;
        if(allocationMode == AllocationMode.HEAP) {
            if(copy) {
                floatData = ArrayUtil.copy(data);
            }
            else {
                this.floatData = data;
            }
        }
        else {
            dataBuffer = Unpooled.copyFloat(data).order(ByteOrder.nativeOrder());
        }
        length = data.length;

    }

    /**
     *
     * @param data
     * @param copy
     */
    public BaseDataBuffer(double[] data, boolean copy) {
        allocationMode = Nd4j.alloc;
        if(allocationMode == AllocationMode.HEAP) {
            if(copy) {
                doubleData = ArrayUtil.copy(data);
            }
            else {
                this.doubleData = data;
            }
        }
        else {
            dataBuffer = Unpooled.copyDouble(data).order(ByteOrder.nativeOrder());
        }
        length = data.length;

    }

    /**
     *
     * @param data
     * @param copy
     */
    public BaseDataBuffer(int[] data, boolean copy) {
        allocationMode = Nd4j.alloc;
        if(allocationMode == AllocationMode.HEAP) {
            if(copy)
                intData = ArrayUtil.copy(data);

            else
                this.intData = data;

        }
        else
            dataBuffer = Unpooled.copyInt(data).order(ByteOrder.nativeOrder());

        length = data.length;
    }

    public BaseDataBuffer(double[] data) {
        this(data,Nd4j.copyOnOps);
    }

    public BaseDataBuffer(int[] data) {
        this(data, Nd4j.copyOnOps);
    }

    public BaseDataBuffer(float[] data) {
        this(data,Nd4j.copyOnOps);
    }

    public BaseDataBuffer(int length, int elementSize) {
        allocationMode = Nd4j.alloc;
        this.length = length;
        this.elementSize = elementSize;
        this.dataBuffer = Unpooled.buffer(elementSize * length, Integer.MAX_VALUE).order(ByteOrder.nativeOrder());
        for(int i = 0; i < length; i++) {
            if(dataType() == Type.DOUBLE) {
                put(i,0.0);
            }
            else {
                put(i,(float) 0.0);
            }
        }
    }

    public BaseDataBuffer(byte[] data, int length) {
        this(Unpooled.wrappedBuffer(data),length);
    }


    @Override
    public AllocationMode allocationMode() {
        return allocationMode;
    }

    @Override
    public void persist() {
        isPersist = true;
    }

    @Override
    public boolean isPersist() {
        return isPersist;
    }

    @Override
    public void unPersist() {
        isPersist = false;
    }

    /**
     * Instantiate a buffer with the given length
     *
     * @param length the length of the buffer
     */
    protected BaseDataBuffer(int length) {
        this.length = length;
        allocationMode = Nd4j.alloc;
        if(length < 0)
            throw new IllegalArgumentException("Unable to create a buffer of length <= 0");

        ref = new WeakReference<DataBuffer>(this,Nd4j.bufferRefQueue());
        if(allocationMode == AllocationMode.HEAP) {
            if(length >= Integer.MAX_VALUE)
                throw new IllegalArgumentException("Length of data buffer can not be > Integer.MAX_VALUE for heap (array based storage) allocation");
            if(dataType() == Type.DOUBLE)
                doubleData = new double[length];
            else if(dataType() == Type.FLOAT)
                floatData = new float[length];
        }
        else {
            dataBuffer = allocationMode == AllocationMode.DIRECT ?
                    Unpooled.buffer(length * getElementSize()).order(ByteOrder.nativeOrder())
                    : Unpooled.buffer(length * getElementSize()).order(ByteOrder.nativeOrder());
        }

    }


    @Override
    public void removeReferencing(String id) {
        referencing.remove(id);
    }

    @Override
    public Collection<String> references() {
        return referencing;
    }

    @Override
    public void addReferencing(String id) {
        referencing.add(id);
    }

    @Override
    public void assign(int[] indices, float[] data, boolean contiguous, int inc) {
        if (indices.length != data.length)
            throw new IllegalArgumentException("Indices and data length must be the same");
        if (indices.length > length())
            throw new IllegalArgumentException("More elements than space to assign. This buffer is of length " + length() + " where the indices are of length " + data.length);
        for (int i = 0; i < indices.length; i++) {
            put(indices[i], data[i]);
        }
    }



    @Override
    public void setData(int[] data) {
        if(intData != null)
            this.intData = data;
        else {
            for (int i = 0; i < data.length; i++) {
                dataBuffer.setInt(i, data[i]);
            }
        }

    }

    @Override
    public void setData(float[] data) {
        if(floatData != null) {
            this.floatData = data;
        }
        else {
            for(int i = 0; i < data.length; i++)
                put(i,data[i]);
        }

    }

    @Override
    public void setData(double[] data) {
        if(doubleData != null) {
            this.doubleData = data;
        }
        else {
            for(int i = 0; i < data.length; i++)
                put(i, data[i]);
        }
    }


    @Override
    public void assign(int[] indices, double[] data, boolean contiguous, int inc) {
        if (indices.length != data.length)
            throw new IllegalArgumentException("Indices and data length must be the same");
        if (indices.length > length())
            throw new IllegalArgumentException("More elements than space to assign. This buffer is of length " + length() + " where the indices are of length " + data.length);
        for (int i = 0; i < indices.length; i += inc) {
            put(indices[i], data[i]);
        }
    }

    @Override
    public void assign(DataBuffer data) {
        if (data.length() != length())
            throw new IllegalArgumentException("Unable to assign buffer of length " + data.length() + " to this buffer of length " + length());

        for (int i = 0; i < data.length(); i++) {
            put(i, data.getDouble(i));
        }
    }

    @Override
    public void assign(int[] indices, float[] data, boolean contiguous) {
        assign(indices, data, contiguous, 1);
    }

    @Override
    public void assign(int[] indices, double[] data, boolean contiguous) {
        assign(indices, data, contiguous, 1);
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public void assign(Number value) {
        for(int i = 0; i < length(); i++)
            assign(value,i);
    }


    @Override
    public double[] getDoublesAt(int offset, int length) {
        return getDoublesAt(offset, 1, length);
    }

    @Override
    public float[] getFloatsAt(int offset, int inc, int length) {
        if (offset + length > length())
            length -= offset;
        float[] ret = new float[length];
        for (int i = 0; i < length; i++) {
            ret[i] = getFloat(i + offset);
        }
        return ret;
    }


    @Override
    public DataBuffer dup() {
        if(floatData != null) {
            return create(floatData);
        }
        else if(doubleData != null) {
            return create(doubleData);
        }
        else if(intData != null) {
            return create(intData);
        }

        DataBuffer ret = create(length);
        for(int i = 0; i < ret.length(); i++)
            ret.put(i,getDouble(i));

        return ret;
    }

    /**
     * Create with length
     * @param length a databuffer of the same type as
     *               this with the given length
     * @return a data buffer with the same length and datatype as this one
     */
    protected abstract  DataBuffer create(int length);


    /**
     * Create the data buffer
     * with respect to the given byte buffer
     * @param data the buffer to create
     * @return the data buffer based on the given buffer
     */
    public abstract DataBuffer create(double[] data);
    /**
     * Create the data buffer
     * with respect to the given byte buffer
     * @param data the buffer to create
     * @return the data buffer based on the given buffer
     */
    public abstract DataBuffer create(float[] data);

    /**
     * Create the data buffer
     * with respect to the given byte buffer
     * @param data the buffer to create
     * @return the data buffer based on the given buffer
     */
    public abstract DataBuffer create(int[] data);

    /**
     * Create the data buffer
     * with respect to the given byte buffer
     * @param buf the buffer to create
     * @return the data buffer based on the given buffer
     */
    public abstract DataBuffer create(ByteBuf buf,int length);

    @Override
    public double[] getDoublesAt(int offset, int inc, int length) {
        if (offset + length > length())
            length -= offset;

        double[] ret = new double[length];
        for (int i = 0; i < length; i++) {
            ret[i] = getDouble(i + offset);
        }


        return ret;
    }

    @Override
    public float[] getFloatsAt(int offset, int length) {
        return getFloatsAt(offset, 1, length);
    }

    @Override
    public IComplexFloat getComplexFloat(int i) {
        return Nd4j.createFloat(getFloat(i), getFloat(i + 1));
    }

    @Override
    public IComplexDouble getComplexDouble(int i) {
        return Nd4j.createDouble(getDouble(i), getDouble(i + 1));
    }

    @Override
    public IComplexNumber getComplex(int i) {
        return dataType() == DataBuffer.Type.FLOAT ? getComplexFloat(i) : getComplexDouble(i);
    }


    @Override
    public void put(int i, IComplexNumber result) {
        put(i, result.realComponent().doubleValue());
        put(i + 1, result.imaginaryComponent().doubleValue());
    }


    @Override
    public void assign(int[] offsets, int[] strides, DataBuffer... buffers) {
        assign(offsets, strides, length(), buffers);
    }

    @Override
    public byte[] asBytes() {
        if(allocationMode == AllocationMode.HEAP) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(getElementSize() * length());
            DataOutputStream dos = new DataOutputStream(bos);

            if(dataType() == Type.DOUBLE) {
                if(doubleData == null)
                    throw new IllegalStateException("Double array is null!");

                try {
                    for(int i = 0; i < doubleData.length; i++)
                        dos.writeDouble(doubleData[i]);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            else {
                if(floatData == null)
                    throw new IllegalStateException("Double array is null!");

                try {
                    for(int i = 0; i < floatData.length; i++)
                        dos.writeFloat(floatData[i]);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }

            return bos.toByteArray();

        }
        else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            if(dataType() == Type.DOUBLE) {
                for(int i = 0; i < length(); i++) {
                    try {
                        dos.writeDouble(getDouble(i));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                for(int i = 0; i < length(); i++) {
                    try {
                        dos.writeFloat(getFloat(i));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return bos.toByteArray();
        }
    }

    @Override
    public float[] asFloat() {
        if(allocationMode == AllocationMode.HEAP) {
            if(floatData != null) {
                return floatData;
            }
        }

        float[] ret = new float[length];
        for(int i = 0; i < length; i++)
            ret[i] = getFloat(i);
        return ret;

    }

    @Override
    public double[] asDouble() {
        if(allocationMode == AllocationMode.HEAP) {
            if(doubleData != null) {
                return doubleData;
            }
        }


        double[] ret = new double[length];
        for(int i = 0; i < length; i++)
            ret[i] = getDouble(i);
        return ret;

    }

    @Override
    public int[] asInt() {
        if(allocationMode == AllocationMode.HEAP) {
            if(intData != null) {
                return intData;
            }
        }
        return dataBuffer.nioBuffer().asIntBuffer().array();
    }

    @Override
    public double getDouble(int i) {
        if(doubleData != null) {
            if(i >= doubleData.length)
                throw new IllegalStateException("Index out of bounds " + i);
            return doubleData[i];
        }
        else if(floatData != null) {
            if(i >= floatData.length)
                throw new IllegalStateException("Index out of bounds " + i);
            return (double) floatData[i];
        }
        else if(intData != null) {
            return (double) intData[i];
        }

        if(dataType() == Type.FLOAT)
            return dataBuffer.getFloat(i * getElementSize());

        return dataBuffer.getDouble(i * getElementSize());
    }

    @Override
    public float getFloat(int i) {
        if(doubleData != null) {
            if(i >= doubleData.length)
                throw new IllegalStateException("Index out of bounds " + i);
            return (float) doubleData[i];
        } else if(floatData != null) {
            if(i >= floatData.length)
                throw new IllegalStateException("Index out of bounds " + i);
            return floatData[i];
        }
        else if(intData != null) {
            return (float) intData[i];
        }

        if(dataType() == Type.DOUBLE)
            return (float) dataBuffer.getDouble(i * getElementSize());

        return dataBuffer.getFloat(i * getElementSize());
    }

    @Override
    public Number getNumber(int i) {
        if(dataType() == Type.DOUBLE)
            return getDouble(i);
        return getFloat(i);
    }

    @Override
    public void put(int i, float element) {

        if(doubleData != null) {
            doubleData[i] = element;
        }
        else if(floatData != null) {
            floatData[i] = element;
        }
        else if(intData != null) {
            intData[i] = (int) element;
        }
        else {
            if (dataType() == Type.DOUBLE) {
                ensureWritable(i, 8);
                dataBuffer.setDouble(i * 8, (double) element);
            }
            else {
                ensureWritable(i, 4);
                dataBuffer.setFloat(i * 4, element);
            }
        }

    }

    @Override
    public void put(int i, double element) {
        if(i < 0 || i >= length())
            throw new IllegalArgumentException("Illegal index " + i);

        if(doubleData != null)
            doubleData[i] = element;

        else if(floatData != null)
            floatData[i] = (float) element;

        else if(intData != null)
            intData[i] = (int) element;

        else {
            if(dataType() == Type.DOUBLE) {
                ensureWritable(i,8);
                dataBuffer.setDouble(i * 8, element);

            }

            else
                put(i,(float) element);

        }
    }


    protected void ensureWritable(int pos, int len) {
        int ni = pos + len;
        int cap = dataBuffer.capacity();
        int over = ni - cap;
        if (over > 0) {
            dataBuffer.writerIndex(cap);
            dataBuffer.ensureWritable(over);
        }
        //We have to make sure that the writerindex is always positioned on the last bit of data set in the buffer
        if (ni > dataBuffer.writerIndex()) {
            dataBuffer.writerIndex(ni);
        }
    }

    @Override
    public DoubleBuffer asNioDouble() {
        return dataBuffer.nioBuffer().asDoubleBuffer();
    }

    @Override
    public FloatBuffer asNioFloat() {
        return dataBuffer.nioBuffer().asFloatBuffer();
    }

    @Override
    public ByteBuffer asNio() {
        return dataBuffer.nioBuffer(0,dataBuffer.capacity()).order(ByteOrder.nativeOrder());
    }

    @Override
    public ByteBuf asNetty() {
        return dataBuffer;
    }

    @Override
    public void put(int i, int element) {
        put(i,(double) element);
    }

    @Override
    public void assign(Number value, int offset) {
        put(offset, value.doubleValue());
    }

    @Override
    public void write(OutputStream dos) {
        if(dos instanceof DataOutputStream) {
            try {

                write((DataOutputStream) dos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            DataOutputStream dos2 = new DataOutputStream(dos);
            try {

                write( dos2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void read(InputStream is) {
        if(is instanceof DataInputStream) {
            read((DataInputStream) is);
        }

        else {
            DataInputStream dis2 = new DataInputStream(is);
            read(dis2);
        }
    }

    @Override
    public void flush() {

    }

    @Override
    public int getInt(int ix) {
        return (int) getDouble(ix);
    }

    @Override
    public void assign(int[] offsets, int[] strides, long n, DataBuffer... buffers) {
        if (offsets.length != strides.length || strides.length != buffers.length)
            throw new IllegalArgumentException("Unable to assign buffers, please specify equal lengths strides, offsets, and buffers");
        int length = 0;
        for (int i = 0; i < buffers.length; i++)
            length += buffers[i].length();

        int count = 0;
        for (int i = 0; i < buffers.length; i++) {
            for (int j = offsets[i]; j < buffers[i].length(); j += strides[i]) {
                put(count++, buffers[i].getDouble(j));
            }
        }

        if (count != n)
            throw new IllegalArgumentException("Strides and offsets didn't match up to length " + n);

    }

    @Override
    public void assign(DataBuffer... buffers) {
        int[] offsets = new int[buffers.length];
        int[] strides = new int[buffers.length];
        for (int i = 0; i < strides.length; i++)
            strides[i] = 1;
        assign(offsets, strides, buffers);
    }


    @Override
    public void destroy() {
        this.dataBuffer.clear();
        this.dataBuffer = null;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof DataBuffer) {
            DataBuffer d = (DataBuffer) o;
            if(d.length() != length())
                return false;
            for(int i = 0; i < length(); i++) {
                double eps = Math.abs(getDouble(i) - d.getDouble(i));
                if(eps > Nd4j.EPS_THRESHOLD)
                    return false;
            }
        }

        return true;
    }

    private void readObject(ObjectInputStream s) {
        doReadObject(s);
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.defaultWriteObject();
        write(out);
    }


    protected void doReadObject(ObjectInputStream s) {
        try {
            s.defaultReadObject();
            read(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }




    protected void read(DataInputStream s) {
        try {
            ref = new WeakReference<DataBuffer>(this,Nd4j.bufferRefQueue());
            referencing = Collections.synchronizedSet(new HashSet<String>());

            allocationMode = AllocationMode.valueOf(s.readUTF());
            length = s.readInt();
            Type t = Type.valueOf(s.readUTF());
            if(t == Type.DOUBLE) {
                if(allocationMode == AllocationMode.HEAP) {
                    floatData = new float[(int) length()];
                    for(int i = 0; i < length(); i++) {
                        put(i,s.readDouble());
                    }
                }
                else {
                    dataBuffer = Unpooled.buffer((int) length() * getElementSize()).order(ByteOrder.nativeOrder());
                    for(int i = 0; i < length(); i++) {
                        put(i,s.readDouble());
                    }
                }
            }
            else {
                if(allocationMode == AllocationMode.HEAP) {
                    doubleData = new double[(int) length()];
                    for(int i = 0; i < length(); i++) {
                        put(i,s.readFloat());
                    }
                }
                else {
                    dataBuffer = Unpooled.buffer((int) length() * getElementSize()).order(ByteOrder.nativeOrder());
                    for(int i = 0; i < length(); i++) {
                        put(i,s.readFloat());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    protected void write(DataOutputStream out) throws IOException {
        out.writeUTF(allocationMode.name());
        out.writeInt(length());
        out.writeUTF(dataType().name());
        if(dataType() == Type.DOUBLE) {
            for(int i = 0; i < length(); i++)
                out.writeDouble(getDouble(i));
        }
        else {
            for(int i = 0; i < length(); i++)
                out.writeFloat(getFloat(i));
        }

    }




    @Override
    public Object array() {
        if(floatData != null)
            return floatData;
        if(doubleData != null)
            return doubleData;
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("[");
        for(int i = 0; i < length(); i++) {
            ret.append(getNumber(i));
            if(i < length() - 1)
                ret.append(",");
        }
        ret.append("]");

        return ret.toString();
    }

    @Override
    public int hashCode() {
        int result = length;
        result = 31 * result + (dataBuffer != null ? dataBuffer.hashCode() : 0);
        result = 31 * result + (referencing != null ? referencing.hashCode() : 0);
        result = 31 * result + (ref != null ? ref.hashCode() : 0);
        result = 31 * result + (isPersist ? 1 : 0);
        result = 31 * result + (allocationMode != null ? allocationMode.hashCode() : 0);
        return result;
    }
}