package eu.sanprojects.daltonj.filters.impl;

import eu.sanprojects.daltonj.filters.AbstractFilter;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import static eu.sanprojects.daltonj.filters.utils.GammaCorrection.LINRGB_TO_SRGB;
import static eu.sanprojects.daltonj.filters.utils.GammaCorrection.SRGB_TO_LINRGB;

/**
 * A filter for grayscale conversion: perceptual luminance-preserving
 * conversion to grayscale.
 * https://en.wikipedia.org/wiki/Grayscale#Colorimetric_(perceptual_luminance-preserving)_conversion_to_grayscale
 *
 * Method extracted from ColorOracle
 * https://github.com/nvkelso/color-oracle-java
 */
public class GrayscaleFilter extends AbstractFilter {

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
                final int rgb = inData[i];

                final int r = (0xff0000 & rgb) >> 16;
                final int g = (0xff00 & rgb) >> 8;
                final int b = 0xff & rgb;

                // get linear rgb values in the range 0..2^15-1
                final int r_lin = SRGB_TO_LINRGB[r];
                final int g_lin = SRGB_TO_LINRGB[g];
                final int b_lin = SRGB_TO_LINRGB[b];

                // perceptual luminance-preserving conversion to grayscale
                // https://en.wikipedia.org/wiki/Grayscale#Colorimetric_(perceptual_luminance-preserving)_conversion_to_grayscale
                double luminance = 0.2126 * r_lin + 0.7152 * g_lin + 0.0722 * b_lin;
                int linRGB = ((int) (luminance)) >> 8; // divide by 2^8 to rescale

                // convert linear rgb to gamma corrected sRGB
                if (linRGB < 0) {
                    linRGB = 0;
                } else if (linRGB > 255) {
                    linRGB = 255;
                } else {
                    linRGB = LINRGB_TO_SRGB[linRGB];
                    linRGB = linRGB >= 0 ? linRGB : 256 + linRGB; // from unsigned to signed
                }

                final int out = linRGB << 16 | linRGB << 8 | linRGB | 0xff000000;

                outData[i] = out;
                prevIn = in;
                prevOut = out;
            }
        }

        return dst;
    }

}
