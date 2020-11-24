package eu.sanprojects.daltonj.filters;

import eu.sanprojects.daltonj.filters.utils.BufferedImageUtils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;

/**
 * Abstract class that contains common overridden methods of BufferedImageOp
 */
public abstract class AbstractFilter implements BufferedImageOp {

    /**
     * Helper method for filter, in order to avoid colling method with null parameters
     *
     * @param src input
     * @return filtered image
     */
    public BufferedImage filter(BufferedImage src) {
        return this.filter(src, null);
    }

    /**
     * Internal conversion utility
     */
    public BufferedImage convertToIntDataBuffer(BufferedImage src) {
        //conversion not needed
        if (src.getRaster().getDataBuffer() instanceof DataBufferInt){
            return src;
        }

        //convert
        return BufferedImageUtils.clone(src);
    }

    /**
     * Filter to be implemented by extended classes
     */
    @Override
    abstract public BufferedImage filter(BufferedImage src, BufferedImage dest);

    /**
     * Get image bounds
     */
    @Override
    public Rectangle2D getBounds2D(BufferedImage src) {
        return src.getRaster().getBounds();
    }

    /**
     * Internal destination image creator
     */
    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
        if (destCM == null) {
            destCM = src.getColorModel();
        }
        int width = src.getWidth();
        int height = src.getHeight();
        return new BufferedImage(destCM,
                destCM.createCompatibleWritableRaster(width, height),
                destCM.isAlphaPremultiplied(), null);
    }

    /**
     * Affine transformation disabled
     */
    @Override
    public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
        if (dstPt == null) {
            dstPt = new Point2D.Float();
        }
        dstPt.setLocation(srcPt.getX(), srcPt.getY());
        return dstPt;
    }

    @Override
    public RenderingHints getRenderingHints() {
        return null;
    }

}
