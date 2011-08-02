package org.stackednotion.httpserver.server.resources;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.stackednotion.httpserver.Settings;
import org.stackednotion.httpserver.server.SecuredResource;

public class StaticResource extends SecuredResource {
	@Get
	public Representation represent() {
		Representation representation = null;
		String version = null;

		try {
			version = Settings.getContext().getPackageManager()
					.getPackageInfo(Settings.getContext().getPackageName(), 0).versionName;
		} catch (Exception e) {
			setStatus(Status.SERVER_ERROR_INTERNAL);
		}

		if (getOriginalRef().getLastSegment() == null) {
			String body = "<!DOCTYPE html><html lang='en'><head><meta charset='utf-8' /><meta content='IE=edge,chrome=1' http-equiv='X-UA-Compatible' /><title>SmsJs</title><script src=\"https://s3.amazonaws.com/smsjs/"
					+ version
					+ "-libs.js\" type=\"text/javascript\"></script><script src=\"https://s3.amazonaws.com/smsjs/"
					+ version
					+ "-application.js\" type=\"text/javascript\"></script><link rel=\"stylesheet\" href=\"https://s3.amazonaws.com/smsjs/"
					+ version
					+ "-all.css\" /></head><body class='index'><div id='leftColumn'><div id='menubar'></div><div id='threadList'></div><div id='contactsList'></div></div><div id='rightColumn'><div class='threadView'><div class='loading'>No Conversation Selected</div></div></div></body><script type='text/javascript'>//<![CDATA[\nvar config = {}; $(function() { Application.init(config); });\n//]]></script></html>";
			representation = new StringRepresentation(body);
			representation.setMediaType(MediaType.TEXT_HTML);
		} else if (getOriginalRef().getLastSegment().equals("version")) {
			verifyAccessToken();
			representation = new StringRepresentation(version);
			representation.setMediaType(MediaType.TEXT_PLAIN);
		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		}

		return representation;
	}
}
