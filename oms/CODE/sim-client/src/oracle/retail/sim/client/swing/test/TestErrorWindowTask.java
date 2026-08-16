package oracle.retail.sim.client.swing.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.RBaseTaskPanel;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.BusinessError;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * Test Error Window Functionality
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TestErrorWindowTask extends RBaseTaskPanel implements REventListener {
    private static final long serialVersionUID = 3896443317566365075L;

    private static final String TEST = "Test";
    private static final String FATAL = "Fatal";
    private static final String WEIRD = "Weird";
    private static final String DONE = "Done";

    private RButton testButton = new RButton(TEST);
    private RButton fatalButton = new RButton(FATAL);
    private RButton weirdButton = new RButton(WEIRD);
    private RButton exitButton = new RButton(DONE);

    public TestErrorWindowTask() {
        setTaskTitle("Test Error Window");
    }

    public TestErrorWindowTask(String title) {
        setTaskTitle(title);
    }

    public void init() {
        fatalButton.registerAction(this, FATAL);
        testButton.registerAction(this, TEST);
        weirdButton.registerAction(this, WEIRD);
        exitButton.registerAction(this, DONE);
        addButton(fatalButton);
        addButton(testButton);
        addButton(weirdButton);
        addButton(exitButton);
    }

    public void start() {
    }

    public void stop() {
    }

    public boolean isStartable() {
        return true;
    }

    public boolean isStoppable() {
        return true;
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();

        if (command.equals(FATAL)) {
            doFatalAction();
        } else if (command.equals(TEST)) {
            doTestAction();
        } else if (command.equals(WEIRD)) {
            doWeirdAction();
        } else if (command.equals(DONE)) {
            doDoneAction();
        }
    }

    private void doFatalAction() {
        UIStatusUtility.displayException(this, new IllegalArgumentException("Illegal Argument Exception"));
    }

    private void doWeirdAction() {
        TestWindow dialog = new TestWindow(new JFrame());
        dialog.setVisible(true);
    }

    private void doTestAction() {
        List<BusinessError> errors = new ArrayList<>();
        errors.add(new BusinessError(CommonMessageText.ACTION_INVALID));
        errors.add(new BusinessError(CommonMessageText.VALUE_NOT_VALID, new Integer(1)));
        errors.add(new BusinessError(CommonMessageText.VALUE_NOT_WHOLE, new BigDecimal(11.11)));
        UIStatusUtility.displayException(this, new BusinessException(errors));
    }

    private void doDoneAction() {
        closeTask();
    }
}
