package com.visara.transportationweb.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

	private static FirebaseMessaging firebaseMessagingInstance;

	@Bean
	public FirebaseMessaging firebaseMessaging() throws IOException {
		if (firebaseMessagingInstance == null) {
			synchronized (FirebaseConfig.class) {
				if (firebaseMessagingInstance == null) {
					initializeFirebaseApp();
					firebaseMessagingInstance = FirebaseMessaging.getInstance();
				}
			}
		}
		return firebaseMessagingInstance;
	}

	private void initializeFirebaseApp() throws IOException {
		String filePath = "src/main/resources/static/serviceac/visaratransportationproject-firebase-adminsdk-agtvv-f97118794e.json";
		FileInputStream serviceAccount = new FileInputStream(filePath);

		FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();

		if (FirebaseApp.getApps().isEmpty()) {
			FirebaseApp.initializeApp(options);
		}
	}
}
