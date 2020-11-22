package eu.sanprojects.daltonj.ui;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

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
}
