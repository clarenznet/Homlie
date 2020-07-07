package com.luns.neuro.mlkn.DataAdapter;

/**
 * Created by Clarence on 7/31/2016.
 */
public class Notifs {
    private String strNotifId;
    private String strNotifTitle;
    private String strNotifBody;
    private String strNotifTime;
    private String strNotifPostId;
    private String strNotifColor;
    public Notifs() {
    }
    public Notifs(String strNotifId, String strNotifTitle, String strNotifBody, String strNotifTime,String strNotifPostId,String strNotifColor) {
        this.strNotifId = strNotifId;
        this.strNotifTitle = strNotifTitle;
        this.strNotifBody = strNotifBody;
        this.strNotifTime = strNotifTime;
        this.strNotifPostId = strNotifPostId;
        this.strNotifColor = strNotifColor;
    }

    public String getStrNotifId() {
        return strNotifId;
    }
    public void setStrNotifId(String strNotifId) {
        this.strNotifId = strNotifId;
    }

    public String getStrNotifTitle() {
        return strNotifTitle;
    }
    public void setStrNotifTitle(String strNotifTitle) {
        this.strNotifTitle = strNotifTitle;
    }

    public String getStrNotifBody() {
        return strNotifBody;
    }
    public void setStrNotifBody(String strNotifBody) {
        this.strNotifBody = strNotifBody;
    }

    public String getStrNotifTime() {
        return strNotifTime;
    }
    public void setStrNotifTime(String strNotifTime) {
        this.strNotifTime = strNotifTime;
    }

    public String getStrNotifPostId() {
        return strNotifPostId;
    }
    public void setStrNotifPostId(String strNotifPostId) { this.strNotifPostId = strNotifPostId; }

    public String getStrNotifColor() {
        return strNotifColor;
    }
    public void setStrNotifColor(String strNotifColor) { this.strNotifColor = strNotifColor; }


}