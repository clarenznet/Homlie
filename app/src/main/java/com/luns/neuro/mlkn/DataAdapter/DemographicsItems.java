package com.luns.neuro.mlkn.DataAdapter;

/**
 * Created by Clarence on 7/31/2016.
 */
public class DemographicsItems {
    private String strId;
    private String strDemographic;

    public DemographicsItems() {
    }

    public DemographicsItems(String strId, String strDemographic) {
        this.strId = strId;
        this.strDemographic = strDemographic;
    }
    public String getStrId() { return strId;}
    public void setStrId(String strId) { this.strId = strId; }

    public String getStrDemographic() {return strDemographic; }
    public void setStrDemographic(String strDemographic) {this.strDemographic = strDemographic;}

}