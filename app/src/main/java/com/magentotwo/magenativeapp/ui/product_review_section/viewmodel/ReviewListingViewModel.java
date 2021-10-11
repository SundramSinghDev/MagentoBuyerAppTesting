package com.magentotwo.magenativeapp.ui.product_review_section.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.magentotwo.magenativeapp.repository.Repository;
import com.magentotwo.magenativeapp.rest.ApiCall;
import com.magentotwo.magenativeapp.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class ReviewListingViewModel extends ViewModel {
    Boolean showloader = true;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;
    private final JsonObject parameters;

    @Inject
    public ReviewListingViewModel(Repository repository) {
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> productreviewlisting(Context context, JsonObject postData) {
        MutableLiveData<ApiResponse> mutableData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.productreviewlisting(parameters);
        apiCall.postRequest(call, context, mutableData, showloader);
        return mutableData;
    }
}