package oracle.retail.sim.client.swing.util;

import java.util.List;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Formats and translates exception information into a string.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UIExceptionDisplayer extends AbstractDisplayer {

    private boolean primaryOnly = false;

    public UIExceptionDisplayer() {
        this(false);
    }

    public UIExceptionDisplayer(boolean primaryOnly) {
        this.primaryOnly = primaryOnly;
    }

    public String getDisplayText(Object value) {
        StringBuilder text = new StringBuilder();
        if (value instanceof UIException) {
            List<UIProblem> problems = ((UIException) value).getProblems();
            if (problems.size() > 0) {
                if (primaryOnly) {
                    return getProblemText(problems.get(0));
                }
            }
            boolean isFirst = true;
            for (UIProblem problem : problems) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    text.append("\n");
                }
                text.append(getProblemText(problem));
            }
        }
        return text.toString();
    }

    private String getProblemText(UIProblem problem) {
        MessageText message = problem.getMessageText();
        Object[] messageValues = problem.getMessageValues();
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