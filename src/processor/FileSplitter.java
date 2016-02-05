package processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.omg.PortableInterceptor.LOCATION_FORWARD;

public class FileSplitter {

	private static String FILE_PATH = "//home//cts1//reddy//logs//audit1/";
	//private static String FILE_PATH = "D:\\apache-maven-3.1.1\\new\\";
	private static String OUTPUT_FILE_NAME = "//home//cts1//reddy//logs//audit1//splittedFiles//";
	//private static String OUTPUT_FILE_NAME = "D:\\apache-maven-3.1.1\\new\\splittedFiles\\";
	private static int PART_SIZE = 161200000;
	private static File f = null;
	private static String[] paths = null;

	private final static Logger LOGGER = Logger.getLogger(FileSplitter.class);

	public static void main(String[] args) {
		try {
			f = new File(OUTPUT_FILE_NAME);
			FileUtils.cleanDirectory(f);
			splitFiles(FILE_PATH, OUTPUT_FILE_NAME);

			paths = f.list();

			// for each name in the path array
			for (String path : paths) {
				System.out.println(path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void splitFiles(String inputLocation, String outputLocation)
			throws IOException {
		LOGGER.info("File Splitter Starts");
		/*File outputLoc = new File(outputLocation);
		if (outputLoc.exists()) {
			FileUtils.cleanDirectory(outputLoc);
		}*/

		File inputFile = new File(inputLocation);
		int nChunks = 0;
		FileInputStream inputStream = null;
		String[] inputFiles = inputFile.list();
		try {
			for (String fileName : inputFiles) {

				inputFile = new File(inputLocation + fileName);
				if (!inputFile.isDirectory()) {
					LOGGER.info("input file:::" + inputFile);
					FileOutputStream filePart;
					int fileSize = (int) inputFile.length();
					int read = 0, readLength = PART_SIZE;
					byte[] byteChunkPart;

					inputStream = new FileInputStream(inputFile);
					while (fileSize > 0) {
						if (fileSize <= 161200000) {
							readLength = fileSize;
						}
						byteChunkPart = new byte[readLength];
						read = inputStream.read(byteChunkPart, 0, readLength);
						fileSize -= read;
						assert (read == byteChunkPart.length);
						nChunks++;

						File newFileName = new File(outputLocation + "auditlog"
								+ Integer.toString(nChunks - 1) + ".txt");
						if (!newFileName.getParentFile().exists()) {
							newFileName.getParentFile().mkdirs();
						}
						filePart = new FileOutputStream(newFileName);
						filePart.write(byteChunkPart);
						filePart.flush();
						filePart.close();
						byteChunkPart = null;
						filePart = null;

					}
				}
			}

			LOGGER.info("File Splitter Ends");
		} catch (IOException exception) {
			LOGGER.error(exception);
			exception.printStackTrace();
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}
}
