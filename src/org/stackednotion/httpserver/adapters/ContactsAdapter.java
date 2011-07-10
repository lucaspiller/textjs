package org.stackednotion.httpserver.adapters;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.stackednotion.httpserver.Settings;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;

public class ContactsAdapter {
	private static Map<String, String> phoneNumberToKey;

	public static String find_key_from_phone_number(String number) {
		if (phoneNumberToKey == null) {
			phoneNumberToKey = new HashMap<String, String>();
		}

		if (phoneNumberToKey.containsKey(number)) {
			return phoneNumberToKey.get(number);
		} else {

			Uri uri = Uri.withAppendedPath(
					Uri.parse("content://com.android.contacts/phone_lookup"),
					Uri.encode(number));

			Cursor cursor = Settings.getContext().getContentResolver()
					.query(uri, null, null, null, null);

			if (cursor.moveToFirst()) {
				String key = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));

				cursor.close();

				// store in cache
				phoneNumberToKey.put(number, key);

				return key;
			} else {
				// no contact exists for the number
				cursor.close();

				return null;
			}
		}
	}

	public static Contact find_by_key(String key) {
		Uri lookupUri = Uri.withAppendedPath(Contacts.CONTENT_LOOKUP_URI, key);

		Cursor cursor = Settings.getContext().getContentResolver()
				.query(lookupUri, null, null, null, null);
		cursor.moveToFirst();

		if (cursor.getCount() > 0) {
			return createContactFromCursor(cursor);
		} else {
			return null;
		}
	}

	public static InputStream find_photo(String id) {
		Uri uri = Uri.parse(ContactsContract.Contacts.CONTENT_URI + "/" + id);

		InputStream stream = ContactsContract.Contacts
				.openContactPhotoInputStream(Settings.getContext()
						.getContentResolver(), uri);

		return stream;
	}

	public static Collection<Contact> all() {
		Cursor cursor = Settings
				.getContext()
				.getContentResolver()
				.query(ContactsContract.Contacts.CONTENT_URI, null,
						ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1",
						null, null);
		cursor.moveToFirst();

		ArrayList<Contact> contacts = new ArrayList<Contact>();

		if (cursor.getCount() > 0) {
			do {
				Contact c = createContactFromCursor(cursor);
				contacts.add(c);
			} while (cursor.moveToNext());
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

		// get phone numbers
		Cursor phonesCur = Settings
				.getContext()
				.getContentResolver()
				.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
								+ " = ?", new String[] { c.key }, null);

		c.numbers = new ArrayList<Contact.PhoneNumber>();

		while (phonesCur.moveToNext()) {
			Integer phoneType = phonesCur
					.getInt(phonesCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
			String phoneNumber = phonesCur
					.getString(phonesCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			c.numbers.add(new Contact.PhoneNumber(phoneType, phoneNumber));
		}
		phonesCur.close();

		// get emails
		Cursor emailsCur = Settings
				.getContext()
				.getContentResolver()
				.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Email.LOOKUP_KEY
								+ " = ?", new String[] { c.key }, null);

		c.emails = new ArrayList<Contact.EmailAddress>();

		while (emailsCur.moveToNext()) {
			Integer emailType = emailsCur
					.getInt(emailsCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
			String emailAddress = emailsCur
					.getString(emailsCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
			c.emails.add(new Contact.EmailAddress(emailType, emailAddress));
		}
		emailsCur.close();

		return c;
	}

	public static class Contact {
		public String key;
		public String name;
		public Collection<PhoneNumber> numbers;
		public Collection<EmailAddress> emails;

		public JSONObject toJson() {
			try {
				JSONObject json = new JSONObject();
				json.put("key", key);
				json.put("name", name);

				// phone numbers
				JSONArray jsonNumbers = new JSONArray();
				for (PhoneNumber number : numbers) {
					JSONObject numberJson = new JSONObject();
					numberJson.put("type", number.type);
					numberJson.put("number", number.number);
					jsonNumbers.put(numberJson);
				}
				json.put("numbers", jsonNumbers);

				// email addresses
				JSONArray jsonEmails = new JSONArray();
				for (EmailAddress email : emails) {
					JSONObject emailJson = new JSONObject();
					emailJson.put("type", email.type);
					emailJson.put("address", email.address);
					jsonEmails.put(emailJson);
				}
				json.put("emails", jsonEmails);

				return json;
			} catch (Exception e) {
				return null;
			}
		}

		public JSONObject toJsonShort() {
			try {
				JSONObject json = new JSONObject();
				json.put("key", key);
				json.put("name", name);

				return json;
			} catch (Exception e) {
				return null;
			}
		}

		public static class PhoneNumber {
			public int type;
			public String number;

			public PhoneNumber(Integer type, String number) {
				this.type = type;
				this.number = number;
			}
		}

		public static class EmailAddress {
			public int type;
			public String address;

			public EmailAddress(Integer type, String address) {
				this.type = type;
				this.address = address;
			}
		}
	}
}
