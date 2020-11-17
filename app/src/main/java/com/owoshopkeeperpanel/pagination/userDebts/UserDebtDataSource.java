package com.owoshopkeeperpanel.pagination.userDebts;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;
import com.owoshopkeeperpanel.ApiAndClient.RetrofitClient;
import com.owoshopkeeperpanel.Model.UserDebts;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDebtDataSource extends PageKeyedDataSource<Integer, UserDebts> {

    private static final int FIRST_PAGE = 0;

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, UserDebts> callback) {

        RetrofitClient.getInstance().getApi()
                .getUserDebtLists(FIRST_PAGE, Prevalent.currentOnlineUser.getPhone())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.code() == 200)
                        {
                            callback.onResult((List<UserDebts>) response.body(), null, FIRST_PAGE+1);
                        }
                        else if(response.code() == 204)
                        {
                            Log.d("Message", "No more content");
                        }
                        else
                        {
                            Log.d("Message", "No shop found");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Log.e("Error", Objects.requireNonNull(t.getMessage()));
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, UserDebts> callback) {

        RetrofitClient.getInstance().getApi()
                .getUserDebtLists(params.key, Prevalent.currentOnlineUser.getPhone())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.code() == 200)
                        {
                            Integer key = (params.key > 0) ? params.key - 1 : null;
                            callback.onResult((List<UserDebts>) response.body(), key);
                        }
                        else if(response.code() == 204)
                        {
                            Log.d("Message", "No more content");
                        }
                        else
                        {
                            Log.d("Message", "No shop found");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Log.e("Error", Objects.requireNonNull(t.getMessage()));
                    }
                });

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, UserDebts> callback) {
        RetrofitClient.getInstance().getApi()
                .getUserDebtLists(params.key, Prevalent.currentOnlineUser.getPhone())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                        if(response.code() == 200)
                        {
                            callback.onResult((List<UserDebts>) response.body(), params.key+1);
                        }
                        else if(response.code() == 204)
                        {
                            Log.d("Message", "No more content");
                        }
                        else
                        {
                            Log.d("Message", "No shop found");
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Log.e("Error", Objects.requireNonNull(t.getMessage()));
                    }
                });
    }
}
