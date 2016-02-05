package processor

import groovy.io.FileType
import groovy.time.TimeCategory
import groovy.time.TimeDuration

import org.rauschig.jarchivelib.Archiver
import org.rauschig.jarchivelib.ArchiverFactory

import reportGenerator.APIGWReport

class Extractor {
	
	private static int count=0;
	
	
	public static void main(String[] args) {
		//Extractor.tarGzFileExtractor();
		Extractor.tarGZExtractor();
	}
	
	
	def static tarGZExtractor() {
		
		def list = []
		
		def dir = new File("/home/cts1/reddy/logs/")
		dir.eachFileRecurse (FileType.FILES) { file ->
		  list << file
		}
		
		list.each {
			if(it.path.endsWith("tar.gz")){
				tarGzFileExtractor(it.path)
			}
			
		  }
	}
	
	def static tarGzFileExtractor(String fileLocation) {
		def timeStart = new Date()
		//println 'extraction start time:'+timeStart+'for:::'+fileLocation
		File archive = new File(fileLocation);
		File destination = new File("/home/cts1/reddy/logs/audit/");
		
		Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");
		archiver.extract(archive, destination);
		
		def timeStop = new Date()
		//println 'extraction end time:'+timeStop+'for:::'+fileLocation
		TimeDuration duration = TimeCategory.minus(timeStop, timeStart)
		println 'total extraction time:'+duration+'for:::'+fileLocation
		
		File oldfile =new File("/home/cts1/reddy/logs/audit/audit.log");
		File newfile =new File("/home/cts1/reddy/logs/audit/audit"+count+".log");
 
		if(oldfile.renameTo(newfile)){
			//System.out.println("Rename succesful");
			count++;
		}else{
			//System.out.println("Rename failed");
		}
	}

}
