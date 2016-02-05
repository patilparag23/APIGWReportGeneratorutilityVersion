package models;

public class OccurenceCounter implements Comparable<OccurenceCounter> {

	private int timeSpan;
	private int occurence;
	
	public int getTimeSpan() {
		return timeSpan;
	}
	public void setTimeSpan(int timeSpan) {
		this.timeSpan = timeSpan;
	}
	public int getOccurence() {
		return occurence;
	}
	public void setOccurence(int occurence) {
		this.occurence = occurence;
	}
	
	@Override
	public int compareTo(OccurenceCounter occurenceCountero) {
		if(getOccurence() < occurenceCountero.getOccurence()){
			return -1;
		}else if(getOccurence() > occurenceCountero.getOccurence()){
			return 1;
		}else{
			return 0;
		}
	}
}
