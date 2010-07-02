package org.stackednotion.httpserver.server.resources;

import java.io.InputStream;

import org.restlet.data.Status;
import org.restlet.representation.InputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.stackednotion.httpserver.adapters.ContactsAdapter;

public class ContactPhotoResource extends ServerResource {
	private String contactId;
	
	@Override
    protected void doInit() throws ResourceException {  
        contactId = (String) getRequest().getAttributes().get("contactId");  
    }
	
    @Get
    public Representation represent() {
    	InputStream photo = ContactsAdapter.find_photo(contactId);
    	
    	if (photo != null)
    	{
    		return new InputRepresentation(photo);
    	} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return new StringRepresentation("Image not available for Contact #"
					+ contactId + " or contact doesn't exist.");
    	}
    }
}

