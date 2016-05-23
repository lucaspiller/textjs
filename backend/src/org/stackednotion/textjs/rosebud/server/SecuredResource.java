package org.stackednotion.textjs.rosebud.server;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.stackednotion.textjs.rosebud.Settings;

public abstract class SecuredResource extends ServerResource {
	protected void verifyAccessToken() throws ResourceException {
		Settings.wakeUpDevice();
		Form requestHeaders = (Form) getRequest().getAttributes().get("org.restlet.http.headers");
		
		String suppliedAccessCode;
		
		// header should be lowercase, but some proxies / browsers may uppercase the X
		suppliedAccessCode = requestHeaders.getFirstValue("xaccesscode");
		if (suppliedAccessCode == null) {
			suppliedAccessCode = requestHeaders.getFirstValue("Xaccesscode");
		}
		
		if (!Settings.getAccessCode().equals(suppliedAccessCode)) {
			throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED);
		}
	}
}
