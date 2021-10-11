package com.magentotwo.magenativeapp.repository;

import com.google.gson.JsonObject;
import com.magentotwo.magenativeapp.rest.ApiInterface;
import com.magentotwo.magenativeapp.utils.Urls;

import org.json.JSONObject;

import javax.inject.Inject;

import retrofit2.Call;

public class Repository {
    private ApiInterface apiInterface;
    /*private String header = Urls.HEADER;*/
    private String contentType = "application/json";

    @Inject
    public Repository(ApiInterface apiInterface) {
        this.apiInterface = apiInterface;
    }

    public Call<Object> checkValidity(String url) {
        return apiInterface.checkValidity(url);
    }

    public Call<Object> getModuleList(JsonObject postData) {
        return apiInterface.getModuleList(postData);
    }

    public Call<Object> getCartCount(JsonObject postData) {
        return apiInterface.getCartCount(/*header, contentType, */postData);
    }

    public Call<Object> getAllCategories(JsonObject postData) {
        return apiInterface.getAllCategories(/*header, contentType, */postData);
    }

    public Call<Object> getLoginData(JsonObject postData) {
        return apiInterface.getLoginData(postData);
    }

    public Call<Object> removefrom_Compare(JsonObject postData, String hashkey) {
        return apiInterface.removefrom_Compare(hashkey, /*contentType,*/ postData);
    }

    public Call<Object> getSocialLoginData(JsonObject postData) {
        return apiInterface.getSocialLoginData(postData);
    }

    public Call<Object> getForgotPassData(JsonObject postData/*,String hashkey*/) {
        return apiInterface.getForgotPassData(/*hashkey,*/postData);
    }

    public Call<Object> getRegisterData(JsonObject postData) {
        return apiInterface.getRegisterData(postData);
    }

    public Call<Object> getRegisterFields() {
        return apiInterface.getRegisterFields(/*header, contentType*/);
    }

    public Call<Object> getProfileUpdate(JsonObject postData, String hashkey) {
        return apiInterface.getProfileUpdate(hashkey,/* contentType,*/ postData);
    }

    public Call<Object> getBanners(String store_id) {
        return apiInterface.getBanners(store_id);
    }

    public Call<Object> getnewthemehomepage(JsonObject postData) {
        return apiInterface.getnewthemehomepage(/*header,contentType,*/postData);
    }

    public Call<Object> getcomparelist(JsonObject postData, String hashkey) {
        return apiInterface.getcomparelist(hashkey,/*contentType,*/postData);
    }

    public Call<Object> getDeals(String store_id) {
        return apiInterface.getDeals(store_id);
    }

    public Call<Object> getFeatured(String page, String store_id) {
        return apiInterface.getFeatured(page, store_id);
    }

    public Call<Object> getStates(String countryid) {
        return apiInterface.getState(countryid);
    }

    public Call<Object> getcountry() {
        return apiInterface.getcountry();
    }

    public Call<Object> getproductratingoption() {
        return apiInterface.getproductratingoption();
    }

    public Call<Object> getratingoption() {
        return apiInterface.getratingoption();
    }

    public Call<Object> getCategories(JsonObject postData) {
        return apiInterface.getCategories(postData);
    }

    /*public Call<Object> getWebCheckout(String url){
        return apiInterface.getDataFromUrl(url);
    }*/

    public Call<Object> addToCart(JsonObject postData) {
        return apiInterface.addToCart(/*header, contentType, */postData);
    }

    public Call<Object> addproductreview(JsonObject postData) {
        return apiInterface.addproductreview(/*header, contentType, */postData);
    }

    public Call<Object> productreviewlisting(JsonObject postData) {
        return apiInterface.productreviewlisting(postData);
    }

    public Call<Object> addToCompare(JsonObject postData, String hashkey) {
        return apiInterface.addToCompare(hashkey, /*contentType,*/ postData);
    }

    public Call<Object> viewimage(JsonObject postData) {
        return apiInterface.viewimage(postData);
    }

    public Call<Object> getViewCart(JsonObject postData) {
        return apiInterface.getViewCart(postData);
    }

    public Call<Object> registerdevicetoserver(JsonObject postData) {
        return apiInterface.registerdevicetoserver(postData);
    }

    public Call<Object> emptyCart(JsonObject postData) {
        return apiInterface.emptyCart(postData);
    }

