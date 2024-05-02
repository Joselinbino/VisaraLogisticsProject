/*
 * package com.visara.transportationweb.ServiceImpl;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.mail.SimpleMailMessage; import
 * org.springframework.mail.javamail.JavaMailSender; import
 * org.springframework.stereotype.Service;
 * 
 * import com.visara.transportationweb.Service.EmailService;
 * 
 * @Service public class EmailServiceImpl implements EmailService {
 * 
 * @Autowired private JavaMailSender emailSender;
 * 
 * @Override public void sendUserEmail(String username, String to) { String
 * subject = "Registration Successful - Visara Tech";
 * 
 * String text = "Dear," + username +
 * "\n\nWe hope this message finds you well. We wanted to provide you with your user ID for your goldpal account. This ID is essential for accessing your account, and we recommend keeping it safe and confidential."
 * + "\n\nYour User ID: " +
 * "\n\nIn the event that you forget your password or need to recover your account, this user ID will be a key piece of information. Please ensure you do not share it with anyone else to maintain the security of your account."
 * ; sendSimpleMessage(to, subject, text); }
 * 
 * @Override public void sendSimpleMessage(String to, String subject, String
 * text) { SimpleMailMessage message = new SimpleMailMessage();
 * message.setTo(to); message.setSubject(subject); message.setText(text);
 * emailSender.send(message); }
 * 
 * }
 */