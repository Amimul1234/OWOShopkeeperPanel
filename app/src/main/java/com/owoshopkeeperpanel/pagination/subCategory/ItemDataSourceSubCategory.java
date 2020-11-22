package com.owoshopkeeperpanel.pagination.subCategory;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import com.owoshopkeeperpanel.Model.Owo_product;
import com.owoshopkeeperpanel.ApiAndClient.RetrofitClient;
import org.jetbrains.annotations.NotNull;
import java.util.List;
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
                .enqueue(new Callback<List<Owo_product>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Owo_product>> call, @NotNull Response<List<Owo_product>> response) {
                        if(response.code() == 200)
                        {
                            callback.onResult((List<Owo_product>) response.body(), null, FIRST_PAGE+1);
                        }
                        else
                        {
                            Log.e("Error", "Server error");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Owo_product>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Owo_product> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getProductsBySubCategory(params.key, category)
                .enqueue(new Callback<List<Owo_product>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Owo_product>> call, @NotNull Response<List<Owo_product>> response) {
                        if(response.code() == 200)
                        {
                            Integer key = (params.key > 0) ? params.key - 1 : null;
                            callback.onResult((List<Owo_product>) response.body(), key);
                        }
                        else
                        {
                            Log.e("Error", "Server error occurred");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Owo_product>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Owo_product> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getProductsBySubCategory(params.key, category)
                .enqueue(new Callback<List<Owo_product>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Owo_product>> call, @NotNull Response<List<Owo_product>> response) {
                        if(response.code() == 200)
                        {
                            if(params.key < 15)
                            {
                                Log.d("loadAfter", String.valueOf(params.key));
                                callback.onResult((List<Owo_product>) response.body(), params.key+1);
                            }
                            else
                            {
                                callback.onResult((List<Owo_product>) response.body(), null);
                            }
                        }
                        else
                        {
                            Log.e("Error", "Server error occurred");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Owo_product>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });
    }
}
