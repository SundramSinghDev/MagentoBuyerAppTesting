package com.magentotwo.magenativeapp.ui.checkoutsection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.magentotwo.magenativeapp.base.activity.Ced_NavigationActivity;
import com.magentotwo.magenativeapp.R;
import com.magentotwo.magenativeapp.databinding.ActivityShippingMethodsBinding;
import com.magentotwo.magenativeapp.databinding.ShippingMethodGroupBinding;
import com.magentotwo.magenativeapp.ui.checkoutsection.viewmodel.CheckoutViewModel;
import com.magentotwo.magenativeapp.utils.Urls;
import com.magentotwo.magenativeapp.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ShippingMethodList extends Ced_NavigationActivity {
    ActivityShippingMethodsBinding methodBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    CheckoutViewModel checkoutViewModel;
    JsonObject params = new JsonObject();
    String shippingcodetosend = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showtootltext(getResources().getString(R.string.shippingmethods));
        showbackbutton();
        checkoutViewModel = new ViewModelProvider(ShippingMethodList.this, viewModelFactory).get(CheckoutViewModel.class);
        methodBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_shipping_methods, content, true);
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
            get_ShippingMethods(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            methodBinding.continueshipping.setOnClickListener(v -> {
                prepareshipingoptions();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareshipingoptions() {
        Boolean skipPrepareApi_AsCheckoutIsNormalCheckout=false;
        JSONArray shipping_code_arraytosend=new JSONArray();
        String msg = (getResources().getString(R.string.selectshippingforvendor_msg));
        for(int k=0;k<methodBinding.allshippingmethodsParent.getChildCount();k++)
        {
            ConstraintLayout constraintLayout= (ConstraintLayout) methodBinding.allshippingmethodsParent.getChildAt(k);
            TextView selectedship_option= (TextView) constraintLayout.getChildAt(0);
            TextView vendorname= (TextView) constraintLayout.getChildAt(1);
            if(selectedship_option.getText().toString().isEmpty())
            {
                msg+=vendorname.getText().toString();
                if(k!=(methodBinding.allshippingmethodsParent.getChildCount()-1))
                {
                    msg+="\n";
                }
            }
            else
            {
                shipping_code_arraytosend.put(selectedship_option.getText().toString());
            }
            if(vendorname.getTag()!=null && vendorname.getTag().toString().equals(getResources().getString(R.string.normalcheckout)))
            {
                skipPrepareApi_AsCheckoutIsNormalCheckout=true;
                shippingcodetosend =selectedship_option.getText().toString();
            }
        }
        if(msg.equals(getResources().getString(R.string.selectshippingforvendor_msg)))
        {
            if(skipPrepareApi_AsCheckoutIsNormalCheckout)
            {
                saveshipping(params,shippingcodetosend);
            }
            else
            {
                JsonObject param = new JsonObject();
                param.addProperty("shipping_code", shipping_code_arraytosend.toString());
                checkoutViewModel.prepareShippingMethod(ShippingMethodList.this, param).observe(ShippingMethodList.this, apiResponse -> {
                    switch (apiResponse.status) {
                        case SUCCESS:
                            try {
                                JSONObject jsonObject = new JSONObject(apiResponse.data);
                                if (jsonObject.getBoolean("success")) {
                                    shippingcodetosend = jsonObject.getString("shipping_method");
                                    saveshipping(params, shippingcodetosend);
                                } else {
                                    showmsg(getResources().getString(R.string.somethingbadhappended));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;

                        case ERROR:
                            Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                            showmsg(getResources().getString(R.string.errorString));
                            break;
                    }
                });
            }
        }
        else
        {
            if(methodBinding.allshippingmethodsParent.getChildCount()==1)
            {
                showmsg(getResources().getString(R.string.select_method_for_shipping));
            }
            else
            {
                showmsg(msg);
            }

        }
    }

    private void saveshipping(JsonObject params, String shippingcodetosend) {
        if (shippingcodetosend.isEmpty() || shippingcodetosend == null) {
            showmsg(getResources().getString(R.string.selectshippinfmethodfirst));
        }
        else
        {
            params.addProperty("shipping_method", shippingcodetosend);
            checkoutViewModel.saveShippingMethods(ShippingMethodList.this, params).observe(ShippingMethodList.this, apiResponse -> {
                switch (apiResponse.status){
                    case SUCCESS:
                        try {
                            JSONObject jsonObject = new JSONObject(apiResponse.data);
                            if (jsonObject.getString("success").equals("true")) {
                                Intent intent = new Intent(ShippingMethodList.this, PayMentMethodList.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            } else {
                                showmsg(getResources().getString(R.string.somethingbadhappended));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });
        }
    }

    private void createshipmenthods(String output)
    {
        try
        {
            JSONObject object = new JSONObject(output);
            if (object.getBoolean("success"))
            {
                if (!object.get("shipping").equals("No Quotes Availabile."))
                {
                    JSONObject shipping = object.getJSONObject("shipping");
                    Object shipmethod=shipping.get("methods");
                    if(shipmethod instanceof JSONObject)  //this is multishipping case
                    {
                        JSONObject shippingmethods = shipping.getJSONObject("methods");
                        if (shippingmethods.length() > 0)
                        {
                            Iterator<String> iter = shippingmethods.keys(); //This should be the iterator you want.
                            while(iter.hasNext())
                            {
                                String key = iter.next();
                                ShippingMethodGroupBinding shippingMethodGroupBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.shipping_method_group, null, true);
                                shippingMethodGroupBinding.vendorname.setText(key);
                                JSONArray vendormethods=shippingmethods.getJSONArray(key);
                                for(int k=0;k<vendormethods.length();k++)
                                {
                                    HashMap<String, String> tittle_methodcode = new HashMap<>();
                                    JSONObject object1 = vendormethods.getJSONObject(k);
                                    final RadioButton rdbtn = new RadioButton(this);
                                    set_regular_font_forRadio(rdbtn);
                                    rdbtn.setButtonDrawable(checkbox_visibility);
                                    rdbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                        try {
                                            if (isChecked) {
                                                shippingMethodGroupBinding.selectedshipOption.setText(tittle_methodcode.get(rdbtn.getText().toString()));
                                            } else {
                                                shippingMethodGroupBinding.selectedshipOption.setText("");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    });
                                    if (tittle_methodcode.containsKey(object1.getString("label"))) {
                                        tittle_methodcode.put(object1.getString("label") + "_new", object1.getString("value"));
                                        rdbtn.setText(object1.getString("label") + "_new");
                                    } else {
                                        tittle_methodcode.put(object1.getString("label"), object1.getString("value"));
                                        rdbtn.setText(object1.getString("label"));
                                    }
                                    shippingMethodGroupBinding.vendorRadioGroup.addView(rdbtn);
                                }
                                methodBinding.allshippingmethodsParent.addView(shippingMethodGroupBinding.getRoot());
                            }
                        }
                    }
                    else// normal checkout case
                    {
                        JSONArray vendormethods=shipping.getJSONArray("methods");
                        ShippingMethodGroupBinding shippingMethodGroupBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.shipping_method_group, null, true);
                        shippingMethodGroupBinding.vendorname.setVisibility(View.GONE);
                        shippingMethodGroupBinding.vendorname.setTag(getResources().getString(R.string.normalcheckout));
                        for(int k=0;k<vendormethods.length();k++)
                        {
                            HashMap<String, String> tittle_methodcode = new HashMap<>();
                            JSONObject object1 = vendormethods.getJSONObject(k);
                            final RadioButton rdbtn = new RadioButton(this);
                            set_regular_font_forRadio(rdbtn);
                            rdbtn.setButtonDrawable(checkbox_visibility);
                            rdbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                try {
                                    if (isChecked) {
                                        shippingMethodGroupBinding.selectedshipOption.setText(tittle_methodcode.get(rdbtn.getText().toString()));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                            if (tittle_methodcode.containsKey(object1.getString("label"))) {
                                tittle_methodcode.put(object1.getString("label") + "_new", object1.getString("value"));
                                rdbtn.setText(object1.getString("label") + "_new");
                            } else {
                                tittle_methodcode.put(object1.getString("label"), object1.getString("value"));
                                rdbtn.setText(object1.getString("label"));
                            }
                            shippingMethodGroupBinding.vendorRadioGroup.addView(rdbtn);
                        }
                        methodBinding.allshippingmethodsParent.addView(shippingMethodGroupBinding.getRoot());
                    }
                }
                else
                {
                    showmsg("No Quotes Availabile.");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void get_ShippingMethods(JsonObject data) {
        try {
            checkoutViewModel.getShippingMethods(ShippingMethodList.this, data).observe(ShippingMethodList.this, apiResponse -> {
                switch (apiResponse.status){
                    case SUCCESS:
                        createshipmenthods(apiResponse.data);
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
}
