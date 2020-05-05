package org.netspeak.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;
import org.netspeak.generated.NetspeakMessages.RawResponse;
import org.netspeak.generated.NetspeakMessages.Response;


public class TestNetspeakClient {

	@Test
	public final void testGetBaseUrl() {
		NetspeakClient netspeak = new NetspeakClient();
		assertEquals(NetspeakClient.getDefaultBaseUrl(), netspeak.getBaseUrl());

		String someOtherBaseUrl = "some/other/base/url";
		netspeak = new NetspeakClient(someOtherBaseUrl);
		assertEquals(someOtherBaseUrl, netspeak.getBaseUrl());
	}

	@Test
	public final void testSearch() {
		NetspeakClient netspeak = new NetspeakClient();
		Request request = new Request();
		request.put(Request.QUERY, "test ?");
		try {
			Response response = netspeak.search(request);
			assertTrue(response.getPhraseCount() > 0);
		} catch (MalformedURLException e) {
			fail("Unexpected exception");
		} catch (IOException e) {
			fail("Unexpected exception");
		}
	}

	@Test
	public final void testSearchRaw() {
		NetspeakClient netspeak = new NetspeakClient();
		Request request = new Request();
		request.put(Request.QUERY, "test ?");
		try {
			RawResponse response = netspeak.searchRaw(request);
			assertTrue(response.getQueryResultCount() > 0);
		} catch (MalformedURLException e) {
			fail("Unexpected exception");
		} catch (IOException e) {
			fail("Unexpected exception");
		}
	}

	@Test(expected = IOException.class)
	public final void testSearchRawThrowingIOException() throws MalformedURLException, IOException {
		NetspeakClient netspeak = new NetspeakClient("http://does-not-exist.xxx");
		Request request = new Request();
		request.put(Request.QUERY, "test ?");
		netspeak.searchRaw(request);
	}

	@Test(expected = MalformedURLException.class)
	public final void testSearchRawThrowingMalformedURLException() throws MalformedURLException, IOException {
		NetspeakClient netspeak = new NetspeakClient("some/malformed/url");
		Request request = new Request();
		request.put(Request.QUERY, "test ?");
		netspeak.searchRaw(request);
	}

	@Test
	public final void testSearchRawWithSearchRawAsyncFailure() {
		NetspeakClient netspeak = new NetspeakClient("http://does-not-exist.xxx");
		Request request = new Request();

		request.put(Request.QUERY, "one ?");
		try {
			netspeak.searchRawAsync(request).get();
			fail("Unexpected event");
		} catch (Exception e) {
			// it's supposed to fail
		}

		request.put(Request.QUERY, "two ?");
		try {
			netspeak.searchRawAsync(request).get();
			fail("Unexpected event");
		} catch (Exception e) {
			// it's supposed to fail
		}

		request.put(Request.QUERY, "three ?");
		try {
			netspeak.searchRawAsync(request).get();
			fail("Unexpected event");
		} catch (Exception e) {
			// it's supposed to fail
		}
	}

	@Test
	public final void testSearchRawAsyncSuccess() {
		NetspeakClient netspeak = new NetspeakClient();
		Request request = new Request();
		request.put(Request.QUERY, "one ?");

		try {
			netspeak.searchRawAsync(request).get();
		} catch (Exception e) {
			fail("Unexpected event");
		}
	}

	@Test(expected = IOException.class)
	public final void testSearchThrowingIOException() throws MalformedURLException, IOException {
		NetspeakClient netspeak = new NetspeakClient("http://does-not-exist.xxx");
		Request request = new Request();
		request.put(Request.QUERY, "test ?");
		netspeak.search(request);
	}

	@Test(expected = MalformedURLException.class)
	public final void testSearchThrowingMalformedURLException() throws MalformedURLException, IOException {
		NetspeakClient netspeak = new NetspeakClient("some/malformed/url");
		Request request = new Request();
		request.put(Request.QUERY, "test ?");
		netspeak.search(request);
	}

	@Test
	public final void testSearchWithSearchAsyncFailure() {
		NetspeakClient netspeak = new NetspeakClient("http://does-not-exist.xxx");
		Request request = new Request();

		request.put(Request.QUERY, "one ?");
		try {
			netspeak.searchAsync(request).get();
			fail("Unexpected event");
		} catch (Exception e) {
			// it's supposed to fail
		}

		request.put(Request.QUERY, "two ?");
		try {
			netspeak.searchAsync(request).get();
			fail("Unexpected event");
		} catch (Exception e) {
			// it's supposed to fail
		}

		request.put(Request.QUERY, "three ?");
		try {
			netspeak.searchAsync(request).get();
			fail("Unexpected event");
		} catch (Exception e) {
			// it's supposed to fail
		}
	}

	@Test
	public final void testSearchWithSearchAsyncSuccess() {
		NetspeakClient netspeak = new NetspeakClient();
		Request request = new Request();
		request.put(Request.QUERY, "one ?");

		try {
			netspeak.searchAsync(request).get();
		} catch (Exception e) {
			fail("Unexpected event");
		}
	}

	@Test
	public final void testSetBaseUrl() {
		NetspeakClient netspeak = new NetspeakClient();
		String newBaseUrl = "http://some/base/url";
		netspeak.setBaseUrl(newBaseUrl);
		assertEquals(newBaseUrl, netspeak.getBaseUrl());
	}

}