    public Call<Object> deleteFromCart(JsonObject postData) {
        return apiInterface.deleteFromCart(postData);
    }

    public Call<Object> applycoupon(JsonObject postData) {
        return apiInterface.applycoupon(postData);
    }

    public Call<Object> updateCart(JsonObject postData) {
        return apiInterface.updateCart(postData);
    }

    public Call<Object> saveAddress(JsonObject postData, String hashkey) {
        return apiInterface.saveAddress(hashkey, postData);
    }

    public Call<Object> deleteAddress(JsonObject postData, String hashkey) {
        return apiInterface.deleteAddress(hashkey, postData);
    }

    public Call<Object> getAddressList(JsonObject postData, String hashkey) {
        return apiInterface.getAddressList(hashkey, postData);
    }

    public Call<Object> getWishList(JsonObject postData, String hashkey) {
        return apiInterface.getWishList(hashkey, postData);
    }

    public Call<Object> clearWishList(JsonObject postData, String hashkey) {
        return apiInterface.clearWishList(hashkey, postData);
    }

    public Call<Object> getnotificationlist(JsonObject postData, String hashkey) {
        return apiInterface.getnotificationlist(hashkey, postData);
    }

    public Call<Object> removeWishList(JsonObject postData, String hashkey) {
        return apiInterface.removeWishList(hashkey, postData);
    }

    public Call<Object> addToWishList(JsonObject postData, String hashkey) {
        return apiInterface.addToWishList(hashkey, postData);
    }

    public Call<Object> getOrders(JsonObject postData, String hashkey) {
        return apiInterface.getOrders(hashkey, postData);
    }

    public Call<Object> getOrderView(JsonObject postData, String hashkey) {
        return apiInterface.getOrderView(hashkey, postData);
    }

    public Call<Object> getReorder(JsonObject postData, String hashkey) {
        return apiInterface.getReorder(hashkey, postData);
    }

    public Call<Object> getAutoCompleteData(JsonObject postData) {
        return apiInterface.getAutoComplete(postData);
    }

    public Call<Object> getSearchList(JsonObject postData) {
        return apiInterface.getSearchList(postData);
    }

    public Call<Object> getDownloadProducts(JsonObject postData, String hashkey) {
        return apiInterface.getDownloadProducts(hashkey, postData);
    }

    public Call<Object> getProductView(JsonObject postData) {
        return apiInterface.getProductView(postData);
    }

    public Call<Object> getProductList(JsonObject postData) {
        return apiInterface.getProductList(postData);
    }

    public Call<Object> getSellerList(JsonObject postData) {
        return apiInterface.getsellersList(postData);
    }

    public Call<Object> getVendorReview(JsonObject postData) {
        return apiInterface.getVendorReview(postData);
    }

    public Call<Object> submitvendorreviewdata(JsonObject postData) {
        return apiInterface.submitvendorreviewdata(postData);
    }

    public Call<Object> getSellerShops(JsonObject postData) {
        return apiInterface.getsellersShops(postData);
    }

    public Call<Object> saveBillingShipping(JsonObject postData) {
        return apiInterface.saveBillingShipping(postData);
    }

    //-------------shipping payemnt ----------------//
    public Call<Object> getShippingMethods(JsonObject postData) {
        return apiInterface.getShippingMethods(postData);
    }

    public Call<Object> saveShippingMethods(JsonObject postData) {
        return apiInterface.saveShippingMethods(postData);
    }

    public Call<Object> prepareShippingMethod(JsonObject postData) {
        return apiInterface.prepareShippingMethod(postData);
    }

    public Call<Object> getPaymentMethods(JsonObject postData) {
        return apiInterface.getPaymentMethods(postData);
    }

    public Call<Object> savePaymentMethods(JsonObject postData) {
        return apiInterface.savePaymentMethods(postData);
    }
//-----------------shipping payemnt ----------------//

    public Call<Object> saveOrder(JsonObject postData) {
        return apiInterface.saveOrder(postData);
    }

    public Call<Object> additionalInfo(JsonObject postData) {
        return apiInterface.additionalInfo(postData);
    }

    public Call<Object> getDataFromUrl(String url) {
        return apiInterface.getDataFromUrl(url);
    }

    public Call<Object> setstore(String store_id) {
        return apiInterface.setstore(store_id);
    }

    public Call<Object> getrequiredfields() {
        return apiInterface.getrequiredfields();
    }
}
