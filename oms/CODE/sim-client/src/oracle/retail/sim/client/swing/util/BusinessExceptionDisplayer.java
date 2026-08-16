package oracle.retail.sim.client.swing.util;

import java.util.List;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.business.BusinessError;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Formats and translates exception information into a string.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class BusinessExceptionDisplayer extends AbstractDisplayer {

    private boolean primaryOnly = false;

    public BusinessExceptionDisplayer() {
        this(false);
    }

    public BusinessExceptionDisplayer(boolean primaryOnly) {
        this.primaryOnly = primaryOnly;
    }

    public String getDisplayText(Object value) {
        StringBuilder text = new StringBuilder();
        if (value instanceof BusinessException) {
            List<BusinessError> errors = ((BusinessException) value).getBusinessErrors();
            if (errors.size() > 0) {
                if (primaryOnly) {
                    return getErrorText(errors.get(0));
                }
            }
            boolean isFirst = true;
            for (BusinessError problem : errors) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    text.append("\n");
                }
                text.append(getErrorText(problem));
            }
        }
        return text.toString();
    }

    private String getErrorText(BusinessError error) {
        MessageText message = error.getMessageText();
        Object[] messageValues = error.getMessageValues();
        if (message != null) {
            if (messageValues != null) {
                return Translator.getMessage(message.getText(), messageValues);
            } else {
                return Translator.getMessage(message.getText());
            }
        }
        return StringConstants.EMPTY;
    }
}