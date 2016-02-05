package sendMail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import processor.WritingInExcel;

public class SendMail {
	private static Properties localProperties;
	private final static Logger LOGGER = Logger.getLogger(SendMail.class);
	static {
		localProperties = new Properties();
		try {
			InputStream inStream = SendMail.class.getClassLoader()
					.getResourceAsStream("api_GW_Config.properties");
			localProperties.load(inStream);
		} catch (FileNotFoundException e) {
			LOGGER.info("FileNotFoundException thrown in sendMail::: "
					+ e.getMessage());
		} catch (IOException e) {
			LOGGER.info("IOException thrown in sendMail::: " + e.getMessage());
		}
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		BasicConfigurator.configure();
		SendMail sendMail = new SendMail();
		sendMail.sendMail("");
	}

	public boolean sendMail(String reportLocation)
			throws FileNotFoundException, IOException {

		try {
			

		String fileName = localProperties.get("mail.report.file.name")
				.toString();
			String filePath = localProperties.get("mail.report.file.path")
					.toString();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String todayDate = df.format(new Date());
			filePath = filePath + "\\" + fileName;

			File file = new File(reportLocation);
			if (file.exists()) {
				if (file.length() > 3072) {
					LOGGER.info("File " + fileName + " on path " + filePath
							+ " will be sent as attachment in the mail.");
					sendMailWithAttachment(reportLocation);
				} else {
					LOGGER.info("File "
							+ fileName
							+ " on path "
							+ filePath
							+ " has size less than 10 kb. so, will send a WIP mail.");
					sendMailWithoutAttachment();
				}
			} else {
				LOGGER.info("File " + fileName + " was not found on the path: "
						+ filePath + ". so, will send a WIP mail.");
				sendMailWithoutAttachment();
			}
		} catch (Exception e) {
			LOGGER.info("Exception thrown in sendMail::: " + e.getMessage());
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
	protected static Session createSession() throws FileNotFoundException,
			IOException {
		Properties props = new Properties();
		props.put("mail.smtps.host", localProperties.get("mail.smtp.host"));
		props.put("mail.smtps.auth", localProperties.get("mail.smtp.auth"));
		props.put("mail.smtps.starttls.enable", "true");

		Authenticator auth = null;
		auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(localProperties.get(
						"mail.smtp.user").toString(), localProperties.get(
						"mail.smtp.password").toString());
			}
		};

		Session session = Session.getInstance(props, auth);
		return session;
	}

	public boolean sendMailWithAttachment(String reportLocation)
			throws FileNotFoundException, IOException {

		try {
			BasicConfigurator.configure();
			Session session = createSession();

			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			//String fileName=WritingInExcel.getReportName().toString()+".xlsx";
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Calendar cal = Calendar.getInstance();
			cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			System.out.println(dateFormat.format(cal.getTime()));
			String fileName="API GW report : "+cal.getTime()+".xlsx";
			// Set Subject: header field
			message.setSubject("API Gateway Audit log "+fileName);

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();
			String messageText = " Hi,\n \n "
					+ "The audit log has been processed from the API Gateway audit log and report has been generated.\n"
					+ "Please find the attachment.\n" + "\n" + "Thanks,\n"
					+ "O2 Offshore Pune \n";
			// Fill the message
			messageBodyPart.setText(messageText);

			Multipart multipart = new MimeMultipart();

			multipart.addBodyPart(messageBodyPart);

			messageBodyPart = new MimeBodyPart();
			//String fileName=WritingInExcel.getReportName().toString()+".xlsx";
//			String fileName = localProperties.get("mail.report.file.name")
//					.toString();
			String filePath = localProperties.get("mail.report.file.path")
					.toString();
			System.out.println("filePath::" + filePath);
			String recipientsMailIds[] = (localProperties
					.get("mail.recipients").toString()).split(":");

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//	String todayDate = df.format(new Date());
			filePath = filePath + "\\" + fileName;
			DataSource source = new FileDataSource(reportLocation);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);
			for (String mailId : recipientsMailIds) {
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(mailId));
			}

			Transport transport = session.getTransport("smtps");
			transport.connect(localProperties.get("mail.smtp.host").toString(),
					localProperties.get("mail.smtp.user").toString(),
					localProperties.get("mail.smtp.password").toString());
			transport.sendMessage(message, message.getAllRecipients());

			LOGGER.info(" Audit log report Mail sent successfully..");

		} catch (MessagingException mex) {
			LOGGER.info("MessagingException thrown in sendMail::: "
					+ mex.getMessage());
			mex.printStackTrace();
			return false;
		} catch (Exception e) {
			LOGGER.info("Exception thrown in sendMail::: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean sendMailWithoutAttachment() {

		try {
			BasicConfigurator.configure();
			Session session = createSession();

			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set Subject: header field
			message.setSubject("API Gateway Audit log report");

			// Create the message part
			String messageText = " Hi,\n \n "
					+ "We are facing some issues with the audit report generation. So, we are unable to send the report.\n"
					+ "We will get back to you shortly with the report. .\n"
					+ "\n"
					+ "Please, do let us know in case of any concerns.\n"
					+ "\n" + "Thanks,\n" + "O2 Offshore Pune \n";

			message.setText(messageText);
			String recipientsMailIds[] = (localProperties
					.get("mail.recipients").toString()).split(":");
			for (String mailId : recipientsMailIds) {
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(mailId));
			}
			Transport transport = session.getTransport("smtps");
			transport.connect(localProperties.get("mail.smtp.host").toString(),
					localProperties.get("mail.smtp.user").toString(),
					localProperties.get("mail.smtp.password").toString());
			transport.sendMessage(message, message.getAllRecipients());
			LOGGER.info("Without Audit log report, Mail sent successfully..");

		} catch (MessagingException mex) {
			LOGGER.info("MessagingException thrown in sendMail::: "
					+ mex.getMessage());
			mex.printStackTrace();
			return false;
		} catch (Exception e) {
			LOGGER.info("Exception thrown in sendMail::: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
}