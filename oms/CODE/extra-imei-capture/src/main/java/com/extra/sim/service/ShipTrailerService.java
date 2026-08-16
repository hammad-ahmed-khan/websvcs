package com.extra.sim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.sim.dao.ShipTrailerDAO;
import com.extra.sim.model.DMShipTrailer;

/**
 * @author aibrahim
 *
 */
@Service
public class ShipTrailerService {

	@Autowired
	private ShipTrailerDAO shipTrailerDAO;

	public DMShipTrailer getShipTrailer(Long transferReturnId, Boolean isTransfer) {
		DMShipTrailer shipTrailer = shipTrailerDAO.getShipTrailer(transferReturnId);
		if (shipTrailer == null) {
			shipTrailer = new DMShipTrailer();
			shipTrailer.setTransferReturnId(transferReturnId);
			shipTrailer.setTransfer(isTransfer);
			shipTrailer.setShipmentBOLID(shipTrailerDAO.getShipmentBOLID(transferReturnId, isTransfer));
		}
		return shipTrailer;
	}

	public void saveShipTrailer(DMShipTrailer shipTrailer) {
		shipTrailerDAO.saveShipTrailer(shipTrailer);
	}

	public void updateShipTrailer(DMShipTrailer shipTrailer) {
		shipTrailerDAO.updateShipTrailer(shipTrailer);
	}

	public boolean hasShipTrailer(Long transferReturnId) {
		return shipTrailerDAO.hasShipTrailer(transferReturnId);
	}

	public boolean hasShipTrailerConfigured(Long storeId) {
		return shipTrailerDAO.hasShipTrailerConfigured(storeId);
	}

	public boolean hasShipTrailerRequired(Long storeId) {
		return shipTrailerDAO.hasShipTrailerRequired(storeId);
	}
}
