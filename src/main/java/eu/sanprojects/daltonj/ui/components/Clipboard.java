package eu.sanprojects.daltonj.ui.components;

import eu.sanprojects.daltonj.filters.utils.BufferedImageUtils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;

public class Clipboard {

    /**
     * Get image from clipboard as BufferedImage
     */
    public static BufferedImage getImageFromClipboard() {

        BufferedImage bufferedImage = null;
        try {
            // get clipboard content
            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            // check content
            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                //copy to a buffered image
                Image image = (Image) transferable.getTransferData(DataFlavor.imageFlavor);
                bufferedImage = BufferedImageUtils.clone(image);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bufferedImage;
    }

}
