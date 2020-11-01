package com.owoshopkeeperpanel.ApiAndClient;

import com.owoshopkeeperpanel.Model.Brands;
import com.owoshopkeeperpanel.Response.OwoApiResponse;
import com.owoshopkeeperpanel.Response.SingleProductResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("getProductByCategories")
    Call<OwoApiResponse> getProducts(
            @Query("page") int page,
            @Query("product_categories") String[] categories
    );

    @GET("getProductBySubCategory")
    Call<OwoApiResponse> getProductsBySubCategory(
            @Query("page") int page,
            @Query("product_sub_category") String categories
    );

    @GET("getBrandsViaCategory")
    Call<List<Brands>> getBrandsViaCategory(
            @Query("page") int page,
            @Query("product_categories") String[] categories
    );


    @GET("searchProduct")
    Call<OwoApiResponse> searchProduct(
            @Query("page") int page,
            @Query("product_categories") String[] product_categories,
            @Query("product_name") String product_name
    );

    @GET("searchProductDesc")
    Call<OwoApiResponse> searchProductDesc(
            @Query("page") int page,
            @Query("product_categories") String[] product_categories,
            @Query("product_name") String product_name
    );

    @GET("getProductById")
    Call<SingleProductResponse> getProductById(
            @Query("id") int product_id
    );

    @GET("getProductByCategory")
    Call<OwoApiResponse> getProductsByCategory(
            @Query("page") int page,
            @Query("product_category") String product_category
    );

    @GET("getProductByBrand")
    Call<OwoApiResponse> getProductViaBrand(
            @Query("page") int page,
            @Query("product_categories") String[] product_categories,
            @Query("product_brand") String product_brand
    );
}
