package extra.retail.sim.client.swing.displayer;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

public class StringDisplayer extends AbstractDisplayer {
	public String getDisplayText(Object value) {
		if (value == null) {
			return Translator.getText("New");
		}
		return value.toString();
	}
}
