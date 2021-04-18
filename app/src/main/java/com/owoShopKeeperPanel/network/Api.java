package com.owoShopKeeperPanel.network;

import com.owoShopKeeperPanel.homeComponents.brandsComponent.entity.Brands;
import com.owoShopKeeperPanel.Model.CartListFromClient;
import com.owoShopKeeperPanel.Model.CartListProduct;
import com.owoShopKeeperPanel.Model.OwoProduct;
import com.owoShopKeeperPanel.Model.Shop_keeper_orders;
import com.owoShopKeeperPanel.Model.Shops;
import com.owoShopKeeperPanel.Model.UserDebts;
import com.owoShopKeeperPanel.Model.UserShopKeeper;
import com.owoShopKeeperPanel.Model.User_debt_details;
import com.owoShopKeeperPanel.categorySpinner.entity.CategoryEntity;
import com.owoShopKeeperPanel.categorySpinner.entity.SubCategoryEntity;
import com.owoShopKeeperPanel.myShopManagement.userDebts.model.DebtDashBoardResponse;
import com.owoShopKeeperPanel.registerRequest.ShopPendingRequest;
import com.owoShopKeeperPanel.userRegistration.ShopKeeperUser;
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

    //Offer Banner
    @GET("/getBannerForSelectedCategories")
    Call<List<String>> bannerImages(@Query("categoryIds") List<Long> categoryIds);

    //Shop Registration
    @POST("/shopRegisterRequest")
    Call<ResponseBody> shopRegisterRequest(@Body ShopPendingRequest shopPendingRequest);

    @POST("/registerShopKeeper")
    Call<ResponseBody> registerShopKeeper(@Body UserShopKeeper userShopKeeper);

    @GET("/getShopKeeper")
    Call<ShopKeeperUser> getShopKeeper(@Query("mobileNumber") String mobileNumber);

    @PUT("/updateShopKeeperInfo")
    Call<ResponseBody> updateShopKeeperInfo(@Body ShopKeeperUser shopKeeperUser);

    @GET("/getProductByCategories")
    Call<List<OwoProduct>> getProducts(@Query("page") int page,
                                       @Query("product_categories") Long[] categories);

    @GET("/getProductBySpecificCategory")
    Call<List<OwoProduct>> getProductsViaSpecificCategory(@Query("page") int page,
                                                          @Query("productCategory") Long productCategory);

    @GET("/getProductBySubCategory")
    Call<List<OwoProduct>> getProductsBySubCategory(
            @Query("page") int page,
            @Query("product_sub_category") String categories
    );

    @GET("/getBrandsViaCategory")
    Call<List<Brands>> getBrandsViaCategory(
            @Query("number") int number, @Query("categoryIds") List<Long> categoryIds);

    @GET("/getAllCategories")
    Call<List<CategoryEntity>> getAllCategories();

    @GET("/getSpecificCategoryData")
    Call<List<CategoryEntity>> getSpecificCategoryData(@Query("categoryIds") List<Long> categoryIds);

    @GET("/getProductById")
    Call<OwoProduct> getProductById(@Query("productId") Long productId);

    @GET("/getBrandNameViaProductId")
    Call<ResponseBody> getBrandNameViaProductId(@Query("productId") Long productId);

    @GET("/getProductByCategory")
    Call<List<OwoProduct>> getProductsByCategory(
            @Query("page") int page,
            @Query("product_category") String product_category
    );

    @GET("/getProductByBrand")
    Call<List<OwoProduct>> getProductViaBrand(
            @Query("page") int page, @Query("brandsId") Long brandsId);

    @POST("/shopKeeperCart")
    Call<ResponseBody> cartListItems(@Body CartListFromClient cartList);

    @GET("/shop_keeper_cart_products")
    Call<List<CartListProduct>> getCartListProducts(
            @Query("mobile_number") String mobile_number
    );

    @PUT("/update_cart_list")
    Call<CartListProduct> updateCartList(
            @Body CartListProduct cartListProduct,
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

    @GET("/getDebtDashBoardEntries")
    Call<DebtDashBoardResponse> getDebtDashBoardEntries(@Query("mobileNumber") String mobileNumber);

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
    Call<List<User_debt_details>> getDebtListForAnSpecificUser(@Query("user_id") Long user_id);

    @DELETE("/clearAllDebtDetails")
    Call<ResponseBody> deleteAUserDebtList(@Query("user_id") Long user_id);

    @POST("/addAdebtDetails")
    Call<ResponseBody> addADebtDetailsForACustomer(@Body User_debt_details user_debt_details, @Query("user_id") Long user_id);

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
            @Part MultipartBody.Part multipartFile);

    @DELETE("/getImageFromServer")
    Call<ResponseBody> deleteImage(@Query("path_of_image") String path_of_image);


    @GET("/getAllSubCategories")
    Call<List<SubCategoryEntity>> getAllSubCategories(@Query("categoryId") Long categoryId);

    @POST("/addPaidAmountForAnUser")
    Call<ResponseBody> addPaidAmountForAnUser(@Query("userId") Long userId, @Query("paidAmount") Double userPaidAmount);


    //Searching and filtering
    @GET("/getAllSubCategoriesForCategory")
    Call<List<String>> getAllSubCategoriesForCategory(@Query("categoryId") Long categoryId);

    @GET("/searchProduct")
    Call<List<OwoProduct>> searchProduct(
            @Query("page") int page,
            @Query("subCategories") List<String> subCategories,
            @Query("product_name") String product_name
    );

    @GET("/searchProductDesc")
    Call<List<OwoProduct>> searchProductDesc(
            @Query("page") int page,
            @Query("subCategories") List<String> subCategories,
            @Query("product_name") String product_name
    );

    @GET("/sortProductAlphabetically")
    Call<List<OwoProduct>> sortProductAlphabetically(
            @Query("page") int page,
            @Query("subCategories") List<String> subCategories,
            @Query("alphabet") String alphabet
    );

    //Get similar products based on sub category
    @GET("/getSimilarProducts")
    Call<List<OwoProduct>> getSimilarProducts(@Query("product_sub_category_id") Long product_sub_category_id);

}
