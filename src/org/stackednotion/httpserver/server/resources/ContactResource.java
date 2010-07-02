package org.stackednotion.httpserver.server.resources;

import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.stackednotion.httpserver.adapters.ContactsAdapter;
import org.stackednotion.httpserver.adapters.ContactsAdapter.Contact;

public class ContactResource extends ServerResource {
	private String contactKey;

	@Override
	protected void doInit() throws ResourceException {
		contactKey = (String) getRequest().getAttributes().get("contactKey");
	}

	@Get
	public Representation represent() {
		Contact contact = ContactsAdapter.find_by_key(contactKey);

		if (contact != null) {
			return new JsonRepresentation(contact.toJson());
		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return new StringRepresentation("Contact #" + contactKey
					+ " doesn't exist.");
		}
	}
}
