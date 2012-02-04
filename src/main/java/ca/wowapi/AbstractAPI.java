package ca.wowapi;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import ca.wowapi.exceptions.InvalidApplicationSignatureException;
import ca.wowapi.exceptions.NotModifiedException;
import ca.wowapi.exceptions.TooManyRequestsException;
import ca.wowapi.utils.Base64Converter;

public class AbstractAPI {

	private final Logger log = Logger.getLogger(this.getClass());

	public static final String REGION_US = "us";
	public static final String REGION_EU = "eu";

	private String publicKey;
	private String privateKey;

	public AbstractAPI() {

	}

	public AbstractAPI(String publicKey, String privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	public JSONObject getJSONFromRequest(String url) throws NotModifiedException, InvalidApplicationSignatureException, TooManyRequestsException {
		return this.getJSONFromRequest(url, 0);
	}

	public JSONObject getJSONFromRequest(String url, long lastModifiedDate) throws NotModifiedException, InvalidApplicationSignatureException, TooManyRequestsException {
		JSONObject jsonobject = null;

		String str = null;
		if (null != publicKey && null != privateKey) {
			str = this.getStringJSONFromRequestAuth(url, publicKey, privateKey, lastModifiedDate);
		} else {
			str = this.getStringJSONFromRequest(url, lastModifiedDate);
		}

		try {

			if (null != str) {
				jsonobject = new JSONObject(str);

				if (null != jsonobject && jsonobject.has("status")) {
					if (jsonobject.getString("status").equalsIgnoreCase("nok")) {
						if (jsonobject.getString("reason").equalsIgnoreCase("Invalid application signature.")) {
							log.error("Invalid application signatrue used when communicating with the Blizzard WOW API.");
							throw new InvalidApplicationSignatureException();
						} else if (jsonobject.getString("reason").contains("too many requests") || jsonobject.getString("reason").contains("Daily limit exceeded")) {
							log.error("Too many requests made against the Blizzard WOW API.");
							throw new TooManyRequestsException();
						}
					}

				}
			}
		} catch (JSONException e) {
			log.error("Error parsing JSON response from the Blizzard WOW API..");
			e.printStackTrace();
		}
		return jsonobject;
	}

	public String encode(String value) {
		try {
			return java.net.URLEncoder.encode(value, "UTF-8").replace("+", "%20");
		} catch (Exception e) {
			return value;
		}
	}

	public String getStringJSONFromRequest(String url) {
		String string = null;
		try {
			string = getStringJSONFromRequest(url, 0);
		} catch (NotModifiedException e) {
			// won't happen since we aren't passing the lastModified time
		}
		return string;
	}

	public String getStringJSONFromRequest(String url, long lastModified) throws NotModifiedException {

		String str = null;
		try {
			URL jURL = new URL(url);
			HttpURLConnection urlConnection = (HttpURLConnection) jURL.openConnection();
			urlConnection.setReadTimeout(30);
			if (lastModified != 0) {
				urlConnection.setIfModifiedSince(lastModified);
			}

			str = readJSONStream(urlConnection);
		} catch (SocketTimeoutException e) {
			log.trace("Socket timed out, returning null for response.");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return str;
	}

	public String getStringJSONFromRequestAuth(String url, String publicKey, String privateKey) {
		String string = null;
		try {
			string = getStringJSONFromRequestAuth(url, publicKey, privateKey, 0);
		} catch (NotModifiedException e) {
			// won't happen since we aren't passing the lastModified time
		}
		return string;
	}

	public String getStringJSONFromRequestAuth(String url, String publicKey, String privateKey, long lastModified) throws NotModifiedException {

		String UrlPath = null;
		if (url.contains("?")) {
			UrlPath = url.substring(url.indexOf("/api"), url.indexOf("?"));
		} else {
			UrlPath = url.substring(url.indexOf("/api"));
		}

		String str = null;
		try {
			URL jURL = new URL(url);
			HttpURLConnection urlConnection = (HttpURLConnection) jURL.openConnection();
			urlConnection.setReadTimeout(30);

			String fmtStr = "E, dd MMM yyyy HH:mm:ss";
			java.util.Date myDate = new java.util.Date();
			SimpleDateFormat sdf = new java.text.SimpleDateFormat(fmtStr);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			String dateStr = sdf.format(myDate) + " GMT";

			String stringToSign = urlConnection.getRequestMethod() + "\n" + dateStr + "\n" + UrlPath + "\n";
			String sig = generateHmacSHA1Signature(stringToSign, privateKey);
			try {
				urlConnection.setRequestProperty("Authorization", "BNET" + " " + publicKey + ":" + sig);
				urlConnection.setRequestProperty("Date", dateStr);

				if (lastModified != 0) {
					urlConnection.setIfModifiedSince(lastModified);
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}

			str = readJSONStream(urlConnection);

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}

		return str;
	}

	private String readJSONStream(HttpURLConnection urlConnection) throws IOException, NotModifiedException {
		String str = null;

		try {
			final char[] buffer = new char[0x1000];
			StringBuilder out = new StringBuilder();
			Reader in = null;
			if (urlConnection.getResponseCode() == 304) {
				throw new NotModifiedException();
			} else if (urlConnection.getResponseCode() < 400) {
				in = new InputStreamReader(urlConnection.getInputStream(), "UTF-8");
			} else {
				in = new InputStreamReader(urlConnection.getErrorStream(), "UTF-8");
			}

			int read;
			do {
				read = in.read(buffer, 0, buffer.length);
				if (read > 0) {
					out.append(buffer, 0, read);
				}
			} while (read >= 0);

			str = out.toString();
			in.close();
		} catch (SocketTimeoutException e) {
			log.trace("Socket timed out, returning null for response.");
		}

		return str;
	}

	private String generateHmacSHA1Signature(String data, String key) throws GeneralSecurityException, IOException {
		byte[] hmacData = null;
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(secretKey);
		hmacData = mac.doFinal(data.getBytes("UTF-8"));
		return Base64Converter.encode(hmacData);
	}

}
