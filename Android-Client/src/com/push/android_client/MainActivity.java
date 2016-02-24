package com.push.android_client;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	static final String TAG = "Main Activity";
	ShareExternalServer appUtil;
	String regId;
	AsyncTask shareRegidTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		String message = intent.getStringExtra(RegisterActivity.REG_ID);
		TextView tv = (TextView) findViewById(R.id.content);
		tv.setText("Registration id is : " + message);
		appUtil = new ShareExternalServer();

		regId = getIntent().getStringExtra("regId");
		Log.d("MainActivity", "regId: " + regId);

		final Context context = this;
		shareRegidTask = new AsyncTask() {

			@Override
			protected Object doInBackground(Object... params) {
				Log.i(TAG, "Sharing reg id with server");
				String result = appUtil.shareRegIdWithAppServer(context, regId);
				return result;
			}

			protected void onPostExecute(String result) {
				shareRegidTask = null;
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
			}

		};
		shareRegidTask.execute(null, null, null);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle app bar item clicks here. The app bar
		// automatically handles clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_main, container, false);
			return rootView;
		}
	}
}
