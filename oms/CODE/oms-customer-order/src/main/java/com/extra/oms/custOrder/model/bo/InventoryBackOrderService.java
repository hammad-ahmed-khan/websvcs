package com.extra.oms.custOrder.model.bo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.oms.service.client.IOracleRMSClient;
import com.oracle.retail.integration.base.bo.invbackordcoldesc.v1.InvBackOrdColDesc;
import com.oracle.retail.integration.base.bo.invbackorddesc.v1.InvBackOrdDesc;
import com.oracle.retail.integration.base.bo.invbackorddesc.v1.LocType;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.CreateInvBackOrdColDesc;

/**
 * @author aibrahim
 *
 */
@Service
public class InventoryBackOrderService {

	private static final Logger LOG = Logger.getLogger(InventoryBackOrderService.class);

	private static final String INV_MAP_KEY_PREFIX = "INV-";

	private Map<String, InvBackOrdDesc> invBackOrdMap = new ConcurrentHashMap<>(100, 1f);

	@Autowired
	private IOracleRMSClient oracleRMSClient;

	public void addInvBackOrdEntry(InvBackOrdDesc backOrdDesc) {
		String invKey = (INV_MAP_KEY_PREFIX + backOrdDesc.getItem() + "-" + backOrdDesc.getLocType() + "-" + (LocType.S.equals(backOrdDesc.getLocType()) ? backOrdDesc.getLocation() : (backOrdDesc.getLocation() + "-" + backOrdDesc.getChannelId()))).intern();
		synchronized (invKey) {
			InvBackOrdDesc desc = invBackOrdMap.get(invKey);
			if (desc == null) {
				invBackOrdMap.put(invKey, backOrdDesc);
			} else {
				desc.setBackorderQty(desc.getBackorderQty().add(backOrdDesc.getBackorderQty()));
			}
		}
	}

	public void clearInvBackOrd() {
		invBackOrdMap.clear();
	}

	public void processInvBackOrds() {
		try {
			int boInvSize = invBackOrdMap.size();
			if (boInvSize > 0) {
				CreateInvBackOrdColDesc colDesc = new CreateInvBackOrdColDesc();
				InvBackOrdColDesc backOrdColDesc = new InvBackOrdColDesc();
				backOrdColDesc.setCollectionSize(invBackOrdMap.size());
				backOrdColDesc.getInvBackOrdDesc().addAll(invBackOrdMap.values());
				colDesc.setInvBackOrdColDesc(backOrdColDesc);
				oracleRMSClient.createInvBackOrdColDesc(colDesc);
			}
		} catch (Exception e) {
			LOG.warn("Error while back order reverse adjustments", e);
		} finally {
			clearInvBackOrd();
		}
	}
}
