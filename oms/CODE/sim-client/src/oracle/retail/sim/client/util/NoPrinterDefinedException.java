package oracle.retail.sim.client.util;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.MessageText;

/**
 * The <code>NoPrinterDefinedException</code> class and its subclasses are used to indicate that an
 * exceptional condition has occurred in the print system.
 */
public class NoPrinterDefinedException extends BusinessException {
    private static final long serialVersionUID = 8176955412487652373L;

    /**
     * Constructs a new <code>NoPrinterDefinedException</code> object with the specified detail
     * message.
     * @param message the message to generate when a <code>NoPrinterDefinedException</code> is thrown
     */
    public NoPrinterDefinedException(MessageText message) {
        super(message);
    }
}
