package eu.sanprojects.daltonj.ui.components.fileloader;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Image filter for file loader
 */
public class ImageFilter extends FileFilter {

    /**
     * Supported image extensions
     */
    public static final String[] EXTENSIONS = {
            "jpeg", "jpg", "png", "bmp"
    };

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true; //otherwise we cannot navigate folders
        }
        return isSupportedExtension(file.getName());
    }

    /**
     * Description on UI
     */
    @Override
    public String getDescription() {
        String exts = Stream.of(EXTENSIONS).map(e -> "*." + e).collect(Collectors.joining(", "));
        return "Image files (" + exts + ")";
    }

    /**
     * Check if the file is a supported extension
     */
    public static boolean isSupportedExtension(String filename) {
        for (String ext : EXTENSIONS) {
            if (filename.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }


}