package com.magentotwo.magenativeapp.ui.checkoutsection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.magentotwo.magenativeapp.repository.Repository;
import com.magentotwo.magenativeapp.rest.ApiCall;
import com.magentotwo.magenativeapp.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class CheckoutViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public CheckoutViewModel(Repository repository){
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> saveBillingAddress(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableSaveAddressData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.saveBillingShipping(parameters);
        apiCall.postRequest(call, context, mutableSaveAddressData,showloader);
        return mutableSaveAddressData;
    }


    public MutableLiveData<ApiResponse> getShippingMethods(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableShippingPaymentData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getShippingMethods(parameters);
        apiCall.postRequest(call, context, mutableShippingPaymentData,showloader);
        return mutableShippingPaymentData;
    }
    public MutableLiveData<ApiResponse> getPaymentMethods(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableShippingPaymentData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getPaymentMethods(parameters);
        apiCall.postRequest(call, context, mutableShippingPaymentData,showloader);
        return mutableShippingPaymentData;
    }
    public MutableLiveData<ApiResponse> saveShippingMethods(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableSaveMethodsData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.saveShippingMethods(parameters);
        apiCall.postRequest(call, context, mutableSaveMethodsData,showloader);
        return mutableSaveMethodsData;
    }
    public MutableLiveData<ApiResponse> savePaymentMethods(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableSaveMethodsData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.savePaymentMethods(parameters);
        apiCall.postRequest(call, context, mutableSaveMethodsData,showloader);
        return mutableSaveMethodsData;
    }
    public MutableLiveData<ApiResponse> prepareShippingMethod(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableSaveMethodsData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.prepareShippingMethod(parameters);
        apiCall.postRequest(call, context, mutableSaveMethodsData,showloader);
        return mutableSaveMethodsData;
    }



    public MutableLiveData<ApiResponse> saveOrder(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableSaveOrderResponse = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.saveOrder(parameters);
        apiCall.postRequest(call, context, mutableSaveOrderResponse,showloader);
        return mutableSaveOrderResponse;
    }

    public MutableLiveData<ApiResponse> getAdditionalInfo(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableAdditionalInfoResponse = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.additionalInfo(parameters);
        apiCall.postRequest(call, context, mutableAdditionalInfoResponse,showloader);
        return mutableAdditionalInfoResponse;
    }
}
