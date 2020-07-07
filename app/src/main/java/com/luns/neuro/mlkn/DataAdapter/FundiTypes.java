package com.luns.neuro.mlkn.DataAdapter;

/**
 * Created by Clarence on 7/31/2016.
 */
public class FundiTypes {
    private String strFtId;
    private String strFtTitle;
    private String strFtDescription;
    private String strFtUrl;
    public FundiTypes() {
    }

    public FundiTypes(String strFtId, String strFtTitle, String strFtDescription, String strFtUrl) {
        this.strFtId = strFtId;
        this.strFtTitle = strFtTitle;
        this.strFtDescription = strFtDescription;
        this.strFtUrl = strFtUrl;
    }

    public String getStrFtId() {
        return strFtId;
    }
    public void setStrFtId(String strFtId) { this.strFtId = strFtId;}

    public String getStrFtTitle() {return strFtTitle;}
    public void setStrFtTitle(String strFtTitle) {this.strFtTitle = strFtTitle;}

    public String getStrFtDescription() {return strFtDescription;}
    public void setStrFtDescription(String strFtDescription) {this.strFtDescription= strFtDescription;}

    public String getStrFtUrl() {return strFtUrl;}
    public void setStrFtUrl(String strFtUrl) {this.strFtUrl = strFtUrl;}

}