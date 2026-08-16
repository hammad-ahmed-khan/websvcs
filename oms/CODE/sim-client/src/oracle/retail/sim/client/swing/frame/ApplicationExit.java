package oracle.retail.sim.client.swing.frame;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JFrame;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.common.configutil.ConfigManager;

/********************************************************************************************************
 * This class handles closing down an application in four different modes: dissove, shrink, spin and
 * normal.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ApplicationExit extends JComponent implements Runnable {
    private static final long serialVersionUID = -771984957168630443L;

    public static final String GUI_EXIT = "GUI.EXIT";

    private ApplicationExitMode mode = ApplicationExitMode.NORMAL;

    private Frame frame;
    private Window screen;
    private BufferedImage frameBuffer;
    private BufferedImage screenBuffer;
    private int baseX;

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    public ApplicationExit() {
        setDoubleBuffered(true);
    }

    /****************************************************************************************************
     * Assigns the frame to control on exit.
     * <p>
     * @param frame The frame to control on exit.
     ***************************************************************************************************/
    public void setFrame(JFrame frame) {
        this.frame = frame;
        this.baseX = frame.getX();
        ConfigManager manager = Application.getConfigManager();
        if (manager != null) {
            mode = ApplicationExitMode.toValue(manager.getString(GUI_EXIT));
        }
    }

    /****************************************************************************************************
     * Method is called to execute the correct exit strategy.
     ***************************************************************************************************/
    private void execute() throws AWTException {
        if (mode == ApplicationExitMode.NORMAL) {
            frame.dispose();
            System.exit(0);
            return;
        }

        Robot robot = new Robot();

        frameBuffer = robot.createScreenCapture(frame.getBounds());

        frame.setVisible(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRect = new Rectangle(0, 0, screenSize.width, screenSize.height);

        screenBuffer = robot.createScreenCapture(screenRect);

        screen = new Window(new JFrame());
        screen.setSize(screenSize);
        screen.add(this);
        setSize(screenSize);
        screen.setVisible(true);

        new Thread(this).start();
    }

    /****************************************************************************************************
     * Implementation of the runnable interface that calls repaint.
     ***************************************************************************************************/
    public void run() {
        screen.repaint();
    }

    /****************************************************************************************************
     * Implements the paint method to call to the appropriate sub-paint method for the mode. It then
     * disposes the primary frame and exits the JVM.
     ***************************************************************************************************/
    public void paint(Graphics graphics) {
        if (mode == ApplicationExitMode.SPIN) {
            paintSpin(graphics);
            frame.dispose();
            System.exit(0);
            return;
        }
        if (mode == ApplicationExitMode.SHRINK) {
            paintShrink(graphics);
            frame.dispose();
            System.exit(0);
            return;
        }
        if (mode == ApplicationExitMode.DISSOLVE) {
            paintDissolve(graphics);
            frame.dispose();
            System.exit(0);
        }
    }

    /****************************************************************************************************
     * Paint in the spin mode.
     ***************************************************************************************************/
    private void paintSpin(Graphics graphics) {
        Graphics2D bufferedGraphics = null;
        double x = 0.0;
        float scale = 0.0f;
        int increments = 100;
        AffineTransform oldTransform = null;
        BufferedImage bufferedImage = null;

        Graphics2D graphics2D = (Graphics2D) graphics;
        for (int i = 0; i < increments; i++) {
            scale = 1f / ((float) i + 1);
            oldTransform = graphics2D.getTransform();
            bufferedImage = new BufferedImage(screenBuffer.getWidth(), screenBuffer.getHeight(), BufferedImage.TYPE_INT_RGB);
            bufferedGraphics = bufferedImage.createGraphics();
            bufferedGraphics.drawImage(screenBuffer, -screen.getX(), -screen.getY(), null);
            bufferedGraphics.translate(frame.getX(), frame.getY());
            x = -((i + 1) * (frame.getX() + frame.getWidth()) / increments);
            if (baseX + x < -40) {
                break;
            }
            bufferedGraphics.translate(x, 0);
            bufferedGraphics.scale(scale, scale);
            bufferedGraphics.rotate(i / 3.14 / 1.3, frame.getWidth() / 2, frame.getHeight() / 2);
            bufferedGraphics.drawImage(frameBuffer, 0, 0, null);
            bufferedGraphics.setTransform(oldTransform);

            graphics2D.drawImage(bufferedImage, 0, 0, null);
        }
    }

    /****************************************************************************************************
     * Paint in the shrink mode.
     ***************************************************************************************************/
    private void paintShrink(Graphics graphics) {
        Graphics2D bufferedGraphics = null;
        float scale = 0.0f;
        int increments = 50;
        int xextra = 0;
        int yextra = 0;
        int xplus = frame.getWidth() / 100;
        int yplus = frame.getWidth() / 100;
        AffineTransform oldTransform = null;
        BufferedImage bufferedImage = null;

        Graphics2D graphics2D = (Graphics2D) graphics;
        for (int i = 0; i < increments; i++) {
            scale = 1f - (float) i / increments;
            xextra = xextra + xplus;
            yextra = yextra + yplus;
            oldTransform = graphics2D.getTransform();
            bufferedImage = new BufferedImage(screenBuffer.getWidth(), screenBuffer.getHeight(), BufferedImage.TYPE_INT_RGB);
            bufferedGraphics = bufferedImage.createGraphics();
            bufferedGraphics.drawImage(screenBuffer, 0, 0, null);
            bufferedGraphics.translate(frame.getX() + xextra, frame.getY() + yextra);
            bufferedGraphics.scale(scale, scale);
            bufferedGraphics.drawImage(frameBuffer, 0, 0, null);
            bufferedGraphics.setTransform(oldTransform);

            graphics2D.drawImage(bufferedImage, 0, 0, null);
        }
    }

    /****************************************************************************************************
     * Paint in the dissolve mode.
     ***************************************************************************************************/
    private void paintDissolve(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        BufferedImage bufferedImage = null;
        Graphics2D bufferedGraphics = null;

        for (int i = 0; i < 25; i++) {
            Composite oldComposite = graphics2D.getComposite();
            bufferedImage = new BufferedImage(screenBuffer.getWidth(), screenBuffer.getHeight(), BufferedImage.TYPE_INT_RGB);
            bufferedGraphics = bufferedImage.createGraphics();
            bufferedGraphics.drawImage(screenBuffer, 0, 0, null);
            Composite fadeComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f - i / 25f);
            bufferedGraphics.setComposite(fadeComposite);
            bufferedGraphics.drawImage(frameBuffer, frame.getX(), frame.getY(), null);
            bufferedGraphics.setComposite(oldComposite);
            graphics2D.drawImage(bufferedImage, 0, 0, null);
        }
    }

    /****************************************************************************************************
     * Handles exit on the passed in frame. This will exit the frame using the correct visual cues and
     * then stop the entire JVM with a System.exit call.
     * <p>
     * @param frame The frame to exit with.
     ***************************************************************************************************/
    public static void exit(JFrame frame) {
        ApplicationExit controller = new ApplicationExit();
        controller.setFrame(frame);
        try {
            controller.execute();
        } catch (Throwable exception) {
            System.exit(1);
        }
    }
}
