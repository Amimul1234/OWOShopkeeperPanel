package com.owoShopKeeperPanel.pagination.orders;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import com.owoShopKeeperPanel.ApiAndClient.RetrofitClient;
import com.owoShopKeeperPanel.Model.Shop_keeper_orders;
import com.owoShopKeeperPanel.Prevalent.Prevalent;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderItemDataSource extends PageKeyedDataSource<Integer, Shop_keeper_orders> {

    private static final int FIRST_PAGE = 0;

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Shop_keeper_orders> callback) {

        RetrofitClient.getInstance().getApi()
                .getShopKeeperOrders(FIRST_PAGE, Prevalent.currentOnlineUser.getPhone())
                .enqueue(new Callback<List<Shop_keeper_orders>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Response<List<Shop_keeper_orders>> response) {
                        if(response.isSuccessful())
                        {
                            callback.onResult(response.body(), null, FIRST_PAGE+1);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Throwable t) {
                        Log.e("Error: ", t.getMessage());
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Shop_keeper_orders> callback) {


        RetrofitClient.getInstance().getApi()
                .getShopKeeperOrders(params.key, Prevalent.currentOnlineUser.getPhone())
                .enqueue(new Callback<List<Shop_keeper_orders>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Response<List<Shop_keeper_orders>> response) {
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
                    public void onFailure(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Throwable t) {
                        Log.e("Error: ", t.getMessage());
                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Shop_keeper_orders> callback) {

        RetrofitClient.getInstance().getApi()
                .getShopKeeperOrders(params.key, Prevalent.currentOnlineUser.getPhone())
                .enqueue(new Callback<List<Shop_keeper_orders>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Response<List<Shop_keeper_orders>> response) {
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
                    public void onFailure(@NotNull Call<List<Shop_keeper_orders>> call, @NotNull Throwable t) {
                        Log.e("Error: ", t.getMessage());
                    }
                });
    }
}
