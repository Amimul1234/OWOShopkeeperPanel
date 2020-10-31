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

public class ItemDataSource extends PageKeyedDataSource<Integer, Owo_product> {

    private static final int FIRST_PAGE = 0;
    private String[] categories;

    public ItemDataSource(String[] categories) {
        this.categories = categories;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Owo_product> callback) {

        RetrofitClient.getInstance()//Calling the getProductApi
                .getApi()
                .getProducts(FIRST_PAGE, categories)
                .enqueue(new Callback<OwoApiResponse>() {
                    @Override
                    public void onResponse(Call<OwoApiResponse> call, Response<OwoApiResponse> response) {
                        if(response.isSuccessful()){
                            callback.onResult(response.body().products, null, FIRST_PAGE+1);//(First page +1) is representing next page
                        }
                    }

                    @Override
                    public void onFailure(Call<OwoApiResponse> call, Throwable t) {
                        Log.d("Error msg", t.getMessage());
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Owo_product> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getProducts(params.key, categories)
                .enqueue(new Callback<OwoApiResponse>() {
                    @Override
                    public void onResponse(Call<OwoApiResponse> call, Response<OwoApiResponse> response) {
                        if(response.isSuccessful()){
                            Integer key = (params.key > 0) ? params.key - 1 : null;
                            callback.onResult(response.body().products, key);
                        }
                    }

                    @Override
                    public void onFailure(Call<OwoApiResponse> call, Throwable t) {
                        Log.d("Error msg", t.getMessage());
                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Owo_product> callback) {
        RetrofitClient.getInstance()
                .getApi()
                .getProducts(params.key, categories)
                .enqueue(new Callback<OwoApiResponse>() {
                    @Override
                    public void onResponse(Call<OwoApiResponse> call, Response<OwoApiResponse> response) {
                        if(response.isSuccessful()){
                            if(params.key < 12)
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
                        Log.d("Error msg", t.getMessage());
                    }
                });
    }
}
