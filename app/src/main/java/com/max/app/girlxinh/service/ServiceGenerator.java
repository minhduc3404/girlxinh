package com.max.app.girlxinh.service;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpEngine;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by mac on 10/6/15.
 */
public class ServiceGenerator {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder();

    static Interceptor interceptor = new Interceptor() {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {

            Request request = chain.request();

            okhttp3.Response response = chain.proceed(request);

            RequestBody requestBody = request.body();
            boolean hasRequestBody = requestBody != null;
            Log.e("OkHttpClient", String.format("headers:\n %s", request.headers()));
            if (hasRequestBody) {
                Log.e("OkHttpClient", String.format("\nrequest:\n %s\nheaders:\n %s", request.body().toString(), request.headers()));
            }

            ResponseBody responseBody = response.body();
            long contentLength = responseBody.contentLength();
            String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
            Log.e("OkHttpClient", "<-- " + response.code() + ' ' + response.message() + ' '
                    + response.request().url() + " (" + 0 + "ms" + (true ? ", "
                    + bodySize + " body" : "") + ')');

            if (!HttpEngine.hasBody(response)) {
                Log.e("OkHttpClient", "<-- END HTTP");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    try {
                        charset = contentType.charset(UTF8);
                    } catch (UnsupportedCharsetException e) {
                        Log.e("OkHttpClient", "");
                        Log.e("OkHttpClient", "Couldn't decode the response body; charset is likely malformed.");
                        Log.e("OkHttpClient", "<-- END HTTP");

                        return response;
                    }
                }

                if (contentLength != 0) {
                    Log.e("OkHttpClient", "");
                    Log.e("OkHttpClient", buffer.clone().readString(charset));
                }

                Log.e("OkHttpClient", "<-- END HTTP (" + buffer.size() + "-byte body)");
            }

            return response;
        }
    };

    private ServiceGenerator() {
    }


    public static <S> S createService(Class<S> serviceClass, String baseUrl) {
        /*HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.interceptors().add(logging);*/

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(15, TimeUnit.SECONDS);
        //httpClient.interceptors().add(interceptor);

        retrofitBuilder.baseUrl(baseUrl);
        retrofitBuilder.client(httpClient.build());
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(serviceClass);
    }
}
