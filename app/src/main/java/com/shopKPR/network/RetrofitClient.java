package com.shopKPR.network;

import com.shopKPR.configurations.HostAddress;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient mInstance;
    private final Retrofit retrofit;

    private RetrofitClient()
    {
        String username = "userOfShopKpr";
        String password = "ShopKprUser2021";

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(username, password))
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .connectTimeout(90, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(HostAddress.HOST_ADDRESS.getHostAddress())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static synchronized RetrofitClient getInstance()
    {
        if(mInstance == null)
        {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public Api getApi()
    {
        return retrofit.create(Api.class);
    }

    static class BasicAuthInterceptor implements Interceptor {

        private final String credentials;

        public BasicAuthInterceptor(String user, String password) {
            this.credentials = Credentials.basic(user, password);
        }

        @NotNull
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            Request authenticatedRequest = request.newBuilder()
                    .header("Authorization", credentials).build();

            return chain.proceed(authenticatedRequest);
        }

    }
}
