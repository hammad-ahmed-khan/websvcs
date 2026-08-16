package org.logicinfo.label.creation.service;


import java.sql.SQLException;

import org.logicinfo.label.creation.model.LableCreationModel;

public interface LableService {

	String getlablePackageResponse(String s)throws SQLException;
	
}
