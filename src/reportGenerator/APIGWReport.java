package reportGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import models.LineItem;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import processor.LineObjectProcessor;
import processor.WritingInExcel;
import sendMail.SendMail;

public class APIGWReport {

	public static FileOutputStream out = null;

	public static String logPath = "";
	// public static String[] splittedFilePath = null;

	public static String reportPath = "";
	public static String reportName = "";

	public static String reportLocName = "";
	public static String endPointURL = null;
	public static String clientName = null;
	public static String[] lineBySpace = null;

	public static int rownum = 0;
	public static String ignoredArray[] = null;
	public static int countDuration = 0;
	private static Properties localProperties;
	private static String endPointCorrectionList[] = null;
	private final static Logger LOGGER = Logger.getLogger(APIGWReport.class);
	private static String dateWiseReportRequired = "";
	private String YES = "Yes";
	private String NO = "No";
	private String splitFolderName = "/splittedFiles/";

	private void initializeVariables(String env) {
		try {

			localProperties = new Properties();
			InputStream inStream = null;
			if (env.equals("perf")) {
				inStream = getClass().getClassLoader().getResourceAsStream(
						"perf_api_GW_Config.properties");
				localProperties.load(inStream);
			} else if (env.equals("live")) {
				inStream = getClass().getClassLoader().getResourceAsStream(
						"live_api_GW_Config.properties");
				localProperties.load(inStream);
			} else if (env.equals("ref")) {
				inStream = getClass().getClassLoader().getResourceAsStream(
						"ref_api_GW_Config.properties");
				localProperties.load(inStream);
			}
			logPath = localProperties.get("log.files.path").toString();
			// splittedFilePath=(localProperties.get("splitted_file_path").toString()).split(":");
			countDuration = Integer.parseInt(localProperties.get(
					"countDuration").toString());
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			reportPath = localProperties.get("report.files.path").toString();
			// reportName=localProperties.get("reportName").toString();
			reportName = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss'.xlsx'")
					.format(new Date());
			reportLocName = reportPath + "/Report_" + reportName;
			ignoredArray = (localProperties.get("ignoredList").toString())
					.split(":");
			endPointCorrectionList = (localProperties
					.get("endPointCorrectionList").toString()).split(":");
			out = new FileOutputStream(new File(reportLocName));
			dateWiseReportRequired = localProperties.get(
					"dateWiseReportRequired").toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		initializeReport("live");
	}

	public static String initializeReport(String environment) {
		BasicConfigurator.configure();
		LOGGER.info("initializeReport Start Time : " + new Date());
		APIGWReport report = new APIGWReport();
		report.initializeVariables(environment);
		report.startGenerateReport(environment, reportLocName);
		LOGGER.info("initializeReport End Time : " + new Date());
		return reportLocName;
	}

	public void startGeneratingReport() {
		String env = "perf";
		BasicConfigurator.configure();
		LOGGER.info("startGeneratingReport Start Time : " + new Date());
		APIGWReport report = new APIGWReport();
		report.initializeVariables(env);
		report.startGenerateReport(env, reportLocName);
		LOGGER.info("startGeneratingReport Done with the report Generation.. Going to call send mail"
				+ new Date());
		sendReportMail();

	}

	public void sendReportMail() {
		Timer timer = new Timer();

		TimerTask action = new TimerTask() {
			public void run() {
				try {
					SendMail sendMail = new SendMail();
					;
					sendMail.sendMail("");
				} catch (FileNotFoundException e) {
					LOGGER.error("FileNotFoundException thrown in sendReportMail.sendMail :: "
							+ e.getMessage());
					e.printStackTrace();
				} catch (IOException e) {
					LOGGER.error("IOException thrown in method sendReportMail.sendMail :: "
							+ e.getMessage());
					e.printStackTrace();
				}
			}

		};
		Date date = new Date();
		LOGGER.info("Will send mail after 5 mints :: " + date.getTime());
		timer.schedule(action, 3000);
	}

	public void startGenerateReport(String env, String reportName) {
		LOGGER.info("startGeneratingReport Start Time : " + new Date()
				+ " environment::" + env + "::reportName::" + reportName);
		String line = null;
		try {
			Map<String, LineItem> lineItemMap = new HashMap<String, LineItem>();
			String keyForClientMap = "";
			String keyForProviderMap = "";
			String separator = "-zz-";
			Map<String, List> clientMap = new HashMap<String, List>();
			Map<String, List> providerMap = new HashMap<String, List>();
			BufferedReader reader = null;
			String log = null;
			String splittedFolderName = null;

			Map<Integer, LineItem> lineMap = new HashMap<Integer, LineItem>();

			splittedFolderName = logPath + splitFolderName;
			LOGGER.info("splittedFolderName::" + splittedFolderName);
			File f = new File(splittedFolderName);

			// LOGGER.info("size:::"+f.list().length);
			/*
			 * if(!f.exists()){ f.mkdir(); }
			 */
			String[] splittedFiles = f.list();
			if (splittedFiles != null) {
				for (int z = 0; z < splittedFiles.length; z++) {
					log = splittedFolderName + splittedFiles[z];
					LOGGER.info(log);
					boolean filseSizeCheck = checkFileSize(log);
					if (filseSizeCheck) {
						reader = new BufferedReader(new FileReader(log));
						Integer count = 0;

						LineItem lineItem = null;
						while ((line = reader.readLine()) != null) {
							++count;
							lineItem = getLineItem(line);
							WritingInExcel write =new WritingInExcel();
							
							if (lineItem != null && lineItem.isNotNull()) {
								lineMap.put(count, lineItem);
								if (dateWiseReportRequired
										.equalsIgnoreCase(YES)) {
									keyForClientMap = lineItem.getClient()
											+ separator
											+ lineItem.getProvider()
											+ separator
											+ lineItem.getEndPointUrl()
											+ separator + lineItem.getLogDate()
											+ separator + lineItem.getMethod();
									keyForProviderMap = lineItem.getProvider()
											+ separator
											+ lineItem.getEndPointUrl()
											+ separator + lineItem.getLogDate()
											+ separator + lineItem.getMethod();
								
									
								} else if (dateWiseReportRequired
										.equalsIgnoreCase(NO)) {
									keyForClientMap = lineItem.getClient()
											+ separator
											+ lineItem.getProvider()
											+ separator
											+ lineItem.getEndPointUrl()
											+ separator + lineItem.getMethod();
									keyForProviderMap = lineItem.getProvider()
											+ separator
											+ lineItem.getEndPointUrl()
											+ separator + lineItem.getMethod();
								}
								addToMap(keyForClientMap, clientMap, count);
								addToMap(keyForProviderMap, providerMap, count);
							}
						}
					} else {
						LOGGER.error("File "
								+ splittedFiles[z]
								+ " present on "
								+ splittedFolderName
								+ " has size less than 100 MB . So not processing the same.");
					}
				}
				LineObjectProcessor processor = new LineObjectProcessor();
				processor.processLineObjects(lineMap, clientMap, providerMap,
						dateWiseReportRequired, env, reportName);
				LOGGER.info("startGeneratingReport Done with the report Generation.. Going to call send mail"
						+ new Date()
						+ " environment::"
						+ env
						+ "::reportName::" + reportName);

			}

		} catch (Exception e) {
			LOGGER.error(line);
			e.printStackTrace();
		}
	}

	private void addToMap(String key, Map<String, List> mapToAdd, int lineItemNo) {
		int count = 1;
		if (mapToAdd.get(key) == null) {
			mapToAdd.put(key, new ArrayList<Integer>());
		}
		((ArrayList) mapToAdd.get(key)).add(lineItemNo);

	}

	private LineItem getLineItem(String line) throws IOException {
WritingInExcel write =new WritingInExcel();
		
		if (!(line.trim().length() > 0)) {
			return null;
		}
		String lineValues[] = line.split(" ");
		LineItem lineItem = null;
		String[] lineByColon = null;
		String endPointURL = "";
		String client = "";
		String urlArray[] = null;
		String provider = "";
		StringTokenizer st = null;
		String[] str4 = null;
		String identityUrl[] = null;
		
		if ((lineValues.length > 12) && (lineValues[0].length() > 5)
				&& (lineValues[0].charAt(3) + "").equals("/")
				&& (lineValues[0].charAt(7) + "").equals("/")) {
			if (!ignoredURL(lineValues[5])) {
				lineItem = new LineItem();
				lineItem.setMethod(lineValues[4].substring(1));
				lineItem.setLogDate(lineValues[0].substring(1, 12));
				endPointURL = lineValues[5];
				String s[] = null;
				if (endPointURL.indexOf('?') > -1) {
					endPointURL = endPointURL.split("\\?")[0];// removing
																// parameters
																// from end poit
																// url and
																// setting in
																// end point url
				} else if (endPointURL.indexOf('-') > -1) {
					endPointURL = endPointURL.substring(0,
							(endPointURL.split("-")[0]).lastIndexOf("/"));
				}

				for (String url : endPointCorrectionList) {
					if (endPointURL.indexOf(url) > -1) {
						endPointURL = endPointURL.substring(0,
								(endPointURL.indexOf(url)) + url.length());
						break;
					}
				}

				lineItem.setEndPointUrl(endPointURL);
				urlArray = lineItem.getEndPointUrl().split("/");
				if (urlArray.length >= 2) {
					provider = urlArray[1];
					lineItem.setProvider(provider);
				}
				if (lineValues.length > 9) {
					lineItem.setTime(getInTime(lineValues[0], lineValues[10])); // set
																				// in
																				// time
																				// in
																				// sec
					lineItem.setResponseTime(lineValues[10]);
				}
				st = new StringTokenizer(line, "(");
				int i = 0;
				str4 = new String[5];
				while (st.hasMoreElements()) {
					str4[i] = (String) st.nextElement();
					i++;
				}
				if (str4[1] != null) {
					client = str4[1].split(":")[0];
					lineItem.setClient(client);// split client name by : and set
												// in lineItem
					if (client.equals("null") || null == client) {
						LOGGER.info("null client line" + line);
					}
				}
			}

		}

		return lineItem;
	}

	private boolean ignoredURL(String endPointURL) {

		boolean ignoredURL = false;
		for (String ignoredParameter : ignoredArray) {
			if (endPointURL.equals("/")) {
				ignoredURL = true;
				break;
			}
			if (endPointURL.indexOf(ignoredParameter) > -1) {
				ignoredURL = true;
				break;
			}
		}
		return ignoredURL;

	}

	public int getInTime(String time, String responseTime) {

		String timeSplit[] = time.split(":");
		int timeDuration = countDuration * 60;
		float inSec = Integer.parseInt(timeSplit[1]) * 3600
				+ Integer.parseInt(timeSplit[2]) * 60
				+ Integer.parseInt(timeSplit[3]);
		inSec = inSec - Float.parseFloat(responseTime);
		return (int) inSec / timeDuration;
	}

	private boolean checkFileSize(String filePath) {
		boolean sizeCheck = false;
		if (filePath != null && !"".equals(filePath)) {
			File file = new File(filePath);
			if (file.exists() && !file.isFile()) {
				if (file.length() > 104) {
					sizeCheck = true;
				}
			}
		}
		return true;
	}
}
