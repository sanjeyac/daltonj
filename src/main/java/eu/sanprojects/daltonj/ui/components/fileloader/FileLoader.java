package eu.sanprojects.daltonj.ui.components.fileloader;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileLoader {

    public static BufferedImage loadImage(Component parent) {
        BufferedImage image = null;
        try {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new ImageFilter());
            int returnVal = fc.showOpenDialog(parent);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                image = ImageIO.read(file);
            }
        } catch (IOException e) {
            System.err.println("Could not open image");
            e.printStackTrace();
        }
        return image;
    }

}
