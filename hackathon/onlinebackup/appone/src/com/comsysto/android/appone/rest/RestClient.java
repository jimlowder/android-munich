package com.comsysto.android.appone.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.ClientParamsStack;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.comsysto.vooone.Contact;

import android.util.Log;

/**
 * 
 * @author stefandjurasic
 * @author danielbartl
 */
public class RestClient {

	private static final String HOST = "http://10.0.2.2:8080/rest/";
	private static final String GET_URL =  HOST + "contacts";
	private static final String CREATE_URL = HOST + "contact";
	


	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	/*
	 * This is a test function which will connects to a given rest service and
	 * prints it's response to Android Log with labels "Praeda".
	 */
	public static List<Contact> getContacts() {

		List<Contact> contacts = new ArrayList<Contact>();
		HttpClient httpclient = new DefaultHttpClient();

		// Prepare a request object
		HttpGet httpget = new HttpGet(GET_URL);
		Log.i("Trying to connect to URL: ", httpget.getURI().toString());

		// Execute the request
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			// Examine the response status
			Log.i("Response status : ", response.getStatusLine().toString());

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release

			if (entity != null) {

				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();
				Log.i("Resulting string", result);

				// A Simple JSONObject Creation
				JSONArray json = new JSONArray(result);
				for (int i = 0; i < json.length(); i++) {
					Contact contact = new Contact();
					JSONObject jsonObject = new JSONObject(json.getString(i));
					contact.setFirstName(jsonObject.getString("firstName"));
					contact.setLastName(jsonObject.getString("lastName"));
					contact.seteMail(jsonObject.getString("eMail"));
					contact.setPhoneNumber(jsonObject.getString("phoneNumber"));
					contact.setId(jsonObject.getLong("id"));
					contacts.add(contact);
					Log.i("Kontakt erhalten: ", contact.toString());
				}
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contacts;
	}

	public static void createContact(Contact contact) {
		Log.i("Trying to add new Contact", contact.toString());
		HttpClient httpclient = new DefaultHttpClient();

		// Prepare a request object
		HttpPut httpPut = new HttpPut(CREATE_URL);
		httpPut.setParams(createParams(contact));
		Log.i("Trying to connect to URL: ", httpPut.getURI().toString());

		// Execute the request
		try {
			HttpResponse response = httpclient.execute(httpPut);
			Log.i("Response status : ", response.getStatusLine().toString());
			Log.i("Trying to add Contact: ", "success");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	private static HttpParams createParams(Contact contact) {
		BasicHttpParams params = new BasicHttpParams();
		params.setParameter("id", contact.getId());
		params.setParameter("firstName", contact.getFirstName());
		params.setParameter("lastName", contact.getLastName());
		params.setParameter("eMail", contact.geteMail());
		params.setParameter("phoneNumber", contact.getPhoneNumber());
		return params;
	}


}
