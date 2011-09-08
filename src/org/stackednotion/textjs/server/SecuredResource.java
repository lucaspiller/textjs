package org.stackednotion.textjs.server;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.stackednotion.textjs.Settings;

public abstract class SecuredResource extends ServerResource {
	protected void verifyAccessToken() throws ResourceException {
		Settings.wakeUpDevice();
		Form requestHeaders = (Form) getRequest().getAttributes().get("org.restlet.http.headers");
		String suppliedAccessCode = requestHeaders.getFirstValue("Xaccesscode");
		if (!Settings.getAccessCode().equals(suppliedAccessCode)) {
			throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED);
		}
	}
}
