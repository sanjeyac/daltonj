package eu.sanprojects.daltonj.ui;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import java.awt.BorderLayout;

public class MainForm {
    private JPanel imagePanel;
    private JPanel rootComponent;
    private JButton getScreenshotButton;
    private JButton openFileButton;
    private JToggleButton grayscaleToggleButton;
    private JToggleButton deuteranopiaToggleButton;
    private JToggleButton tritanopiaToggleButton;
    private JToggleButton protanopiaToggleButton;
    private JToggleButton normalToggleButton;
    private JButton clipboardButton;

    public MainForm() {
        setupUI();
        ButtonGroup filtersButtonGroup = new ButtonGroup();
        filtersButtonGroup.add(grayscaleToggleButton);
        filtersButtonGroup.add(deuteranopiaToggleButton);
        filtersButtonGroup.add(tritanopiaToggleButton);
        filtersButtonGroup.add(protanopiaToggleButton);
        filtersButtonGroup.add(normalToggleButton);
    }

    public JPanel getImagePanel() {
        return imagePanel;
    }

    public JPanel getRootComponent() {
        return rootComponent;
    }

    public JButton getGetScreenshotButton() {
        return getScreenshotButton;
    }

    public JButton getOpenFileButton() {
        return openFileButton;
    }

    public JToggleButton getGrayscaleButton() {
        return grayscaleToggleButton;
    }

    public JToggleButton getDeuteranopiaButton() {
        return deuteranopiaToggleButton;
    }

    public JToggleButton getTritanopiaButton() {
        return tritanopiaToggleButton;
    }

    public JToggleButton getProtanopiaButton() {
        return protanopiaToggleButton;
    }

    public JToggleButton getNormalButton() {
        return normalToggleButton;
    }

    public JButton getClipboardButton() {
        return clipboardButton;
    }

    /**
     * Autogenerate: to improve
     */
    private void setupUI() {

        rootComponent = new JPanel();
        BorderLayout layout = new BorderLayout();
        rootComponent.setLayout(layout);

        final JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        rootComponent.add(toolbar, BorderLayout.PAGE_START);

        // toolbar content
        final JLabel label1 = new JLabel();
        label1.setText("From:");
        toolbar.add(label1);
        getScreenshotButton = new JButton();
        getScreenshotButton.setText("Screenshot");
        toolbar.add(getScreenshotButton);
        clipboardButton = new JButton();
        clipboardButton.setText("Clipboard");
        toolbar.add(clipboardButton);
        openFileButton = new JButton();
        openFileButton.setText("File");
        toolbar.add(openFileButton);
        final JLabel label2 = new JLabel();
        label2.setText("Filter:");
        toolbar.add(label2);
        normalToggleButton = new JToggleButton();
        normalToggleButton.setText("Normal");
        toolbar.add(normalToggleButton);
        deuteranopiaToggleButton = new JToggleButton();
        deuteranopiaToggleButton.setText("Deuteranopia");
        toolbar.add(deuteranopiaToggleButton);
        protanopiaToggleButton = new JToggleButton();
        protanopiaToggleButton.setText("Protanopia");
        toolbar.add(protanopiaToggleButton);
        tritanopiaToggleButton = new JToggleButton();
        tritanopiaToggleButton.setText("Tritanopia");
        toolbar.add(tritanopiaToggleButton);
        grayscaleToggleButton = new JToggleButton();
        grayscaleToggleButton.setText("Grayscale");
        toolbar.add(grayscaleToggleButton);

        // image panel
        imagePanel = new JPanel();
        rootComponent.add(imagePanel, BorderLayout.CENTER);
    }

}
