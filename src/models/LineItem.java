package models;

import java.util.Date;

public class LineItem   {

	private String endPointUrl;
	private int time;
	private String responseTime;
	private String client;
	private String provider;
	private String logDate;
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
	public String getEndPointUrl() {
		return endPointUrl;
	}
	public void setEndPointUrl(String endPointUrl) {
		this.endPointUrl = endPointUrl;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String toString() {
		return this.endPointUrl+"::"+this.provider+"::"+this.client+"::"+this.responseTime+"::"+this.time;
	}
	public boolean isNotNull(){
		if(this.endPointUrl==null && this.provider==null && this.client==null && this.responseTime==null && this.method==null){
			return false;
		}else		
		return true;
	}
}
