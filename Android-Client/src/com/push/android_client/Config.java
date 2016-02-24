package com.push.android_client;

public interface Config {

	// Replace with your local ip. Donot use local host coz it will point to the
	// local host of your device.
	static final String APP_SERVER_URL = "http://192.168.5.88:8080/GCM-App-Server/GCMNotification?shareRegId=1";
	// Replace with your project number on google developer console
	static final String GOOGLE_PROJECT_ID = "123456789012";
	static final String MESSAGE_KEY = "message";

}
