package org.stackednotion.textjs.rosebud.server.resources;

import java.util.Collection;

import org.json.JSONArray;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.stackednotion.textjs.rosebud.adapters.ContactsAdapter;
import org.stackednotion.textjs.rosebud.adapters.ContactsAdapter.Contact;
import org.stackednotion.textjs.rosebud.server.SecuredResource;

public class ContactResource extends SecuredResource {
	@Override
	protected void doInit() throws ResourceException {
		verifyAccessToken();
	}

	@Get
	public Representation represent() {
		return indexAction();
	}
	
	public Representation indexAction() {
		Collection<Contact> contacts = ContactsAdapter.all();
		JSONArray array = new JSONArray();

		for (Contact c : contacts) {
			array.put(c.toJson());
		}

		return new JsonRepresentation(array);
	}
}
