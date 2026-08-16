package oracle.retail.sim.client.swing.displayer;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Displays yes/no for boolean values.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class BooleanDisplayer extends AbstractDisplayer {
    private String trueText;
    private String falseText;

    public BooleanDisplayer() {
        trueText = "Yes";
        falseText = "No";
    }

    public BooleanDisplayer(String trueText, String falseText) {
        setTrueText(trueText);
        setFalseText(falseText);
    }

    public void setTrueFalseOption() {
        setTrueText("True");
        setFalseText("False");
    }

    public void setRemoveOption() {
        setTrueText("Remove");
        setFalseText(StringConstants.SPACE);
    }

    public void setTrueText(String value) {
        if (value == null) {
            value = StringConstants.EMPTY;
        }
        trueText = value;
    }

    public void setFalseText(String value) {
        if (value == null) {
            value = StringConstants.EMPTY;
        }
        falseText = value;
    }

    public String getDisplayText(Object object) {
        if (object == null) {
            return StringConstants.EMPTY;
        }
        if ((Boolean) object) {
            return Translator.getText(trueText);
        }
        return Translator.getText(falseText);
    }

    public String getDisplayText(boolean value) {
        if (value) {
            return Translator.getText(trueText);
        }
        return Translator.getText(falseText);
    }
}
