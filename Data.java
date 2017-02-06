/**
 * Data class is an object, which stores the data
 * type, time, value and data center
 */
public class Data {
	private String type;
	private int time;
	private double value;
	private char dc;
	
	public Data(String type, int time, double value, char dc) {
		this.type = type;
		this.time = time;
		this.value = value;
		this.dc = dc;
	}
	public Data() {
		
	}
	 /**
     * Returns the type of data
     * All data appears to be rtb.requests in the text file
     * 
     * @return data type
     */
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	/**
     * Returns time of data
     * 
     * @return data time
     */
	public int getTime() {
		return this.time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	 /**
     * Returns value of the data
     * 
     * @return data value
     */
	public double getValue() {
		return this.value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	 /**
     * Returns the data center value, which is either I, S, or A
     * 
     * @return The data center type
     */
	public char getDC() {
		return this.dc;
	}
	
	public void setDC(char dc) {
		this.dc = dc;
	}
}
