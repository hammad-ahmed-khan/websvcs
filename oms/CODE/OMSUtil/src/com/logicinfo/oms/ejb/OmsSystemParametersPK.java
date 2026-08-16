package com.logicinfo.oms.ejb;

import java.io.Serializable;

public class OmsSystemParametersPK implements Serializable {
    public String parameterId;
    public String parameterName;
    public String parameterValue;

    public OmsSystemParametersPK() {
    }

    public OmsSystemParametersPK(String parameterId, String parameterName) {
        this.parameterId = parameterId;
        this.parameterName = parameterName;
       
    }

   

    public boolean equals(Object other) {
        if (other instanceof OmsSystemParametersPK) {
            final OmsSystemParametersPK otherOmsSystemParametersPK = (OmsSystemParametersPK)other;
            final boolean areEqual =
                (otherOmsSystemParametersPK.parameterId.equals(parameterId) && otherOmsSystemParametersPK.parameterName.equals(parameterName) &&
                 otherOmsSystemParametersPK.parameterValue.equals(parameterValue));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getParameterId() {
        return parameterId;
    }

    public void setParameterId(String parameterId) {
        this.parameterId = parameterId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }
}
