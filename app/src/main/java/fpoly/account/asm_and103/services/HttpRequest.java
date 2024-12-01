package fpoly.account.asm_and103.services;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {
    private static final String BASE_URL = "http://192.168.1.5:3000/";
    private ApiServices requestInterface;

    public HttpRequest() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Content-Type", "application/json") // Thêm header Content-Type
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                })
                .build();

        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client) // Sử dụng OkHttpClient đã cấu hình
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiServices.class);
    }

    public ApiServices callAPI() {
        return requestInterface;
    }
}
