package processor

import java.util.logging.Logger;

import groovy.time.Duration;
import groovy.time.TimeCategory
import groovy.time.TimeDuration

class SCPTransfer {
	
	//private final static Logger LOGGER=Logger.getLogger(SCPTransfer.class)
    
    public static void main(String[] args) {
        
        SCPTransfer.scpFileTransferWithConsoleLog();
    }
    
    
   /* def static scpFileTransfer() {
		def timeStart = new Date()
		LOGGER.info("start time:"+timeStart)
		def sout = new StringBuffer(), serr = new StringBuffer()
		//def proc ='/home/cts1/reddy/logs/script.sh'.execute()
		def proc='/home/cts1/reddy/logs/script_mars.sh'.execute()
		proc.waitForProcessOutput(sout, serr);
		proc.waitForOrKill(1000)
		println sout
		def timeStop = new Date()
		LOGGER.info("end time:"+timeStop)
		TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
		LOGGER.info("SCP download duration"+duration);
     }*/
	
	def static scpFileTransferWithConsoleLog() {
		println("in scp method for scp file transfer initiated");
		def timeStart = new Date()
		//LOGGER.info("start time:"+timeStart)
		def process=new ProcessBuilder("/home/cts1/reddy/logs/script_mars_20160203.sh").redirectErrorStream(true).start()
		process.inputStream.eachLine {println it}
		def timeStop = new Date()
		/*LOGGER.info("end time:"+timeStop)
		TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
		LOGGER.info("SCP download duration"+duration);*/
		println("in scp method for scp file transfer completed");
		
	}

}
