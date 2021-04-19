package com.shopKPR.pagination.brands;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import com.shopKPR.network.Api;
import com.shopKPR.Model.OwoProduct;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.pagination.NetworkState;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDataSourceBrands extends PageKeyedDataSource<Integer, OwoProduct> {

    private static final int FIRST_PAGE = 0;

    private final Api restApiFactory;
    private final Long brandId;

    private final MutableLiveData<NetworkState> networkState;

    public ItemDataSourceBrands(Long brandId) {
        this.brandId = brandId;
        restApiFactory = RetrofitClient.getInstance().getApi();
        networkState = new MutableLiveData<>();
    }

    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, OwoProduct> callback) {

        networkState.postValue(NetworkState.LOADING);

        restApiFactory.getProductViaBrand(FIRST_PAGE, brandId)
                .enqueue(new Callback<List<OwoProduct>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<OwoProduct>> call, @NotNull Response<List<OwoProduct>> response) {

                        if(response.isSuccessful())
                        {
                            assert response.body() != null;
                            callback.onResult(response.body(), null, FIRST_PAGE+1);
                            networkState.postValue(NetworkState.LOADED);
                        }
                        else
                        {
                            Log.e("Error", "Failed on server");
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<OwoProduct>> call, @NotNull Throwable t) {
                        String errorMessage = t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, OwoProduct> callback) {

        networkState.postValue(NetworkState.LOADING);

        restApiFactory.getProductViaBrand(params.key, brandId)
                .enqueue(new Callback<List<OwoProduct>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<OwoProduct>> call, @NotNull Response<List<OwoProduct>> response) {
                        if(response.isSuccessful())
                        {
                            Integer key = (params.key > 0) ? params.key - 1 : null;
                            assert response.body() != null;
                            callback.onResult(response.body(), key);

                            networkState.postValue(NetworkState.LOADED);
                        }
                        else
                        {
                            Log.e("Error", "Error caught on server");
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }
                    @Override
                    public void onFailure(@NotNull Call<List<OwoProduct>> call, @NotNull Throwable t) {
                        String errorMessage = t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, OwoProduct> callback) {

        networkState.postValue(NetworkState.LOADING);

        restApiFactory.getProductViaBrand(params.key, brandId)
                .enqueue(new Callback<List<OwoProduct>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<OwoProduct>> call, @NotNull Response<List<OwoProduct>> response) {

                        if(response.isSuccessful())
                        {
                            if(params.key < 12)
                            {
                                Log.d("loadAfter", String.valueOf(params.key));
                                assert response.body() != null;
                                callback.onResult(response.body(), params.key+1);

                                networkState.postValue(NetworkState.LOADED);
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
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<OwoProduct>> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                        String errorMessage = t.getMessage();
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage));
                    }
                });
    }
}
