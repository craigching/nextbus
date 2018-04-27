package net.webasap.nextbus.core.services.impl;

import lombok.val;
import net.webasap.nextbus.core.services.HttpClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * An implemenation of the simple HttpClient interface based on Square's OkHttp
 * project
 */
public class OkHttpClientImpl implements HttpClient {

    final private OkHttpClient client = new OkHttpClient();

    @Override
    public String get(String url) throws  IOException {
        val request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
