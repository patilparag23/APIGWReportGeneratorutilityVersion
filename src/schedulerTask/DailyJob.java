package schedulerTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import reportGenerator.APIGWReport;
import sendMail.SendMail;

import com.cts.sftp.service.SFTPImpl;

public class DailyJob implements Job {
	
	static String logFilePath="";
	private static Properties localProperties;
	private final static String CLASS_NAME = DailyJob.class.getName();
	private final static String JOB_NAME = "DailyJob";
	private final static String JOB_GROUP = "DailyJob";
	private final static Logger LOGGER = Logger.getLogger(APIGWReport.class.getName());
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException
    {
		 	BasicConfigurator.configure();
			String remoteFolderLocation = "/tmp/logs/APIGw_logs";
			String localFolderLocation="logFiles";
			localProperties = new Properties();
		    try {
				localProperties.load(new FileReader("api_GW_Config.properties"));
			} catch (FileNotFoundException e) {
				LOGGER.error("File not found Exception in DailyJob"+e.getMessage());
			} catch (IOException e) {
				LOGGER.error("IO Exception in DailyJob"+e.getMessage());
			}
		    logFilePath=localProperties.get("log.files.path").toString();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");    
		    String todayDate=df.format(new Date());
		    logFilePath=logFilePath+"/"+todayDate;
		    File f1=new File(logFilePath); 
		    localFolderLocation=localFolderLocation+"/"+todayDate;
		   
		    if(f1.exists()){
		    	deleteDir(f1);
		    }   
		    f1.mkdir();
			
			SFTPImpl impl=new SFTPImpl();
			LOGGER.info("localFolderLocation:: "+localFolderLocation);
			boolean downloadStatus=impl.downloadFiles(remoteFolderLocation, localFolderLocation);
			LOGGER.info(" downloadStatus  is :::"+downloadStatus);
			if(downloadStatus){
				APIGWReport report= new APIGWReport();
				report.startGeneratingReport();
				}
			else{
				SendMail sendMail=new SendMail();;
				sendMail.sendMailWithoutAttachment();
				LOGGER.info(" Audit log File download failed. So, sending mail without attachment.");
			}
    }
	public static DailyJob getJob() {
        return new DailyJob();
    }
	
	public static String getJobName() {
	        return JOB_NAME;
	    }
	public static String getJobGroup() {
        return JOB_GROUP;
    }
	public static boolean deleteDir(File dir) {
	      if (dir.isDirectory()) {
	         String[] children = dir.list();
	         for (int i = 0; i < children.length; i++) {
	            boolean success = deleteDir
	            (new File(dir, children[i]));
	            if (!success) {
	               return false;
	            }
	         }
	      }
	      return dir.delete();
	  }
	 public static void main(String[] args) {

		 	BasicConfigurator.configure();
			String remoteFolderLocation = "/tmp/logs/APIGw_logs";
			String localFolderLocation="logFiles";
			localProperties = new Properties();
		    try {
				localProperties.load(new FileReader("live_api_GW_Config.properties"));
			} catch (FileNotFoundException e) {
				LOGGER.error("File not found Exception in DailyJob"+e.getMessage());
			} catch (IOException e) {
				LOGGER.error("IO Exception in DailyJob"+e.getMessage());
			}
		    logFilePath=localProperties.get("log.files.path").toString();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");    
		    String todayDate=df.format(new Date());
		    logFilePath=logFilePath+"/"+todayDate;
		    File f1=new File(logFilePath); 
		    localFolderLocation=localFolderLocation+"//"+todayDate;
		   
		    if(f1.exists()){
		    	deleteDir(f1);
		    }   
		    f1.mkdir();
			
			SFTPImpl impl=new SFTPImpl();
			LOGGER.info("localFolderLocation:: "+localFolderLocation);
			boolean downloadStatus=impl.downloadFiles(remoteFolderLocation, localFolderLocation);
			if(downloadStatus){
				APIGWReport report= new APIGWReport();
				report.startGeneratingReport();
				}
			LOGGER.info(downloadStatus+":::: is the downloadStatus :)");
	    }    


}
