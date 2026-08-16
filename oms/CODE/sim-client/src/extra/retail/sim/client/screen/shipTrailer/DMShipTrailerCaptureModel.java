/**
 * 
 */
package extra.retail.sim.client.screen.shipTrailer;

import java.util.List;
import java.util.Map;

import extra.retail.sim.client.common.model.BaseLV;
import extra.retail.sim.webservice.shipTrailer.client.DMShipTrailerCaptureClient;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;

/**
 * @author aibrahim
 *
 */
public class DMShipTrailerCaptureModel extends SimScreenModel {

	private DMShipTrailer shipTrailer;

	private DMShipTrailerCaptureClient client = new DMShipTrailerCaptureClient();

	public void loadShipmentBOLID() {
		Long transReturnId = (Long) RepositoryManager.getStateObject(SimClientStateKey.SHIP_TRAILER_TRANSFER_RETURN_ID);
		if (transReturnId != null) {
			Boolean isTransfer = (Boolean) RepositoryManager.getStateObject(SimClientStateKey.SHIP_TRAILER_IS_TRANSFER);
	        RepositoryManager.removeStateObject(SimClientStateKey.SHIP_TRAILER_TRANSFER_RETURN_ID);
	        RepositoryManager.removeStateObject(SimClientStateKey.SHIP_TRAILER_IS_TRANSFER);
	        shipTrailer = getDMShipTrailer(transReturnId, isTransfer);
	        shipTrailer.setTransfer(isTransfer);
	        if (shipTrailer.getTransporter() == null) {
	        	shipTrailer.setNew(true);
	        }
		} else if (RepositoryManager.getStateObject(SimClientStateKey.SHIP_TRAILER_OBJECT) != null) {
			shipTrailer = (DMShipTrailer) RepositoryManager.getStateObject(SimClientStateKey.SHIP_TRAILER_OBJECT);
		}
	}

	public DMShipTrailer getShipTrailerDetail() {
		return shipTrailer;
	}

	private DMShipTrailer getDMShipTrailer(Long transReturnId, Boolean isTransfer) {
		return client.getDMShipTrailer(transReturnId, isTransfer);
	}

	public boolean isDraft() {
		return shipTrailer == null || shipTrailer.getTransferReturnId() == null;
	}

	public Map<String, List<BaseLV>> getBaseLVs() {
		return client.getBaseLVs();
	}

	public void draftShipTrailer(DMShipTrailer shipTrailer) {
		RepositoryManager.addStateObject(SimClientStateKey.SHIP_TRAILER_OBJECT, shipTrailer);
	}

	public void updateShipTrailer(DMShipTrailer shipTrailer) {
		if (shipTrailer.isNew()) {
			shipTrailer.setCreatedUser(getUser().getId().toString());
			client.savaShipTrailer(shipTrailer);
		} else {
			shipTrailer.setUpdatedUser(getUser().getId().toString());
			client.updateShipTrailer(shipTrailer);
		}
	}

	public boolean isNew() {
		return shipTrailer != null && shipTrailer.isNew();
	}
}
