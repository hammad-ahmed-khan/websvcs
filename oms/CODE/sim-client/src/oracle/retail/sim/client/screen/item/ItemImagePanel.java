package oracle.retail.sim.client.screen.item;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.panel.RButtonPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RArrowButton;
import oracle.retail.sim.common.item.ItemMessageText;

/********************************************************************************************************
 * Item Image Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemImagePanel extends RPanel {
    private static final long serialVersionUID = 7250384997160285139L;

    private JLabel imageLabel = new JLabel();

    private RArrowButton prevButton = new RArrowButton(RArrowButton.WEST);
    private RArrowButton nextButton = new RArrowButton(RArrowButton.EAST);

    private List<ImageIcon> itemImages;
    private int currentIndex;

    /****************************************************************************************************
     * Assigns Images To Window
     ***************************************************************************************************/
    public void setImages(List<ImageIcon> images) {
        itemImages = images;

        if (itemImages.isEmpty()) {
            imageLabel.setFont(UIManager.getFont(UIThemeName.RERRORDIALOG_FONT));
            imageLabel.setText(Translator.getText(ItemMessageText.ITEM_NO_IMAGE.getText()));
            imageLabel.setOpaque(false);
            imageLabel.setIcon(null);
        } else {
            imageLabel.setOpaque(false);
            imageLabel.setIcon(itemImages.get(currentIndex));
        }

        Dimension dim = new Dimension(25, 25);
        nextButton.setMinimumSize(dim);
        prevButton.setMinimumSize(dim);
        nextButton.setPreferredSize(dim);
        prevButton.setPreferredSize(dim);
        prevButton.addActionListener(createPreviousAction());
        nextButton.addActionListener(createNextAction());

        RButtonPanel buttonPanel = new RButtonPanel();
        buttonPanel.setCenterLayout();
        if (itemImages.size() > 1) {
            buttonPanel.addButton(prevButton);
            buttonPanel.addButton(nextButton);
        }

        removeAll();
        setLayout(new GridBagLayout());
        add(new JLabel(), GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        add(imageLabel, GridTool.constraints(1, 0, 1, 1, 0, 1, 0, 2, 10, 10, 10, 10));
        add(new JLabel(), GridTool.constraints(2, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        add(buttonPanel, GridTool.constraints(0, 1, 3, 1, 1, 0, 0, 1, 0, 0, 0, 0));
    }

    private ActionListener createPreviousAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                currentIndex--;
                if (currentIndex < 0) {
                    currentIndex = itemImages.size() - 1;
                }
                imageLabel.setIcon(itemImages.get(currentIndex));
            }
        };
    }

    private ActionListener createNextAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                currentIndex++;
                if (currentIndex == itemImages.size()) {
                    currentIndex = 0;
                }
                imageLabel.setIcon(itemImages.get(currentIndex));
            }
        };
    }
}
