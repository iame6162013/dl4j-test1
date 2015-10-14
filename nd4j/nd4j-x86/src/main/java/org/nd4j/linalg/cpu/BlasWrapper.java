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

package org.nd4j.linalg.cpu;

import com.github.fommil.netlib.BLAS;
import org.nd4j.linalg.factory.BaseBlasWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;


/**
 * Copy of SimpleBlas to handle offsets implementing
 * an interface for library neutral
 * jblas operations
 *
 * @author Adam Gibson
 */
public class BlasWrapper extends BaseBlasWrapper {

    private static final Logger log = LoggerFactory.getLogger(BlasWrapper.class);

    public final static String FORCE_NATIVE = "org.nd4j.linalg.cpu.force_native";
    static {
        String forceNative = System.getProperty(FORCE_NATIVE,"true");
        if(Boolean.parseBoolean(forceNative)) {
            try {
                Field blasInstance = BLAS.class.getDeclaredField("INSTANCE");
                BLAS newInstance = (BLAS) Class.forName("com.github.fommil.netlib.NativeSystemBLAS").newInstance();
                setFinalStatic(blasInstance, newInstance);
            } catch(ClassNotFoundException e) {
                log.warn("Native BLAS not available on classpath");
            } catch (Exception e) {
                log.warn("unable to force native BLAS", e);
            }
        }
    }

    static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

}
