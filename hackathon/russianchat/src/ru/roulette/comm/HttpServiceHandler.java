package ru.roulette.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.net.URLDecoder;
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
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.util.Log;

public class HttpServiceHandler {

	public final static String SERVERNAME = "http://192.168.0.167:8000/";

	private HttpClient httpClient;

	public HttpServiceHandler() {
		// sets up parameters
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "utf-8");
		// params.setBooleanParameter("http.protocol.expect-continue", false);
		// Set the timeout in milliseconds until a connection is established.
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(params, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(params, timeoutSocket);
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

		this.httpClient = new DefaultHttpClient(manager, params);
	}

	public int postData(byte[] image, String url) {
		HttpPost httpPostRequest = new HttpPost(url);
		if (image != null)
			httpPostRequest.setEntity(new ByteArrayEntity(image));
		try {
			HttpResponse response = httpClient.execute(httpPostRequest);

			String s = getStringFromEntity(response.getEntity());
			
			if (s.startsWith("Not")) {
				return -1;
			}
			return Integer.parseInt(s);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return 0;
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
			Log.e("http", "Error while reading inputstream: " + e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					Log.e("http", "Error while closing reader: " + e);
				}
			}
		}
		return sb.toString();
	}
	
	public Identity getImage(String url) {
		HttpGet httpGet = new HttpGet(url);
		InputStream is = null;
		try {
			int id = 0;
			HttpResponse response = httpClient.execute(httpGet);

			String s = getStringFromEntity(response.getEntity());
			if (s.length() > 0) {
				id = Integer.parseInt(s);
			}

			Identity ident = new Identity();
			ident.setId(id);
			ident.setImage(null);

			Log.d("getImage", "My chat partner: " + id);

			return ident;
		} catch (Exception ex) {
			Log.e("HttpServerHandler"," getImage"+ex);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					//fuck off
				}
			}
		}

		return null;

	}

	public void sendMessage(int myid, int toid, String message, String url) {
		
		message = URLEncoder.encode(message);
		Log.d("begin sendMessage", "---> " + myid + " " + toid + " -- "
				+ message);

		HttpGet httpGetRequest = new HttpGet(url + "?fromID=" + myid
				+ "&toID=" + toid + "&txt=" + message);
		try {
			HttpResponse response = httpClient.execute(httpGetRequest);
			
			 if (response.getStatusLine().getStatusCode() != 200) {
				 Log.d("sendMessage", response.getStatusLine().getReasonPhrase());
			 }
		
		} catch (Exception ex) {
			Log.e("sendMessage","sendMessage Error e="+ex);
		}
		Log.d("sendMessage", "---> " + myid + " " + toid + " -- " + message);
	}

	public String getString(int myid, String url) {
		HttpGet httpGet = new HttpGet(url + "?myID=" + myid);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getEntity() == null)
				return null;

			String s = getStringFromEntity(response.getEntity());
			
			return URLDecoder.decode(s);

		} catch (ClientProtocolException ex) {
			return null;
		} catch (ProtocolException ex) {
			return null;
		} catch (Exception ex) {
			Log.e("sendMessage","getString Error e="+ex);
		}

		return null;
	}

	public void sendID(int myid, String url) {
		HttpGet httpGet = new HttpGet(url + "?myID=" + myid);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			response.getEntity();
		} catch (ClientProtocolException e) {
			Log.e("sendMessage","sendID Error e="+e);
		} catch (IOException e) {
			Log.e("sendMessage","sendID Error e="+e);
		}

	}
	
}
