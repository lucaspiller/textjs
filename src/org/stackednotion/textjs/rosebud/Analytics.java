package org.stackednotion.textjs.rosebud;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

/*
 * Basic analytics class which sends data to a remote server.
 * 
 * Usage example:
 *   
 *   Analytics analytics = new Analytics(getApplicationContext());
 *   analytics.event("appLaunch");
 * 
 */
public class Analytics {
	public static final String ENDPOINT = "https://stats.textjs.com/";
	public static final String PREFERENCES_UID_KEY = "analytics_id";

	private String mUid;
	private String mSid;
	private String mAppName;
	private String mAppVersion;
	private String mDevice;
	private String mLanguage;
	private String mCountry;
	private String mSystemName;
	private String mSystemVersion;
	private String mOperator;

	/*
	 * Constructor.
	 * 
	 * @param context An application context for the application.
	 */
	public Analytics(Context context) {
		setConfiguration(context);
	}
	
	/*
	 * Record an event.
	 * 
	 * @param name The name of the event.
	 */
	public void event(String name) {
		event(name, null);
	}
	
	/*
	 * Record an event with extra data.
	 * 
	 * @param name The name of the event.
	 * @param extra Any extra data to be sent with the event.
	 */
	public void event(String name, String extra) {
		try {
			HttpPost httppost = new HttpPost(ENDPOINT);
			List<NameValuePair> params = defaultParams();
			params.add(new BasicNameValuePair("type", "event"));
			params.add(new BasicNameValuePair("name", name));
			if (extra != null)
			{
				params.add(new BasicNameValuePair("extra", extra));
			}
			httppost.setEntity(new UrlEncodedFormEntity(params));
			new AnalyticsClientTask().execute(httppost);
		} catch (Exception e) {
			Log.e("Analytics", "Caught error building request: "
					+ e.getClass().getName() + " : " + e.getMessage());
		}
	}
	
	private void setConfiguration(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		if (sharedPreferences.contains(PREFERENCES_UID_KEY)) {
			mUid = sharedPreferences.getString(PREFERENCES_UID_KEY, "unknown");
		} else {
			mUid = UUID.randomUUID().toString();
			Editor editor = sharedPreferences.edit();
			editor.putString(PREFERENCES_UID_KEY, mUid);
			editor.commit();
		}
		mSid = UUID.randomUUID().toString();
		
		mAppName = context.getApplicationInfo().packageName;
		try {
			mAppVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			mAppVersion = "unknown";
		}
		
		mSystemName = "Android";
		mSystemVersion = Build.VERSION.RELEASE;
		mDevice = Build.MANUFACTURER + "," + Build.MODEL + "," + Build.BRAND;
		
		Configuration configuration = context.getResources().getConfiguration();
		mCountry = configuration.locale.getCountry();
		mLanguage = configuration.locale.getLanguage();
		mOperator = configuration.mcc + "," + configuration.mnc;
	}

