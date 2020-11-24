package eu.sanprojects.daltonj.ui.components;

import eu.sanprojects.daltonj.filters.AbstractFilter;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * JPanel with support for Zooming and Panning of images
 */
public class NavImagePanel extends JPanel {

    private BufferedImage image;
    private AbstractFilter filter;

    private double zoomIncrement = 0.2;
    private double zoomFactor = 1.0 + zoomIncrement;

    private double initialScale = 0.0;
    private double scale = 0.0;
    private int originX = 0;
    private int originY = 0;

    private Point mousePosition;
    private Dimension previousPanelSize;

    /**
     * Mouse event listener classes for Zoom with mouse wheel and clicks
     */
    private class MouseEventsListener extends MouseAdapter implements MouseWheelListener, MouseMotionListener {

        /**
         * on mouse click then zoom on the clicked point
         */
        public void mouseClicked(MouseEvent event) {
            if (SwingUtilities.isRightMouseButton(event)) {
                zoomFactor = 1.0 - zoomIncrement;
                zoomImage();
            } else {
                zoomFactor = 1.0 + zoomIncrement;
                zoomImage();
            }
        }

        /**
         * on mouse wheel zoom
         */
        public void mouseWheelMoved(MouseWheelEvent event) {
            if (event.getWheelRotation() < 0) { // mouuse wheel direction
                zoomFactor = 1.0 + zoomIncrement;
            } else {
                zoomFactor = 1.0 - zoomIncrement;
            }
            zoomImage();
        }

        /**
         * on mouse drag everything should be offset
         */
        public void mouseDragged(MouseEvent event) {
            if (SwingUtilities.isLeftMouseButton(event)) {
                moveImage(event.getPoint());
            }
        }

        public void mouseMoved(MouseEvent event) {
            mousePosition = event.getPoint();
        }
    }


