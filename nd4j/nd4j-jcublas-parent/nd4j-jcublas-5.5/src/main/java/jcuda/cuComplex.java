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

package jcuda;

/**
 * Java port of the CUDA complex number structure.
 */
public class cuComplex
{
	/** The real part of the complex number */
    public float x;

	/** The imaginary part of the complex number */
    public float y;

    /* Private constructor */
    private cuComplex()
    {
    }
    
    /**
     * Returns the real part of the given complex number.
     * 
     * @param x The complex number whose real part should be returned
     * @return The real part of the given complex number
     */
    public static float cuCreal (cuComplex x)
    {
        return x.x;
    }

    /**
     * Returns the imaginary part of the given complex number.
     * 
     * @param x The complex number whose imaginary part should be returned
     * @return The imaginary part of the given complex number
     */
    public static float cuCimag (cuComplex x)
    {
        return x.y;
    }

    /**
     * Creates a new complex number consisting of the given real and
     * imaginary part.
     * 
     * @param r The real part of the complex number
     * @param i The imaginary part of the complex number
     * @return A complex number with the given real and imaginary part
     */
    public static cuComplex cuCmplx (float r, float i)
    {
        cuComplex res = new cuComplex();
        res.x = r;
        res.y = i;
        return res;
    }

    /**
     * Returns the complex conjugate of the given complex number.
     * 
     * @param x The complex number whose complex conjugate should be returned
     * @return The complex conjugate of the given complex number
     */
    public static  cuComplex cuConj (cuComplex x)
    {
        return cuCmplx (cuCreal(x), -cuCimag(x));
    }

    /**
     * Returns a new complex number that is the sum of the given
     * complex numbers.
     * 
     * @param x The first addend
     * @param y The second addend
     * @return The sum of the given addends 
     */
    public static  cuComplex cuCadd (cuComplex x, cuComplex y)
    {
        return cuCmplx (cuCreal(x) + cuCreal(y), cuCimag(x) + cuCimag(y));
    }

    /**
     * Returns the product of the given complex numbers.<br />
     * <br />
     * Original comment:<br />
     * <br />
     * This implementation could suffer from intermediate overflow even though
     * the final result would be in range. However, various implementations do
     * not guard against this (presumably to avoid losing performance), so we
     * don't do it either to stay competitive.
     * 
     * @param x The first factor
     * @param y The second factor
     * @return The product of the given factors 
     */
    public static cuComplex cuCmul (cuComplex x, cuComplex y)
    {
        cuComplex prod;
        prod = cuCmplx ((cuCreal(x) * cuCreal(y)) - (cuCimag(x) * cuCimag(y)),
                        (cuCreal(x) * cuCimag(y)) + (cuCimag(x) * cuCreal(y)));
        return prod;
    }

    /**
     * Returns the quotient of the given complex numbers.<br />
     * <br />
     * Original comment:<br />
     * <br />
     * This implementation guards against intermediate underflow and overflow
     * by scaling. Such guarded implementations are usually the default for
     * complex library implementations, with some also offering an unguarded,
     * faster version.
     * 
     * @param x The dividend
     * @param y The divisor
     * @return The quotient of the given complex numbers 
     */
    public static  cuComplex cuCdiv (cuComplex x, cuComplex y)
    {
        cuComplex quot;
        float s = ((float)Math.abs(cuCreal(y))) + ((float)Math.abs(cuCimag(y)));
        float oos = 1.0f / s;
        float ars = cuCreal(x) * oos;
        float ais = cuCimag(x) * oos;
        float brs = cuCreal(y) * oos;
        float bis = cuCimag(y) * oos;
        s = (brs * brs) + (bis * bis);
        oos = 1.0f / s;
        quot = cuCmplx (((ars * brs) + (ais * bis)) * oos,
                        ((ais * brs) - (ars * bis)) * oos);
        return quot;
    }

    /**
     * Returns the absolute value of the given complex number.<br />
     * <br />
     * Original comment:<br />
     * <br />
     * This implementation guards against intermediate underflow and overflow
     * by scaling. Otherwise the we'd lose half the exponent range. There are
     * various ways of doing guarded computation. For now chose the simplest
     * and fastest solution, however this may suffer from inaccuracies if sqrt
     * and division are not IEEE compliant.
     * 
     * @param x The complex number whose absolute value should be returned
     * @return The absolute value of the given complex number
     */
    public static float cuCabs (cuComplex x)
    {
        float p = cuCreal(x);
        float q = cuCimag(x);
        float r;
        if (p == 0) return q;
        if (q == 0) return p;
        p = (float)Math.sqrt(p);
        q = (float)Math.sqrt(q);
        if (p < q) {r = p; p = q; q = r;}
        r = q / p;
        return p * (float)Math.sqrt (1.0f + r * r);
    }

    /**
     * Returns a String representation of this complex number.
     * 
     * @return A String representation of this complex number
     */
    public String toString()
    {
        return "("+x+","+y+")";
    }
}

