package com.shopKPR.shopKeeperPanel.searchAscending;

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

public class ItemDataSourceForSearch extends PageKeyedDataSource<Integer, OwoProduct> {

    private static final int FIRST_PAGE = 0;
    private List<String> subCategories;
    private String searchedProduct;

    public ItemDataSourceForSearch(List<String> subCategories, String searchedProduct) {
        this.subCategories = subCategories;
        this.searchedProduct = searchedProduct;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, OwoProduct> callback) {

        RetrofitClient.getInstance()//Calling the getProductApi
                .getApi()
                .searchProduct(FIRST_PAGE, subCategories, searchedProduct)
                .enqueue(new Callback<List<OwoProduct>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<OwoProduct>> call, @NotNull Response<List<OwoProduct>> response) {

                        if(response.code() == 200)
                        {
                            callback.onResult((List<OwoProduct>) response.body(), null, FIRST_PAGE+1);
                        }
                        else
                        {
                            Log.e("Error", "Error on server");
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
                .searchProduct(params.key, subCategories, searchedProduct)
                .enqueue(new Callback<List<OwoProduct>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<OwoProduct>> call, @NotNull Response<List<OwoProduct>> response) {
                        if(response.code() == 200)
                        {
                            Integer key = (params.key > 0) ? params.key - 1 : null;
                            callback.onResult((List<OwoProduct>) response.body(), key);
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
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, OwoProduct> callback) {

        RetrofitClient.getInstance()
                .getApi()
                .searchProduct(params.key, subCategories, searchedProduct)
                .enqueue(new Callback<List<OwoProduct>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<OwoProduct>> call, @NotNull Response<List<OwoProduct>> response) {
                        if(response.code() == 200)
                        {
                            if(params.key < 12)
                            {
                                callback.onResult(response.body(), params.key+1);
                            }
                            else
                            {
                                callback.onResult(response.body(), null);
                            }
                        }
                        else
                        {
                            Log.e("Error", "Error on server");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<OwoProduct>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });
    }
}
