package eu.sanprojects.daltonj;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;
import eu.sanprojects.daltonj.filters.Filters;
import eu.sanprojects.daltonj.filters.impl.AbstractFilter;
import eu.sanprojects.daltonj.ui.MainForm;
import eu.sanprojects.daltonj.ui.components.Clipboard;
import eu.sanprojects.daltonj.ui.components.ImagePanel;
import eu.sanprojects.daltonj.ui.components.Screenshot;
import eu.sanprojects.daltonj.ui.components.fileloader.FileLoader;

import javax.swing.JFrame;
import javax.swing.JToggleButton;
import java.awt.CardLayout;
import java.awt.image.BufferedImage;

/**
 * Main class
 */
public class DaltonjApp {

    public static void main(String[] args) {

        // look and feel
        setupLookAndFeel();

        // setup a frame and frame content
        JFrame frame = new JFrame();
        MainForm mainForm = new MainForm();

        // add custom image JPanel to UI
        ImagePanel imagePanel = new ImagePanel();
        mainForm.getImagePanel().setLayout(new CardLayout());
        mainForm.getImagePanel().add(imagePanel);

        setupActionListeners(frame, mainForm, imagePanel);

        mainForm.getNormalButton().setSelected(true);

        frame.setTitle("DaltonJ - Daltonism Simulator");
        frame.setContentPane(mainForm.getRootComponent());
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    /**
     * Setup all button click listeners
     */
    private static void setupActionListeners(JFrame frame, MainForm mainForm, ImagePanel imagePanel) {
        mainForm.getGetScreenshotButton().addActionListener(e -> {
            System.out.println("clicked");
            frame.setVisible(false);
            Screenshot.screenshot(img -> {
                imagePanel.setBufferedImage(img);
                frame.setVisible(true);
            });
        });

        mainForm.getOpenFileButton().addActionListener(e -> {
            BufferedImage image = FileLoader.loadImage(frame);
            if (image != null) {
                imagePanel.setBufferedImage(image);
            }
        });

        mainForm.getClipboardButton().addActionListener(e -> {
            BufferedImage imageFromClipboard = Clipboard.getImageFromClipboard();
            if (imageFromClipboard != null) {
                imagePanel.setBufferedImage(imageFromClipboard);
            }
        });

        //setup buttons
        setFilter(mainForm.getGrayscaleButton(), imagePanel, Filters.GRAY_SCALE);
        setFilter(mainForm.getTritanopiaButton(), imagePanel, Filters.TRITANOPIA);
        setFilter(mainForm.getDeuteranopiaButton(), imagePanel, Filters.DEUTERANOPIA);
        setFilter(mainForm.getProtanotopiaButton(), imagePanel, Filters.PROTANOTOPIA);
        setFilter(mainForm.getNormalButton(), imagePanel, null);
    }


    /**
     * Enable filter on click
     */
    private static void setFilter(JToggleButton button, ImagePanel imagePanel, AbstractFilter filter) {
        button.addActionListener(e -> {
            imagePanel.setFilter(filter);
            imagePanel.validate();
            imagePanel.repaint();
        });
    }


    /**
     * Setup look and feel of Swing components
     */
    private static void setupLookAndFeel() {
        LafManager.install();
        LafManager.install(new DarculaTheme());
    }
}
