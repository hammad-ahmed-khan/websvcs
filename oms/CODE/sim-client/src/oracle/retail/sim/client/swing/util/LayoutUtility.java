package oracle.retail.sim.client.swing.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import oracle.retail.sim.client.swing.editor.REditorLabel;
import oracle.retail.sim.client.swing.editor.RetailEditor;
import oracle.retail.sim.client.swing.panel.REditorPanel;

/********************************************************************************************************
 * Utility class is used to perform utility operations on matrix panels and editors.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class LayoutUtility {

    /****************************************************************************************************
     * Private constructor makes static class.
     ***************************************************************************************************/
    private LayoutUtility() {
    }

    /****************************************************************************************************
     * Aligns the editor labels on the REditorPanel parameters. This method will make all the editor
     * labels of equal size. This works particularly well if all the labels are aligned in a single
     * manner. If there are multiple alignments (such as left, right and top) mixed together, some labels
     * may be larger than expected.
     * <p>
     * @param panelOne The first panel.
     * @param panelTwo The second panel.
     ***************************************************************************************************/
    public static void alignPanels(REditorPanel panelOne, REditorPanel panelTwo) {
        int columnCount = panelOne.getLastColumn();
        if (panelTwo.getLastColumn() < columnCount) {
            columnCount = panelTwo.getLastColumn();
        }

        int maxWidthOne = 0;
        int maxWidthTwo = 0;
        for (int i = 0; i <= columnCount; i++) {
            maxWidthOne = panelOne.getMaximumLabelWidth(i);
            maxWidthTwo = panelTwo.getMaximumLabelWidth(i);

            if (maxWidthOne > maxWidthTwo) {
                panelTwo.setLabelWidth(maxWidthOne, i);
            } else {
                panelOne.setLabelWidth(maxWidthTwo, i);
            }
        }
    }

    /****************************************************************************************************
     * Aligns the editor labels on all the panels passed in. This method will make all the editor labels
     * of equal size. This works particularly well if all the labels are aligned in a single manner. If
     * there are multiple alignments (such as left, right and top) mixed together, some labels may be
     * larger than expected.
     * <p>
     * @param panels A collection of REditorPanel.
     * @exception IllegalArgumentException Occurs if one of the panels in the collection is not an
     *                REditorPanel.
     ***************************************************************************************************/
    public static void alignPanels(Collection<REditorPanel> panels) {
        int maxCount = 0;
        int tmpCount = 0;
        int maxWidth = 0;
        int tmpWidth = 0;
        try {
            for (REditorPanel rEditorPanel : panels) {
                tmpCount = rEditorPanel.getLastColumn();
                if (tmpCount > maxCount) {
                    maxCount = tmpCount;
                }
            }
            for (int i = 0; i <= maxCount; i++) {
                for (REditorPanel rEditorPanel : panels) {
                    tmpWidth = rEditorPanel.getMaximumLabelWidth(i);
                    if (tmpWidth > maxWidth) {
                        maxWidth = tmpWidth;
                    }
                }
                for (REditorPanel rEditorPanel : panels) {
                    rEditorPanel.setLabelWidth(maxWidth, i);
                }
            }
        } catch (Throwable exception) {
            throw new IllegalArgumentException("Collection must contain REditorPanels!");
        }
    }

    /****************************************************************************************************
     * Aligns the labels within the collection of editors. This will lock the width of all title labels
     * found within the editors (thus making the editors line up vertically if layed out vertically).
     * <p>
     * @param editors A collection of RetailEditors.
     * @exception IllegalArgumentException Occurs if one of the objects in the collection is not an
     *                RetailEditor.
     ***************************************************************************************************/
    public static void alignEditors(Collection<Component> editors) {
        try {
            int maxWidth = 0;
            int tmpWidth = 0;
            for (Component retailEditor : editors) {
                tmpWidth = ((RetailEditor) retailEditor).getLabel().getPreferredSize().width;
                if (tmpWidth > maxWidth) {
                    maxWidth = tmpWidth;
                }
            }
            REditorLabel label;
            Dimension dimension;
            for (Component retailEditor : editors) {
                label = ((RetailEditor) retailEditor).getLabel();
                dimension = new Dimension(maxWidth, label.getPreferredSize().height);
                label.setMinimumSize(dimension);
                label.setPreferredSize(dimension);
                label.setMaximumSize(dimension);
            }
        } catch (Throwable exception) {
            throw new IllegalArgumentException("Collection must contain RetailEditors!");
        }
    }

    /****************************************************************************************************
     * Aligns all the retail editors that can be found within the panel. This only goes one layer deep
     * and only works on retail editors. This will lock the width of all title labels found within the
     * editors (thus making the editors line up vertically if layed out vertically).
     * <p>
     * @param panel A panel with GridBagLayout as its layout.
     * @exception IllegalArgumentException Occurs if the panel does not contain a GridBagLayout.
     ***************************************************************************************************/
    public static void alignEditorsInGridBag(JPanel panel) {
        if (!(panel.getLayout() instanceof GridBagLayout)) {
            throw new IllegalArgumentException("Panel must contain a GridBagLayout as its layout manager.");
        }

        GridBagLayout gridBagLayout = (GridBagLayout) panel.getLayout();
        Component[] components = panel.getComponents();
        Map<Component, GridBagConstraints> componentMap = new HashMap<>();
        GridBagConstraints constraints = null;
        int maxGridx = -1;

        for (Component component : components) {
            constraints = gridBagLayout.getConstraints(component);
            if (constraints.gridx > maxGridx) {
                maxGridx = constraints.gridx;
            }
            componentMap.put(component, constraints);
        }

        for (int x = 0; x <= maxGridx; x++) {
            List<Component> editorList = new ArrayList<>();
            for (Component component : components) {
                if (component instanceof RetailEditor) {
                    constraints = gridBagLayout.getConstraints(component);
                    if (constraints.gridx == x) {
                        editorList.add(component);
                    }
                }
            }
            alignEditors(editorList);
        }
    }

    /****************************************************************************************************
     * Retrieves the maximum preferred label width from a column of a grid layout.
     * <p>
     * <p>
     * @param panel A panel with GridBagLayout as its layout.
     * @exception IllegalArgumentException Occurs if the panel does not contain a GridBagLayout.
     ***************************************************************************************************/
    public static int getMaxLabelWidth(JPanel panel, int column) {
        if (!(panel.getLayout() instanceof GridBagLayout)) {
            throw new IllegalArgumentException("Panel must contain a GridBagLayout as its layout manager.");
        }
        GridBagLayout gridBagLayout = (GridBagLayout) panel.getLayout();
        Component[] components = panel.getComponents();
        List<Component> editorList = new ArrayList<>();
        for (Component component : components) {
            if (component instanceof RetailEditor) {
                if (gridBagLayout.getConstraints(component).gridx == column) {
                    editorList.add(component);
                }
            }
        }
        return getMaxLabelWidth(editorList);
    }

    /****************************************************************************************************
     * Retrieves the maximum preferred label width of the collection of editors.
     * <p>
     * @param editors A collection of RetailEditors.
     * @exception IllegalArgumentException Occurs if one of the objects in the collection is not an
     *                RetailEditor.
     ***************************************************************************************************/
    public static int getMaxLabelWidth(Collection<Component> editors) {
        try {
            int maxWidth = 0;
            int tmpWidth = 0;
            for (Component retailEditor : editors) {
                tmpWidth = ((RetailEditor) retailEditor).getLabel().getPreferredSize().width;
                if (tmpWidth > maxWidth) {
                    maxWidth = tmpWidth;
                }
            }
            return maxWidth;
        } catch (Throwable exception) {
            throw new IllegalArgumentException("Collection must contain RetailEditors!");
        }
    }
}
