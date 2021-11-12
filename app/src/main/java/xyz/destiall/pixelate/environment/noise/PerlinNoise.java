package xyz.destiall.pixelate.environment.noise;

import xyz.destiall.pixelate.position.Vector2;

public class PerlinNoise {

    //Randomised permutation array as designed by Perlin
    private static int[] P = makePermutation();
    private static final float m_707 = 1.414213562373095f;

    //2D noise generation
    public static double noise(double x, double y)
    {
        int X = (int)Math.floor(x) & 255;
        int Y = (int)Math.floor(y) & 255;

        double xf = x-Math.floor(x);
        double yf = y-Math.floor(y);

        Vector2 topRight = new Vector2(xf-1.0, yf-1.0);
        Vector2 topLeft = new Vector2(xf, yf-1.0);
        Vector2 bottomRight = new Vector2(xf-1.0, yf);
        Vector2 bottomLeft = new Vector2(xf, yf);

        //Select value from randomised array
        int valueTopRight = P[P[X+1]+Y+1];
        int valueTopLeft = P[P[X]+Y+1];
        int valueBottomRight = P[P[X+1]+Y];
        int valueBottomLeft = P[P[X]+Y];

        double dotTopRight = topRight.dot(getConstantVector(valueTopRight));
        double dotTopLeft = topLeft.dot(getConstantVector(valueTopLeft));
        double dotBottomRight = bottomRight.dot(getConstantVector(valueBottomRight));
        double dotBottomLeft = bottomLeft.dot(getConstantVector(valueBottomLeft));

        double u = fade(xf);
        double v = fade(yf);

        //Value normalisation converting into 0 to 1 range
        double val = lerp(u, lerp(v, dotBottomLeft, dotTopLeft), lerp(v, dotBottomRight, dotTopRight));
        //Since in 2D, perlin generates estimated [-0.707, 0.707] we multiply returned value by 1/0.707 and interpolate it
        val = val * m_707 * 0.5 + 0.5;
        return val;
    }

    //Permutation table initializer
    private static int[] makePermutation()
    {
        int[] permutation = new int[512];
        for(int i = 0; i < 256; ++i) permutation[i] = i;
        shuffle(permutation, 0, 256);

        //Add shuffled to prevent IndexOutOfBounds
        for(int i = 256; i < 512; ++i) permutation[i] = permutation[i-256];

        return permutation;
    }

    //Optimized perlin fade function
    private static double fade(double t)
    {
        return ((6.0*t - 15)*t + 10.0)*t*t*t;
    }

    //Linear interpolation function
    private static double lerp(double t, double a1, double a2)
    {
        return a1 + t*(a2-a1);
    }

    //Permutation table shuffler
    private static void shuffle(int[] table, int start, int end)
    {
        for(int e = end-1; e > start; --e)
        {
            int i = (int) Math.round(Math.random() * (e-1));
            int temp = table[e];

            table[e] = table[i];
            table[i] = temp;
        }
    }

    private static Vector2 getConstantVector(int v)
    {
        //v is the value from the randomised permutation table
        int a = v & 3;
        switch(a)
        {
            case 0:
                return new Vector2(1.0, 1.0);
            case 1:
                return new Vector2(-1.0,1.0);
            case 2:
                return new Vector2(-1.0, -1.0);
            default:
                return new Vector2(1.0, -1.0);
        }
    }

}
