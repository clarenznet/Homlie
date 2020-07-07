package com.luns.neuro.mlkn.DataAdapter;

/**
 * Created by Clarence on 7/31/2016.
 */
public class MyRequests {
    private String strRequestId;
    private String strRequestTitle;
    private String strRequestBody;
    private String strRequestTime;
    private String strRequestColor;
    private String strCtreatedAt;
    private String strRequestStatus;
    public MyRequests() {
    }
    public MyRequests(String strRequestId, String strRequestTitle, String strRequestBody, String strRequestTime, String strRequestColor, String strRequestStatus, String strCtreatedAt) {
        this.strRequestId = strRequestId;
        this.strRequestTitle = strRequestTitle;
        this.strRequestBody = strRequestBody;
        this.strRequestTime = strRequestTime;
        this.strRequestColor = strRequestColor;
        this.strCtreatedAt = strCtreatedAt;
        this.strRequestStatus = strRequestStatus;
    }

    public String getStrRequestId() {
        return strRequestId;
    }
    public void setStrRequestId(String strRequestId) {
        this.strRequestId = strRequestId;
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