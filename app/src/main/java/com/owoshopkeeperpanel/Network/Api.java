package com.owoshopkeeperpanel.Network;


import com.owoshopkeeperpanel.Response.OwoApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Api {

    @GET("allProducts")
    Call<OwoApiResponse> getAnswers(
            @Query("page") int page
    );


    @GET("searchProduct")
    Call<OwoApiResponse> searchProduct(
            @Query("product_name") String product_name
    );

}
