package com.owoShopKeeperPanel.ApiAndClient;

import com.owoShopKeeperPanel.Model.Brands;
import com.owoShopKeeperPanel.Model.CartListFromClient;
import com.owoShopKeeperPanel.Model.Cart_list_product;
import com.owoShopKeeperPanel.Model.Owo_product;
import com.owoShopKeeperPanel.Model.Shop_keeper_orders;
import com.owoShopKeeperPanel.Model.Shops;
import com.owoShopKeeperPanel.Model.UserDebts;
import com.owoShopKeeperPanel.Model.User_debt_details;
import com.owoShopKeeperPanel.registerRequest.ShopPendingRequest;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @POST("/shopRegisterRequest")
    Call<ResponseBody> shopRegisterRequest(
            @Body ShopPendingRequest shopPendingRequest
            );

    @GET("/getProductByCategories")
    Call<List<Owo_product>> getProducts(
            @Query("page") int page,
            @Query("product_categories") String[] categories
    );

    @GET("/getProductBySubCategory")
    Call<List<Owo_product>> getProductsBySubCategory(
            @Query("page") int page,
            @Query("product_sub_category") String categories
    );

    @GET("/getBrandsViaCategory")
    Call<List<Brands>> getBrandsViaCategory(
            @Query("page") int page,
            @Query("product_categories") String[] categories
    );


    @GET("/searchProduct")
    Call<List<Owo_product>> searchProduct(
            @Query("page") int page,
            @Query("product_categories") String[] product_categories,
            @Query("product_name") String product_name
    );

    @GET("/searchProductDesc")
    Call<List<Owo_product>> searchProductDesc(
            @Query("page") int page,
            @Query("product_categories") String[] product_categories,
            @Query("product_name") String product_name
    );

    @GET("/getProductById")
    Call<Owo_product> getProductById(
            @Query("id") long product_id
    );

    @GET("/getProductByCategory")
    Call<List<Owo_product>> getProductsByCategory(
            @Query("page") int page,
            @Query("product_category") String product_category
    );

    @GET("/getProductByBrand")
    Call<List<Owo_product>> getProductViaBrand(
            @Query("page") int page,
            @Query("product_categories") String[] product_categories,
            @Query("product_brand") String product_brand
    );

    @POST("/shop_keeper_cart")
    Call<ResponseBody> cartListItems(
            @Body CartListFromClient cartList
    );

    @GET("/shop_keeper_cart_products")
    Call<List<Cart_list_product>> getCartListProducts(
            @Query("mobile_number") String mobile_number
    );

    @PUT("/update_cart_list")
    Call<Cart_list_product> updateCartList(
            @Body Cart_list_product cart_list_product,
            @Query("mobile_number") String mobile_number
    );

    @DELETE("/delete_cart_product")
    Call<ResponseBody> delete_product_from_cart(
            @Query("product_id") long product_id,
            @Query("mobile_number") String mobile_number
    );

    @POST("/shop_keeper_order")
    Call<ResponseBody> createOrder(
            @Body Shop_keeper_orders shop_keeper_orders,
            @Query("mobile_number") String mobile_number
    );

    @GET("/get_shop_keeper_order")
    Call<List<Shop_keeper_orders>> getShopKeeperOrders(
            @Query("page") int page,
            @Query("mobile_number") String mobile_number
    );

    @GET("/getShopInfo")
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

    @GET("/getUserSpecificDebtDetails")
    Call<UserDebts> getUserDebtDetails(
            @Query("user_id") Long user_id
    );

    @GET("/getADebtListForAUser")
    Call<List<User_debt_details>> getDebtListForAnSpecificUser(
            @Query("user_id") Long user_id
    );

    @DELETE("/clearAllDebtDetails")
    Call<ResponseBody> deleteAUserDebtList(
            @Query("user_id") Long user_id
    );

    @POST("/addAdebtDetails")
    Call<ResponseBody> addADebtDetailsForACustomer(
            @Body User_debt_details user_debt_details,
            @Query("user_id") Long user_id
    );

    @DELETE("/deleteAdebtDetails")
    Call<ResponseBody> deleteADebtDetails(
            @Query("id_of_debt_details") long id_of_debt_details,
            @Query("user_id") long user_id
    );

    @PUT("/updateAdebtDetails")
    Call<ResponseBody> updateADebtDetails(
            @Body User_debt_details user_debt_details,
            @Query("user_id") long user_id
    );

    @Multipart
    @POST("/imageController/{directory}")
    Call<ResponseBody> uploadImageToServer(
            @Path("directory") String directory,
            @Part MultipartBody.Part multipartFile
    );
}