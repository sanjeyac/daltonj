package eu.sanprojects.daltonj.ui.components;

import eu.sanprojects.daltonj.filters.AbstractFilter;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Implementation of a JPanel for image visualization with filter integration
 */
public class ImagePanel extends JPanel {

    /**
     * Image to show
     */
    private BufferedImage bufferedImage;

    /**
     * Image Filter
     */
    private AbstractFilter filter;

    /**
     * Paint image with applied filter
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D panelGraphics = (Graphics2D) getGraphics();
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        panelGraphics.setRenderingHints(rh);
        super.paintComponent(g);
        if (bufferedImage != null) {
            BufferedImage output = bufferedImage;
            if (filter != null) {
                output = this.filter.filter(bufferedImage);
            }
            g.drawImage(output, 0, 0, this);
        }
    }


    /**
     * Getter and Setters
     */

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        this.validate();
        this.repaint();
    }

    public void setFilter(AbstractFilter filter) {
        this.filter = filter;
    }

}
