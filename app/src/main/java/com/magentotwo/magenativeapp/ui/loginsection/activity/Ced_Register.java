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
package com.magentotwo.magenativeapp.ui.loginsection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.widget.AppCompatButton;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import com.magentotwo.magenativeapp.base.activity.Ced_NavigationActivity;
import com.magentotwo.magenativeapp.R;
import com.magentotwo.magenativeapp.base.activity.Ced_MainActivity;
import com.magentotwo.magenativeapp.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.magentotwo.magenativeapp.databinding.MagenativeNewRegistrationLayoutBinding;
import com.magentotwo.magenativeapp.rest.ApiResponse;
import com.magentotwo.magenativeapp.ui.loginsection.viewmodel.RegisterViewModel;
import com.magentotwo.magenativeapp.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.magentotwo.magenativeapp.utils.Urls;
import com.magentotwo.magenativeapp.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_Register extends Ced_NavigationActivity {

    @Inject
    ViewModelFactory viewModelFactory;
    RegisterViewModel registerViewModel;
    MagenativeNewRegistrationLayoutBinding registerBinding;

    EditText edt_firstname, edt_lastname, edt_email, edt_password, edt_cnf_password;
    ImageView mr, mis;
    AppCompatButton register_button;
    CheckBox newsletter;
    String status, cart_summary, customer_id, hash, outputstring, isConfirmationRequired,
            message, firstname, lastname, email, password, cnf_password, customergroup_id = "";
    TextView account;

    static final String KEY_OBJECT = "data";
    static final String KEY_STATUS = "status";
    static final String KEY_CUSTOMER = "customer";
    static final String KEY_MESSAGE = "message";
    static final String KEY_IS_CONFIRMATION_REQUIRED = "isConfirmationRequired";
    static final String KEY_Message = "message";
    static final String KEY_CUSTOMER_ID = "customer_id";
    static final String KEY_CUSTOMERGROUP_ID = "customer_group";
    static final String KEY_CART_SUMMARY = "cart_summary";
    static final String KEY_HASH = "hash";
    LinearLayout prefixsection, suffixsection, dobsection;
    TextInputLayout middlenamesection,taxvatsection;
    TextView prefixlabel, suffixlabel;
    RadioGroup prefix;
    RadioGroup suffix;
    TextView prefixname, suffixname, prefixoptions, suffixoptions;
    String prefixvalue = "";
    String suffixvalue = "";
    TextView dob;
    EditText MageNative_midllename;
    EditText MageNative_taxvat;

    boolean male = false;
    boolean female = false;
    boolean prefixflag = false;
    boolean suffixflag = false;
    boolean dobflag = false;
    boolean taxvatflag = false;
    boolean middlenamevatflag = false;
    boolean flag = true;
    boolean isfromcheckout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_new_registration_layout, content, true);
        registerViewModel = new ViewModelProvider(Ced_Register.this, viewModelFactory).get(RegisterViewModel.class);
        Objects.requireNonNull(getSupportActionBar()).hide();
        navBinding.MageNativeTawkSupport.setVisibility(View.GONE);

        if (getIntent().getStringExtra("isfromcheckout") != null) {
            isfromcheckout = true;
        }
        hide_bottom_navigation();
        edt_firstname = registerBinding.edtFirstName;
        edt_lastname = registerBinding.edtLastName;
        edt_email = registerBinding.edtEmail;
        account = registerBinding.txtAccount;
        newsletter = registerBinding.chkNewsLetter;
        mr = registerBinding.male;
        mis = registerBinding.female;
        prefixsection = registerBinding.prefixsection;
        middlenamesection = registerBinding.middlenamesection;
        suffixsection = registerBinding.suffixsection;
        dobsection = registerBinding.dobsection;
        taxvatsection = registerBinding.taxvatsection;
        prefixlabel = registerBinding.prefixlabel;
        suffixlabel = registerBinding.suffixlabel;
      /*  middlenamelabel = registerBinding.middlenamelabel;
        taxvatlabel = registerBinding.taxvatlabel;*/
        prefix = registerBinding.prefix;
        suffix = registerBinding.suffix;
        prefixname = registerBinding.prefixname;
        suffixname = registerBinding.suffixname;
      /*  taxvatname = registerBinding.taxvatname;
        middlename = registerBinding.midllenamename;*/
        prefixoptions = registerBinding.prefixoptions;
        suffixoptions = registerBinding.suffixoptions;
        dob = registerBinding.dob;
        MageNative_midllename = registerBinding.MageNativeMidllename;
        MageNative_taxvat = registerBinding.MageNativeTaxvat;
        edt_password = registerBinding.edtPassword;
        edt_cnf_password = registerBinding.edtConfirmPass;
        register_button = registerBinding.btnRegister;

        mr.setOnClickListener(v -> {
            mr.setBackground(getResources().getDrawable(R.drawable.selected));
            male = true;
            female = false;
            mis.setBackground(getResources().getDrawable(R.drawable.selectedwhite));
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker_DDMMYYYY(dob);
            }
        });
        mis.setOnClickListener(v -> {
            mis.setBackground(getResources().getDrawable(R.drawable.selected));
            male = false;
            female = true;
            mr.setBackground(getResources().getDrawable(R.drawable.selectedwhite));
        });

        final JsonObject hashMap = new JsonObject();
        register_button.setOnClickListener(v -> {
            try {
                firstname = edt_firstname.getText().toString();
                lastname = edt_lastname.getText().toString();
                email = edt_email.getText().toString();
                password = edt_password.getText().toString();
                cnf_password = edt_cnf_password.getText().toString();

                if (firstname.isEmpty()) {
                    edt_firstname.requestFocus();
                    edt_firstname.setError(getResources().getString(R.string.empty));
                } else {
                    if (lastname.isEmpty()) {
                        edt_lastname.requestFocus();
                        edt_lastname.setError(getResources().getString(R.string.empty));
                    } else {
                        if (email.isEmpty()) {
                            edt_email.requestFocus();
                            edt_email.setError("Please fill email");
                        } else {
                            if (!isValidEmail(email)) {
                                edt_email.requestFocus();
                                edt_email.setError(getResources().getString(R.string.invalidemail));
                            } else {
                                if (password.isEmpty() || password.length() < 8) {
                                    edt_password.requestFocus();
                                    edt_password.setError(getResources().getString(R.string.minimum_8_character));
                                } else {
                                    if (cnf_password.isEmpty() || !cnf_password.equals(password)) {
                                        edt_cnf_password.requestFocus();
                                        edt_cnf_password.setError(getResources().getString(R.string.confirmnotmatch));
                                    } else {
                                        if (prefixflag) {
                                            if (prefixvalue.isEmpty()) {
                                                flag = false;
                                                showmsg( getResources().getString(R.string.selectsomeprefixvalue));
                                            } else {
                                                flag = true;
                                                hashMap.addProperty(prefixname.getText().toString(), prefixvalue);
                                            }
                                        }
                                        if (middlenamevatflag) {
                                            if (flag) {
                                                if (MageNative_midllename.getText().toString().isEmpty()) {
                                                    flag = false;
                                                    MageNative_midllename.setError(getResources().getString(R.string.empty));
                                                    MageNative_midllename.requestFocus();
                                                } else {
                                                    flag = true;
                                                    hashMap.addProperty(MageNative_midllename.getTag().toString(), MageNative_midllename.getText().toString());
                                                }
                                            }
                                        }
                                        if (suffixflag) {
                                            if (flag) {
                                                if (suffixvalue.isEmpty()) {
                                                    flag = false;
                                                    showmsg(getResources().getString(R.string.selectsomesuffixvalue));
                                                } else {
                                                    flag = true;
                                                    hashMap.addProperty(suffixname.getText().toString(), suffixvalue);
                                                }
                                            }
                                        }
                                        if (dobflag) {
                                            if (dob.getText().toString().isEmpty())
                                            {
                                                flag = false;
                                                dob.setError(getResources().getString(R.string.selectdob));
                                                dob.requestFocus();
                                            }
                                            else
                                            {
                                                dob.setError(null);
                                                flag = true;
                                                String[] parts = dob.getText().toString().split("/");
                                                String year = String.valueOf(Integer.parseInt(parts[2]));
                                                String month = String.valueOf(Integer.parseInt(parts[1]));
                                                String day = String.valueOf(Integer.parseInt(parts[0]));
                                                if (month.length() < 2) {
                                                    month = "0" + month;
                                                }
                                                if (day.length() < 2) {
                                                    day = "0" + day;
                                                }
                                                hashMap.addProperty(dob.getTag().toString(), month + "/" + day + "/" + year);
                                            }

                                        }
                                        if (taxvatflag) {
                                            /*if (flag) {
                                                hashMap.addProperty(taxvatname.getText().toString(), MageNative_taxvat.getText().toString());
                                            }*/
                                            if(MageNative_taxvat.getText().toString().isEmpty())
                                            {
                                                flag=false;
                                                MageNative_taxvat.setError(getResources().getString(R.string.empty));
                                                MageNative_taxvat.requestFocus();
                                            }
                                            else
                                            {
                                                MageNative_taxvat.setError(null);
                                                flag=true;
                                                hashMap.addProperty(MageNative_taxvat.getTag().toString(), MageNative_taxvat.getText().toString());
                                            }
                                        }

                                        hashMap.addProperty("firstname", firstname);
                                        hashMap.addProperty("lastname", lastname);
                                        hashMap.addProperty("email", email);
                                        hashMap.addProperty("password", password);
                                        if (male) {
                                            hashMap.addProperty("gender", "1");
                                            session.savegender("Male");
                                        } else {
                                            hashMap.addProperty("gender", "3");
                                        }
                                        if (female) {
                                            hashMap.addProperty("gender", "2");
                                        } else {
                                            hashMap.addProperty("gender", "3");
                                        }
                                        if (cedSessionManagement.getStoreId() != null) {
                                            hashMap.addProperty("store_id", cedSessionManagement.getStoreId());
                                        }
                                        /* if (management.getCartId() != null)
                    {
                        hashMap.put("cart_id", management.getCartId());
                    }*/
                                        if (newsletter.isChecked()) {
                                            hashMap.addProperty("is_subscribed", "1");
                                        } else {
                                            hashMap.addProperty("is_subscribed", "0");
                                        }
                                        if (flag) {

                                            registerViewModel.getRegisterData(Ced_Register.this, hashMap)
                                                    .observe(Ced_Register.this, Ced_Register.this::consumeRegisterResponse);

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        account.setOnClickListener(v -> {
            Intent Loginpage = new Intent(getApplicationContext(), Ced_Login.class);
            startActivity(Loginpage);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        });

        get_the_fields();
    }


    private void consumeRegisterResponse(ApiResponse apiResponse){
        switch (apiResponse.status)
        {
            case SUCCESS:
                register(apiResponse.data);
                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    private void get_the_fields() {
        registerViewModel.getFieldsData(Ced_Register.this).observe(Ced_Register.this, apiResponse -> {
            switch (apiResponse.status){
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(Objects.requireNonNull(apiResponse.data));
                        String success = object.getString("success");
                        if (success.equals("true"))
                        {
                            JSONArray data = object.getJSONArray("data");
                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject jsonObject = data.getJSONObject(i);
                                    if (jsonObject.has("prefix")) {
                                        if (jsonObject.getString("prefix").equals("true")) {
                                            prefixflag = true;
                                            prefixsection.setVisibility(View.VISIBLE);
                                            prefixlabel.setText(jsonObject.getString("label"));
                                            prefixname.setText(jsonObject.getString("name"));
                                            RadioGroup ll = new RadioGroup(Ced_Register.this);
                                            ll.setOrientation(LinearLayout.VERTICAL);
                                            if(jsonObject.get("prefix_options") instanceof JSONObject)
                                            {
                                                JSONObject prefix_options = jsonObject.getJSONObject("prefix_options");
                                                prefixoptions.setText(prefix_options.toString());
                                                if (Objects.requireNonNull(prefix_options.names()).length() > 0) {
                                                    for (int j = 0; j < Objects.requireNonNull(prefix_options.names()).length(); j++) {
                                                        final RadioButton rdbtn = new RadioButton(Ced_Register.this);
                                                        rdbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                                            if (isChecked) {
                                                                try {
                                                                    JSONObject object1 = new JSONObject(prefixoptions.getText().toString());
                                                                    prefixvalue = object1.getString(rdbtn.getText().toString());
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                        rdbtn.setText((CharSequence) Objects.requireNonNull(prefix_options.names()).get(j));
                                                        ll.addView(rdbtn);
                                                    }
                                                }
                                            }
                                            prefix.addView(ll);
                                        }
                                    }
                                    if (jsonObject.has("dob")) {
                                        if (jsonObject.getString("dob").equals("true"))
                                        {
                                            dobflag = true;
                                            dobsection.setVisibility(View.VISIBLE);
                                            dob.setHint(jsonObject.getString("label")+"*");
                                            dob.setTag(jsonObject.getString("name"));
                                        }
                                    }
                                    if (jsonObject.has("taxvat")) {
                                        if (jsonObject.getString("taxvat").equals("true")) {
                                            taxvatflag = true;
                                            taxvatsection.setVisibility(View.VISIBLE);
                                            MageNative_taxvat.setHint(jsonObject.getString("label")+"*");
                                            MageNative_taxvat.setTag(jsonObject.getString("name"));
                                        }
                                    }
                                    if (jsonObject.has("middlename")) {
                                        if (jsonObject.getString("middlename").equals("true")) {
                                            middlenamevatflag = true;
                                            middlenamesection.setVisibility(View.VISIBLE);
                                            MageNative_midllename.setHint(jsonObject.getString("label")+"*");
                                            MageNative_midllename.setTag(jsonObject.getString("name"));
                                        }
                                    }
                                    if (jsonObject.has("suffix")) {
                                        if (jsonObject.getString("suffix").equals("true")) {
                                            suffixflag = true;
                                            suffixsection.setVisibility(View.VISIBLE);
                                            suffixlabel.setText(jsonObject.getString("label"));
                                            suffixname.setText(jsonObject.getString("name"));
                                            RadioGroup ll = new RadioGroup(Ced_Register.this);
                                            ll.setOrientation(LinearLayout.VERTICAL);
                                            JSONObject suffixx_options = jsonObject.getJSONObject("suffix_options");
                                            suffixoptions.setText(suffixx_options.toString());
                                            if (Objects.requireNonNull(suffixx_options.names()).length() > 0) {
                                                for (int j = 0; j < Objects.requireNonNull(suffixx_options.names()).length(); j++) {
                                                    final RadioButton rdbtn = new RadioButton(Ced_Register.this);
                                                    rdbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                                        if (isChecked) {
                                                            try {
                                                                JSONObject object1 = new JSONObject(suffixoptions.getText().toString());
                                                                suffixvalue = object1.getString(rdbtn.getText().toString());
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                                    rdbtn.setText((CharSequence) Objects.requireNonNull(suffixx_options.names()).get(j));
                                                    ll.addView(rdbtn);
                                                }
                                            }
                                            suffix.addView(ll);
                                        }
                                    }
                                }
                            }
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

        /*//TODO
        Ced_ClientRequestResponseRest requestResponse = new Ced_ClientRequestResponseRest(output -> {

        }, Ced_Register.this);
        requestResponse.execute(requiredfieldurl);*/
    }

    private void register(String output) {
        try {
              JSONObject  jsonObj = new JSONObject(output);
                if (jsonObj.has("header") && jsonObj.getString("header").equals("false")) {
                    Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                } else {
                  JSONArray  response = jsonObj.getJSONObject(KEY_OBJECT).getJSONArray(KEY_CUSTOMER);
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject c = response.getJSONObject(i);
                        status = c.getString(KEY_STATUS);
                        message = c.getString(KEY_MESSAGE);
                        if (status.equals("success")) {
                            isConfirmationRequired = c.getString(KEY_IS_CONFIRMATION_REQUIRED);
                            if (isConfirmationRequired.equals("NO"))
                            {
                                JsonObject hashMap = new JsonObject();
                                hashMap.addProperty("email", email);
                                hashMap.addProperty("password", password);
                                if (cedSessionManagement.getCartId() != null) {
                                    hashMap.addProperty("cart_id", cedSessionManagement.getCartId());
                                }
                                try {
                                    registerViewModel.getLoginData(Ced_Register.this, hashMap)
                                            .observe(Ced_Register.this, this::consumeLoginResponse);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(main);
                                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                }
                            }
                            else {
                                showmsg(message);
                                Intent popActivity = new Intent(getApplicationContext(), Ced_Login.class);
                                popActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                popActivity.putExtra("isHavingdownloadable", true);
                                popActivity.putExtra("Checkout", "CheckoutModule");
                                startActivity(popActivity);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            }
                        }
                        else {
                            showmsg(message);
                        }
                    }
                }
        } catch (JSONException e) {
            e.printStackTrace();
            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private void consumeLoginResponse(ApiResponse apiResponse){
        switch (apiResponse.status){
            case SUCCESS:
                outputstring = apiResponse.data;
                checklogin();
                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    private void checklogin() {
        try {
              JSONObject  jsonObj = new JSONObject(outputstring);
                if (jsonObj.has("header") && jsonObj.getString("header").equals("false")) {
                    Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                } else {
                  JSONArray  response = jsonObj.getJSONObject(KEY_OBJECT).getJSONArray(KEY_CUSTOMER);
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject c = null;
                        c = response.getJSONObject(i);
                        status = c.getString(KEY_STATUS);
                        if (status.equals("success")) {
                            customer_id = c.getString(KEY_CUSTOMER_ID);
                            customergroup_id = c.getString(KEY_CUSTOMERGROUP_ID);
                            hash = c.getString(KEY_HASH);
                            session.savegender(c.getString("gender"));
                            session.savename(c.getString("name"));
                            cart_summary = String.valueOf(c.getInt(KEY_CART_SUMMARY));
                            Ced_MainActivity.latestcartcount = cart_summary;
                            showmsg(getResources().getString(R.string.succesfullLogin));
                            session.createLoginSession(hash, email);
                            session.saveCustomerId(customer_id);
                            session.set_customergroup_id(customergroup_id);
                            if (isfromcheckout) {
                                cedhandlecheckout();
                            } else {
                                Intent homeintent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                                homeintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(homeintent);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            }
                            setdevice_withusermail();
                        } else if (status.equals("exception")) {
                            message = c.getString(KEY_Message);
                            showmsg(message);
                        }
                    }
                }
        } catch (JSONException e) {
            e.printStackTrace();
            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    @Override
    public void onBackPressed() {
        invalidateOptionsMenu();
        super.onBackPressed();
    }
}
