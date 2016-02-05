package reportGenerator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import processor.WritingInExcel;
public class SendMail {
	private static Properties localProperties;

	
	
	
	private final static Logger LOGGER = Logger.getLogger(SendMail.class.getName());
	static{
		localProperties = new Properties();
	    try {
	    	InputStream inStream = SendMail.class.getClassLoader()
					.getResourceAsStream("api_GW_Config.properties");
			localProperties.load(inStream);
		} catch (FileNotFoundException e) {
			 LOGGER.info("FileNotFoundException thrown in sendMail::: "+e.getMessage());
		} catch (IOException e) {
			LOGGER.info("IOException thrown in sendMail::: "+e.getMessage());
		}
	}
	
	public static void main(String[] args) throws AddressException, MessagingException, FileNotFoundException, IOException {
		sendMail();
		//GoogleMail.Send("o2odcaccessoryshop", "o2odcaccessoryshop@123", "reddy90143@gmail.com", "test", "test bhai");
	}
	/*public static void main(String [] args) throws FileNotFoundException, IOException
	   {
		BasicConfigurator.configure();
	      // Recipient's email ID needs to be mentioned.
	      String to = "vishal.pandey2@cognizant.com";

	      // Sender's email ID needs to be mentioned
	      String from = "testmailid.o2.offshore@gmail.com";
	      Session session = createSession();
	      

	      try{
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);


	         // Set Subject: header field
	         message.setSubject("API Gateway Audit log report");

	         // Create the message part 
	         BodyPart messageBodyPart = new MimeBodyPart();
	         String messageText = " Hi,\n \n " +
	         		"The audit log has been processed from the API Gateway audit log and report has been generated.\n" +
	         		"Please find the attachment.\n" +
	         		"\n" +
	         		"Thanks,\n" +
	         		"O2 Offshore Pune \n";
	         // Fill the message
	         messageBodyPart.setText(messageText);
	         
	         Multipart multipart = new MimeMultipart();

	         multipart.addBodyPart(messageBodyPart);
	         
	         messageBodyPart = new MimeBodyPart();
	         String filename = "D:\\Vishal\\api gateway logs\\09072014\\Report.xlsx";
	         DataSource source = new FileDataSource(filename);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(filename);
	         multipart.addBodyPart(messageBodyPart);

	         // Send the complete message parts
	         message.setContent(multipart );
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	         
	         
	         Transport transport = session.getTransport("smtps");
	         String mfrom = "testmailid.o2.offshore@gmail.com";
	         transport.connect("smtp.gmail.com", mfrom, "nitapavis");
	         transport.sendMessage(message, message.getAllRecipients());

	         LOGGER.info("Mail sent successfully..");
	      }catch (MessagingException mex) {
	    	  LOGGER.info("MessagingException thrown in sendMail::: "+mex.getMessage());
	          mex.printStackTrace();
	      }
	      catch(Exception e){
	    	  LOGGER.info("Exception thrown in sendMail::: "+e.getMessage());
	    	  e.printStackTrace();
	     }
	   }
	*/
	public static boolean sendMail() throws FileNotFoundException, IOException
	   {

		BasicConfigurator.configure();
	      // Recipient's email ID needs to be mentioned.
	      //String to = "vishal.pandey2@cognizant.com";
		
		
		//String to = "kondareddy.l@cognizant.com";
	      
	      Session session = createSession();
	      
	      try{
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set Subject: header field
	         message.setSubject("API Gateway Audit log report");

	         // Create the message part 
	         BodyPart messageBodyPart = new MimeBodyPart();
	         String messageText = " Hi,\n \n " +
	         		"The audit log has been processed from the API Gateway audit log and report has been generated.\n" +
	         		"Please find the attachment.\n" +
	         		"\n" +
	         		"Thanks,\n" +
	         		"O2 Offshore Pune \n";
	         // Fill the message
	         messageBodyPart.setText(messageText);
	         
	         Multipart multipart = new MimeMultipart();

	         multipart.addBodyPart(messageBodyPart);
	         
	         messageBodyPart = new MimeBodyPart();
	       
	         
	         /*Address[] ia= new Address[1];
	         ia[0]=new InternetAddress(localProperties.get("mail.smtp.from.user").toString());
	         message.addFrom(ia);*/
	        //String fileName = localProperties.get("mail.report.file.name").toString();
	       // String fileName=WritingInExcel.getReportName();
	      //  System.out.println("reportname is "+fileName );
	         String filePath = localProperties.get("mail.report.file.path").toString();
	         String recipientsMailIds[]=(localProperties.get("mail.recipients").toString()).split(":");
	         DataSource source = new FileDataSource(filePath);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	     //    messageBodyPart.setFileName(fileName);
	         multipart.addBodyPart(messageBodyPart);

	         // Send the complete message parts
	         message.setContent(multipart );
	         for(String mailId: recipientsMailIds){
	        	   message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailId));
	         }
	         Transport transport = session.getTransport("smtps");
	         transport.connect("cognizant-com.mail.protection.outlook.com", "kondareddy.l@cognizant.com", "");
	         transport.sendMessage(message, message.getAllRecipients());

	         LOGGER.info("Mail sent successfully..");
	      }catch (MessagingException mex) {
	    	  LOGGER.info("MessagingException thrown in sendMail::: "+mex.getMessage());
	          mex.printStackTrace();
	          return false;
	      }
	      catch(Exception e){
	    	  LOGGER.info("Exception thrown in sendMail::: "+e.getMessage());
	    	  e.printStackTrace();
	    	  return false;
	     }
		return true;
	   }
	
	/**
	   * Create mail session.
	   * 
	   * @return mail session, may not be null.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	   */
	  protected static Session createSession() throws FileNotFoundException, IOException
	  {
	    Properties props = new Properties();
	    props.put("mail.smtps.host", localProperties.get("mail.smtp.host"));
	    props.put("mail.smtps.auth", localProperties.get("mail.smtp.auth"));
	    
	    Authenticator auth = null;
	      auth = new Authenticator()
	      {
	        protected PasswordAuthentication getPasswordAuthentication()
	        {
	          return new PasswordAuthentication("kondareddy.l@cognizant.com", "");
	        }
	      };
	    
	    Session session = Session.getInstance(props, auth);
	    return session;
	  }

	
}
