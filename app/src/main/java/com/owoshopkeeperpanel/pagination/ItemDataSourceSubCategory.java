package com.owoshopkeeperpanel.pagination;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.owoshopkeeperpanel.Model.Owo_product;
import com.owoshopkeeperpanel.Network.RetrofitClient;
import com.owoshopkeeperpanel.Response.OwoApiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDataSourceSubCategory extends PageKeyedDataSource<Integer, Owo_product> {

    private static final int FIRST_PAGE = 0;
    private String category;

    public ItemDataSourceSubCategory(String category) {
        this.category = category;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Owo_product> callback) {

        RetrofitClient.getInstance()//Calling the getProductApi
                .getApi()
                .getProductsBySubCategory(FIRST_PAGE, category)
                .enqueue(new Callback<OwoApiResponse>() {
                    @Override
                    public void onResponse(Call<OwoApiResponse> call, Response<OwoApiResponse> response) {

                        if(response.body() != null){

                            callback.onResult(response.body().products, null, FIRST_PAGE+1);//(First page +1) is representing next page
                        }
                    }

                    @Override
                    public void onFailure(Call<OwoApiResponse> call, Throwable t) {

                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Owo_product> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getProductsBySubCategory(params.key, category)
                .enqueue(new Callback<OwoApiResponse>() {
                    @Override
                    public void onResponse(Call<OwoApiResponse> call, Response<OwoApiResponse> response) {

                        if(response.body() != null){
                            Integer key = (params.key > 0) ? params.key - 1 : null;
                            callback.onResult(response.body().products, key);
                        }
                    }

                    @Override
                    public void onFailure(Call<OwoApiResponse> call, Throwable t) {

                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Owo_product> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getProductsBySubCategory(params.key, category)
                .enqueue(new Callback<OwoApiResponse>() {
                    @Override
                    public void onResponse(Call<OwoApiResponse> call, Response<OwoApiResponse> response) {

                        if(response.body() != null){
                            if(params.key < 15)
                            {
                                Log.d("loadAfter", String.valueOf(params.key));
                                callback.onResult(response.body().products, params.key+1);
                            }
                            else
                            {
                                callback.onResult(response.body().products, null);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<OwoApiResponse> call, Throwable t) {

                    }
                });


    }
}
