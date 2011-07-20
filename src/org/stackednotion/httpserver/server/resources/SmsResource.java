package org.stackednotion.httpserver.server.resources;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.stackednotion.httpserver.adapters.SmsAdapter;

import android.net.Uri;

public class SmsResource extends ServerResource {
	private String resourceId;

	@Override
	protected void doInit() throws ResourceException {
		resourceId = (String) getRequest().getAttributes().get("id");
	}

	@Post
	public Representation acceptItem(Representation entity) { 
		if (resourceId == null) {
			return createAction(entity);
		} else {
			// if (this.getOriginalRef().getLastSegment().equals("resend"))
			return resendAction(entity);
		}
	}

	public Representation createAction(Representation entity) {
		try {
			String value = entity.getText();
			JSONObject data = (JSONObject) new JSONTokener(value).nextValue();

			String destination = data.getString("destination");
			String body = data.getString("body");

			Uri result = SmsAdapter.sendSms(destination, body);
			
			if (result != null)
			{
				setStatus(Status.SUCCESS_ACCEPTED);
				this.setLocationRef("/sms/" + result.getLastPathSegment());
			} else {
				setStatus(Status.SERVER_ERROR_INTERNAL);
			}
		} catch (JSONException e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return new StringRepresentation("Invalid JSON");
		} catch (IOException e) {
			setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return new StringRepresentation("No JSON body passed");
		}
		
		return null;
	}
	
	private Representation resendAction(Representation entity) {
		Uri result = SmsAdapter.resendSms(resourceId);
		if (result != null)
		{
			setStatus(Status.SUCCESS_ACCEPTED);
			this.setLocationRef("/sms/" + result.getLastPathSegment());
		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		}
		
		return null;
	}
}
