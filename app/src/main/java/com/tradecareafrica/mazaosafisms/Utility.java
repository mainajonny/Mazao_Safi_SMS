package com.tradecareafrica.mazaosafisms;

import java.util.List;

/**
 * Created by Meech  5/25/2018.
 */
public class Utility {

    //Your current machine IP
    private static String SERVER_IP ="http://192.168.8.107";

    //Playframework always listens to port 9000 so this is static
    private static String SERVER_PORT ="9000";

    //Match backend urls with the app urls for sending and receiving data
    public static String url_sendcustomersms=SERVER_IP+":"+SERVER_PORT+"/SendingcustomerSMS";
    public static String url_sendfarmersms=SERVER_IP+":"+SERVER_PORT+"/SendingfarmerSMS";


}
