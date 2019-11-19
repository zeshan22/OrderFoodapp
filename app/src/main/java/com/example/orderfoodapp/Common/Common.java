package com.example.orderfoodapp.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.orderfoodapp.Model.User;

public class Common {


    public static User currentUser;

    public static final String DELETE = "Delete";

    public static final String User_key = "User";

    public static final String Password_key = "Password";


    public static String covertCodeToStatus(String status) {

        if (status.equals("0")) {
            return "Placed";
        } else if (status.equals("1")) {

            return "On my way";
        } else
            return "Shipped";


    }

    public static boolean isConnectedToInternet(Context context){

        ConnectivityManager connectivityManager= (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager!=null){

            NetworkInfo[] infos= connectivityManager.getAllNetworkInfo();

            if (infos!=null){

                for (int i=0;i<infos.length;i++){
                    if (infos[i].getState()==NetworkInfo.State.CONNECTED){

                        return true;
                    }
                }
            }

          }
        return false;
    }
}
