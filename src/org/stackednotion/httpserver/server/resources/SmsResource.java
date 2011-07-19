package org.stackednotion.httpserver.server.resources;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.stackednotion.httpserver.sms.Sender;

import android.net.Uri;

public class SmsResource extends ServerResource {
	@Post("json")
	public String acceptJson(String value)
	{
		try
		{
			JSONObject data = (JSONObject) new JSONTokener(value).nextValue();
			
			String destination = data.getString("destination");
			String body = data.getString("body");
			
			Uri result = Sender.send(destination, body);
			return "Cool story bro: " + result.toString();
		} catch (JSONException e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return "Invalid JSON";
		} catch (NullPointerException e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return "No JSON body passed";
		}
	}
}
