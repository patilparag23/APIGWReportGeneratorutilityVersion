package reportGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import processor.FileSplitter;

public class TestSpli {
	
	public static void main(String[] args) {
		TestSpli spli=new TestSpli();
		spli.test();
	}

	public void test() {
	FileSplitter splitter=new FileSplitter();
	//splitter.splitFiles();
	System.out.println(new File("/home/cts1/reddy/logs/audit2/splittedFiles").list().length);
	/*try {
		File file=new File("/home/cts1/reddy/logs/audit/splittedFiles/auditlog0.txt");
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		
		FileOutputStream outputStream=new FileOutputStream(file);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	}
}
