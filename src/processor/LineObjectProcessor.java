package processor;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import models.FileObjects;
import models.LineItem;
import models.OccurenceCounter;
public class LineObjectProcessor {
	// /Map<Integer,LineItem>, List<String>, List<String>
	private String splitter = "-zz-";
	private String client = "Client";
	private String provider = "Provider";
	private String YES="Yes";
	private String NO="No";
	
	private final static Logger LOGGER=Logger.getLogger(LineObjectProcessor.class);
	public void processLineObjects(Map<Integer, LineItem> lineMap,
			Map<String, List> clientMap, Map<String, List> providerMap,String dateWiseReportRequired , String env, String reportName) {
		try {
			LOGGER.info("Processing start::clientMapSize:::"+clientMap.size());
			List<FileObjects> clientDataList = setFileObjects(lineMap,
					clientMap, client,dateWiseReportRequired);
			List<FileObjects> providerDataList = setFileObjects(lineMap,
					providerMap, provider,dateWiseReportRequired);
			WritingInExcel writeinExcel = new WritingInExcel();
			
			writeinExcel.writeInExcel(clientDataList, providerDataList,env,reportName);
			LOGGER.info("Processing ends::providerMapSize:::"+providerMap.size());
		} catch (Exception e) {
			LOGGER.info(e);
			e.printStackTrace();
		}

	}

	public List<FileObjects> setFileObjects(Map<Integer, LineItem> lineMap,
			Map<String, List> dataMap, String identifier, String dateWiseReportRequired) {
		FileObjects fileObj = null;
		String key = "";
		Set keyset = dataMap.keySet();
		Iterator iterator = keyset.iterator();
		String keyElements[] = null;
		String responseTimeElements[] = new String[3];
		String comboTPS = "";
		List<FileObjects> fileObjList = new ArrayList<FileObjects>();
		while (iterator.hasNext()) {
			key = iterator.next().toString();
			List<Integer> lineList = dataMap.get(key);
			keyElements = key.split(splitter);// client, provider, endpoint for client identifier and provider, endpoint for provider 
			fileObj = new FileObjects();

			if (identifier.equalsIgnoreCase(client)) {
				fileObj.setClient(keyElements[0]);
				fileObj.setProvider(keyElements[1]);
				fileObj.setEndPoint(keyElements[2]);
				if(YES.equalsIgnoreCase(dateWiseReportRequired)){
					fileObj.setLogDate(keyElements[3]);
					fileObj.setMethod(keyElements[4]);
				}else{
					fileObj.setLogDate(getCurrentTime());
					fileObj.setMethod(keyElements[3]);
				}
				
				
			} else if (identifier.equalsIgnoreCase(provider)) {
				fileObj.setProvider(keyElements[0]);
				fileObj.setEndPoint(keyElements[1]);
				if(YES.equalsIgnoreCase(dateWiseReportRequired)){
					fileObj.setLogDate(keyElements[2]);
					fileObj.setMethod(keyElements[3]);
				}else{
					fileObj.setLogDate(getCurrentTime());
					fileObj.setMethod(keyElements[2]);
				}
			}
			fileObj.setTotalTxn(lineList.size());
			String comboResTime= calculateComboResponseTime(lineMap, lineList);
			if(!comboResTime.equals("0")){
			responseTimeElements = calculateComboResponseTime(lineMap, lineList)
					.split(splitter);
			}
			if(responseTimeElements[0]!=null && responseTimeElements[0]!=""){
			fileObj.setLongestRespTime(Double.parseDouble(responseTimeElements[0]));// longest
																// response time
			}else{
				fileObj.setLongestRespTime(0);
			}
			try{
			if(responseTimeElements.length>1){
			if(responseTimeElements[1]!=null && responseTimeElements[1]!=""){
				fileObj.setMeanRespTime(Double.parseDouble(responseTimeElements[1]));// mean response
				}else{
					fileObj.setMeanRespTime(0);
				}
			}
			}catch(Exception e){
				fileObj.setMeanRespTime(0);
			}
			try{
			if(calculateAvgTPS(lineList.size())!=null && calculateAvgTPS(lineList.size())!="")	{												// time
				fileObj.setAvgTPS(Double.parseDouble(calculateAvgTPS(lineList.size())));
			}else{
				fileObj.setAvgTPS(0);
			}
			}catch(Exception e){
				fileObj.setAvgTPS(0);
			}
			try{
			if(calculateMaxTPS(lineMap, lineList)!=null && calculateMaxTPS(lineMap, lineList)!=""){
				fileObj.setMaxTPS(Float.parseFloat(calculateMaxTPS(lineMap, lineList)));
			}else{
				fileObj.setMaxTPS(0);
			}}catch(Exception e){
				fileObj.setMaxTPS(0);
			}
				
			fileObjList.add(fileObj);
			
		}
		return fileObjList;
	}

