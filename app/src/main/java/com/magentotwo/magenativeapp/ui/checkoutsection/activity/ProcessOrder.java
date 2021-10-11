package com.magentotwo.magenativeapp.ui.checkoutsection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.magentotwo.magenativeapp.R;
import com.magentotwo.magenativeapp.base.activity.Ced_MainActivity;
import com.magentotwo.magenativeapp.base.activity.Ced_NavigationActivity;
import com.magentotwo.magenativeapp.ui.orderssection.activity.Ced_ViewOrder;
import com.magentotwo.magenativeapp.databinding.ActivityPlacedOrderRazorPayBinding;
import com.magentotwo.magenativeapp.ui.checkoutsection.viewmodel.CheckoutViewModel;
import com.magentotwo.magenativeapp.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.magentotwo.magenativeapp.utils.Urls;
import com.magentotwo.magenativeapp.utils.ViewModelFactory;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProcessOrder extends Ced_NavigationActivity {
    ActivityPlacedOrderRazorPayBinding placeOrderBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    CheckoutViewModel checkoutViewModel;
    HashMap<String, HashMap<String, String>> finalconfigdata;
    HashMap<String, ArrayList<String>> bundledata;
    HashMap<String, String> configdata;
    ArrayList<HashMap<String, String>> ProductData;
    String email = "";
    String paymentcode = "";
    String order_id = "";
    boolean load = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        checkoutViewModel = new ViewModelProvider(ProcessOrder.this, viewModelFactory).get(CheckoutViewModel.class);
        placeOrderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_placed_order_razor_pay, content, true);

        bundledata = new HashMap<>();
        configdata = new HashMap<>();
        finalconfigdata = new HashMap<>();
        ProductData = new ArrayList<>();


        paymentcode = getIntent().getStringExtra("paymentcode");

        JsonObject orderpostdata = new JsonObject();
        try {
            orderpostdata.addProperty("email", email);
            if (cedSessionManagement.getCartId() != null) {
                orderpostdata.addProperty("cart_id", cedSessionManagement.getCartId());
            }
            if (session.isLoggedIn()) {
                orderpostdata.addProperty("customer_id", session.getCustomerid());
            }
            if (cedSessionManagement.getStoreId() != null) {
                orderpostdata.addProperty("store_id", cedSessionManagement.getStoreId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        saveorder(orderpostdata);
    }

    public void saveorder(JsonObject data) {
        try {
            checkoutViewModel.saveOrder(ProcessOrder.this, data).observe(ProcessOrder.this, apiResponse -> {
                switch (apiResponse.status){
                    case SUCCESS:
                        Processorderdata(apiResponse.data);
                        break;

                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });

            /*Ced_ClientRequestResponseRest crr = new Ced_ClientRequestResponseRest(output -> Processorderdata(output.toString()), ProcessOrder.this, "POST", data);
            crr.execute(saveorderurl);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Processorderdata(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.getString("success").equals("true")) {
                cedSessionManagement.clearcartId();
                load = true;
                placeOrderBinding.warning.setVisibility(View.VISIBLE);
                Ced_MainActivity.latestcartcount = "0";
                cedSessionManagement.clearcartId();
                changecount();

                Intent intent = new Intent(ProcessOrder.this, Ced_ViewOrder.class);
                intent.putExtra("order_id", jsonObject.getString("order_id"));
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                finish();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();
    }

    public void changecount() {
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (load) {
            try {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("order_id", order_id);
                jsonObject.addProperty("failure", "true");
                setFinalordercheck(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            super.onBackPressed();
        }
    }

    public void setFinalordercheck(JsonObject object) {
        try {
            checkoutViewModel.getAdditionalInfo(ProcessOrder.this, object).observe(ProcessOrder.this, apiResponse -> {
                switch (apiResponse.status){
                    case SUCCESS:
                        try {
                            JSONObject jsonObject1 = new JSONObject(Objects.requireNonNull(apiResponse.data));
                            String success = jsonObject1.getString("success");
                            if (success.equals("true")) {
                                Ced_MainActivity.latestcartcount = "0";
                                cedSessionManagement.clearcartId();
                                changecount();
                                Intent intent = new Intent(ProcessOrder.this, Ced_ViewOrder.class);
                                intent.putExtra("order_id", order_id);
                                startActivity(intent);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            } else {
                                Ced_MainActivity.latestcartcount = "0";
                                cedSessionManagement.clearcartId();
                                changecount();
                                Intent intent1 = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                                startActivity(intent1);
                                finishAffinity();
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;

                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });

            /*Ced_ClientRequestResponseRest crr = new Ced_ClientRequestResponseRest(output -> {
                Log.i("FinalData", "" + output.toString());
                JSONObject jsonObject1 = new JSONObject(output.toString());
                String success = jsonObject1.getString("success");
                if (success.equals("true")) {
                    Ced_MainActivity.latestcartcount = "0";
                    cedSessionManagement.clearcartId();
                    changecount();
                    Intent intent = new Intent(ProcessOrder.this, Ced_ViewOrder.class);
                    intent.putExtra("order_id", order_id);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                } else {
                    Ced_MainActivity.latestcartcount = "0";
                    cedSessionManagement.clearcartId();
                    changecount();
                    Intent intent1 = new Intent(getApplicationContext(), Ced_New_home_page.class);
                    startActivity(intent1);
                    finishAffinity();
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
            }, ProcessOrder.this, "POST", object.toString());
            crr.execute(finalordercheck);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
