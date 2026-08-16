/**
 * 
 */
package com.extra.sim.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.sim.dao.UtilDAO;
import com.extra.sim.model.BaseLV;

/**
 * @author aibrahim
 *
 */
@Service
public class UtilService {

	@Autowired
	private UtilDAO utilDAO;

	public Map<String, List<BaseLV>> getBaseLVs(String listCode) {
		return utilDAO.getBaseLVs(listCode);
	}
}
