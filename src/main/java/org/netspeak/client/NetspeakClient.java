package org.netspeak.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.netspeak.JsonUtil;
import org.netspeak.generated.NetspeakMessages.Phrase;
import org.netspeak.generated.NetspeakMessages.Response;

/**
 * A client class for searching the Netspeak service. There are two variants to
 * send a {@link Request} to and receive a {@link Response} from Netspeak:
 * <ul>
 * <li>Synchronously using {@link #search(Request)}</li>
 * <li>Asynchronously using {@link #searchAsync(Request)}</li>
 * </ul>
 */
public class NetspeakClient {

	private String baseUrl;
	private final ExecutorService executor;

	/**
	 * Instantiates a Netspeak client setting its internal base URL the value
	 * returned by {@link #getDefaultBaseUrl()}.
	 */
	public NetspeakClient() {
		this(getDefaultBaseUrl());
	}

	/**
	 * Instantiates a Netspeak client setting the internal base URL to the user
	 * supplied string. This feature allows to request Netspeak instances other than
	 * the public <a href="http://netspeak.org/">netspeak.org</a>, which is by now
	 * for internal usage only.
	 *
	 * @param baseUrl The base URL of the Netspeak service to request.
	 *
	 * @see #getBaseUrl()
	 * @see #setBaseUrl(String)
	 */
	public NetspeakClient(String baseUrl) {
		this(baseUrl, Executors.newCachedThreadPool());
	}

	/**
	 * Instantiates a Netspeak client setting the internal base URL to the user
	 * supplied string. This feature allows to request Netspeak instances other than
	 * the public <a href="http://netspeak.org/">netspeak.org</a>, which is by now
	 * for internal usage only.
	 *
	 * @param baseUrl  The base URL of the Netspeak service to request.
	 * @param executor The executor service which will be used to construct the
	 *                 threads for the asynchronous Netspeak request.
	 *
	 * @see #getBaseUrl()
	 * @see #setBaseUrl(String)
	 */
	public NetspeakClient(String baseUrl, ExecutorService executor) {
		setBaseUrl(baseUrl);
		this.executor = executor;
	}

	/**
	 * Builds a URL parameter string from the key/value pairs given by
	 * {@code request}. If the key {@link Request#QUERY} is present, its value will
	 * be percentage-encoded in the returned string. All other values will be copied
	 * without modification.
	 *
	 * @param request     The {@link Request} containing key/value pairs to encode.
	 * @return The URL-encoded parameter string.
	 */
	private static final String buildUrlParamString(Request request) {
		StringBuilder sb = new StringBuilder();

		sb.append(Request.FORMAT).append('=').append("json");

		for (Map.Entry<String, String> entry : request.entrySet()) {
			String param = entry.getKey().trim().toLowerCase();
			if (Request.protectedParams.contains(param))
				continue;

			String value = entry.getValue().trim();
			try {
				value = URLEncoder.encode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("Your JRE does not support UTF-8", e);
			}
			sb.append('&').append(param).append('=').append(value);
		}

		return sb.toString();
	}

	/**
	 * Returns the base URL of the public Netspeak web service. This URL should be
	 * equal to the one defined under
	 * <a href="http://netspeak.org/#developer">netspeak.org/#developer</a>.
	 *
	 * @return The base URL of the public Netspeak web service.
	 *
	 * @see #getBaseUrl()
	 * @see #setBaseUrl(String)
	 */
	public static final String getDefaultBaseUrl() {
		return "https://api.netspeak.org/netspeak4/search?";
	}

	/**
	 * Builds a complete {@link URL} object, composed of the base URL and a list of
	 * URL parameters, that forms a valid Netspeak request.
	 *
	 * @param request The {@link Request} containing key/value pairs to encode.
	 * @return An {@link URL} object.
	 * @throws MalformedURLException If the composed URL is not well-formatted.
	 */
	private URL buildUrl(Request request) throws MalformedURLException {
		return new URL(getBaseUrl() + buildUrlParamString(request));
	}

	/**
	 * Sends a {@link Request} to the Netspeak service identified by
	 * {@link #getBaseUrl()}. This method blocks until the corresponding
	 * {@link Response} was received or some exception occurred.
	 *
	 * @param request The {@link Request} containing all request parameters.
	 * @return The corresponding {@link Response} containing all retrieved
	 *         {@link Phrase}s and other information.
	 * @throws MalformedURLException If the composed URL is not well-formatted.
	 * @throws IOException           If some communication error occurred.
	 *
	 * @see #searchAsync(Request)
	 */
	public Response search(Request request) throws MalformedURLException, IOException {
		return JsonUtil.parseResponse(buildUrl(request).openStream());
	}

	/**
	 * Sends a {@link Request} to the Netspeak service identified by
	 * {@link #getBaseUrl()}. This method is the asynchronous counterpart of
	 * {@link #search(Request)}.
	 *
	 * @param request The {@link Request} containing all request parameters.
	 *
	 * @see #search(Request)
	 */
	public Future<Response> searchAsync(Request request) {
		return executor.submit(() -> this.search(request));
	}

	/**
	 * Returns the base URL of the Netspeak service to request. The base URL
	 * identifies the connection point to the Netspeak service, but does not include
	 * any request parameters.
	 *
	 * @return The base URL of the Netspeak service.
	 *
	 * @see #getDefaultBaseUrl()
	 * @see #setBaseUrl(String)
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Sets the base URL of the Netspeak service to request. The base URL identifies
	 * the connection point to the Netspeak service, but does not include any
	 * request parameters.
	 *
	 * @param baseUrl The base URL of the Netspeak service.
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

}
