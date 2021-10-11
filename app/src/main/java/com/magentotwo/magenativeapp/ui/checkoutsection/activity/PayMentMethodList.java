package com.magentotwo.magenativeapp.ui.checkoutsection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.magentotwo.magenativeapp.R;
import com.magentotwo.magenativeapp.base.activity.Ced_NavigationActivity;
import com.magentotwo.magenativeapp.databinding.ActivityPaymentMethodsBinding;
import com.magentotwo.magenativeapp.ui.checkoutsection.viewmodel.CheckoutViewModel;
import com.magentotwo.magenativeapp.utils.Urls;
import com.magentotwo.magenativeapp.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PayMentMethodList extends Ced_NavigationActivity {
    ActivityPaymentMethodsBinding methodListBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    CheckoutViewModel checkoutViewModel;
    HashMap<String, String> tittle_methodcode;
    HashMap<String, String> tittle_additional;
    String shippingcode = "";
    boolean custompayment = false;
    String paymentcode = "";
    JsonObject params = new JsonObject();

    //------
    boolean partialPayment = false;
    String paymentcodetmp = "none";
    String amountleftforpay = "none";
    String currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showtootltext(getResources().getString(R.string.paymentmethods));
        showbackbutton();
        checkoutViewModel = new ViewModelProvider(PayMentMethodList.this, viewModelFactory).get(CheckoutViewModel.class);
        methodListBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_payment_methods, content, true);
        methodListBinding.braintreeradio.setButtonDrawable(checkbox_visibility);
        methodListBinding.razorpayradio.setButtonDrawable(checkbox_visibility);


        tittle_methodcode = new HashMap<>();
        tittle_additional = new HashMap<>();

        try {
            try {
                if (cedSessionManagement.getCartId() != null) {
                    params.addProperty("cart_id", cedSessionManagement.getCartId());
                }
                if (session.isLoggedIn()) {
                    params.addProperty("Role", "USER");
                    params.addProperty("customer_id", session.getCustomerid());
                }
                else
                {
                    params.addProperty("Role", "GUEST");
                }
                get_PaymentMethods(params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            methodListBinding.braintreeradio.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                  //  methodListBinding.continueshipping.setText(getResources().getString(R.string.placeorder));
                    methodListBinding.additionaldata.removeAllViews();
                    paymentcode = "Braintree";
                    custompayment = true;
                }
            });
            methodListBinding.razorpayradio.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                   // methodListBinding.continueshipping.setText(getResources().getString(R.string.placeorder));
                    methodListBinding.additionaldata.removeAllViews();
                    paymentcode = "Razorpay";
                    custompayment = true;
                }
            });

            methodListBinding.continueshipping.setOnClickListener(v -> {
                if (paymentcode.isEmpty()) {
                    showmsg(getResources().getString(R.string.selectpaymentfirst));
                } else {
                    if (custompayment) {
                        JsonObject json = new JsonObject();
                        try {
                            if (session.isLoggedIn()) {
                                json.addProperty("Role", "USER");
                                json.addProperty("cart_id", cedSessionManagement.getCartId());
                                json.addProperty("customer_id", session.getCustomerid());
                            } else {
                                json.addProperty("Role", "Guest");
                                json.addProperty("cart_id", cedSessionManagement.getCartId());
                            }
                            json.addProperty("payment_method", "apppayment");
                            json.addProperty("shipping_method", shippingcode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        postshippingpaymentinfo(json);

                    } else {
                        JsonObject json = new JsonObject();
                        try {
                            json.addProperty("Role", "USER");
                            json.addProperty("cart_id", cedSessionManagement.getCartId());
                            json.addProperty("customer_id", session.getCustomerid());
                            json.addProperty("payment_method", paymentcode);
                            json.addProperty("shipping_method", shippingcode);
                            if (methodListBinding.additionaldata.getChildCount() > 0) {
                                LinearLayout layout = (LinearLayout) methodListBinding.additionaldata.getChildAt(0);
                                if (layout.getChildCount() == 3) {
                                    TextView tag = (TextView) layout.getChildAt(1);
                                    EditText value = (EditText) layout.getChildAt(2);
                                    if (value.getText().toString().isEmpty()) {
                                        value.setError(getResources().getString(R.string.fillsomevaluefirst));
                                        value.requestFocus();
                                    } else {
                                        json.addProperty(tag.getText().toString(), value.getText().toString());
                                        postshippingpaymentinfo(json);
                                    }
                                } else {
                                    postshippingpaymentinfo(json);
                                }
                            } else {
                                postshippingpaymentinfo(json);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void get_PaymentMethods(JsonObject params) {
        try {
            checkoutViewModel.getPaymentMethods(PayMentMethodList.this, params).observe(PayMentMethodList.this, apiResponse -> {
                switch (apiResponse.status){
                    case SUCCESS:
                        createpaymentmethods_new(apiResponse.data);
                        break;

                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void createpaymentmethods_new(String methods)
    {
        try {
            JSONObject obj = new JSONObject(methods);
            JSONObject payments = obj.getJSONObject("payments");
            JSONObject object = payments.getJSONObject("methods");
            if (Objects.requireNonNull(object.names()).length() > 0)
            {
                RadioGroup ll = new RadioGroup(this);
                ll.setOrientation(LinearLayout.VERTICAL);
                if (tittle_methodcode.size() > 0)
                {
                    tittle_methodcode.clear();
                    tittle_additional.clear();
                }
                for (int i = 0; i < object.length(); i++) {
                    String key = String.valueOf(object.names().get(i));
                    if (key.contains("apppayment")) {
                        Log.i("REpo", "apppayment");
                    }
                    else if (key.contains("wallet")) {
                        final JSONObject paymentMethodObject = object.getJSONObject(String.valueOf(object.names().get(i)));
                        final CheckBox rdbtn = new CheckBox(this);
                        if (paymentMethodObject.has("currency")) {
                            currency = paymentMethodObject.getString("currency");
                            Log.e("currencyFound", currency);
                        }
                        if (tittle_methodcode.containsKey(paymentMethodObject.getString("label"))) {
                            rdbtn.setText(paymentMethodObject.getString("label") + "_new" + "(" + paymentMethodObject.getString("amount_in_wallet") + ")");
                            tittle_methodcode.put(paymentMethodObject.getString("label") + "_new" + "(" + paymentMethodObject.getString("amount_in_wallet") + ")" + "_new", paymentMethodObject.getString("value"));

                            if (paymentMethodObject.has("additional_data")) {
                                tittle_additional.put(paymentMethodObject.getString("label") + "_new", (String) paymentMethodObject.get("additional_data").toString());
                            }
                        } else {
                            rdbtn.setText(paymentMethodObject.getString("label") + "(" + paymentMethodObject.getString("amount_in_wallet") + ")");
                            tittle_methodcode.put(paymentMethodObject.getString("label") + "(" + paymentMethodObject.getString("amount_in_wallet") + ")", paymentMethodObject.getString("value"));

                            if (paymentMethodObject.has("additional_data")) {
                                tittle_additional.put(paymentMethodObject.getString("label"), paymentMethodObject.get("additional_data").toString());
                            }
                        }
                        rdbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            try {
                                if (isChecked) {
                                    //  continueshipping.setText(getResources().getString(R.string.nativecontinue));
                                    Log.e("amount_left", paymentMethodObject + "");
                                    /*     {"value":"wallet","label":"Wallet System","grandTotal":"₹160.00","amount_in_wallet":"₹350.00","leftamount_in_wallet":"₹190.00"}*/
                                    paymentcodetmp = paymentcode;
                                    if (paymentMethodObject.has("amount_left_for_pay")) {
                                        partialPayment = true;
                                        amountleftforpay = paymentMethodObject.getString("amount_left_for_pay_without_currency");
                                        methodListBinding.radiogroup.setVisibility(View.VISIBLE);
                                    } else {
                                        partialPayment = false;
                                        methodListBinding.radiogroup.setVisibility(View.GONE);
                                        paymentcode = tittle_methodcode.get(rdbtn.getText().toString());
                                    }

                                    if (tittle_additional.containsKey(rdbtn.getText().toString())) {
                                        if (methodListBinding.additionaldata.getChildCount() > 0) {
                                            methodListBinding.additionaldata.removeAllViews();
                                        }
                                        String data = tittle_additional.get(rdbtn.getText().toString());
                                        if (data.startsWith("{")) {
                                            JSONObject object2 = new JSONObject(data);
                                            JSONArray jsonArray = object2.names();
                                            for (int i12 = 0; i12 < jsonArray.length(); i12++) {
                                                LinearLayout layout = new LinearLayout(PayMentMethodList.this);
                                                layout.setOrientation(LinearLayout.VERTICAL);
                                                TextView heading = new TextView(PayMentMethodList.this);
                                                heading.setText(jsonArray.getString(i12) + ":");
                                                heading.setTextColor(getResources().getColor(R.color.AppTheme));
                                                TextView value = new TextView(PayMentMethodList.this);
                                                value.setText(object2.getString(jsonArray.getString(i12)));
                                                layout.addView(heading, 0);
                                                layout.addView(value, 1);
                                                methodListBinding.additionaldata.addView(layout);
                                            }
                                        } else {
                                            if (data.startsWith("[")) {
                                                JSONArray jsonArray = new JSONArray(data);
                                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                LinearLayout layout = new LinearLayout(PayMentMethodList.this);
                                                layout.setOrientation(LinearLayout.VERTICAL);
                                                TextView heading = new TextView(PayMentMethodList.this);
                                                heading.setText(jsonObject.getString("label"));
                                                heading.setTextColor(getResources().getColor(R.color.AppTheme));
                                                TextView value = new TextView(PayMentMethodList.this);
                                                value.setText(jsonObject.getString("name"));
                                                value.setVisibility(View.GONE);
                                                EditText text = new EditText(PayMentMethodList.this);
                                                layout.addView(heading, 0);
                                                layout.addView(value, 1);
                                                layout.addView(text, 2);
                                                methodListBinding.additionaldata.addView(layout);
                                            } else {
                                                LinearLayout layout = new LinearLayout(PayMentMethodList.this);
                                                layout.setOrientation(LinearLayout.VERTICAL);
                                                TextView heading = new TextView(PayMentMethodList.this);
                                                heading.setText(data);
                                                heading.setTextColor(getResources().getColor(R.color.AppTheme));
                                                layout.addView(heading, 0);
                                                methodListBinding.additionaldata.addView(layout);
                                            }
                                        }
                                    } else {
                                        methodListBinding.additionaldata.removeAllViews();
                                    }
                                    Log.i("REpo", "347 1 paymentcode : " + paymentcode);
                                } else {
                                    partialPayment = false;
                                    methodListBinding.radiogroup.setVisibility(View.VISIBLE);
                                    paymentcode = paymentcodetmp;
                                    Log.i("REpo", "347 2 else paymentcode : " + paymentcode);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        methodListBinding.checkboxgroup.addView(rdbtn);
                    }
                    else {
                        JSONObject object1 = object.getJSONObject(String.valueOf(Objects.requireNonNull(object.names()).get(i)));
                        final RadioButton rdbtn = new RadioButton(this);
                        set_regular_font_forRadio(rdbtn);
                        rdbtn.setButtonDrawable(checkbox_visibility);
                        rdbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            try {
                                if (isChecked) {
                                    methodListBinding.continueshipping.setText(getResources().getString(R.string.continuee));
                                    custompayment = false;
                                    paymentcode = tittle_methodcode.get(rdbtn.getText().toString());

                                    if (tittle_additional.containsKey(rdbtn.getText().toString())) {
                                        if (methodListBinding.additionaldata.getChildCount() > 0) {
                                            methodListBinding.additionaldata.removeAllViews();
                                        }
                                        String data = tittle_additional.get(rdbtn.getText().toString());
                                        if (Objects.requireNonNull(data).startsWith("{")) {
                                            JSONObject object2 = new JSONObject(data);
                                            JSONArray jsonArray = object2.names();
                                            for (int i1 = 0; i1 < Objects.requireNonNull(jsonArray).length(); i1++) {
                                                LinearLayout layout = new LinearLayout(PayMentMethodList.this);
                                                layout.setOrientation(LinearLayout.VERTICAL);
                                                TextView heading = new TextView(PayMentMethodList.this);
                                                heading.setText(jsonArray.getString(i1) + ":");
                                                heading.setTextColor(getResources().getColor(R.color.onwhitetextcolor));
                                                TextView value = new TextView(PayMentMethodList.this);
                                                value.setText(object2.getString(jsonArray.getString(i1)));
                                                layout.addView(heading, 0);
                                                layout.addView(value, 1);
                                                methodListBinding.additionaldata.addView(layout);
                                            }
                                        } else {
                                            if (data.startsWith("[")) {
                                                JSONArray jsonArray = new JSONArray(data);
                                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                LinearLayout layout = new LinearLayout(PayMentMethodList.this);
                                                layout.setOrientation(LinearLayout.VERTICAL);
                                                TextView heading = new TextView(PayMentMethodList.this);
                                                heading.setText(jsonObject.getString("label"));
                                                heading.setTextColor(getResources().getColor(R.color.onwhitetextcolor));
                                                TextView value = new TextView(PayMentMethodList.this);
                                                value.setText(jsonObject.getString("name"));
                                                value.setVisibility(View.GONE);
                                                EditText text = new EditText(PayMentMethodList.this);
                                                layout.addView(heading, 0);
                                                layout.addView(value, 1);
                                                layout.addView(text, 2);
                                                methodListBinding.additionaldata.addView(layout);
                                            } else {
                                                LinearLayout layout = new LinearLayout(PayMentMethodList.this);
                                                layout.setOrientation(LinearLayout.VERTICAL);
                                                TextView heading = new TextView(PayMentMethodList.this);
                                                heading.setText(data);
                                                heading.setTextColor(getResources().getColor(R.color.onwhitetextcolor));
                                                layout.addView(heading, 0);
                                                methodListBinding.additionaldata.addView(layout);
                                            }
                                        }
                                    } else {
                                        methodListBinding.additionaldata.removeAllViews();
                                    }
                                } else {
                                    // continueshipping.setText("Continue");
                                    // paymentcode = "";
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        if (tittle_methodcode.containsKey(object1.getString("label"))) {
                            if (object1.has("additional_data")) {
                                tittle_additional.put(object1.getString("label") + "_new", object1.get("additional_data").toString());
                            }
                            tittle_methodcode.put(object1.getString("label") + "_new", object1.getString("value"));
                            rdbtn.setText(object1.getString("label") + "_new");
                        } else {
                            if (object1.has("additional_data")) {
                                tittle_additional.put(object1.getString("label"), object1.get("additional_data").toString());
                            }
                            tittle_methodcode.put(object1.getString("label"), object1.getString("value"));
                            rdbtn.setText(object1.getString("label"));
                        }
                        methodListBinding.radiogroup.addView(rdbtn);
                    }
                }
                Log.i("tittle_additional", "" + tittle_additional);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void postshippingpaymentinfo(JsonObject data) {
        try {
            checkoutViewModel.savePaymentMethods(PayMentMethodList.this, data).observe(PayMentMethodList.this, apiResponse -> {
                switch (apiResponse.status){
                    case SUCCESS:
                        Processdata(apiResponse.data);
                        break;

                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });

            /*Ced_ClientRequestResponseRest crr = new Ced_ClientRequestResponseRest(output -> Processdata(output.toString()), PayMentMethodList.this, "POST", data);
            crr.execute(payment_shippingurl);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Processdata(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.getString("success").equals("true")) {
                    Intent intent = new Intent(PayMentMethodList.this, ReviewOrderSummary.class);
                    intent.putExtra("paymentcode", paymentcode);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
               showmsg(getResources().getString(R.string.somethingbadhappended));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
