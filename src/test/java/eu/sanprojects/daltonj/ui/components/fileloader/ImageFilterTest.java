package eu.sanprojects.daltonj.ui.components.fileloader;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageFilterTest {

    @Test
    void isSupportedExtension_should_return_true_on_images() {
        assertTrue(ImageFilter.isSupportedExtension("image.png"));
        assertTrue(ImageFilter.isSupportedExtension("image.jpg"));
        assertTrue(ImageFilter.isSupportedExtension("image.jpg"));
    }

    @Test
    void isSupportedExtension_should_return_false_on_non_images() {
        assertFalse(ImageFilter.isSupportedExtension("notImage.txt"));
        assertFalse(ImageFilter.isSupportedExtension("notImage.doc"));
        assertFalse(ImageFilter.isSupportedExtension("notImage.xls"));
        assertFalse(ImageFilter.isSupportedExtension("notImage.exe"));
    }

}