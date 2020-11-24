package eu.sanprojects.daltonj;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;
import eu.sanprojects.daltonj.filters.Filters;
import eu.sanprojects.daltonj.filters.AbstractFilter;
import eu.sanprojects.daltonj.ui.MainForm;
import eu.sanprojects.daltonj.ui.components.Clipboard;
import eu.sanprojects.daltonj.ui.components.Screenshot;
import eu.sanprojects.daltonj.ui.components.fileloader.FileLoader;
import eu.sanprojects.daltonj.ui.components.NavImagePanel;

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
//        ImagePanel imagePanel = new ImagePanel();
        NavImagePanel navigableImagePanel = new NavImagePanel();
        mainForm.getImagePanel().setLayout(new CardLayout());
        mainForm.getImagePanel().add(navigableImagePanel);

        setupActionListeners(frame, mainForm, navigableImagePanel);

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
    private static void setupActionListeners(JFrame frame, MainForm mainForm, NavImagePanel imagePanel) {
        mainForm.getGetScreenshotButton().addActionListener(e -> {
            System.out.println("clicked");
            frame.setVisible(false);
            Screenshot.screenshot(img -> {
                imagePanel.setImage(img);
                frame.setVisible(true);
            });
        });

        mainForm.getOpenFileButton().addActionListener(e -> {
            BufferedImage image = FileLoader.loadImage(frame);
            if (image != null) {
                imagePanel.setImage(image);
            }
        });

        mainForm.getClipboardButton().addActionListener(e -> {
            BufferedImage imageFromClipboard = Clipboard.getImageFromClipboard();
            if (imageFromClipboard != null) {
                imagePanel.setImage(imageFromClipboard);
            }
        });

        //setup buttons
        setFilter(mainForm.getGrayscaleButton(), imagePanel, Filters.GRAY_SCALE);
        setFilter(mainForm.getTritanopiaButton(), imagePanel, Filters.TRITANOPIA);
        setFilter(mainForm.getDeuteranopiaButton(), imagePanel, Filters.DEUTERANOPIA);
        setFilter(mainForm.getProtanopiaButton(), imagePanel, Filters.PROTANOPIA);
        setFilter(mainForm.getNormalButton(), imagePanel, null);
    }


    /**
     * Enable filter on click
     */
    private static void setFilter(JToggleButton button, NavImagePanel imagePanel, AbstractFilter filter) {
        button.addActionListener(e -> {
            imagePanel.applyFilter(filter);
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
