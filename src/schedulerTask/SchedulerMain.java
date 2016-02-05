package schedulerTask;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import reportGenerator.APIGWReport;


import com.cts.sftp.service.ConnectionManager;
import com.cts.sftp.service.SFTPImpl;

public class SchedulerMain {
	private final static Logger LOGGER = Logger.getLogger(SchedulerMain.class.getName());
	public static void main(String args[]) throws Exception {
		 
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);	
		 Date aDate = new Date();
		 Calendar with = Calendar.getInstance();
		 with.setTime(aDate);

		 int hour = with.get(Calendar.HOUR_OF_DAY);
		 int intDelayInHour = hour < 14 ? 14 - hour : 14 - (hour - 14);

		 System.out.println("Current Hour: "+hour);
		 System.out.println("Comuted Delay for next 4 AM: "+intDelayInHour);
		 scheduler.scheduleAtFixedRate(new StartReport(), intDelayInHour, 24, TimeUnit.HOURS);
	
	}
	private static void startDownloadingLogFiles(){
		
		 ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);	
		 Date aDate = new Date();
		 Calendar with = Calendar.getInstance();
		 with.setTime(aDate);

		 int hour = with.get(Calendar.HOUR_OF_DAY);
		 int intDelayInHour = hour < 4 ? 4 - hour : 24 - (hour - 4);

		 System.out.println("Current Hour: "+hour);
		 System.out.println("Comuted Delay for next 4 AM: "+intDelayInHour);
		 scheduler.scheduleAtFixedRate(new StartReport(), intDelayInHour, 24, TimeUnit.HOURS);
		 BasicConfigurator.configure();
			Timer timer = new Timer();		
			TimerTask action = new TimerTask() {
				        public void run() {
				        	startDownloadingLogFiles();
				        }
					};
				    LOGGER.info("Will call again after i hour :: "); 
				    timer.schedule(action, 3600000);
	}
	

}
