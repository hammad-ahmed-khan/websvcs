package com.extra.sim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.extra.sim.model.DMShipTrailer;
import com.extra.sim.service.ShipTrailerService;

/**
 * @author aibrahim
 *
 */
@RestController()
@RequestMapping(path = "/ship-trailer")
public class ShipTrailerController {

	@Autowired
	private ShipTrailerService shipTrailerService;

	@GetMapping
	public DMShipTrailer getShipTrailer(@RequestParam(name = "transferReturnId") Long transferReturnId, @RequestParam(name = "isTransfer") Boolean isTransfer) {
		return shipTrailerService.getShipTrailer(transferReturnId, isTransfer);
	}

	@GetMapping(path = "/{id}/exists")
	public boolean hasShipTrailer(@PathVariable(name = "id") Long transferReturnId) {
		return shipTrailerService.hasShipTrailer(transferReturnId);
	}

	@GetMapping(path = "/{storeId}/config")
	public boolean hasShipTrailerConfigured(@PathVariable(name = "storeId") Long storeId) {
		return shipTrailerService.hasShipTrailerConfigured(storeId);
	}

	@GetMapping(path = "/{storeId}/required")
	public boolean hasShipTrailerRequired(@PathVariable(name = "storeId") Long storeId) {
		return shipTrailerService.hasShipTrailerRequired(storeId);
	}

	@PostMapping
	public String saveShipTrailer(@RequestBody DMShipTrailer shipTrailer) {
		shipTrailerService.saveShipTrailer(shipTrailer);
		return "OK";
	}

	@PutMapping
	public String updateShipTrailer(@RequestBody DMShipTrailer shipTrailer) {
		shipTrailerService.updateShipTrailer(shipTrailer);
		return "OK";
	}
}
