package com.example.myfleetcall.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {

    @POST("addUserInfo/")
    Call<UserResponse> saveUser(@Body UserRequest userRequest);

    @POST("verifyPhoneNumber/")
    Call<ResponseVerifyMobile> verifyPhoneNumber(@Body RequestVerifyMobile requestVerifyMobile);

//    @GET("placeCallRequest/")
//    Call<List<ResponsePlaceCall>> placeCall();

    @POST("sendCallDetails/")
    Call<CallDetailsResponse> saveCallDetails(@Body CallDetailsRequest callDetailsRequest);

    @POST("requestRegistrationSMS/")
    Call<ResponseSMS> sendMobileNumber(@Body RequestSMS requestSMS);

    @POST("checkValidity/")
    Call<CheckValidityResponse> checkValidity(@Body CheckValidityRequest checkValidityRequest);

    @POST("initCall/")
    Call<InitCallResponse> initCallStatus(@Body InitCallRequest initCallRequest);

}
