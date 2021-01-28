package com.luns.neuro.mlkn.DataAdapter;

/**
 * Created by Clarence on 7/31/2016.
 */
public class HelpItems {
    private String strHlpId;
    private String strHlpCategory;
    private String strHlpIssue;
    private String strHlpDescription;

    public HelpItems() {
    }

    public HelpItems(String strHlpId, String strHlpCategory, String strHlpIssue, String strHlpDescription) {
        this.strHlpId = strHlpId;
        this.strHlpCategory = strHlpCategory;
        this.strHlpIssue = strHlpIssue;
        this.strHlpDescription = strHlpDescription;
    }

    public String getStrHlpId() {
        return strHlpId;
    }

    public void setStrHlpId(String strHlpId) {
        this.strHlpId = strHlpId;
    }

    public String getStrHlpCategory() {
        return strHlpCategory;
    }

    public void setStrHlpCategory(String strHlpCategory) {
        this.strHlpCategory = strHlpCategory;
    }

    public String getStrHlpIssue() {
        return strHlpIssue;
    }

    public void setStrHlpIssue(String strHlpIssue) {
        this.strHlpIssue = strHlpIssue;
    }

    public String getStrHlpDescription() {
        return strHlpDescription;
    }

    public void setStrHlpDescription(String strHlpDescription) {
        this.strHlpDescription = strHlpDescription;
    }

}