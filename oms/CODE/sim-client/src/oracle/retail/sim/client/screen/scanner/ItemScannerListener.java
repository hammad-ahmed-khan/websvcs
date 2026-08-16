package oracle.retail.sim.client.screen.scanner;

import oracle.retail.sim.common.item.BarcodeItem;


/********************************************************************************************************
 * Interface that must be implemented by any panel wanted to receive input from the barcode scanner.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public interface ItemScannerListener {
    
    public void processBarcodeItem(BarcodeItem barcodeItem);
}