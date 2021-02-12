package com.owoShopKeeperPanel.pagination.brands;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import com.owoShopKeeperPanel.ApiAndClient.Api;
import com.owoShopKeeperPanel.Model.Owo_product;
import com.owoShopKeeperPanel.ApiAndClient.RetrofitClient;
import com.owoShopKeeperPanel.pagination.NetworkState;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDataSourceBrands extends PageKeyedDataSource<Integer, Owo_product> {

    private static final int FIRST_PAGE = 0;

    private final String[] categories;
    private final String brand_name;
    private final Api restApiFactory;

    private final MutableLiveData<NetworkState> networkState;

    public ItemDataSourceBrands(String[] categories, String brand_name) {
        this.categories = categories;
        this.brand_name = brand_name;
        restApiFactory = RetrofitClient.getInstance().getApi();
        networkState = new MutableLiveData<>();
    }

    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Owo_product> callback) {

        networkState.postValue(NetworkState.LOADING);

        restApiFactory.getProductViaBrand(FIRST_PAGE, categories, brand_name)
                .enqueue(new Callback<List<Owo_product>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Owo_product>> call, @NotNull Response<List<Owo_product>> response) {

                        if(response.isSuccessful())
                        {
                            callback.onResult((List<Owo_product>) response.body(), null, FIRST_PAGE+1);
                            networkState.postValue(NetworkState.LOADED);
                        }
                        else
                        {
                            Log.e("Error", "Failed on server");
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Owo_product>> call, @NotNull Throwable t) {
                        String errorMessage = t == null ? "unknown error" : t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Owo_product> callback) {

        networkState.postValue(NetworkState.LOADING);

        restApiFactory.getProductViaBrand(params.key, categories, brand_name)
                .enqueue(new Callback<List<Owo_product>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Owo_product>> call, @NotNull Response<List<Owo_product>> response) {
                        if(response.isSuccessful())
                        {
                            Integer key = (params.key > 0) ? params.key - 1 : null;
                            callback.onResult((List<Owo_product>) response.body(), key);

                            networkState.postValue(NetworkState.LOADED);
                        }
                        else
                        {
                            Log.e("Error", "Error caught on server");
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }
                    @Override
                    public void onFailure(@NotNull Call<List<Owo_product>> call, @NotNull Throwable t) {
                        String errorMessage = t == null ? "unknown error" : t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Owo_product> callback) {

        networkState.postValue(NetworkState.LOADING);

        restApiFactory.getProductViaBrand(params.key, categories, brand_name)
                .enqueue(new Callback<List<Owo_product>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Owo_product>> call, @NotNull Response<List<Owo_product>> response) {

                        if(response.isSuccessful())
                        {
                            if(params.key < 12)
                            {
                                Log.d("loadAfter", String.valueOf(params.key));
                                callback.onResult((List<Owo_product>) response.body(), params.key+1);

                                networkState.postValue(NetworkState.LOADED);
                            }
                            else
                            {
                                callback.onResult((List<Owo_product>) response.body(), null);
                            }
                        }
                        else
                        {
                            Log.e("Error", "Server error occurred");
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Owo_product>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                        String errorMessage = t == null ? "unknown error" : t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });
    }
}
