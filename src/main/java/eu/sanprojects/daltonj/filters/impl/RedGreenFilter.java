package eu.sanprojects.daltonj.filters.impl;

import eu.sanprojects.daltonj.filters.AbstractFilter;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import static eu.sanprojects.daltonj.filters.utils.GammaCorrection.LINRGB_TO_SRGB;
import static eu.sanprojects.daltonj.filters.utils.GammaCorrection.SRGB_TO_LINRGB;

/**
 * A red-green blindness filter (deuteranopia and protanopia).
 *
 * Method extracted from ColorOracle
 * https://github.com/nvkelso/color-oracle-java
 */
public class RedGreenFilter extends AbstractFilter {

    private final int k1;
    private final int k2;
    private final int k3;

    public RedGreenFilter(int k1, int k2, int k3) {
        this.k1 = k1;
        this.k2 = k2;
        this.k3 = k3;
    }

    @Override
    public BufferedImage filter(BufferedImage input, BufferedImage dst) {

        BufferedImage src = convertToIntDataBuffer(input);

        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }

        DataBufferInt inBuffer = (DataBufferInt) src.getRaster().getDataBuffer();
        DataBufferInt outBuffer = (DataBufferInt) dst.getRaster().getDataBuffer();

        int[] inData = inBuffer.getData();
        int[] outData = outBuffer.getData();

        int prevIn = 0;
        int prevOut = 0;
        final int length = inData.length;
        for (int i = 0; i < length; i++) {
            final int in = inData[i];
            if (in == prevIn) {
                outData[i] = prevOut;
            } else {

                final int r = (0xff0000 & in) >> 16;
                final int g = (0xff00 & in) >> 8;
                final int b = 0xff & in;

                // get linear rgb values in the range 0..2^15-1
                final int r_lin = SRGB_TO_LINRGB[r];
                final int g_lin = SRGB_TO_LINRGB[g];
                final int b_lin = SRGB_TO_LINRGB[b];

                // simulated red and green are identical
                // scale the matrix values to 0..2^15 for integer computations
                // of the simulated protan values.
                // divide after the computation by 2^15 to rescale.
                // also divide by 2^15 and multiply by 2^8 to scale the linear rgb to 0..255
                // total division is by 2^15 * 2^15 / 2^8 = 2^22
                // shift the bits by 22 places instead of dividing
                int r_blind = (k1 * r_lin + k2 * g_lin) >> 22;
                int b_blind = (k3 * r_lin - k3 * g_lin + 32768 * b_lin) >> 22;

                if (r_blind < 0) {
                    r_blind = 0;
                } else if (r_blind > 255) {
                    r_blind = 255;
                }

                if (b_blind < 0) {
                    b_blind = 0;
                } else if (b_blind > 255) {
                    b_blind = 255;
                }

                // convert reduced linear rgb to gamma corrected rgb
                int red = LINRGB_TO_SRGB[r_blind];
                red = red >= 0 ? red : 256 + red; // from unsigned to signed
                int blue = LINRGB_TO_SRGB[b_blind];
                blue = blue >= 0 ? blue : 256 + blue; // from unsigned to signed

                final int out = 0xff000000 | red << 16 | red << 8 | blue;

                outData[i] = out;
                prevIn = in;
                prevOut = out;
            }
        }

        return dst;
    }

}
