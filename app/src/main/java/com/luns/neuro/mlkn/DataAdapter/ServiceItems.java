package com.luns.neuro.mlkn.DataAdapter;

/**
 * Created by Clarence on 7/31/2016.
 */
public class ServiceItems {
    private String strId;
    private String strArticle;
    private String strDemographic;
    private String strPrice;
    private String strArticleIconUrl;
    private int intQuantityValue;
    private boolean isSelected;
    public ServiceItems() {
    }

    public ServiceItems(String strId, String strArticle, String strDemographic, String strPrice, String strArticleIconUrl, int intQuantityValue, boolean isSelected) {
        this.strId = strId;
        this.strArticle = strArticle;
        this.strDemographic = strDemographic;
        this.strPrice = strPrice;
        this.strArticleIconUrl = strArticleIconUrl;
        this.intQuantityValue = intQuantityValue;
        this.isSelected = isSelected;
    }
    public String getStrId() { return strId;}
    public void setStrId(String strId) { this.strId = strId; }

    public String getStrArticle() { return strArticle;}
    public void setStrArticle(String strArticle) {this.strArticle = strArticle; }

    public String getStrDemographic() {return strDemographic; }
    public void setStrDemographic(String strDemographic) {this.strDemographic = strDemographic;}

    public String getStrPrice() { return strPrice;}
    public void setStrPrice(String strPrice) { this.strPrice = strPrice; }

    public String getStrArticleIconUrl() {return strArticleIconUrl; }
    public void setStrArticleIconUrl(String strArticleIconUrl) { this.strArticleIconUrl = strArticleIconUrl; }


    public int getIntQuantityValue() {return intQuantityValue; }
    public void setIntQuantityValue(int intQuantityValue) { this.intQuantityValue = intQuantityValue;}

    public boolean isSelected() {return isSelected;}
    public void setIsSelected(boolean isSelected) { this.isSelected = isSelected; }

}