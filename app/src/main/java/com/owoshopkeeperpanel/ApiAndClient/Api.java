package com.owoshopkeeperpanel.ApiAndClient;

import com.owoshopkeeperpanel.Model.Brands;
import com.owoshopkeeperpanel.Model.CartListFromClient;
import com.owoshopkeeperpanel.Model.Cart_list_product;
import com.owoshopkeeperpanel.Model.Owo_product;
import com.owoshopkeeperpanel.Model.Shop_keeper_orders;
import com.owoshopkeeperpanel.Model.Shops;
import com.owoshopkeeperpanel.Model.UserDebts;
import com.owoshopkeeperpanel.Model.User_debt_details;
import com.owoshopkeeperpanel.Response.OwoApiResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    Call<Owo_product> getProductById(
            @Query("id") long product_id
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

    @POST("shop_keeper_cart")
    Call<ResponseBody> cartListItems(
            @Body CartListFromClient cartList
    );

    @GET("shop_keeper_cart_products")
    Call<List<Cart_list_product>> getCartListProducts(
            @Query("mobile_number") String mobile_number
    );

    @PUT("update_cart_list")
    Call<Cart_list_product> updateCartList(
            @Body Cart_list_product cart_list_product,
            @Query("mobile_number") String mobile_number
    );

    @DELETE("delete_cart_product")
    Call<ResponseBody> delete_product_from_cart(
            @Query("product_id") long product_id,
            @Query("mobile_number") String mobile_number
    );

    @POST("shop_keeper_order")
    Call<ResponseBody> createOrder(
            @Body Shop_keeper_orders shop_keeper_orders
    );

    @GET("get_shop_keeper_order")
    Call<List<Shop_keeper_orders>> getShopKeeperOrders(
            @Query("page") int page,
            @Query("mobile_number") String mobile_number
    );

    @GET("getShopInfo")
    Call<Shops> getShopInfo(
            @Query("shop_phone") String shop_phone
    );

    @POST("/addUserDebt")
    Call<ResponseBody> addAUserDebt(
            @Body UserDebts userDebts,
            @Query("shop_mobile_number") String mobile_number
    );

    @GET("/getUserDebtLists")
    Call<List<UserDebts>> getUserDebtLists(
            @Query("page") int page,
            @Query("shop_mobile_number") String shop_mobile_number
    );

    @GET("/getAllDebtDetails")
    Call<List<User_debt_details>> getUserDebtDetails(
            @Query("user_id") Long user_id
    );
}
