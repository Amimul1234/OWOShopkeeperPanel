package com.shopKPR.pagination.orders;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.Model.ShopKeeperOrders;
import com.shopKPR.prevalent.Prevalent;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderItemDataSource extends PageKeyedDataSource<Integer, ShopKeeperOrders> {

    private static final int FIRST_PAGE = 0;

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, ShopKeeperOrders> callback) {

        RetrofitClient.getInstance().getApi()
                .getShopKeeperOrders(FIRST_PAGE, Prevalent.currentOnlineUser.getMobileNumber())
                .enqueue(new Callback<List<ShopKeeperOrders>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<ShopKeeperOrders>> call, @NotNull Response<List<ShopKeeperOrders>> response) {
                        if(response.isSuccessful())
                        {
                            callback.onResult(response.body(), null, FIRST_PAGE+1);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<ShopKeeperOrders>> call, @NotNull Throwable t) {
                        Log.e("Error: ", t.getMessage());
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, ShopKeeperOrders> callback) {


        RetrofitClient.getInstance().getApi()
                .getShopKeeperOrders(params.key, Prevalent.currentOnlineUser.getMobileNumber())
                .enqueue(new Callback<List<ShopKeeperOrders>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<ShopKeeperOrders>> call, @NotNull Response<List<ShopKeeperOrders>> response) {
                        if(response.isSuccessful())
                        {
                            if(response.body() != null){
                                Integer key = (params.key > 0) ? params.key - 1 : null;
                                callback.onResult(response.body(), key);
                            }
                        }
                        else
                        {
                            Log.e("Error : ", String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<ShopKeeperOrders>> call, @NotNull Throwable t) {
                        Log.e("Error: ", t.getMessage());
                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, ShopKeeperOrders> callback) {

        RetrofitClient.getInstance().getApi()
                .getShopKeeperOrders(params.key, Prevalent.currentOnlineUser.getMobileNumber())
                .enqueue(new Callback<List<ShopKeeperOrders>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<ShopKeeperOrders>> call, @NotNull Response<List<ShopKeeperOrders>> response) {
                        if(response.isSuccessful()){
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
                            Log.e("Error: ", String.valueOf(response.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<ShopKeeperOrders>> call, @NotNull Throwable t) {
                        Log.e("Error: ", t.getMessage());
                    }
                });
    }
}
