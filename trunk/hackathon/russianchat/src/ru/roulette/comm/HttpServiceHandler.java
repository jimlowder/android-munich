package ru.roulette.comm;

import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;

import org.apache.http.Header;
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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
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
			InputStream is = response.getEntity().getContent();
			int c = 0;
			String s = "";
			while ((c = is.read()) >= 0) {
				s = s + (char) c;
			}
			if (s.startsWith("Not"))
				return -1;
			return Integer.parseInt(s);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return 0;
	}

	public Identity getImage(String url) {
		HttpGet httpGet = new HttpGet(url);
		try {
			int id = 0;
			HttpResponse response = httpClient.execute(httpGet);
			InputStream is = response.getEntity().getContent();

			String s = "";
			int c = is.read();
			while (c >= 0) {
				s = s + (char) c;
				c = is.read();
			}

			if (s.length() > 0) {
				id = Integer.parseInt(s);
			}

			Identity ident = new Identity();
			ident.setId(id);
			ident.setImage(null);

			Log.d("getImage", "My chat partner: " + id);

			return ident;
		} catch (Exception ex) {
			Log.e("HttpServerHandler getImage", ex.getClass().getName());
			ex.printStackTrace();
		}

		return null;

	}

	public void sendMessage(int myid, int toid, String message, String url) {
		HttpPost httpPostRequest = new HttpPost(url + "?fromID=" + myid
				+ "&toID=" + toid + "&txt=" + message);
		try {
			httpPostRequest.setEntity(new StringEntity(message));

			HttpResponse response = httpClient.execute(httpPostRequest);
			if (response.getStatusLine().getStatusCode() != 200)
				Log
						.d("sendMessage", response.getStatusLine()
								.getReasonPhrase());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Log.d("sendMessage","---> " + myid + " " + toid + " -- " + message);
	}

	public String getString(int myid, String url) {
		HttpGet httpGet = new HttpGet(url + "?myID=" + myid);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getEntity() == null)
				return null;
			InputStream is = response.getEntity().getContent();

			String s = "";
			int c = is.read();
			while (c >= 0) {
				s = s + (char) c;
				c = is.read();
			}
			Log.d("getString" , s);
			return s;

		} catch (ClientProtocolException ex) {
			return null;
		} catch (ProtocolException ex) {
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public void sendID(int myid, String url) {
		HttpGet httpGet = new HttpGet(url + "?myID=" + myid);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			response.getEntity();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
