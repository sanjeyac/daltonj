package eu.sanprojects.daltonj.filters.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class BufferedImageUtils {

    /**
     * A utility to clone Image into new BufferedImage of TYPE_INT_ARGB type
     */
    public static BufferedImage clone(Image initialImage){
        int width = initialImage.getWidth(null);
        int height = initialImage.getHeight(null);
        BufferedImage clone = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = clone.createGraphics();
        bGr.drawImage(initialImage, 0, 0, null);
        bGr.dispose();
        return clone;
    }

}
