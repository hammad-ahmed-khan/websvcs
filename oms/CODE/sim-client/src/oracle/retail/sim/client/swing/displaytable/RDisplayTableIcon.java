package oracle.retail.sim.client.swing.displaytable;

import javax.swing.Icon;
import javax.swing.UIManager;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;

/******************************************************************************************
 * This static helper class returns the correct sorting icon.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RDisplayTableIcon {

    private RDisplayTableIcon() {
    }

    /******************************************************************************************
     * Retrieves the correct icon for the sort number and ascending/descending flag. Ascending
     * data has icon that points down, descending data has icon that points up.
     * <p>
     * @param sortNumber The sort number.
     * @param ascending True if ascending data, false if not.
     *****************************************************************************************/
    public static Icon getIcon(int sortNumber, boolean ascending) {
        if (ascending) {
            switch (sortNumber) {
                case 1:
                    return UIManager.getIcon(UIThemeName.SORT_ASC_1);
                case 2:
                    return UIManager.getIcon(UIThemeName.SORT_ASC_2);
                case 3:
                    return UIManager.getIcon(UIThemeName.SORT_ASC_3);
                case 4:
                    return UIManager.getIcon(UIThemeName.SORT_ASC_4);
                case 5:
                    return UIManager.getIcon(UIThemeName.SORT_ASC_5);
                case 6:
                    return UIManager.getIcon(UIThemeName.SORT_ASC_6);
                case 7:
                    return UIManager.getIcon(UIThemeName.SORT_ASC_7);
                case 8:
                    return UIManager.getIcon(UIThemeName.SORT_ASC_8);
                case 9:
                    return UIManager.getIcon(UIThemeName.SORT_ASC_9);
                default:
                    return UIManager.getIcon(UIThemeName.SORT_ASC_X);
            }
        }
        switch (sortNumber) {
            case 1:
                return UIManager.getIcon(UIThemeName.SORT_DES_1);
            case 2:
                return UIManager.getIcon(UIThemeName.SORT_DES_2);
            case 3:
                return UIManager.getIcon(UIThemeName.SORT_DES_3);
            case 4:
                return UIManager.getIcon(UIThemeName.SORT_DES_4);
            case 5:
                return UIManager.getIcon(UIThemeName.SORT_DES_5);
            case 6:
                return UIManager.getIcon(UIThemeName.SORT_DES_6);
            case 7:
                return UIManager.getIcon(UIThemeName.SORT_DES_7);
            case 8:
                return UIManager.getIcon(UIThemeName.SORT_DES_8);
            case 9:
                return UIManager.getIcon(UIThemeName.SORT_DES_9);
            default:
                return UIManager.getIcon(UIThemeName.SORT_DES_X);
        }
    }
}
