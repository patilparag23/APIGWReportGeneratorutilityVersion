package processor;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import reportGenerator.APIGWReport;
import sendMail.SendMail;

public class MainClass {

	private final static Logger logger = Logger.getLogger(MainClass.class);

	private static Properties localProperties;
	private static String logPath = "";
	private static String splittedFilePath="";

	public static void main(String[] args) throws Exception {

		logger.info("Main Process Starts:");
		
		new MainClass().initializeVariables();
///
File inputLoc = new File(logPath);
if (inputLoc.exists()) {
	FileUtils.cleanDirectory(inputLoc);
	}

//	// downloading the log file from mars to local
	SCPTransfer.scpFileTransferWithConsoleLog();

		// amikbchk@1

		// extracting the downloaded tar.gz file
		//Extractor.tarGZExtractor();

		// splitting the audit.log file into small chunk files
FileSplitter.splitFiles(logPath,
		splittedFilePath);

		// generating the report based on small chunk files
		String reportLocationName = APIGWReport.initializeReport("live");

		if (reportLocationName != null) {
			BasicConfigurator.configure();
			SendMail sendMail = new SendMail();
			sendMail.sendMail(reportLocationName);
		}

		logger.info("Main Process Ends:");
	}

	private  void initializeVariables() {
		try {

			localProperties = new Properties();
			 InputStream inStream=getClass().getClassLoader().getResourceAsStream("api_GW_Config.properties");
		     		
			localProperties
					.load(inStream);
			logPath = localProperties.get("log.files.path").toString();
			System.out.println(logPath);
			splittedFilePath=localProperties.get("splitted_file_path").toString();
			System.out.println(splittedFilePath);
			System.out.println("Done With splitting");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
