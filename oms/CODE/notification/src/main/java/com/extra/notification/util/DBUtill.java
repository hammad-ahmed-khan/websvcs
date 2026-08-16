package com.extra.notification.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.extra.notification.common.BaseException;
import com.extra.notification.common.Constant;

public class DBUtill {

	private static Logger LOGGER = Logger.getLogger(NotificationUtil.class);

	public static Connection getConnection() {
		Connection connection = null;
		String url = PropertyHolder.getValue(Constant.OMS_DATABASE_URL);
		String userName = PropertyHolder.getValue(Constant.OMS_DATABASE_USERNAME);
		String password = PropertyHolder.getValue(Constant.OMS_DATABASE_PASSWORD);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Creating database connection....");
			LOGGER.info("Database URL -> " + url);
			LOGGER.info("Database username -> " + userName);
			LOGGER.info("Database password -> " + password);
		}
		try {
			connection = DriverManager.getConnection(url, userName, password);
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Database connection created successfully.");
			}
		} catch (SQLException e) {
			LOGGER.error("Error while getting the connection", e);
			throw new BaseException(e);
		}
		return connection;
	}

	public static void closeConnection(Connection connection) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Closing the database connection....");
		}
		if (connection == null) {
			LOGGER.warn("Database connection instance is null");
			return;
		}
		try {
			if (connection.isClosed()) {
				LOGGER.warn("Database connection is already closed.");
				return;
			}
			connection.close();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Database connection closed successfully");
			}
		} catch (SQLException e) {
			LOGGER.warn("Error while closing the connection.", e);
		}
	}
}
