package com.owoshopkeeperpanel.Network;

import com.owoshopkeeperpanel.Response.OwoApiResponse;
import com.owoshopkeeperpanel.Response.SingleProductResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("getProductByCategories")
    Call<OwoApiResponse> getProducts(
            @Query("page") int page,
            @Query("product_category[]") String[] categories
    );

    @GET("getProductBySubCategory")
    Call<OwoApiResponse> getProductsBySubCategory(
            @Query("page") int page,
            @Query("product_sub_category") String categories
    );

    @GET("searchProduct")
    Call<OwoApiResponse> searchProduct(
            @Query("page") int page,
            @Query("product_name") String product_name,
            @Query("product_category[]") String[] categories
    );

    @GET("searchProductDesc")
    Call<OwoApiResponse> searchProductDesc(
            @Query("page") int page,
            @Query("product_name") String product_name,
            @Query("product_category[]") String[] categories
    );

    @GET("getProductById")
    Call<SingleProductResponse> getProductById(
            @Query("product_id") int product_id
    );

    @GET("getProductByCategory")
    Call<OwoApiResponse> getProductsByCategory(
            @Query("page") int page,
            @Query("product_category") String product_category
    );


}
