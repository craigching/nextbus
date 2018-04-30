package net.webasap.nextbus.core.services.impl;

import lombok.val;
import net.webasap.nextbus.core.services.HttpClient;
import net.webasap.nextbus.core.services.HttpException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * An implemenation of the simple HttpClient interface based on Square's OkHttp
 * project
 */
public class OkHttpClientImpl implements HttpClient {

    final static private Logger LOG = LoggerFactory.getLogger(OkHttpClientImpl.class);

    final private OkHttpClient client = new OkHttpClient();

    @Override
    public String get(String url) throws HttpException {
        val request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .build();
        try (Response response = client.newCall(request).execute()) {
            int statusCode = response.code();
            LOG.debug("URL: {}, status: {}", url, statusCode);
            if (statusCode != 200) {
                throw new HttpException(statusCode);
            }
            val body = response.body();
            return body != null ? body.string() : "";
        } catch (IOException e) {
            throw new HttpException("There was an error communicating with the service", e);
        }
    }
}
