package com.luns.neuro.mlkn.DataAdapter;

/**
 * Created by Clarence on 7/31/2016.
 */
public class MyRequests {
    private String strRequestTypeUrl;
    private String strRequestTcktCode;
    private String strRequestTitle;
    private String strRequestBody;
    private String strRequestTime;
    private String strRequestColor;
    private String strCtreatedAt;
    private String strRequestStatus;
    public MyRequests() {
    }

    public MyRequests(String strRequestTypeUrl, String strRequestTcktCode, String strRequestTitle, String strRequestBody, String strRequestTime, String strRequestColor, String strRequestStatus, String strCtreatedAt) {
        this.strRequestTypeUrl = strRequestTypeUrl;
        this.strRequestTcktCode = strRequestTcktCode;
        this.strRequestTitle = strRequestTitle;
        this.strRequestBody = strRequestBody;
        this.strRequestTime = strRequestTime;
        this.strRequestColor = strRequestColor;
        this.strCtreatedAt = strCtreatedAt;
        this.strRequestStatus = strRequestStatus;
    }

    public String getStrRequestTypeUrl() {
        return strRequestTypeUrl;
    }

    public void setStrRequestTypeUrl(String strRequestTypeUrl) {
        this.strRequestTypeUrl = strRequestTypeUrl;
    }

    public String getStrRequestTcktCode() {
        return strRequestTcktCode;
    }
    public void setStrRequestTcktCode(String strRequestTcktCode) {
        this.strRequestTcktCode = strRequestTcktCode;
    }

    public String getStrRequestTitle() {
        return strRequestTitle;
    }
    public void setStrRequestTitle(String strRequestTitle) {
        this.strRequestTitle = strRequestTitle;
    }

    public String getStrRequestBody() {
        return strRequestBody;
    }
    public void setStrRequestBody(String strRequestBody) {
        this.strRequestBody = strRequestBody;
    }

    public String getStrRequestTime() {
        return strRequestTime;
    }
    public void setStrRequestTime(String strRequestTime) {
        this.strRequestTime = strRequestTime;
    }

    public String getStrRequestColor() {
        return strRequestColor;
    }
    public void setStrRequestColor(String strRequestColor) { this.strRequestColor = strRequestColor; }

    public String getStrRequestStatus() {
        return strRequestStatus;
    }
    public void setStrRequestStatus(String strRequestStatus) { this.strRequestStatus = strRequestStatus; }

    public String getStrCtreatedAt() {
        return strCtreatedAt;
    }
    public void setStrCtreatedAt(String strCtreatedAt) { this.strCtreatedAt = strCtreatedAt; }
}