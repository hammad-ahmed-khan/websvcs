package com.logicinfo.oms.util;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * PreOrderItemLocationSync.java
 * aibrahim
 * 2024
 * 
 * Need to add table lock if this application deployed in load balancer with multiple instances.
 *
 */
public class PreOrderItemLocationSync {

	private final static Logger _LOG = Logger.getLogger(PreOrderItemLocationSync.class.getName());

	private static PreOrderItemLocationSync itemLocationSync = new PreOrderItemLocationSync();

	private Set<String> itemLocationKeys = new HashSet<String>();


	private PreOrderItemLocationSync() {
		super();
	}

	public static boolean obtainLock(String item, BigDecimal location, String custOrdNo) throws Exception {
		return itemLocationSync.acquireLock(item, location, custOrdNo);
	}

	private boolean acquireLock(String item, BigDecimal location, String custOrdNo) throws Exception {

		String key = (item + "~" + location.toPlainString()).intern();
		_LOG.info("Trying for lock key -> " + key + " order ->" + custOrdNo);
		synchronized (key) {
			_LOG.info("Got access lock key -> " + key + " order ->" + custOrdNo);
			while(itemLocationKeys.contains(key)) {
				_LOG.info("Already lock on hold lock key -> " + key + " order ->" + custOrdNo);
				key.wait();
			}
			/*
			 * Need to add table lock if this application deployed in load balancer with multiple instances.
			 */
			_LOG.info("key released try to hold list, lock key -> " + key + " order ->" + custOrdNo);
			itemLocationKeys.add(key);
			_LOG.info("Proceding further, lock key -> " + key + " order ->" + custOrdNo);
			return true;
		}
	}

	public static void releaseLock(String item, BigDecimal location, String custOrdNo) {
		itemLocationSync.freeLock(item, location, custOrdNo);
	}

	private void freeLock(String item, BigDecimal location, String custOrdNo) {
		String key = (item + "~" + location.toPlainString()).intern();
		_LOG.info("Trying for lock key to release, key -> " + key  + " order ->" + custOrdNo);
		synchronized (key) {
			_LOG.info("Got lock key to release, key -> " + key );
			itemLocationKeys.remove(key);
			key.notify();
			_LOG.info("completly released, key -> " + key  + " order ->" + custOrdNo);
		}
	}
}
