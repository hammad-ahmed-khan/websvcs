package oracle.retail.sim.client.screen.item;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.task.UITask;
import oracle.retail.sim.client.swing.task.UITaskExecutor;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.widget.SimTab;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.logging.LogService;

/********************************************************************************************************
 * Item Image Tab - Displays item images for viewing.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemImageTab extends SimTab {
    private static final long serialVersionUID = -4285452046029137468L;

    private ItemImageTabModel model = new ItemImageTabModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RLongTextFieldEditor itemDescEditor = new RLongTextFieldEditor("Item Description");
    private RCheckBoxEditor rangedEditor = new RCheckBoxEditor("Ranged");

    private ItemImagePanel itemImagePanel = new ItemImagePanel();

    /****************************************************************************************************
     * Build Tab
     ***************************************************************************************************/

    public ItemImageTab() {
        initializeTab();
        layoutTab();
    }

    private void initializeTab() {
        rangedEditor.setEnabled(false);
    }

    private void layoutTab() {
        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.setLineBorder(1);
        headerPanel.add(itemEditor, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 0));
        headerPanel.add(itemDescEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 5, 0, 0));
        headerPanel.add(rangedEditor, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 5));

        setLayout(new GridBagLayout());
        add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 10, 10, 10, 10));
        add(itemImagePanel, GridTool.constraints(0, 1, 1, 1, 0, 1, 0, 2, 0, 0, 0, 0));
    }

    /****************************************************************************************************
     * Load Tab
     ***************************************************************************************************/

    public void loadTab(ItemDetailVO item) throws Exception {
        if (model.setItemDetail(item)) {
            itemEditor.setData(item.getId());
            if (SimConfigManager.isItemShortDescription()) {
                itemDescEditor.setText(item.getShortDescription());
            } else {
                itemDescEditor.setText(item.getLongDescription());
            }
            rangedEditor.setSelected(item.isRanged());

            UITaskExecutor.execute(this, new LaunchItemTask());
        }
    }

    /****************************************************************************************************
     * Launch Item Task - Thread task to display image popup dialog
     ***************************************************************************************************/

    private class LaunchItemTask implements UITask {

        private List<ImageIcon> itemImages = new ArrayList<>();

        public boolean executeRequest() {
            try {
                itemImages = model.loadImages(getSize());
            } catch (Throwable exception) {
                LogService.error(this, exception.getMessage());
                return false;
            }
            return true;
        }

        public void executeResponse() {
            itemImagePanel.setVisible(false);
            itemImagePanel.setImages(itemImages);
            itemImagePanel.setVisible(true);
        }
    }
}
