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

package org.canova.api.split;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.URI;

/**
 * String split used for single line inputs
 * @author Adam Gibson
 */
public class StringSplit implements InputSplit {
    private String data;

    public StringSplit(String data) {
        this.data = data;
    }

    @Override
    public long length() {
        return data.length();
    }

    @Override
    public URI[] locations() {
        return new URI[0];
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.write(data.getBytes());
    }

    @Override
    public void readFields(DataInput in) throws IOException {

    }

    public String getData() {
        return data;
    }
}
