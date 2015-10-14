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

package org.canova.nd4j.nlp.reader;

import org.canova.api.conf.Configuration;
import org.canova.api.records.reader.RecordReader;
import org.canova.api.split.FileSplit;
import org.canova.api.writable.Writable;
import org.canova.nd4j.nlp.vectorizer.TfidfVectorizer;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author Adam Gibson
 */
public class TfidfRecordReaderTest {

    @Test
    public void testReader() throws Exception {
        TfidfVectorizer vectorizer = new TfidfVectorizer();
        Configuration conf = new Configuration();
        conf.setInt(TfidfVectorizer.MIN_WORD_FREQUENCY, 1);
        conf.setBoolean(RecordReader.APPEND_LABEL, true);
        vectorizer.initialize(conf);
        TfidfRecordReader reader = new TfidfRecordReader();
        reader.initialize(conf, new FileSplit(new ClassPathResource("labeled").getFile()));
        int count = 0;
        while(reader.hasNext()) {
            Collection<Writable> record = reader.next();
            count++;
        }
        assertEquals(3,reader.getLabels().size());
        assertEquals(3,count);
    }

}
