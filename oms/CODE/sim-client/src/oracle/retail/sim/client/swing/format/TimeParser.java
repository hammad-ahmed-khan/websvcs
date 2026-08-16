package oracle.retail.sim.client.swing.format;

/******************************************************************************************
 * Classes interested in parsing a time string for a particular class should implement
 * this method (and be installed in the TimeMaskUtility).
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public interface TimeParser {

    String parseText(String text);
}
