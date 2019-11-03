package tn.esprit.crm.impl;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.util.Properties;

public class EmailClient {
  private static final String senderEmail = "mohamedali.mezni@esprit.tn";//change with your sender email
  private static final String senderPassword = "monia@2009";//change with your sender password

  public static void sendAsHtml(String to, String title, String html) throws MessagingException {
      System.out.println("Sending email to " + to);

      Session session = createSession();

      //create message using session
      MimeMessage message = new MimeMessage(session);
      prepareEmailMessage(message, to, title, html);

      //sending message
      Transport.send(message);
      System.out.println("Done");
  }
  
  public static void sendAsHtmlWithAttachement(String to, String title, String html, String PathfileName) throws MessagingException {
      System.out.println("Sending email to " + to);

      Session session = createSession();

      //create message using session
      MimeMessage message = new MimeMessage(session);
      prepareEmailMessageWithAttachement(message, to, title, html,PathfileName);

      //sending message
      Transport.send(message);
      System.out.println("Done");
  }

  
  private static void prepareEmailMessageWithAttachement(MimeMessage message, String to, String title, String html,String PathfileName)
          throws MessagingException {
      
      message.setFrom(new InternetAddress(senderEmail));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
      message.setSubject(title);
      // Create the message part
      BodyPart messageBodyPart = new MimeBodyPart();

      messageBodyPart.setContent(html, "text/html; charset=utf-8");
      // Now set the actual message
  //    messageBodyPart.setText(html);

     // Create a multipar message
      Multipart multipart = new MimeMultipart();

      // Set text message part
      multipart.addBodyPart(messageBodyPart);

      // Part two is attachment
      messageBodyPart = new MimeBodyPart();
      String filename = PathfileName;
      DataSource source = new FileDataSource(filename);
      messageBodyPart.setDataHandler(new DataHandler(source));
      messageBodyPart.setFileName(filename);
      multipart.addBodyPart(messageBodyPart);

      // Send the complete message parts
      message.setContent(multipart);

     
  }
  
  private static void prepareEmailMessage(MimeMessage message, String to, String title, String html)
          throws MessagingException {
      message.setContent(html, "text/html; charset=utf-8");
      message.setFrom(new InternetAddress(senderEmail));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
      message.setSubject(title);
  }
  
  

  private static Session createSession() {
      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");//Outgoing server requires authentication
      props.put("mail.smtp.starttls.enable", "true");//TLS must be activated
      props.put("mail.smtp.host", "smtp.gmail.com"); //Outgoing server (SMTP) - change it to your SMTP server
      props.put("mail.smtp.port", "587");//Outgoing port

      Session session = Session.getInstance(props, new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
              return new PasswordAuthentication(senderEmail, senderPassword);
          }
      });
      return session;
  }

  
}

