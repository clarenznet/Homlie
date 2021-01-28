package com.luns.neuro.mlkn.Interceptor;

public class Constants {
    public static final int CONNECT_TIMEOUT = 60 * 1000;

    public static final int READ_TIMEOUT = 60 * 1000;

    public static final int WRITE_TIMEOUT = 60 * 1000;

    public static final String BASE_URL = "https://api.safaricom.co.ke/";

    public static final String BUSINESS_SHORT_CODE = "504628";
    public static final String PASSKEY = "ba95f91d4c495092444e625821fb00cc5eec70f8a4d64c0fdc95f8c23b501283";
    public static final String TRANSACTION_TYPE = "CustomerBuyGoodsOnline";//"CustomerPayBillOnline";
    public static final String PARTYB = "507984"; //174379same as 504628business shortcode above7099181
    public static final String CALLBACKURL = "https://www.homlie.co.ke/aqim/callback_url.php?strticketcode=";

    //You should get these values from the earlier post Part 1

}