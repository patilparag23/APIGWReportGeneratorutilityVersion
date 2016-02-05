package models;

import java.util.Comparator;

public class FileObjects {
	
	private String logDate;
	private String client;
	private String endPoint;
	private int totalTxn;
	private float maxTPS;
	private double avgTPS;
	private double longestRespTime;
	private double meanRespTime;
	private String provider;
	private String method;
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getLogDate() {
		return logDate;
	}
	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	public int getTotalTxn() {
		return totalTxn;
	}
	public void setTotalTxn(int totalTxn) {
		this.totalTxn = totalTxn;
	}
	public float getMaxTPS() {
		return maxTPS;
	}
	public void setMaxTPS(float maxTPS) {
		this.maxTPS = maxTPS;
	}
	public double getAvgTPS() {
		return avgTPS;
	}
	public void setAvgTPS(double avgTPS) {
		this.avgTPS = avgTPS;
	}
	public double getLongestRespTime() {
		return longestRespTime;
	}
	public void setLongestRespTime(double longestRespTime) {
		this.longestRespTime = longestRespTime;
	}
	public double getMeanRespTime() {
		return meanRespTime;
	}
	public void setMeanRespTime(double meanRespTime) {
		this.meanRespTime = meanRespTime;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	
	
}
