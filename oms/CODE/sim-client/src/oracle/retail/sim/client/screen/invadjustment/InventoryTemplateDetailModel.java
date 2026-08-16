package oracle.retail.sim.client.screen.invadjustment;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplate;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateLineItem;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateStatus;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Inventory Template Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryTemplateDetailModel extends SimScreenModel {

    private InventoryAdjustmentTemplate template;
    private boolean isTemplateEditable = true;

    public void loadTemplate() throws Exception {
        template = (InventoryAdjustmentTemplate) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_INVENTORY_ADJUSTMENT_TEMPLATE);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_INVENTORY_ADJUSTMENT_TEMPLATE);
        if (template == null) {
            template = BOFactory.createInventoryAdjustmentTemplate();
            template.doSetStoreId(getStoreId());
            template.doSetCreateDate(SimDateUtil.getCurrentDate());
            template.doSetCreateUser(getUserName());
            return;
        }
        assignTemplateViewState();
    }

    private void assignTemplateViewState() throws Exception {
        if (template.getStatus() != InventoryAdjustmentTemplateStatus.IN_PROGRESS) {
            isTemplateEditable = false;
        }
        for (InventoryAdjustmentTemplateLineItem lineItem : template.getLineItems()) {
            if (!hasDataPermission(PermissionKey.DATA_INV_ADJUSTMENT_REASON, lineItem.getReason().getId())) {
                isTemplateEditable = false;
            }
        }
        if (isTemplateEditable) {
            isTemplateEditable = obtainLock(ActivityLockType.INVENTORY_ADJUSTMENT_TEMPLATE, template.getIdAsString());
        }
    }

    public InventoryAdjustmentTemplate getTemplate() {
        return template;
    }

    public boolean isTemplateEditable() {
        return isTemplateEditable;
    }

    public List<InventoryAdjustmentReason> getInventoryAdjustmentReasons() throws Exception {
        List<InventoryAdjustmentReason> availableReasons = new ArrayList<>();
        for (InventoryAdjustmentReason reason : ClientDataCacheUtility.getDisplayableInventoryAdjustmentReasons()) {
            if (hasDataPermission(PermissionKey.DATA_INV_ADJUSTMENT_REASON, reason.getId())) {
                if (reason.isValidTemplateReason()) {
                    availableReasons.add(reason);
                }
            }
        }
        return availableReasons;
    }

    public List<InventoryTemplateLineItemWrapper> getLineItemWrappers() {
        List<InventoryTemplateLineItemWrapper> wrappers = new ArrayList<>();
        for (InventoryAdjustmentTemplateLineItem lineItem : template.getLineItems()) {
            wrappers.add(ClientWrapperFactory.createInventoryTemplateLineItemWrapper(template, lineItem));
        }
        return wrappers;
    }

    public InventoryTemplateLineItemWrapper createNewLineItemWrapper(InventoryAdjustmentReason reason) {
        InventoryTemplateLineItemWrapper wrapper = ClientWrapperFactory.createInventoryTemplateLineItemWrapper(template);
        wrapper.setDefaultReason(reason);
        return wrapper;
    }

    public void deleteLineItem(InventoryTemplateLineItemWrapper wrapper) throws BusinessException {
        InventoryAdjustmentTemplateLineItem lineItem = wrapper.getLineItem();
        if (lineItem != null) {
            template.removeLineItem(lineItem);
        }
    }

    public void confirmTemplate() throws Exception {
        ClientServiceFactory.getInventoryAdjustmentServices().confirmTemplate(template);
        RepositoryManager.addStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_TEMPLATE_MODIFIED, Boolean.TRUE);
    }

    public void saveTemplate() throws Exception {
        if (template.isNew() || template.isDirty()) {
            ClientServiceFactory.getInventoryAdjustmentServices().updateTemplate(template);
        }
        RepositoryManager.addStateObject(SimClientStateKey.INVENTORY_ADJUSTMENT_TEMPLATE_MODIFIED, Boolean.TRUE);
    }

    public boolean confirmTemplateLock() throws Exception {
        if (template.isNew()) {
            return true;
        }
        if (isTemplateEditable) {
            return confirmLock(ActivityLockType.INVENTORY_ADJUSTMENT_TEMPLATE, template.getIdAsString());
        }
        return true;
    }

    public void releaseTemplateLock() throws Exception {
        if (template.isNew()) {
            return;
        }
        if (isTemplateEditable) {
            releaseLock(ActivityLockType.INVENTORY_ADJUSTMENT_TEMPLATE, template.getIdAsString());
        }
    }
}
