package com.shopKPR.pagination.subCategory;

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

public class ItemDataSourceSubCategory extends PageKeyedDataSource<Integer, OwoProduct> {

    private static final int FIRST_PAGE = 0;
    private final Long subCategoryId;

    public ItemDataSourceSubCategory(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, OwoProduct> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getProductsBySubCategory(FIRST_PAGE, subCategoryId)
                .enqueue(new Callback<List<OwoProduct>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<OwoProduct>> call, @NotNull Response<List<OwoProduct>> response) {
                        if(response.isSuccessful())
                        {
                            assert response.body() != null;
                            callback.onResult(response.body(), null, FIRST_PAGE+1);
                        }
                        else
                        {
                            Log.e("Error", "Server error");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<OwoProduct>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, OwoProduct> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getProductsBySubCategory(params.key, subCategoryId)
                .enqueue(new Callback<List<OwoProduct>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<OwoProduct>> call, @NotNull Response<List<OwoProduct>> response) {
                        if(response.isSuccessful())
                        {
                            Integer key = (params.key > 0) ? params.key - 1 : null;

                            assert response.body() != null;
                            callback.onResult(response.body(), key);
                        }
                        else
                        {
                            Log.e("Error", "Server error occurred");
                        }
                    }
                    @Override
                    public void onFailure(@NotNull Call<List<OwoProduct>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, OwoProduct> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .getProductsBySubCategory(params.key, subCategoryId)
                .enqueue(new Callback<List<OwoProduct>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<OwoProduct>> call, @NotNull Response<List<OwoProduct>> response) {
                        if(response.code() == 200)
                        {
                            if(params.key < 15)
                            {
                                Log.d("loadAfter", String.valueOf(params.key));
                                assert response.body() != null;
                                callback.onResult(response.body(), params.key+1);
                            }
                            else
                            {
                                assert response.body() != null;
                                callback.onResult(response.body(), null);
                            }
                        }
                        else
                        {
                            Log.e("Error", "Server error occurred");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<OwoProduct>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });
    }
}
