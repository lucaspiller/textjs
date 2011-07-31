package org.stackednotion.httpserver.server.resources;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.stackednotion.httpserver.Settings;

public class StaticResource extends ServerResource {
	@Get
	public Representation represent() {
		Representation representation = null;
		
		if (getOriginalRef().getLastSegment() == null)
		{
			try {
				String version = Settings.getContext().getPackageManager().getPackageInfo(Settings.getContext().getPackageName(), 0).versionName;
				String body = "<!DOCTYPE html><html lang='en'><head><meta charset='utf-8' /><meta content='IE=edge,chrome=1' http-equiv='X-UA-Compatible' /><title>SmsJs</title><script src=\"https://s3.amazonaws.com/smsjs/" + version + "-libs.js\" type=\"text/javascript\"></script><script src=\"https://s3.amazonaws.com/smsjs/" + version + "-application.js\" type=\"text/javascript\"></script><link rel=\"stylesheet\" href=\"https://s3.amazonaws.com/smsjs/" + version + "-all.css\" /></head><body class='index'><div id='leftColumn'><div id='menubar'></div><div id='threadList'></div><div id='contactsList'></div></div><div id='rightColumn'><div class='threadView'><div class='loading'>No Conversation Selected</div></div></div></body><script type='text/javascript'>//<![CDATA[\nvar config = {}; $(function() { Application.init(config); });\n//]]></script></html>";
				representation = new StringRepresentation(body);
				representation.setMediaType(MediaType.TEXT_HTML);
			} catch (Exception e) {
				setStatus(Status.CLIENT_ERROR_FORBIDDEN);
			}
		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);			
		}
		
		return representation;
	}
}
	