    /**
     * Contructor
     * On windows resize listener and mouse events
     */
    public NavImagePanel() {

        setOpaque(false);

        // register listener on window resize
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                if (scale > 0.0) {
                    if (isFullImageInPanel()) {
                        centerImage();
                    } else if (isImageEdgeInPanel()) {
                        scaleOrigin();
                    }
                    repaint();
                }
                previousPanelSize = getSize();
            }
        });

        // register listener for mouse events
        MouseEventsListener mouseEventsListener = new MouseEventsListener();
        addMouseListener(mouseEventsListener);
        addMouseWheelListener(mouseEventsListener);
        addMouseMotionListener(mouseEventsListener);
    }

    /**
     * setup initial scale and image center
     */
    private void init() {
        double xScale = (double) getWidth() / image.getWidth();
        double yScale = (double) getHeight() / image.getHeight();
        initialScale = Math.min(xScale, yScale);
        scale = initialScale;
        centerImage();
    }

    /**
     * Center image
     */
    private void centerImage() {
        originX = (getWidth() - getScreenImageWidth()) / 2;
        originY = (getHeight() - getScreenImageHeight()) / 2;
    }

    /**
     * Set image to show
     */
    public void setImage(BufferedImage image) {
        this.image = image;
        scale = 0.0;
        validate();
        repaint();
    }

    /**
     * Apply a filter, set null for no filter
     */
    public void applyFilter(AbstractFilter filter) {
        this.filter = filter;
        validate();
        repaint();
    }

    /**
     * Transform panel x,y coordinates to image coordinates
     */
    private Point2D.Double panelToImageTransform(Point point) {
        return new Point2D.Double((point.x - originX) / scale, (point.y - originY) / scale);
    }


    /**
     * Transform image x,y coordinates to panel coordinates
     */
    private Point2D.Double imageToPanelTransform(Point2D.Double p) {
        return new Point2D.Double((p.x * scale) + originX, (p.y * scale) + originY);
    }

    /**
     * Check scale of image: is edge inside the window? useful for window resizing
     */
    private boolean isImageEdgeInPanel() {
        if (previousPanelSize == null) {
            return false;
        }
        return (originX > 0 && originX < previousPanelSize.width || originY > 0 && originY < previousPanelSize.height);
    }


    /**
     * Check scale of image: is full inside the window? useful for window resizing
     */
    private boolean isFullImageInPanel() {
        return (originX >= 0 && (originX + getScreenImageWidth()) < getWidth()
                && originY >= 0 && (originY + getScreenImageHeight()) < getHeight());
    }

    /**
     * scale origin position
     */
    private void scaleOrigin() {
        originX = originX * getWidth() / previousPanelSize.width;
        originY = originY * getHeight() / previousPanelSize.height;
        repaint();
    }

    /**
     * get scale
     */
    private double zoomToScale(double zoom) {
        return initialScale * zoom;
    }

    /**
     * get zoom
     */
    public double getZoom() {
        return scale / initialScale;
    }

    /**
     * set zoom externally by a pivot
     * usefullt for external zoom
     */
    public void setZoom(double newZoom, Point pivot) {
        Point2D.Double imageP = panelToImageTransform(pivot);
        if (imageP.x < 0.0) {
            imageP.x = 0.0;
        }
        if (imageP.y < 0.0) {
            imageP.y = 0.0;
        }
        if (imageP.x >= image.getWidth()) {
            imageP.x = image.getWidth() - 1.0;
        }
        if (imageP.y >= image.getHeight()) {
            imageP.y = image.getHeight() - 1.0;
        }

        Point2D.Double correctedP = imageToPanelTransform(imageP);
        double oldZoom = getZoom();
        scale = zoomToScale(newZoom);
        Point2D.Double panelP = imageToPanelTransform(imageP);

        originX += (correctedP.getX() - (int) panelP.x);
        originY += (correctedP.getY() - (int) panelP.y);

        validate();
        repaint();
    }

    /**
     * Update image zoom ( on mouse clicked )
     */
    private void zoomImage() {
        Point2D.Double imageP = panelToImageTransform(mousePosition);
        double oldZoom = getZoom();
        scale *= zoomFactor;
        Point2D.Double panelP = imageToPanelTransform(imageP);

        originX += (mousePosition.x - (int) panelP.x);
        originY += (mousePosition.y - (int) panelP.y);

        validate();
        repaint();
    }

    /**
     * Drag image and change origin offset
     */
    private void moveImage(Point p) {
        int xDelta = p.x - mousePosition.x;
        int yDelta = p.y - mousePosition.y;
        originX += xDelta;
        originY += yDelta;
        mousePosition = p;

        validate();
        repaint();
    }

    /**
     * Get image bounds
     */
    private Rectangle getImageBounds() {
        Point2D.Double startCoords = panelToImageTransform(new Point(0, 0));
        Point2D.Double endCoords = panelToImageTransform(new Point(getWidth() - 1, getHeight() - 1));
        int panelX1 = (int) startCoords.getX();
        int panelY1 = (int) startCoords.getY();
        int panelX2 = (int) endCoords.getX();
        int panelY2 = (int) endCoords.getY();

        if (panelX1 >= image.getWidth() || panelX2 < 0 || panelY1 >= image.getHeight() || panelY2 < 0) {
            return null;
        }

        int x1 = Math.max(panelX1, 0);
        int y1 = Math.max(panelY1, 0);
        int x2 = (panelX2 >= image.getWidth()) ? image.getWidth() - 1 : panelX2;
        int y2 = (panelY2 >= image.getHeight()) ? image.getHeight() - 1 : panelY2;
        return new Rectangle(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
    }

    /**
     * Paint image and apply filter if possibile
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Paints the background
        if (image == null) {
            return;
        }
        if (scale == 0.0) {
            init();
        }

        Rectangle rect = getImageBounds();
        if (rect == null || rect.width == 0 || rect.height == 0) {
            return;
        }

        BufferedImage filtered = image;
        if (filter != null) {
            filtered = filter.filter(image);
        }

        BufferedImage subimage = filtered.getSubimage(rect.x, rect.y, rect.width,
                rect.height);

        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(subimage, Math.max(0, originX), Math.max(0, originY),
                Math.min((int) (subimage.getWidth() * scale), getWidth()),
                Math.min((int) (subimage.getHeight() * scale), getHeight()), null);

    }

    /**
     * get image width with scale
     */
    private int getScreenImageWidth() {
        return (int) (scale * image.getWidth());
    }

    /**
     * get image height with scale
     */
    private int getScreenImageHeight() {
        return (int) (scale * image.getHeight());
    }

}

