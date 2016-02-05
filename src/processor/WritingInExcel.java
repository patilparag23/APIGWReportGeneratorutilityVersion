package processor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import models.FileObjects;
import models.LineItem;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import sendMail.SendMail;
import comparator.ClientSort;
import comparator.ProviderSort;

public class WritingInExcel {

	private Properties localProperties;
	private String logPath = "";
	private String reportPath = "";
	private String reportName = "";
	private FileOutputStream fileOut;
	private String client = "Client";
	private String provider = "Provider";
	private static String reportNameForMail="";
	
	private final static Logger LOGGER=Logger.getLogger(WritingInExcel.class);

	public boolean writeInExcel(List<FileObjects> clientDataList,
			List<FileObjects> providerDataList, String env, String reportName) {

		/*System.out.println(clientDataList.size());
		System.out.println(providerDataList.size());*/
		LOGGER.info("writeInExcel starts");
		initializeVariables(env);
		Collections.sort(clientDataList,new ClientSort());
		Collections.sort(providerDataList, new ProviderSort());
	    BufferedWriter writer = null;
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
			String date = format.format(new Date());
			XSSFCellStyle cellStyle = workbook.createCellStyle();
			XSSFFont xssfFont = workbook.createFont();
			xssfFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			//cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			cellStyle.setFont(xssfFont);
			cellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); 
			XSSFCellStyle valueCellStyle = workbook.createCellStyle();
			valueCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
			valueCellStyle.setDataFormat((short) XSSFCell.CELL_TYPE_NUMERIC);
			if (!clientDataList.isEmpty()) {
				XSSFSheet xssfSheet = workbook.createSheet(client);
				XSSFRow row = xssfSheet.createRow(0);
				XSSFCell cell0 = row.createCell(0);
				cell0.setCellStyle(cellStyle);
				cell0.setCellValue("Date");

				XSSFCell cell1 = row.createCell(1);
				cell1.setCellStyle(cellStyle);
				cell1.setCellValue("Client");

				XSSFCell cell2 = row.createCell(2);
				cell2.setCellStyle(cellStyle);
				cell2.setCellValue("Provider");

				XSSFCell cell3 = row.createCell(3);
				cell3.setCellStyle(cellStyle);
				cell3.setCellValue("Method");
				
				XSSFCell cell4 = row.createCell(4);
				cell4.setCellStyle(cellStyle);
				cell4.setCellValue("EndPoint");

				XSSFCell cell5 = row.createCell(5);
				cell5.setCellStyle(cellStyle);
				cell5.setCellValue("Total Transactions");

				XSSFCell cell6 = row.createCell(6);
				cell6.setCellStyle(cellStyle);
				cell6.setCellValue("MaxTPS");

				XSSFCell cell7 = row.createCell(7);
				cell7.setCellStyle(cellStyle);
				cell7.setCellValue("AverageTPS");

				XSSFCell cell8 = row.createCell(8);
				cell8.setCellStyle(cellStyle);
				cell8.setCellValue("LongestResponseTime(in sec)");

				XSSFCell cell9 = row.createCell(9);
				cell9.setCellStyle(cellStyle);
				cell9.setCellValue("MeanResponseTime(in sec)");

				int rowNum = 1;
				XSSFRow nextRow =null;
	
				for (FileObjects obj : clientDataList) {
				//	System.out.println(obj.getEndPoint());
					nextRow = xssfSheet.createRow(rowNum);
					System.out.println("rowNum : " +rowNum);
					nextRow.createCell(0).setCellValue(obj.getLogDate());
					nextRow.createCell(1).setCellValue(obj.getClient());
					nextRow.createCell(2).setCellValue(obj.getProvider());
					nextRow.createCell(3).setCellValue(obj.getMethod());
					nextRow.createCell(4).setCellValue(obj.getEndPoint());
					nextRow.createCell(5).setCellValue(obj.getTotalTxn());
					nextRow.createCell(6).setCellValue(obj.getMaxTPS());
					nextRow.createCell(7).setCellValue(obj.getAvgTPS());
					nextRow.createCell(8).setCellValue(obj.getLongestRespTime());
					nextRow.createCell(9).setCellValue(obj.getMeanRespTime());
					for (int i = 4; i < 10; i++) {
						nextRow.getCell(i).setCellStyle(valueCellStyle);
					}
					rowNum++;
				}
				for (int i = 0; i < 10; i++) {
					xssfSheet.autoSizeColumn(i);
				}

			}
			if (!providerDataList.isEmpty()) {
				XSSFSheet xssfSheet = workbook.createSheet(provider);

				XSSFRow row = xssfSheet.createRow(0);
				XSSFCell cell0 = row.createCell(0);
				cell0.setCellStyle(cellStyle);
				cell0.setCellValue("Date");

				XSSFCell cell1 = row.createCell(1);
				cell1.setCellStyle(cellStyle);
				cell1.setCellValue("Provider");

				XSSFCell cell2 = row.createCell(2);
				cell2.setCellStyle(cellStyle);
				cell2.setCellValue("Method");
				
				XSSFCell cell3 = row.createCell(3);
				cell3.setCellStyle(cellStyle);
				cell3.setCellValue("EndPoint");

				XSSFCell cell4 = row.createCell(4);
				cell4.setCellStyle(cellStyle);
				cell4.setCellValue("Total Transactions");

				XSSFCell cell5 = row.createCell(5);
				cell5.setCellStyle(cellStyle);
				cell5.setCellValue("MaxTPS");

				XSSFCell cell6 = row.createCell(6);
				cell6.setCellStyle(cellStyle);
				cell6.setCellValue("AverageTPS");

				XSSFCell cell7 = row.createCell(7);
				cell7.setCellStyle(cellStyle);
				cell7.setCellValue("LongestResponseTime(in sec)");

				XSSFCell cell8 = row.createCell(8);
				cell8.setCellStyle(cellStyle);
				cell8.setCellValue("MeanResponseTime(in sec)");
				int rowNum = 1;
				XSSFRow nextRow=null;
				for (FileObjects obj : providerDataList) {
					nextRow = xssfSheet.createRow(rowNum);
					nextRow.createCell(0).setCellValue(obj.getLogDate());
					nextRow.createCell(1).setCellValue(obj.getProvider());
					nextRow.createCell(2).setCellValue(obj.getMethod());
					nextRow.createCell(3).setCellValue(obj.getEndPoint());
					nextRow.createCell(4).setCellValue(obj.getTotalTxn());
					nextRow.createCell(5).setCellValue(obj.getMaxTPS());
					nextRow.createCell(6).setCellValue(obj.getAvgTPS());
					nextRow.createCell(7).setCellValue(obj.getLongestRespTime());
					nextRow.createCell(8).setCellValue(obj.getMeanRespTime());
					for (int i = 4; i < 9; i++) {
						nextRow.getCell(i).setCellStyle(valueCellStyle);
					}
					rowNum++;
				}
				for (int i = 0; i < 9; i++) {
					xssfSheet.autoSizeColumn(i);
				}

			}
			fileOut = new FileOutputStream(reportName);
			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
			LOGGER.info("writeInExcel ends");
		} catch (Exception exception) {
			LOGGER.error(exception);
			exception.printStackTrace();
		}
		return true;
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	private void initializeVariables(String env) {

		try {
			localProperties = new Properties();
		    if(env.equals("perf")){
		    	 InputStream inStream=getClass().getClassLoader().getResourceAsStream("perf_api_GW_Config.properties");
		    	localProperties.load(inStream);
		    }else if (env.equals("live")){
		    	 InputStream inStream=getClass().getClassLoader().getResourceAsStream("live_api_GW_Config.properties");
		    	localProperties.load(inStream);
		    }else if (env.equals("ref")){
		    	 InputStream inStream=getClass().getClassLoader().getResourceAsStream("ref_api_GW_Config.properties");
		    	localProperties.load(inStream);
		    }
		    
			logPath = localProperties.get("log.files.path").toString();
		    reportName=localProperties.get("reportName").toString();
			reportPath = logPath + "/" + reportName;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	

	
}
