package com.shopKPR.homeComponents.productComponents;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import com.shopKPR.Model.OwoProduct;
import com.shopKPR.network.RetrofitClient;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDataSource extends PageKeyedDataSource<Integer, OwoProduct> {

    private static final int FIRST_PAGE = 0;
    private final Long[] categories;

    public ItemDataSource(Long[] categories) {
        this.categories = categories;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, OwoProduct> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getProducts(FIRST_PAGE, categories)
                .enqueue(new Callback<List<OwoProduct>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<OwoProduct>> call, @NotNull Response<List<OwoProduct>> response) {
                        if(response.code() == 200)
                        {
                            assert response.body() != null;
                            callback.onResult(response.body(), null, FIRST_PAGE+1);
                        }else
                        {
                            Log.e("Error", "Server error");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<OwoProduct>> call, @NotNull Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, OwoProduct> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getProducts(params.key, categories)
                .enqueue(new Callback<List<OwoProduct>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<OwoProduct>> call, @NotNull Response<List<OwoProduct>> response) {
                        if(response.code() == 200)
                        {
                            Integer key = (params.key > 0) ? params.key - 1 : null;
                            callback.onResult(response.body(), key);
                        }else
                        {
                            Log.e("Error", "Server error");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<OwoProduct>> call, @NotNull Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, OwoProduct> callback) {
        RetrofitClient.getInstance()
                .getApi()
                .getProducts(params.key, categories)
                .enqueue(new Callback<List<OwoProduct>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<OwoProduct>> call, @NotNull Response<List<OwoProduct>> response) {
                        if(response.code() == 200)
                        {
                            if(params.key < 12)
                            {
                                Log.d("loadAfter", String.valueOf(params.key));
                                callback.onResult(response.body(), params.key+1);
                            }
                            else
                            {
                                callback.onResult(response.body(), null);
                            }
                        }
                        else
                        {
                            Log.e("Error", "Server error");
                        }

                    }
                    @Override
                    public void onFailure(@NotNull Call<List<OwoProduct>> call, @NotNull Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });
    }
}
