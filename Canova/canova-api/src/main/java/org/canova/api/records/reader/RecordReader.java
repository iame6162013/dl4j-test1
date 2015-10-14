/*
 *
 *  *
 *  *  * Copyright 2015 Skymind,Inc.
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *    See the License for the specific language governing permissions and
 *  *  *    limitations under the License.
 *  *
 *
 */

package org.canova.api.records.reader;

import org.canova.api.conf.Configurable;
import org.canova.api.conf.Configuration;
import org.canova.api.split.InputSplit;
import org.canova.api.writable.Writable;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

/**
 * Record reader
 * @author Adam Gibson
 */
public interface RecordReader extends Closeable,Serializable,Configurable {

    public final static String NAME_SPACE = RecordReader.class.getName();

    public final static String APPEND_LABEL = NAME_SPACE + ".appendlabel";
    public final static String LABELS = NAME_SPACE + ".labels";

    /**
     * Called once at initialization.
     * @param split the split that defines the range of records to read
     * @throws java.io.IOException
     * @throws InterruptedException
     */
    void initialize(InputSplit split) throws IOException, InterruptedException;

    /**
     * Called once at initialization.
     * @param conf a configuration for initialization
     * @param split the split that defines the range of records to read
     * @throws java.io.IOException
     * @throws InterruptedException
     */
    void initialize(Configuration conf,InputSplit split) throws IOException, InterruptedException;

    /**
     * Get the next record
     * @return
     */
    Collection<Writable> next();


    /**
     * Whether there are anymore records
     * @return
     */
    boolean hasNext();




}
