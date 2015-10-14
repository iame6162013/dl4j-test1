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

package jcuda.runtime;

/**
 * Texture address modes.
 *
 * @see textureReference
 */
public class cudaTextureAddressMode
{
    /**
     * Wrapping address mode
     */
    public static final int cudaAddressModeWrap = 0;

    /**
     * Clamping address mode
     */
    public static final int cudaAddressModeClamp = 1;

    /**
     * Returns the String identifying the given cudaTextureAddressMode
     *
     * @param m The cudaTextureAddressMode
     * @return The String identifying the given cudaTextureAddressMode
     */
    public static String stringFor(int m)
    {
        switch (m)
        {
            case cudaAddressModeWrap: return "cudaAddressModeWrap";
            case cudaAddressModeClamp: return "cudaAddressModeClamp";
        }
        return "INVALID cudaTextureAddressMode: " + m;
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private cudaTextureAddressMode()
    {
    }

}
