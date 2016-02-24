package com.push.android_client;

import java.io.IOException;

import com.google.android.gms.iid.InstanceID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	Context context;
	Button btnGCMRegister;
	Button btnAppShare;
	String regId;
	String instanceID;
	InstanceID instance;

	public static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";

	static final String TAG = "Register Activity";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		context = getApplicationContext();
		btnGCMRegister = (Button) findViewById(R.id.btnGCMRegister);
		btnGCMRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(regId)) {
					regId = registerGCM();
					Log.d("RegisterActivity", "GCM RegId: " + regId);
				} else {
					Log.d(TAG, "GCM RegId already available: " + regId);
					Toast.makeText(getApplicationContext(), "Already Registered with GCM Server!", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		btnAppShare = (Button) findViewById(R.id.btnAppShare);
		btnAppShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(regId)) {
					Toast.makeText(getApplicationContext(), "RegId is empty!", Toast.LENGTH_LONG).show();
				} else {
					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					intent.putExtra("regId", regId);
					Log.d("RegisterActivity", "onClick of Share: Before starting main activity.");
					startActivity(intent);
					finish();
					Log.d("RegisterActivity", "onClick of Share: After finish.");
				}
			}
		});

	}

	public String registerGCM() {
		instance = InstanceID.getInstance(this);
		regId = getRegistrationID(context);
		if (TextUtils.isEmpty(regId)) {

			registerInBackground();

			Log.d("RegisterActivity", "registerGCM - successfully registered with GCM server - regId: " + regId);
		} else {
			Toast.makeText(getApplicationContext(), "RegId already available. RegId: " + regId, Toast.LENGTH_LONG)
					.show();
		}
		return regId;
	}

	public String getRegistrationID(Context context) {
		final SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		String registrationId = prefs.getString(REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;

	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.d("RegisterActivity", "I never expected this! Going down, going down!" + e);
			throw new RuntimeException(e);
		}

	}

	@SuppressWarnings("unchecked")
	private void registerInBackground() {
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... params) {
				String msg = "";
				System.out.println("we are here on 116");
				try {
					if (instance == null) {
						instance = InstanceID.getInstance(context);
					}
					regId = InstanceID.getInstance(context).getToken(Config.GOOGLE_PROJECT_ID, "GCM");
					Log.i("RegisterActivity", "registerInBackground - regId: " + regId);
					msg = "Device registered, registration ID=" + regId;

					storeRegistrationId(context, regId);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					Log.d("RegisterActivity", "Error: " + msg);
				}

				Log.d("RegisterActivity", "AsyncTask completed: " + msg);
				return msg;
			}

			protected void onPostExecute(String msg) {
				Toast.makeText(getApplicationContext(), "Registered with GCM Server." + msg, Toast.LENGTH_LONG).show();
			}

		}.execute(null, null, null);

	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(REG_ID, regId);
		editor.putInt(APP_VERSION, appVersion);
		editor.commit();
	}

}
