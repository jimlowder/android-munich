package ru.roulette.comm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.util.Log;

public class HttpServiceHandler {

	public final static String SERVERNAME = "http://DUMMY/";

	private HttpClient httpClient;

	public HttpServiceHandler() {
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

		this.httpClient = new DefaultHttpClient(manager, params);
	}

	public int postData(byte[] image, String url) {
		HttpPost httpPostRequest = new HttpPost(url);
		httpPostRequest.setEntity(new ByteArrayEntity(image));
		try {
			HttpResponse response = httpClient.execute(httpPostRequest);
			InputStream is = response.getEntity().getContent();
			int c = 0;
			String s = "";
			while ((c = is.read()) >= 0) {
				s = s + (char) c;
			}

			return Integer.parseInt(s);
		} catch (Exception ex) {
			Log.e("HttpServerHandler postData", ex.getLocalizedMessage());
		}

		return 0;
	}

	public Identity getImage(String url) {
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			Header header = response.getFirstHeader("Content-disposition");
			String filename = header.getValue().split("=")[1];
			byte[] bytebuffer = new byte[(int) response.getEntity()
					.getContentLength()];
			Identity ident = new Identity();
			ident.setId(filename);
			ident.setImage(bytebuffer);

			return ident;
		} catch (Exception ex) {
			Log.e("HttpServerHandler getImage", ex.getLocalizedMessage());
		}

		return null;

	}

	public void sendMessage(int myid, int toid, String message, String url) {
		HttpPost httpPostRequest = new HttpPost(url + "?fromID=" + myid
				+ "&toID=" + toid);
		try {
			httpPostRequest.setEntity(new StringEntity(message));

			HttpResponse response = httpClient.execute(httpPostRequest);
			if (response.getStatusLine().getStatusCode() != 200)
				Log
						.d("sendMessage", response.getStatusLine()
								.getReasonPhrase());
		} catch (Exception ex) {
			Log.e("HttpServerHandler getImage", ex.getLocalizedMessage());
		}
	}

	public String getString(int myid, String url) {
		HttpGet httpGet = new HttpGet(url + "?myID=" + myid);
		byte[] bytebuffer = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);

			bytebuffer = new byte[(int) response.getEntity().getContentLength()];
			response.getEntity().getContent().read(bytebuffer);
		} catch (Exception ex) {
			Log.e("getString", ex.getLocalizedMessage());
		}

		return new String(bytebuffer);
	}
}