	private List<NameValuePair> defaultParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("uid", mUid));
		params.add(new BasicNameValuePair("sid", mSid));
		params.add(new BasicNameValuePair("appName", mAppName));
		params.add(new BasicNameValuePair("appVersion", mAppVersion));
		params.add(new BasicNameValuePair("systemName", mSystemName));
		params.add(new BasicNameValuePair("systemVersion", mSystemVersion));
		params.add(new BasicNameValuePair("country", mCountry));
		params.add(new BasicNameValuePair("language", mLanguage));
		params.add(new BasicNameValuePair("device", mDevice));
		params.add(new BasicNameValuePair("operator", mOperator));
		return params;
	}

	private static class AnalyticsClientTask extends
			AsyncTask<HttpUriRequest, Void, Void> {

		@Override
		protected Void doInBackground(HttpUriRequest... request) {
			try {
				SchemeRegistry schemeRegistry = new SchemeRegistry();
		        schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
		        HttpParams params = new BasicHttpParams();
		        ClientConnectionManager clientConnectionManager = new ThreadSafeClientConnManager(params, schemeRegistry);
		        HttpClient httpclient = new DefaultHttpClient(clientConnectionManager, params);
				httpclient.execute(request[0]);
				Log.d("Analytics", "Submitted to server");
			} catch (Exception e) {
				Log.e("Analytics", "Caught error sending request: "
						+ e.getClass().getName() + " : " + e.getMessage());
			}
			return null;
		}
	}
	
	private static class EasySSLSocketFactory implements SocketFactory, LayeredSocketFactory {
		 
	    private SSLContext sslcontext = null;
	 
	    private static SSLContext createEasySSLContext() throws IOException {
	        try {
	            SSLContext context = SSLContext.getInstance("TLS");
	            context.init(null, new TrustManager[] { new EasyX509TrustManager(null) }, null);
	            return context;
	        } catch (Exception e) {
	            throw new IOException(e.getMessage());
	        }
	    }
	 
	    private SSLContext getSSLContext() throws IOException {
	        if (this.sslcontext == null) {
	            this.sslcontext = createEasySSLContext();
	        }
	        return this.sslcontext;
	    }
	 
	    /**
	     * @see org.apache.http.conn.scheme.SocketFactory#connectSocket(java.net.Socket, java.lang.String, int,
	     *      java.net.InetAddress, int, org.apache.http.params.HttpParams)
	     */
	    public Socket connectSocket(Socket sock, String host, int port, InetAddress localAddress, int localPort,
	            HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
	        int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
	        int soTimeout = HttpConnectionParams.getSoTimeout(params);
	        InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
	        SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock : createSocket());
	 
	        if ((localAddress != null) || (localPort > 0)) {
	            // we need to bind explicitly
	            if (localPort < 0) {
	                localPort = 0; // indicates "any"
	            }
	            InetSocketAddress isa = new InetSocketAddress(localAddress, localPort);
	            sslsock.bind(isa);
	        }
	 
	        sslsock.connect(remoteAddress, connTimeout);
	        sslsock.setSoTimeout(soTimeout);
	        return sslsock;
	 
	    }
	 
	    /**
	     * @see org.apache.http.conn.scheme.SocketFactory#createSocket()
	     */
	    public Socket createSocket() throws IOException {
	        return getSSLContext().getSocketFactory().createSocket();
	    }
	 
	    /**
	     * @see org.apache.http.conn.scheme.SocketFactory#isSecure(java.net.Socket)
	     */
	    public boolean isSecure(Socket socket) throws IllegalArgumentException {
	        return true;
	    }
	 
	    /**
	     * @see org.apache.http.conn.scheme.LayeredSocketFactory#createSocket(java.net.Socket, java.lang.String, int,
	     *      boolean)
	     */
	    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
	            UnknownHostException {
	        return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
	    }
	 
	    // -------------------------------------------------------------------
	    // javadoc in org.apache.http.conn.scheme.SocketFactory says :
	    // Both Object.equals() and Object.hashCode() must be overridden
	    // for the correct operation of some connection managers
	    // -------------------------------------------------------------------
	 
	    public boolean equals(Object obj) {
	        return ((obj != null) && obj.getClass().equals(EasySSLSocketFactory.class));
	    }
	 
	    public int hashCode() {
	        return EasySSLSocketFactory.class.hashCode();
	    }
	 
	}
	
	private static class EasyX509TrustManager implements X509TrustManager {
		 
	    private X509TrustManager standardTrustManager = null;
	 
	    /**
	     * Constructor for EasyX509TrustManager.
	     */
	    public EasyX509TrustManager(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {
	        super();
	        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	        factory.init(keystore);
	        TrustManager[] trustmanagers = factory.getTrustManagers();
	        if (trustmanagers.length == 0) {
	            throw new NoSuchAlgorithmException("no trust manager found");
	        }
	        this.standardTrustManager = (X509TrustManager) trustmanagers[0];
	    }
	 
	    /**
	     * @see javax.net.ssl.X509TrustManager#checkClientTrusted(X509Certificate[],String authType)
	     */
	    public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
	        standardTrustManager.checkClientTrusted(certificates, authType);
	    }
	 
	    /**
	     * @see javax.net.ssl.X509TrustManager#checkServerTrusted(X509Certificate[],String authType)
	     */
	    public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
	        if ((certificates != null) && (certificates.length == 1)) {
	            certificates[0].checkValidity();
	        } else {
	            standardTrustManager.checkServerTrusted(certificates, authType);
	        }
	    }
	 
	    /**
	     * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
	     */
	    public X509Certificate[] getAcceptedIssuers() {
	        return this.standardTrustManager.getAcceptedIssuers();
	    }
	 
	}
}
