package ru.roulette.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONHttpClient {

	/** TAG for logging. */
	private static final String TAG = "JSONCommunication";

	private static final String SERVER_URL = "http://10.60.10.45:8080/invitem/";

	private static DefaultHttpClient httpClient = initHttpClient();

	public static JSONObject getJSONObject(HttpClient httpClient, String url) {
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response;

		try {
			response = httpClient.execute(httpGet);

			Log.i(TAG, "Got response status code: "
					+ response.getStatusLine().getStatusCode());
			// TODO: handle HTTP-Status (e.g. 404)

			return createJSONResponse(url, response);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			httpGet.abort();
		}
		return null;
	}

	private static JSONObject createJSONResponse(String url,
			HttpResponse response) throws IOException, JSONException {
		HttpEntity entity = response.getEntity();

		if (entity != null) {

			String result = getStringFromEntity(entity);

			if (result.startsWith("<?xml version")) {
				Log.w(TAG,
						"Warning, expected JSON but received xml header from URL="
								+ url);
				return null;
			} else {
				return new JSONObject(result);
			}
		}
		return null;
	}

	public static void doLogout() {
		// JUST reinit the client...
		httpClient = initHttpClient();
	}

	public static boolean doLogin(String email, String password) throws JSONRPCException {
		JSONObject json = null;
		try {
			// TODO maybe do other method later - and don't request a whole html
			// page....
			String parameters = URLEncoder.encode(email, "UTF-8") + "/"
					+ URLEncoder.encode(password, "UTF-8");
			HttpPost httpPost = new HttpPost(SERVER_URL + "signin/"
					+ parameters); // TODO https in production...
			httpClient.execute(httpPost);
			// Just check if its possible to execute a method the JSON way...
			json = postJSONObject("ping", "'invitem.net rulez!'");
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "Login error: " + e);
			return false;
		} catch (ClientProtocolException e) {
			Log.e(TAG, "Login error: " + e);
			return false;
		} catch (IOException e) {
			throw new JSONRPCException("Login error IOException", e);
		}
		if (json != null) {
			return true;
		} else {
			// signin errors...
			return false;
		}
	}

	public static JSONObject postJSONObject(String method, String... params)
			throws JSONRPCException {

		String url = SERVER_URL + "json/";
		HttpPost httpPostRequest = new HttpPost(url);

		// Copy method arguments in a json array
		JSONArray jsonParams = new JSONArray();
		for (int i = 0; i < params.length; i++) {
			jsonParams.put(params[i]);
		}
		try {
			// Create the json request object
			JSONObject jsonRequest = new JSONObject();
			// id hard-coded at 1 for now
			jsonRequest.put("id", 1);
			jsonRequest.put("method", method);
			jsonRequest.put("params", jsonParams);
			httpPostRequest.setEntity(new StringEntity(jsonRequest.toString()));
			httpPostRequest.setHeader("Accept", "application/json");
			httpPostRequest.setHeader("Content-type", "application/json");
			Log.d(TAG, "Post request, json data: " + jsonRequest.toString());
			// Execute the request and try to decode the JSON Response
			long t = System.currentTimeMillis();
			HttpResponse response = httpClient.execute(httpPostRequest);
			t = System.currentTimeMillis() - t;
			Log.d(TAG, "Request time = " + t + "ms");
			String responseString = getStringFromEntity(response.getEntity());
			responseString = responseString.trim();
			JSONObject jsonResponse = new JSONObject(responseString);
			Log.d(TAG, "Got response status code: " + jsonResponse.toString());
			Log.d(TAG, "Got JSON response : "
					+ response.getStatusLine().getStatusCode());
			// TODO: handle HTTP-Status (e.g. 404)

			// Check for remote errors
			if (jsonResponse.has("error")) {
				Object jsonError = jsonResponse.get("error");
				if (!jsonError.equals(null)) {
					throw new JSONRPCException(jsonResponse.get("error"));
				}
				return jsonResponse; // JSON-RPC 1.0
			} else {
				return jsonResponse; // JSON-RPC 2.0
			}
		}
		// Underlying errors are wrapped into a JSONRPCException instance
		catch (ClientProtocolException e) {
			throw new JSONRPCException("HTTP error", e);
		} catch (IOException e) {
			throw new JSONRPCException("IO error", e);
		} catch (JSONException e) {
			throw new JSONRPCException("Invalid JSON response", e);
		} finally {
			httpPostRequest.abort();
		}
	}

	private static String getStringFromEntity(HttpEntity entity) {
		StringBuilder sb = new StringBuilder();
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(entity.getContent());
			BufferedReader buffer = new BufferedReader(reader, 8192);			
			String cur;
			while ((cur = buffer.readLine()) != null) {
				sb.append(cur);
			}
		} catch (IOException e) {
			Log.e(TAG, "Error while reading inputstream: " + e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					Log.e(TAG, "Error while closing reader: " + e);
				}
			}
		}
		return sb.toString();
	}

	// initialize the http client.
	private static DefaultHttpClient initHttpClient() {
		// sets up parameters
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "utf-8");
		params.setBooleanParameter("http.protocol.expect-continue", false);

		// registers schemes for both http and https
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		final SSLSocketFactory sslSocketFactory = SSLSocketFactory
				.getSocketFactory();
		sslSocketFactory
				.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		registry.register(new Scheme("https", sslSocketFactory, 443));

		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(
				params, registry);
		return new DefaultHttpClient(manager, params);
	}
}
