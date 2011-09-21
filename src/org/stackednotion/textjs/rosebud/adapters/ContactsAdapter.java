package org.stackednotion.textjs.rosebud.adapters;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONObject;
import org.stackednotion.textjs.rosebud.Settings;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class ContactsAdapter {
	// times_contacted,contact_status,custom_ringtone,has_phone_number,phonetic_name,phonetic_name_style,
	// contact_status_label,lookup,contact_status_icon,last_time_contacted,display_name,sort_key_alt,
	// in_visible_group,_id,starred,sort_key,display_name_alt,contact_presence,display_name_source,
	// contact_status_res_package,contact_chat_capability,contact_status_ts,photo_id,send_to_voicemail
	public static final String[] CONTACTS_PROJECTION = {
			ContactsContract.Contacts.LOOKUP_KEY,
			ContactsContract.Contacts.DISPLAY_NAME };

	public static Contact find_by_address(String address) {
		Uri contactUri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI, address);
		Cursor cursor = Settings.getContext().getContentResolver()
				.query(contactUri, CONTACTS_PROJECTION, null, null, null);
		
		if (cursor.moveToFirst()) {
			return createContactFromCursor(cursor);
		} else {
			return null;
		}
	}

	public static Collection<Contact> all() {
		Cursor cursor = Settings
				.getContext()
				.getContentResolver()
				.query(ContactsContract.Contacts.CONTENT_URI,
						CONTACTS_PROJECTION,
						ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1",
						null, null);

		ArrayList<Contact> contacts = new ArrayList<Contact>();

		cursor.moveToPosition(-1);
		while (cursor.moveToNext()) {
			Contact c = createContactFromCursor(cursor);
			contacts.add(c);
		}
		cursor.close();

		return contacts;
	}

	private static Contact createContactFromCursor(Cursor cursor) {
		Contact c = new Contact();

		c.key = cursor.getString(cursor
				.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
		c.name = cursor.getString(cursor
				.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

		return c;
	}

	public static class Contact {
		public String key;
		public String name;

		public JSONObject toJson() {
			try {
				JSONObject json = new JSONObject();
				json.put("key", key);
				json.put("name", name);

				return json;
			} catch (Exception e) {
				return null;
			}
		}
	}
}
