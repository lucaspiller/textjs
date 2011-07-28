package org.stackednotion.httpserver.server.resources;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.InputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.stackednotion.httpserver.R;
import org.stackednotion.httpserver.Settings;

public class StaticResource extends ServerResource {
	@Get
	public Representation represent() {
		InputRepresentation representation = null;
		
		if (getOriginalRef().getLastSegment() == null)
		{
			representation = new InputRepresentation(Settings.getContext().getResources().openRawResource(R.raw.index));
			representation.setMediaType(MediaType.TEXT_HTML);
		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		}
		
		return representation;
	}
}
	