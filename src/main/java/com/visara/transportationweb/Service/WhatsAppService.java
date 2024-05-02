package com.visara.transportationweb.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.visara.transportationweb.Config.TwilioConfiguration;

import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

	private final TwilioConfiguration twilioConfiguration;

	public WhatsAppService(TwilioConfiguration twilioConfiguration) {
		this.twilioConfiguration = twilioConfiguration;
		Twilio.init(twilioConfiguration.getAccountSid(), twilioConfiguration.getAuthToken());
	}

	/*
	 * public void sendWhatsAppMessage(String to, String message) {
	 * Message.creator(new PhoneNumber(to), // To WhatsApp number new
	 * PhoneNumber(twilioConfiguration.getFromWhatsAppNumber()), // From Twilio
	 * WhatsApp number message).create(); }
	 */

	public void sendWhatsAppMessage(String to, String message) {
		Message.creator(new PhoneNumber(to), // To WhatsApp number
				new PhoneNumber(twilioConfiguration.getFromWhatsAppNumber()), // From Twilio WhatsApp number
				message).create();

		System.out.println("Message sent successfully!");
	}
}
