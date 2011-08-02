package org.stackednotion.httpserver.server;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.stackednotion.httpserver.Settings;

public abstract class SecuredResource extends ServerResource {
	protected void verifyAccessToken() throws ResourceException {
		Form requestHeaders = (Form) getRequest().getAttributes().get("org.restlet.http.headers");
		String suppliedAccessCode = requestHeaders.getFirstValue("Xaccesscode");
		if (!Settings.getAccessCode().equals(suppliedAccessCode)) {
			throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED);
		}
	}
}