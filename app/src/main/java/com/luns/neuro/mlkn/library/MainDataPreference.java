package com.luns.neuro.mlkn.library;

/**
 * Created by neuro on 2/24/2018.
 */
public class MainDataPreference {

    private String strResponse,strMyPhoneNo,strMyServiceRegion;

    public MainDataPreference(String strResponse,String strMyServiceRegion,String strMyPhoneNo) {
        this.strResponse = strResponse;
        this.strMyServiceRegion = strMyServiceRegion;
        this.strMyPhoneNo = strMyPhoneNo;

    }

    public String getStrResponse() {
        return strResponse;
    }
    public String getStrMyServiceRegion() {
        return strMyServiceRegion;
    }
    public String getStrMyPhoneNo() {
        return strMyPhoneNo;
    }

}