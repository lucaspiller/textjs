package org.stackednotion.textjs.server.resources;

import java.io.InputStream;
import java.util.Collection;

import org.json.JSONArray;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.InputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.stackednotion.textjs.adapters.ContactsAdapter;
import org.stackednotion.textjs.adapters.ContactsAdapter.Contact;
import org.stackednotion.textjs.server.SecuredResource;

public class ContactResource extends SecuredResource {
	private String resourceId;

	@Override
	protected void doInit() throws ResourceException {
		verifyAccessToken();
		resourceId = (String) getRequest().getAttributes().get("id");
	}

	@Get
	public Representation represent() {
		if (resourceId == null) {
			return indexAction();
		} else if (this.getOriginalRef().getLastSegment().equals("photo")) {
			return showPhotoAction();
		} else {
			return showAction();
		}
	}
	
	public Representation indexAction() {
		Collection<Contact> contacts = ContactsAdapter.all();
		JSONArray array = new JSONArray();

		for (Contact c : contacts) {
			array.put(c.toJsonShort());
		}

		return new JsonRepresentation(array);
	}
	
	public Representation showPhotoAction() {
		InputStream photo = ContactsAdapter.find_photo(resourceId);

		if (photo != null) {
			return new InputRepresentation(photo);
		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return new StringRepresentation("Image not available for Contact #"
					+ resourceId + " or contact doesn't exist.");
		}
	}
	
	public Representation showAction() {
		Contact contact = ContactsAdapter.find_by_key(resourceId);

		if (contact != null) {
			return new JsonRepresentation(contact.toJson());
		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return new StringRepresentation("Contact #" + resourceId
					+ " doesn't exist.");
		}
	}
}
