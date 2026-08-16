package oracle.retail.sim.client.screen.translation;

/********************************************************************************************************
 * Wrapper for a translation to be used the ApplicationBuilder UI.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TranslationDetailWrapper {

    private String key;
    private String english;
    private String value;
    private String comment;

    public TranslationDetailWrapper(String key, String english, String value, String comment) {
        this.key = key;
        this.english = english;
        this.value = value;
        this.comment = comment;
    }

    public String getKey() {
        return key;
    }

    public String getEnglish() {
        if (english == null) {
            return key;
        }
        return english;
    }

    public String getValue() {
        return value;
    }

    public String getComment() {
        return comment;
    }

    public void setValue(String text) {
        value = text;
    }

    public void setComment(String text) {
        comment = text;
    }
}
