package oracle.retail.sim.client.swing.test;

import oracle.retail.sim.client.swing.frame.RFixedApplicationFrame;

/********************************************************************************************************
 * This class is the basic application frame for launcher. It represents a good idea of what any new
 * probject would have to implement to get things running.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestFrameApplicationFrame extends RFixedApplicationFrame {
    private static final long serialVersionUID = 2962445487675214179L;

    public TestFrameApplicationFrame() {
        setResizable(true);
        setTitle("GUI Testing Frame");
        setSize(800, 600);
    }

    public void initializeApplication() {
    }

    public void login(boolean login) {
        if (login) {
            TestLoginDialog dialog = new TestLoginDialog(this);
            dialog.setVisible(true);
        }
    }

    public boolean doHotKeyPressed(int key) {
        System.out.println("Hot Key Number: " + key);
        return false;
    }

    public boolean exitApplication() {
        return true;
    }

    public void recover() {
    }
}
