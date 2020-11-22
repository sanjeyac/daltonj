package eu.sanprojects.daltonj.ui.components;

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
                int width = image.getWidth(null);
                int height = image.getHeight(null);
                bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D bGr = bufferedImage.createGraphics();
                bGr.drawImage(image, 0, 0, null);
                bGr.dispose();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bufferedImage;
    }

}
