package eu.sanprojects.daltonj.ui.components;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Consumer;

public class Screenshot {

    public static void screenshot(Consumer<BufferedImage> onImageReceived) {
        new Thread(() -> {
            BufferedImage bufferedImage  = null;
            Robot robot;
            try {
                Thread.sleep(1000);
                robot = new Robot();
                Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                bufferedImage = robot.createScreenCapture(rectangle);
//                JLabel picLabel = new JLabel(new ImageIcon(bufferedImage));
//                picLabel.setVisible(true);
                File file = new File("/tmp/screen-capture.png");
                boolean status = ImageIO.write(bufferedImage, "png", file);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                onImageReceived.accept(bufferedImage);
            }
        }).start();
    }

}
