package com.support.thequietservice;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by eka on 2017. 8. 12..
 */

public interface APIRequest {
    String BaseUrl = "http://soylatte.kr:3000";

    @FormUrlEncoded
    @POST("/place/update")
    Call<ResponseBody> Update(@Field("placeid") String placeid, @Field("decibel") int decibel);

}
