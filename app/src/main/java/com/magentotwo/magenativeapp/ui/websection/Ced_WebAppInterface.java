/*
 * Copyright/**
 *          * CedCommerce
 *           *
 *           * NOTICE OF LICENSE
 *           *
 *           * This source file is subject to the End User License Agreement (EULA)
 *           * that is bundled with this package in the file LICENSE.txt.
 *           * It is also available through the world-wide-web at this URL:
 *           * http://cedcommerce.com/license-agreement.txt
 *           *
 *           * @category  Ced
 *           * @package   MageNative
 *           * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *           * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *           * @license      http://cedcommerce.com/license-agreement.txt
 *
 */
package com.magentotwo.magenativeapp.ui.websection;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.magentotwo.magenativeapp.Ced_MageNative_SharedPrefrence.Ced_SessionManagement;
import com.magentotwo.magenativeapp.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.magentotwo.magenativeapp.ui.orderssection.activity.Ced_Orderview;
import com.magentotwo.magenativeapp.base.activity.Ced_MainActivity;
import com.magentotwo.magenativeapp.ui.homesection.activity.Ced_New_home_page;

public class Ced_WebAppInterface {
    private Context mContext;
    private Ced_SessionManagement session;

    public Ced_WebAppInterface(Context c) {
        mContext = c;
        session =  Ced_SessionManagement.getCed_sessionManagement(mContext);
    }

    @JavascriptInterface
    public void ContinueShopping(String status) {
        if (status.equals("true")) {
            session.clearcartId();
            Ced_MainActivity.latestcartcount = "0";
            Intent intent = new Intent(mContext, Magenative_HomePageNewTheme.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            Log.i("ContinueShopping", "IN");
        }
    }

 /*   @JavascriptInterface
    public void orderSuccessEvent(String status) {
        if (status.equals("true")) {
            session.clearcartId();
            Ced_MainActivity.latestcartcount = "0";
            Log.i("Ordersuccess", "IN");

        }
    }*/

    @JavascriptInterface
    public void orderViewEvent(String orderid) {
        Log.i("OrderView", "IN");
        session.clearcartId();
        Ced_MainActivity.latestcartcount = "0";
        Intent intent = new Intent(mContext, Ced_Orderview.class);
        intent.putExtra("orderid", orderid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

    }
}