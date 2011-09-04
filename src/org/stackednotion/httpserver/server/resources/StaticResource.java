package org.stackednotion.httpserver.server.resources;

import java.io.IOException;
import java.io.InputStream;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.InputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.stackednotion.httpserver.Settings;
import org.stackednotion.httpserver.server.SecuredResource;

import android.content.res.AssetManager;
import android.util.Log;

public class StaticResource extends SecuredResource {
	@Get
	public Representation represent() {
		Representation representation = null;
		String version = null;

		try {
			version = Settings.getContext().getPackageManager()
					.getPackageInfo(Settings.getContext().getPackageName(), 0).versionName;
		} catch (Exception e) {
			setStatus(Status.SERVER_ERROR_INTERNAL);
		}

		if (getOriginalRef().getLastSegment() == null) {
			try {
				representation = new InputRepresentation(getAsset("index.html"));
				Log.d("HttpServer", "Dis: " + representation.getDisposition());
				representation.setMediaType(MediaType.TEXT_HTML);
			} catch (IOException e) {
				Log.e("HttpServer",
						"IoException reading index.html: " + e.getMessage()
								+ "\n" + e.getStackTrace());
				setStatus(Status.SERVER_ERROR_INTERNAL);
			}
		} else if (getOriginalRef().getLastSegment().equals("favicon.ico")) {
			try {
				representation = new InputRepresentation(getAsset("favicon.ico"));
				representation.setMediaType(MediaType.IMAGE_ICON);
			} catch (IOException e) {
				Log.e("HttpServer",
						"IoException reading index.html: " + e.getMessage()
								+ "\n" + e.getStackTrace());
				setStatus(Status.SERVER_ERROR_INTERNAL);
			}
		} else if (getOriginalRef().getLastSegment().equals("version")) {
			verifyAccessToken();
			representation = new StringRepresentation(version);
			representation.setMediaType(MediaType.TEXT_PLAIN);
		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		}

		return representation;
	}
	
	private InputStream getAsset(String filename) throws IOException {
		AssetManager assets = Settings.getContext().getAssets();
		return assets.open(filename);
	}
}
