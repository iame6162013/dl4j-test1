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

package org.canova.api.records.reader.impl;

import org.canova.api.conf.Configuration;
import org.canova.api.io.data.DoubleWritable;
import org.canova.api.io.data.IntWritable;
import org.canova.api.io.data.Text;
import org.canova.api.records.reader.LibSvm;
import org.canova.api.split.InputSplit;
import org.canova.api.writable.Writable;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Adam Gibson
 */
public class LibSvmRecordReader extends LineRecordReader implements LibSvm {
    private boolean appendLabel = false;
    private boolean classification = true;

    @Override
    public void initialize(InputSplit split) throws IOException, InterruptedException {
        super.initialize(split);
    }

    @Override
    public void initialize(Configuration conf, InputSplit split) throws IOException, InterruptedException {
        super.initialize(conf, split);
        appendLabel = conf.getBoolean(APPEND_LABEL,false);
        classification = conf.getBoolean(CLASSIFICATION,true);
    }

    @Override
    public Collection<Writable> next() {
        Text record2 = (Text) super.next().iterator().next();
        String line = record2.toString();


        String[] tokens = line.trim().split("\\s+");
        Double response;
        try {
            response = Integer.valueOf(tokens[0]).doubleValue();
        } catch (NumberFormatException e) {
            try {
                response = Double.valueOf(tokens[0]);
                classification = false;
            } catch (NumberFormatException ex) {
                System.err.println(ex);
                throw new NumberFormatException("Unrecognized response variable value: " + tokens[0]);
            }
        }


        tokens = line.trim().split("\\s+");

        Collection<Writable> record = new ArrayList<>();
        int read = 0;
        for (int k = 1; k < tokens.length; k++) {
            String[] pair = tokens[k].split(":");
            if (pair.length != 2) {
                throw new NumberFormatException("Invalid data: " + tokens[k]);
            }

            int j = Integer.valueOf(pair[0]) - 1;
            if(j != read) {
                record.add(new DoubleWritable(0.0));
                read++;
            }
            try {
                int x = Integer.valueOf(pair[1]);
                record.add(new IntWritable(x));
            }catch(NumberFormatException e) {
                double x = Double.valueOf(pair[1]);
                record.add(new DoubleWritable(x));
            }
            read++;
        }

        if(classification && appendLabel || !classification) {
            record.add(new DoubleWritable(response));
        }

        return record;
    }

    @Override
    public boolean hasNext() {
        return super.hasNext();
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    @Override
    public void setConf(Configuration conf) {
        super.setConf(conf);
    }

    @Override
    public Configuration getConf() {
        return super.getConf();
    }

}
