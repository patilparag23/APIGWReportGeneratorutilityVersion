
package processor

import groovy.time.TimeCategory
import groovy.time.TimeDuration


class RunSCPCommand {

	public static void main(String[] args) {
		/*RunSCPCommand.scpFileTransfer();
		 RunSCPCommand.antFileTransfer();*/
		//RunSCPCommand.scpFileTransferWithShellScript();
		scpFileTransferWithConsoleLog();
	}


	def static scpFileTransfer() {
		def sout = new StringBuffer(), serr = new StringBuffer()
		def consumeout=new StringBuffer(),consumeerr=new StringBuffer()
		try{
			def proc = 'echo Password123 | sudo -S -p \'\' scp -v /home/cts1/projects/Ecom_buildSrc/keys/ee-aws-dev20130517 dev@ecom-dev-demo00.obnubilate.co.uk:/opt/tomcat-master-8080/logs/probe.log /home/cts1/probe.log'.execute()

			def output= proc .in.text;
			println("Output : " + output);

			//proc.waitForProcessOutput(sout, serr);
			//proc.consumeProcessOutput(consumeout, consumeerr)

		}catch(Exception e){
			println "exception cameeeekdfdjkfjdkjfsdlkjfksjfksdjkf"
		}



		println "out> $sout err> $serr"
		println "consumeout> $consumeout consumeerr> $consumeerr"
		//println proc.text
		//echo equalexperts | sudo -S -p '' {your_command_here}
	}

	def static scpFileTransferWithShellScript() {
		def timeStart = new Date()
		println 'start time:'+timeStart


		def sout = new StringBuffer(), serr = new StringBuffer()

		def proc ='/home/cts1/reddy/logs/script.sh'.execute()

		//proc.consumeProcessOutput(sout, serr)
		
		
		
		String line;
		InputStream stdout = proc.getInputStream ()
		BufferedReader reader = new BufferedReader (new InputStreamReader(stdout))

		
		proc.waitForProcessOutput(sout, serr);
		proc.waitForOrKill(1000)
		println sout
		def timeStop = new Date()
		println 'end time:'+timeStop
		TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
		println duration

	}
	
	def static logSCPTransfer() {
		
		
		def process=new ProcessBuilder("/home/cts1/reddy/logs/script.sh").redirectErrorStream(true).start()
		process.inputStream.eachLine {println it}
		
		
	}

	def static antFileTransfer() {
		def ant = new AntBuilder();
		ant.scp(file:'dev@ecom-dev-demo00.obnubilate.co.uk:/opt/tomcat-master-8080/logs/probe.log',
		todir:"/home/cts1/reddy/logs",
		verbose:true,
		keyfile:"/home/cts1/projects/Ecom_buildSrc/keys/ee-aws-dev20130517",
		passphrase:"Password123")
	}
	
	def static scpFileTransferWithConsoleLog() {
		def timeStart = new Date()
		//LOGGER.info("start time:"+timeStart)
		def process=new ProcessBuilder("/home/cts1/reddy/logs/tunneling_db.sh").redirectErrorStream(true).start()
		process.inputStream.eachLine {println it}
		def timeStop = new Date()
		/*LOGGER.info("end time:"+timeStop)
		TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
		LOGGER.info("SCP download duration"+duration);*/
	}

}