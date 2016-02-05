package schedulerTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import reportGenerator.APIGWReport;

import com.cts.sftp.service.SFTPImpl;

public class StartReport implements Runnable {
	private static Properties localProperties;
	static String logFilePath="";
	private final static Logger LOGGER = Logger.getLogger(APIGWReport.class.getName());
	static {
		try { // Write the workbook in file system
			localProperties = new Properties();
		    localProperties.load(new FileReader("live_api_GW_Config.properties"));
		    logFilePath=localProperties.get("log.files.path").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	@Override
	public void run() {
		
		String remoteFolderLocation = "/tmp/logs/APIGw_logs";
		String localFolderLocation="logFiles";
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");    
	    String todayDate=df.format(new Date());
	    logFilePath=logFilePath+"/"+todayDate;
	    File f1=new File(logFilePath); 
	    localFolderLocation=localFolderLocation+todayDate;
	   
	    if(f1.exists()){
	    	deleteDir(f1);
	    }   
	    f1.mkdir();
		
		SFTPImpl impl=new SFTPImpl();
		boolean downloadStatus=impl.downloadFiles(remoteFolderLocation, localFolderLocation);
		if(downloadStatus){
			APIGWReport apiGWReport= new APIGWReport();
			apiGWReport.startGeneratingReport();
			}
		LOGGER.info(downloadStatus+":::: is the downloadStatus :)");
	}
	public static void main(String a[]){
		BasicConfigurator.configure();
		String remoteFolderLocation = "/tmp/logs/APIGw_logs";
		String localFolderLocation="logFiles/";
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");    
	    String todayDate=df.format(new Date());
	    logFilePath=logFilePath+"/"+todayDate;
	    File f1=new File(logFilePath); 
	    localFolderLocation=localFolderLocation+todayDate;
	   
	    if(f1.exists()){
	    	deleteDir(f1);
	    }   
	    f1.mkdir();
		
		SFTPImpl impl=new SFTPImpl();
		boolean downloadStatus=impl.downloadFiles(remoteFolderLocation, localFolderLocation);
		if(downloadStatus){
			APIGWReport apiGWReport= new APIGWReport();
			apiGWReport.startGeneratingReport();
			}
		LOGGER.info(downloadStatus+":::: is the downloadStatus :)");
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
}
