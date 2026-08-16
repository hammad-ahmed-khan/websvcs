package oracle.retail.sim.client.swing.lov;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RHyperlink;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.common.core.locale.StringConstants;

/******************************************************************************************
 * Represents the paging area of the LOV popup.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

class RListOfValuesPagePanel extends RPanel implements ActionListener {
    private static final long serialVersionUID = 6981885299975184770L;

    private static final String FIRST = "<<";
    private static final String PREV = "<";
    private static final String NEXT = ">";
    private static final String LAST = ">>";
    private static final String SEPARATOR = "-";
    private static String OF = Translator.getText("of");

    private RLabel pageLabel = new RLabel();
    private RPanel pagePanel = new RPanel(new FlowLayout(FlowLayout.LEFT));

    private Color linkColor = Color.BLUE;

    private PageLink startLabel = new PageLink(FIRST, false, true);
    private PageLink prevLabel = new PageLink(PREV, false, true);
    private PageLink nextLabel = new PageLink(NEXT, false, true);
    private PageLink finalLabel = new PageLink(LAST, false, true);

    /******************************************************************************************
     * Constructs new page panel.
     ******************************************************************************************/
    public RListOfValuesPagePanel() {
        linkColor = UIManager.getColor(UIThemeName.LIST_OF_VALUES_LINK_FOREGROUND);
        if (linkColor == null) {
            linkColor = Color.BLUE;
        }
        startLabel.setForeground(linkColor);
        prevLabel.setForeground(linkColor);
        nextLabel.setForeground(linkColor);
        finalLabel.setForeground(linkColor);

        startLabel.addActionListener(this);
        prevLabel.addActionListener(this);
        nextLabel.addActionListener(this);
        finalLabel.addActionListener(this);

        setLayout(new BorderLayout());
        add(pageLabel, BorderLayout.EAST);
        add(pagePanel, BorderLayout.CENTER);
    }

    /******************************************************************************************
     * Assigns the page title.
     * <p>
     * @param startValue The start value of the range.
     * @param endValue The end value of the range.
     * @param totalValue The total value of the range.
     ******************************************************************************************/
    protected void setPageTitle(int startValue, int endValue, int totalValue) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(String.valueOf(startValue));
        buffer.append(SEPARATOR);
        buffer.append(String.valueOf(endValue));
        buffer.append(StringConstants.SPACE);
        buffer.append(OF);
        buffer.append(StringConstants.SPACE);
        buffer.append(String.valueOf(totalValue));
        pageLabel.setText(buffer.toString());
    }

    /******************************************************************************************
     * Assigns the page hyperlinks to be displayed.
     * <p>
     * @param page The current page.
     * @param totalPages The total number of pages.
     ******************************************************************************************/
    protected void setPage(int page, int totalPages) {
        pagePanel.removeAll();

        if (totalPages < 2) {
            return;
        }
        if (totalPages < 6) {
            for (int i = 1; i <= totalPages; i++) {
                if (i == page) {
                    pagePanel.add(new PageLink(this, i, false));
                } else {
                    pagePanel.add(new PageLink(this, i, true));
                }
            }
            return;
        }
        int realPage = page;

        if (page == 1) {
            page = page + 2;
        } else if (page == 2) {
            page = page + 1;
        } else if (page == totalPages) {
            page = page - 2;
        } else if (page == totalPages - 1) {
            page = page - 1;
        }

        pagePanel.add(startLabel);
        pagePanel.add(prevLabel);
        pagePanel.add(new PageLink(this, page - 2, realPage != page - 2));
        pagePanel.add(new PageLink(this, page - 1, realPage != page - 1));
        pagePanel.add(new PageLink(this, page, realPage != page));
        pagePanel.add(new PageLink(this, page + 1, realPage != page + 1));
        pagePanel.add(new PageLink(this, page + 2, realPage != page + 2));
        pagePanel.add(nextLabel);
        pagePanel.add(finalLabel);
    }

    /******************************************************************************************
     * Implements the action performed to fire the correct property event.
     ******************************************************************************************/
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals(NEXT)) {
            firePropertyChange(UIPropertyName.LIST_OF_VALUES_NEXT_PAGE, null, command);
        } else if (command.equals(PREV)) {
            firePropertyChange(UIPropertyName.LIST_OF_VALUES_PREV_PAGE, null, command);
        } else if (command.equals(FIRST)) {
            firePropertyChange(UIPropertyName.LIST_OF_VALUES_FIRST_PAGE, null, command);
        } else if (command.equals(LAST)) {
            firePropertyChange(UIPropertyName.LIST_OF_VALUES_LAST_PAGE, null, command);
        } else {
            firePropertyChange(UIPropertyName.LIST_OF_VALUES_PAGE, null, Integer.valueOf(command));
        }
    }

    /******************************************************************************************
     *
     * INNER CLASS - Page link
     *
     ******************************************************************************************/
    private class PageLink extends RHyperlink {
        private static final long serialVersionUID = -1864052047790544973L;

        public PageLink(ActionListener listener, int page, boolean enabled) {
            this(String.valueOf(page), true, enabled);
            addActionListener(listener);
        }

        public PageLink(String text, boolean isUnderlineEnabled, boolean enabled) {
            super(text);
            setActionCommand(text);
            setUnderlineEnabled(isUnderlineEnabled);
            setEnabled(enabled);
            setForeground(linkColor);
        }
    }
}
