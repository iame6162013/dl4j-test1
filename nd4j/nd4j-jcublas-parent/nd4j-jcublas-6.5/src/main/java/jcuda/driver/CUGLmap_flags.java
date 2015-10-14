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

package jcuda.driver;

/**
 * Flags to map or unmap a resource
 *
 * @deprecated As of CUDA 3.0
 */
public class CUGLmap_flags
{
    public static final int CU_GL_MAP_RESOURCE_FLAGS_NONE          = 0x00;
    public static final int CU_GL_MAP_RESOURCE_FLAGS_READ_ONLY     = 0x01;
    public static final int CU_GL_MAP_RESOURCE_FLAGS_WRITE_DISCARD = 0x02;

    /**
     * Returns the String identifying the given CUGLmap_flags
     *
     * @param n The CUGLmap_flags
     * @return The String identifying the given CUGLmap_flags
     */
    public static String stringFor(int n)
    {
        if (n == 0)
        {
            return "CU_GL_MAP_RESOURCE_FLAGS_NONE";
        }
        String result = "";
        if ((n & CU_GL_MAP_RESOURCE_FLAGS_READ_ONLY    ) != 0) result += "CU_GL_MAP_RESOURCE_FLAGS_READ_ONLY ";
        if ((n & CU_GL_MAP_RESOURCE_FLAGS_WRITE_DISCARD) != 0) result += "CU_GL_MAP_RESOURCE_FLAGS_WRITE_DISCARD ";
        return result;
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private CUGLmap_flags()
    {
    }


}
