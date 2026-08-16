package extra.retail.sim.server.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import oracle.retail.sim.common.core.SimServerException;

import extra.retail.sim.server.configutil.ExtraServerConfigManager;

/**
 * ShipmentOrderScriptProcess.java
 * aibrahim
 * 2024
 */
public class ShipmentOrderScriptProcess implements ShipmentOrderProcess {

	private ProcessBuilder pb = new ProcessBuilder();

	private String labelPath;

	private ThreadPoolExecutor printProcessExecutor;

	protected ShipmentOrderScriptProcess() {
		printProcessExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(100));
		labelPath = ExtraServerConfigManager.getAWBLabelFilePath();
	}

	@Override
	public void printAWBLabels(Entry<String, List<String>> queDetail) throws SimServerException {
		printProcessExecutor.submit(new AWBPrintProcessor(queDetail));
	}

	private class AWBPrintProcessor implements Callable<Void> {
	
		private Entry<String, List<String>> queDetail;
		public AWBPrintProcessor(Entry<String, List<String>> queDetail) throws SimServerException {
			if (queDetail == null) {
				throw new SimServerException("Queue name and label path should not be null");
			}
			this.queDetail = queDetail;
		}

		@Override
		public Void call() throws Exception {
			List<String> commands = new ArrayList<>(3);
			for (String command : queDetail.getValue()) {
				commands.add(labelPath);
				commands.add(queDetail.getKey());
				commands.add(command);
				pb.command(commands).start();
				commands.clear();
			}
			return null;
		}
	}
}
