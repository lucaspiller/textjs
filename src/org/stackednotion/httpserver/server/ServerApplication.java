package org.stackednotion.httpserver.server;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import org.stackednotion.httpserver.Network;
import org.stackednotion.httpserver.Settings;
import org.stackednotion.httpserver.server.resources.ContactResource;
import org.stackednotion.httpserver.server.resources.MessagesResource;
import org.stackednotion.httpserver.server.resources.SmsResource;
import org.stackednotion.httpserver.server.resources.ThreadResource;

import android.util.Log;

public class ServerApplication extends Application {

	@Override
	public synchronized Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/contacts", ContactResource.class);
		router.attach("/contacts/{id}", ContactResource.class);
		router.attach("/contacts/{id}/photo",
				ContactResource.class);
		router.attach("/messages", MessagesResource.class);
		router.attach("/thread", ThreadResource.class);
		router.attach("/thread/{id}", ThreadResource.class);
		router.attach("/sms", SmsResource.class);
		router.attach("/sms/{id}/resend", SmsResource.class);

		return router;
	}

	private static Component component;

	public static void startServer(int port) {
		try {
			if (!Network.isConnected()) {
				Log.v(Settings.LOG_TAG,
						"Cannot start server, Wifi not connected.");
				return;
			}

			component = new Component();
			component.getServers().add(Protocol.HTTP, port);
			component.getDefaultHost().attach(new ServerApplication());
			component.start();

			String ip = Network.getLocalIpAddress();

			Log.v(Settings.LOG_TAG, "Server started at http://" + ip + ":"
					+ new Integer(port).toString() + "/");
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));

			Log.e(Settings.LOG_TAG, sw.toString());
		}
	}

	public static void stopServer() {
		try {
			component.stop();

			Log.v(Settings.LOG_TAG, "Server stopped");
		} catch (Exception e) {
			Log.e(Settings.LOG_TAG, e.toString());
		}
	}
}