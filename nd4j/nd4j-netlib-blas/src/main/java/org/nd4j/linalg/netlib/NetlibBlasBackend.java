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

package org.nd4j.linalg.netlib;

import org.nd4j.linalg.factory.Nd4jBackend;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Net lib blas backend
 * @author eron
 * @author Adam Gibson
 */
public class NetlibBlasBackend extends Nd4jBackend {

    private final static String LINALG_PROPS = "/nd4j-netlib.properties";

    @Override
    public boolean isAvailable() {
        // netlib has built-in fallback behavior
        return true;
    }

    @Override
    public boolean canRun() {
        return true;
    }

    @Override
    public int getPriority() {
        return BACKEND_PRIORITY_CPU;
    }

    @Override
    public Resource getConfigurationResource() {
        return new ClassPathResource(LINALG_PROPS, NetlibBlasBackend.class.getClassLoader());
    }
}
