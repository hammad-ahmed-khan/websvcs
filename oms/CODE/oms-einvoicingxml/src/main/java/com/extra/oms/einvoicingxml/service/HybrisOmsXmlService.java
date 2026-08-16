package com.extra.oms.einvoicingxml.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.oms.einvoicingxml.dao.HybrisOMSXmlDAO;
import com.extra.oms.einvoicingxml.model.*;

@Service
public class HybrisOmsXmlService {

	@Autowired
	private HybrisOMSXmlDAO hybrisOMSXmlDAO;

	public void saveXMLInfo(HybrisOmsXmlRequest hybomsxml) {
		hybrisOMSXmlDAO.saveXmlInfo(hybomsxml);
	}
}

