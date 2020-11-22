package eu.sanprojects.daltonj.filters.impl;

import eu.sanprojects.daltonj.filters.utils.GammaCorrection;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * A filter for simulated Tritanopia
 *
 * Method extracted from ColorOracle
 * https://github.com/nvkelso/color-oracle-java
 */
public class TritanFilter extends AbstractFilter  {

    @Override
    public BufferedImage filter(BufferedImage input, BufferedImage dst) {

        /* Code for tritan simulation from GIMP 2.2
         *  This could be optimised for speed.
         *  Performs tritan color image simulation based on
         *  Brettel, Vienot and Mollon JOSA 14/10 1997
         *  L,M,S for lambda=475,485,575,660
         *
         * Load the LMS anchor-point values for lambda = 475 & 485 nm (for
         * protans & deutans) and the LMS values for lambda = 575 & 660 nm
         * (for tritans)
         */
        final float anchor_e0 = 0.05059983f + 0.08585369f + 0.00952420f;
        final float anchor_e1 = 0.01893033f + 0.08925308f + 0.01370054f;
        final float anchor_e2 = 0.00292202f + 0.00975732f + 0.07145979f;
        final float inflection = anchor_e1 / anchor_e0;

        /* Set 1: regions where lambda_a=575, set 2: lambda_a=475 */
        final float a1 = -anchor_e2 * 0.007009f;
        final float b1 = anchor_e2 * 0.0914f;
        final float c1 = anchor_e0 * 0.007009f - anchor_e1 * 0.0914f;
        final float a2 = anchor_e1 * 0.3636f - anchor_e2 * 0.2237f;
        final float b2 = anchor_e2 * 0.1284f - anchor_e0 * 0.3636f;
        final float c2 = anchor_e0 * 0.2237f - anchor_e1 * 0.1284f;

        BufferedImage src = convertToIntDataBuffer(input);

        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }

        // make sure the two images have the same size, color space, etc.
        // MISSING FIXME
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
                final int rgb = inData[i];

                int r = (0xff0000 & rgb) >> 16;
                int g = (0xff00 & rgb) >> 8;
                int b = 0xff & rgb;

                // get linear rgb values in the range 0..2^15-1
                r = GammaCorrection.SRGB_TO_LINRGB[r];
                g = GammaCorrection.SRGB_TO_LINRGB[g];
                b = GammaCorrection.SRGB_TO_LINRGB[b];

                /* Convert to LMS (dot product with transform matrix) */
                final float L = (r * 0.05059983f + g * 0.08585369f + b * 0.00952420f) / 32767.f;
                final float M = (r * 0.01893033f + g * 0.08925308f + b * 0.01370054f) / 32767.f;
                float S; // = (r * 0.00292202f + g * 0.00975732f + b * 0.07145979f) / 32767.f;

                final float tmp = M / L;

                /* See which side of the inflection line we fall... */
                if (tmp < inflection) {
                    S = -(a1 * L + b1 * M) / c1;
                } else {
                    S = -(a2 * L + b2 * M) / c2;
                }

                /* Convert back to RGB (cross product with transform matrix) */
                int ired = (int) (255.f * (L * 30.830854f
                        - M * 29.832659f + S * 1.610474f));
                int igreen = (int) (255.f * (-L * 6.481468f
                        + M * 17.715578f - S * 2.532642f));
                int iblue = (int) (255.f * (-L * 0.375690f
                        - M * 1.199062f + S * 14.273846f));

                // convert reduced linear rgb to gamma corrected rgb
                if (ired < 0) {
                    ired = 0;
                } else if (ired > 255) {
                    ired = 255;
                } else {
                    ired = GammaCorrection.LINRGB_TO_SRGB[ired];
                    ired = ired >= 0 ? ired : 256 + ired; // from unsigned to signed
                }
                if (igreen < 0) {
                    igreen = 0;
                } else if (igreen > 255) {
                    igreen = 255;
                } else {
                    igreen = GammaCorrection.LINRGB_TO_SRGB[igreen];
                    igreen = igreen >= 0 ? igreen : 256 + igreen; // from unsigned to signed
                }
                if (iblue < 0) {
                    iblue = 0;
                } else if (iblue > 255) {
                    iblue = 255;
                } else {
                    iblue = GammaCorrection.LINRGB_TO_SRGB[iblue];
                    iblue = iblue >= 0 ? iblue : 256 + iblue; // from unsigned to signed
                }

                final int out = ired << 16 | igreen << 8 | iblue | 0xff000000;

                outData[i] = out;
                prevIn = in;
                prevOut = out;
            }
        }

        return dst;
    }

}
