package pl.devcrowd.app.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import pl.devcrowd.app.utils.DebugLog;

public final class HttpHelper {

	private HttpHelper() {
	}

	public static String getContentFromUrl(String stringUrl) {
		String response;
		try {
			URL url = new URL(stringUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			response = readStream(con.getInputStream());
		} catch (IOException e) {
			DebugLog.e("IOException during getting content from url");
			response = "";
		}
		return response;

	}

	private static String readStream(InputStream in) {

		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			DebugLog.e("IOException during read stream from url");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					DebugLog.e("IOException during read stream from url");
				}
			}
		}
		return sb.toString();
	}
}
