package net.webasap.nextbus;

import lombok.val;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class HttpClient {

    final private OkHttpClient client = new OkHttpClient();

    public String get(String url) {
        val request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            // TODO Handle exception
            e.printStackTrace();
        }
        // TODO Either optional or throw
        return null;
    }
}
