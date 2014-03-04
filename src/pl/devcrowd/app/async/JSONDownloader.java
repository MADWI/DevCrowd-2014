package pl.devcrowd.app.async;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import pl.devcrowd.app.R;
import pl.devcrowd.app.interfaces.JSONResponseCallback;

import android.content.Context;
import android.os.AsyncTask;

public class JSONDownloader extends AsyncTask<String, Void, String> {

	private Context mContext;
	private String response;
	private JSONResponseCallback mJsonResponseCallback;

	public JSONDownloader(Context mContext,
			JSONResponseCallback jsonResponseCallback) {
		this.mContext = mContext;
		this.mJsonResponseCallback = jsonResponseCallback;
		response = null;
	}

	private String getJSONfromUrl(String url) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;
			HttpGet httpGet = new HttpGet(url);
			httpResponse = httpClient.execute(httpGet);
			httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	@Override
	protected String doInBackground(String... params) {
		String response = getJSONfromUrl(params[0]);
		return response;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mJsonResponseCallback.asyncResponse(mContext.getResources().getString(
				R.string.waitforit));
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		mJsonResponseCallback.asyncResponse(result);
	}

}
