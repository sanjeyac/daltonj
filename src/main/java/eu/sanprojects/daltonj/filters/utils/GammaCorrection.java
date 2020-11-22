package eu.sanprojects.daltonj.filters.utils;

/**
 * Gamma Correction parameters
 */
public class GammaCorrection {

    /** Gamma on Windows is 2.2 **/
    private static final double GAMMA = 2.2;
    private static final double GAMMA_INV = 1. / GAMMA;

    /**
     * A lookup table for the conversion from gamma-corrected sRGB values
     * [0..255] to linear RGB values [0..32767].
     */
    public static final short[] SRGB_TO_LINRGB;

    static {
        // initialize SRGB_TO_LINRGB
        SRGB_TO_LINRGB = new short[256];
        for (int i = 0; i < 256; i++) {
            // compute linear rgb between 0 and 1
            final double lin = (0.992052 * Math.pow(i / 255., GAMMA) + 0.003974);

            // scale linear rgb to 0..32767
            SRGB_TO_LINRGB[i] = (short) (lin * 32767.);
        }
    }

    /**
     * A lookup table for the conversion of linear RGB values [0..255] to
     * gamma-corrected sRGB values [0..255].
     */
    public static final byte[] LINRGB_TO_SRGB;

    static {
        // initialize LINRGB_TO_SRGB
        LINRGB_TO_SRGB = new byte[256];
        for (int i = 0; i < 256; i++) {
            LINRGB_TO_SRGB[i] = (byte) (255. * Math.pow(i / 255., GAMMA_INV));
        }
    }

}
