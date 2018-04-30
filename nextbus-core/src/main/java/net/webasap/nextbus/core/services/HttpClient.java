package net.webasap.nextbus.core.services;

/**
 * A dead simple interface for an HTTP client.  For this project, only a GET
 * is needed.
 */
public interface HttpClient {

    /** Returns the body of the response as a string for a given String url */
    public String get(String url) throws HttpException;

}