	public String calculateComboResponseTime(Map<Integer, LineItem> lineMap,
			List<Integer> lineList) {

		double maxResponseTime = 0;
		String meanResponseTime = "";
		double totalResponseTime = 0;
		double totalSec = 86400;
		int j=0;
		String comboResponseTime="";
		int count=0;
		for (int i : lineList) {
			if (lineMap.get(i) != null
					&& ((LineItem) lineMap.get(i)).getResponseTime() != null) {
				double lineResponseTime = Double.parseDouble(((LineItem) lineMap.get(i))
						.getResponseTime());
				totalResponseTime = totalResponseTime + lineResponseTime;
				if (lineResponseTime > maxResponseTime) {
					maxResponseTime = lineResponseTime;
				}
			} 
		}
		DecimalFormat df = new DecimalFormat("#");
		df.setMaximumFractionDigits(10);
		meanResponseTime = df.format(totalResponseTime / lineList.size());
		
		if ((meanResponseTime).length() < 7) {
			comboResponseTime= "" + maxResponseTime + splitter + meanResponseTime;
		} else {
			String meanTPSStr=meanResponseTime+"";
			meanTPSStr=meanTPSStr.substring(0, 6);
			comboResponseTime= "" + maxResponseTime + splitter + meanTPSStr;
		}
		return comboResponseTime;
	}

	public String calculateAvgTPS(int totalTxn) {
		double totalSec = 86400;
		
		DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(10);
        String avgTPS=(df.format(totalTxn / totalSec));
        if((avgTPS+"").length() >8){
        avgTPS = avgTPS.substring(0, (avgTPS.indexOf(".")) + 6);
        }
		return avgTPS;
	}

	public String calculateMaxTPS(Map<Integer, LineItem> lineMap,
			List<Integer> lineList) {

		LineItem lineItem = null;
		OccurenceCounter occurenceCounter = null;
		int occurence = 0;
		Map<Integer, Integer> occurenceMap = new HashMap<Integer, Integer>();
		for (int i : lineList) {

			lineItem = (LineItem) lineMap.get(i);
			if (occurenceMap.get(lineItem.getTime()) == null) {
				occurenceMap.put(lineItem.getTime(), 1);
			} else {
				occurence = occurenceMap.get(lineItem.getTime()) + 1;
				occurenceMap.put(lineItem.getTime(), occurence);
			}

		}
		int maxTPS = 0;
		for (Map.Entry<Integer, Integer> entry : occurenceMap.entrySet()) {
			if (entry.getValue() > maxTPS) {
				maxTPS = entry.getValue();
			}
		}
		maxTPS=maxTPS/120;
		int roundedMaxTPS = (int) (maxTPS + 0.5)!=0 ? (int) (maxTPS + 0.5): 1;
		
		return roundedMaxTPS + "";
	}
	private String getCurrentTime(){
		
		 DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
		 Calendar cal = Calendar.getInstance();
		 cal.add(Calendar.DATE, -1);
		return (dateFormat.format(cal.getTime()));
	}

}
