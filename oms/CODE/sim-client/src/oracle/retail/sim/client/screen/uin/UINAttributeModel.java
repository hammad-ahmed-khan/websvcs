package oracle.retail.sim.client.screen.uin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.itemticket.TicketTypeFormat;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyCache;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;
import oracle.retail.sim.common.uin.UINLabelVO;
import oracle.retail.sim.common.uin.UINStoreDept;
import oracle.retail.sim.common.uin.UINType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * UIN Attribute Screen Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UINAttributeModel extends SimScreenModel {

    private List<UINStoreDeptWrapper> allStoreDepartments;
    private List<UINLabelVO> allUinLabels = new ArrayList<UINLabelVO>();

    public List<MdseHierarchyNode> findDepartments() throws Exception {
        return MdseHierarchyCache.getAllDepartments();
    }

    public List<UINStoreDeptWrapper> getStoreDepartments(String departmentName) throws Exception {
        if (allStoreDepartments == null) {
            buildAllStoreDepartments();
            return allStoreDepartments;
        }
        if (departmentName == null) {
            return allStoreDepartments;
        }
        List<UINStoreDeptWrapper> filteredStoreDepts = new ArrayList<>();
        for (UINStoreDeptWrapper storeDept : allStoreDepartments) {
            if (storeDept.getNode().getDepartmentName().equalsIgnoreCase(departmentName)) {
                filteredStoreDepts.add(storeDept);
            }
        }
        return filteredStoreDepts;
    }

    private List<MdseHierarchyNode> getClassList(MdseHierarchyNode departmentNode) throws Exception {
        if (departmentNode != null) {
            return MdseHierarchyCache.getClasses(departmentNode);
        }
        return Collections.emptyList();
    }

    private List<UINStoreDeptWrapper> buildAllStoreDepartments() throws Exception {
        List<UINStoreDept> allStoreDepts = getAllStoreDepartments();
        List<UINStoreDept> currentStoreDepts = ClientServiceFactory.getUINServices().findUINStoreDepartments(getStoreId());

        Map<String, UINStoreDept> storeDeptMap = new HashMap<>();
        for (UINStoreDept storeDept : allStoreDepts) {
            storeDeptMap.put(storeDept.getNode().getCompositeId(), storeDept);
        }
        for (UINStoreDept storeDept2 : currentStoreDepts) {
            storeDeptMap.put(storeDept2.getNode().getCompositeId(), storeDept2);
        }
        allStoreDepartments = new ArrayList<UINStoreDeptWrapper>();
        for (UINStoreDept storeDept : storeDeptMap.values()) {
            UINStoreDeptWrapper wrapper = new UINStoreDeptWrapper(storeDept);
            if (storeDept.getReportFormatId() != null) {
                wrapper.setTicketTypeFormat(getTicketTypeFormat(storeDept.getReportFormatId()));
            }
            allStoreDepartments.add(wrapper);
        }
        return allStoreDepartments;
    }

    private List<UINStoreDept> getAllStoreDepartments() throws Exception {
        List<UINStoreDept> uinStoreDepts = new ArrayList<UINStoreDept>();
        List<MdseHierarchyNode> depts = findDepartments();
        for (MdseHierarchyNode departmentNode : depts) {
            List<MdseHierarchyNode> classes = getClassList(departmentNode);
            for (MdseHierarchyNode mdseHierarchyNode : classes) {
                UINStoreDept storeDept = BOFactory.createUINStoreDept();
                storeDept.doSetNode(mdseHierarchyNode);
                storeDept.doSetStoreId(getStoreId());

                uinStoreDepts.add(storeDept);
            }
        }
        return uinStoreDepts;
    }

    public boolean isValidUinStoreDept(UINStoreDeptWrapper storeDept) {
        if (storeDept == null) {
            return true;
        }
        if (storeDept.getType() == UINType.AGSN && storeDept.getTicketTypeFormat() == null) {
            return false;
        }
        if (storeDept.getCaptureTime() == null && storeDept.getType() == null && storeDept.getLabel() == null) {
            return true;
        }
        if (storeDept.getCaptureTime() != null && storeDept.getType() != null && storeDept.getLabel() != null) {
            return true;
        }
        return false;
    }

    public void updateStoreDepartments() throws Exception {
        List<UINStoreDept> storeDepartments = new ArrayList<UINStoreDept>();
        for (UINStoreDeptWrapper storeDept : allStoreDepartments) {
            if (storeDept.isDirty()) {
                storeDepartments.add(storeDept.getUINStoreDept());
            }
        }
        if (!storeDepartments.isEmpty()) {
            ClientServiceFactory.getUINServices().updateStoreDepartments(storeDepartments);
        }
    }

    public List<UINLabelVO> getLabels() throws Exception {
        if (allUinLabels.isEmpty()) {
            Map<Long, UINLabelVO> uinLabels = ClientServiceFactory.getUINServices().findUINLabels();
            for (UINLabelVO label : uinLabels.values()) {
                allUinLabels.add(label);
            }
        }
        return allUinLabels;
    }

    public List<TicketTypeFormat> getTicketTypeFormats() throws Exception {
        return ClientDataCacheUtility.getAGSNTicketFormats();
    }

    private TicketTypeFormat getTicketTypeFormat(Long reportFormatId) throws Exception {
        for (TicketTypeFormat ticketTypeFormat : ClientDataCacheUtility.getAGSNTicketFormats()) {
            if (ticketTypeFormat.getId().equals(reportFormatId.toString())) {
                return ticketTypeFormat;
            }
        }
        return null;
    }
}
