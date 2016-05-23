package org.stackednotion.textjs.rosebud;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

public class Network {
	public static boolean isConnected() {
		return (getLocalIp() != null);
	}

	public static String getLocalIpAddress() {
		return intToIp(getLocalIp());
	}

	private static Integer getLocalIp() {
		try {
			WifiManager wifiManager = (WifiManager) Settings.getContext()
					.getSystemService(Context.WIFI_SERVICE);
			DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
			return dhcpInfo.ipAddress;
		} catch (NullPointerException e) {
			return null;
		}
	}

	/**
	 * Source:
	 * http://teneo.wordpress.com/2008/12/23/java-ip-address-to-integer-and
	 * -back/
	 */
	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + ((i >> 24) & 0xFF);
	}
}
