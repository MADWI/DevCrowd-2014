package pl.devcrowd.app.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import pl.devcrowd.app.R;
import pl.devcrowd.app.models.Presentation;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class HttpPostData extends AsyncTask<Void, Void, Boolean> {

	private Context mContext;
	private String action;
	private float topic_grade;
	private float overall_grade;
	private Presentation presenation;
	private String email;

	public HttpPostData(Context mContext, String action, float topic_grade,
			float overall_grade, Presentation presenation, String email) {
		super();
		this.mContext = mContext;
		this.action = action;
		this.topic_grade = topic_grade;
		this.overall_grade = overall_grade;
		this.presenation = presenation;
		this.email = email;
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		return postData(action, topic_grade, overall_grade, presenation, email);
	}

	private boolean postData(String action, float topic_grade,
			float overall_grade, Presentation presenation, String email) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				"http://2014.devcrowd.pl/mad-api/oceny.php");

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			nameValuePairs.add(new BasicNameValuePair("action", action));
			nameValuePairs.add(new BasicNameValuePair("prelegent_grade", String
					.valueOf(overall_grade)));
			nameValuePairs.add(new BasicNameValuePair("presentation_grade",
					String.valueOf(topic_grade)));
			nameValuePairs.add(new BasicNameValuePair("presentation_name",
					presenation.getTitle()));
			nameValuePairs.add(new BasicNameValuePair("email", email));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity);

			if (result.contains("success")) {
				return true;
			}

		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result) {
			Toast.makeText(
					mContext.getApplicationContext(),
					mContext.getResources().getString(
							R.string.success_voting_toast_text),
					Toast.LENGTH_SHORT).show();
		}
	}

}
