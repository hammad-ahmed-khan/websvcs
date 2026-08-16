package extra.retail.sim.client.swing.displayer;

import org.apache.commons.lang.StringUtils;

import oracle.retail.sim.common.core.type.AbstractDisplayer;

/**
 * TitlecaseDisplayer.java
 * aibrahim
 * 2024
 */
public class TitlecaseDisplayer extends AbstractDisplayer {

	@Override
	public String getDisplayText(Object value) {
		if (value == null) {
			return "";
		}
		return StringUtils.capitalize(value.toString());
	}
}
