package extra.retail.sim.server.process;

import java.util.List;
import java.util.Map.Entry;

import oracle.retail.sim.common.core.SimServerException;

/**
 * ShipmentOrderProcess.java
 * aibrahim
 * 2024
 */
public interface ShipmentOrderProcess {

	void printAWBLabels(Entry<String, List<String>> queDetail) throws SimServerException;
}
