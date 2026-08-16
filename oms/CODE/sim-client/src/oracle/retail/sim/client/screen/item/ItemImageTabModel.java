package oracle.retail.sim.client.screen.item;

import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.ItemImage;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Item Image Tab Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemImageTabModel extends SimScreenModel {

    private ItemDetailVO detailVO;

    public boolean setItemDetail(ItemDetailVO itemVO) throws BusinessException {
        if (detailVO != itemVO) {
            detailVO = itemVO;
            return true;
        }
        return false;
    }

    public List<ImageIcon> loadImages(Dimension dimension) throws Exception {
        List<ImageIcon> images = new ArrayList<>();
        for (ImageIcon icon : findItemImages(detailVO.getId())) {
            if (icon.getIconHeight() > dimension.height - 100 && icon.getIconWidth() > dimension.width) {
                images.add(modifyIconSize(icon, dimension.height - 100, dimension.width));
            } else if (icon.getIconHeight() > dimension.height - 100) {
                images.add(modifyIconHeight(icon, dimension.height - 100));
            } else if (icon.getIconWidth() > dimension.width) {
                images.add(modifyIconWidth(icon, dimension.width));
            } else {
                images.add(icon);
            }
        }
        return images;
    }

    private List<ImageIcon> findItemImages(String itemId) throws Exception {
        List<ImageIcon> imageIcons = new ArrayList<>();
        List<ItemImage> itemImages = ClientServiceFactory.getItemServices().findItemImages(itemId);
        for (ItemImage itemImage : itemImages) {
            imageIcons.add(itemImage.getImage());
        }
        return imageIcons;
    }

    /*********************************************************************************************
     * Modifies the width of the icon to be the width passed in as the parameter. This will not
     * modify the height.
     * <p>
     * @param imageIcon The original icon.
     * @param width The desired width of the icon in pixels.
     * <p>
     * @return A new icon resized to accommodate the width.
     *********************************************************************************************/
    public static ImageIcon modifyIconWidth(ImageIcon imageIcon, int width) {
        ImageIcon icon = new ImageIcon();
        Image image = imageIcon.getImage();
        icon.setImage(image.getScaledInstance(width, imageIcon.getIconHeight(), Image.SCALE_SMOOTH));
        return icon;
    }

    /*********************************************************************************************
     * Modifies the height of the icon to be the height passed in as the parameter. This will not
     * modify the width.
     * <p>
     * @param imageIcon The original icon.
     * @param height The desired height of the icon in pixels.
     * <p>
     * @return A new icon resized to accommodate the height.
     *********************************************************************************************/
    public static ImageIcon modifyIconHeight(ImageIcon imageIcon, int height) {
        ImageIcon icon = new ImageIcon();
        Image image = imageIcon.getImage();
        icon.setImage(image.getScaledInstance(imageIcon.getIconWidth(), height, Image.SCALE_SMOOTH));
        return icon;
    }

    /*********************************************************************************************
     * Modifies the size of the icon.
     * <p>
     * @param imageIcon The original icon.
     * @param height The desired height of the icon in pixels.
     * <p>
     * @return A new icon resized to accommodate the height.
     *********************************************************************************************/
    public static ImageIcon modifyIconSize(ImageIcon imageIcon, int height, int width) {
        ImageIcon icon = new ImageIcon();
        icon.setImage(imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        return icon;
    }
}
