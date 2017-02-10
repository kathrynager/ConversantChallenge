import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Iterator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Conversant Challenge is to read in the text file and find trends in the data.
 * 
 * I read the file in from the command line and stored the information with an 
 * object called Data. I used a HashMap to store the data by data center and 
 * contain an ArrayList as the value to hold all of the Data objects.
 * 
 * After plotting the data on a simple scatter plot to visualize the information,
 * I deducted that one large ArrayList for the data instead of a HashMap would have 
 * been simpler to use.
 * 
 * I made a few methods near the bottom of this class which calculate some information,
 * but the graph better shows the trends of the data. These methods include: average
 * value per DC, max value per DC and min value per DC (these can be used to find range).
 * 
 * Although I spent more time graphing the data, a helpful method to find trends 
 * would be to find the inflection points of the data center's info and compare them.
 * 
 * I can see in the graph that data center I fluctuates the most consistently.
 * Data center S has similar inflection points as I, but the range of difference
 * from maxima to minima is smaller. Finally, A is the most consistent in values
 * across time. A has the smallest range from highest to lowest point and remains
 * around an average values of ~3592.98.
 * 
 * @author Kathryn Ager
 */
public class ConversantChallenge {

	public static void main(String[] args) {

		// Check that the text file is passed in as a program argument
		if(args.length != 1) {
			System.out.println("Pass the 'data.Montoya.txt' file into the program arguments");
			System.exit(0);
		}

		// Get text file
		File inputFile = new File(args[0]);

		int timeBegin = 0;
		int timeEnd = 0;
		ArrayList<Data> allData = new ArrayList<Data>();
		ArrayList<Data> dataArrayA = new ArrayList<Data>();
		ArrayList<Data> dataArrayI = new ArrayList<Data>();
		ArrayList<Data> dataArrayS = new ArrayList<Data>();
		HashMap<Character, ArrayList> dataHash = new HashMap<Character, ArrayList>();
		// Read text file into Data object
		try {
			Scanner in = new Scanner(inputFile);

			// Read in header line
			if(in.hasNextLine())
				in.nextLine();
			while(in.hasNext()) {

				Data data = new Data();
				// Get type rtb.requests
				data.setType(in.next());

				//Get time and check that its an int
				String time = in.next();
				int n = Integer.parseInt(time);
				data.setTime(n);

				timeEnd = n;
				if(timeBegin == 0) {
					timeBegin = n;
				}

				// Get value
				double value = Double.parseDouble(in.next());
				data.setValue(value);

				// Get data center and parse out "dc=" to only get I, A, S
				String dc = in.next();
				char dcChar = dc.charAt(dc.length()-1);
				data.setDC(dcChar);

				if(dcChar == 'S') {
					dataArrayS.add(data);
				} else if(dcChar == 'I') {
					dataArrayI.add(data);
				} else if(dcChar == 'A') {
					dataArrayA.add(data);
				}
				allData.add(data); // if I were to do an arrayList implementation instead
			}
			dataHash.put('S', dataArrayS);
			dataHash.put('A', dataArrayA);
			dataHash.put('I', dataArrayI);

			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error: Cannot access input file");
			System.exit(0);
		} catch (NumberFormatException e) {
			System.out.println("Error: File not properly formatted");
			System.exit(0);
		}

		// Call functions to analyze the data
		avgValue(dataHash);
		double max = highLowValueDC(allData, "max");
		double min = highLowValueDC(allData, "min");

		int size = 0;
		Iterator itr = dataHash.entrySet().iterator();

		while(itr.hasNext()) {

			Map.Entry pair = (Map.Entry) itr.next();
			ArrayList<Data> list = (ArrayList<Data>) pair.getValue();
			size += list.size();
		}
		Graph g = new Graph(dataHash, max, min, timeBegin, timeEnd, size);
		g.createAndShowGUI();
		createCSV(allData);
	}

	/**
	 * Calculates the average value for each data center and prints them to console
	 * 
	 * @return void
	 */
	public static void avgValue(HashMap<Character, ArrayList> dataHash) {
		double avgValue = 0;

		Iterator itr = dataHash.entrySet().iterator();

		while(itr.hasNext()) {
			avgValue = 0;
			Map.Entry pair = (Map.Entry) itr.next();
			ArrayList<Data> list = (ArrayList<Data>) pair.getValue();
			for(Data data : list) {
				avgValue += data.getValue(); 
			}
			avgValue = avgValue/list.size();
			System.out.println("Average value for dc=" + pair.getKey() + " " + avgValue);
		}
		System.out.println();
	}

	/**
	 * Calculates the highest or lowest value for the data and prints it to console
	 * 
	 * @return double max or min of the data
	 */
	public static double highLowValueDC(ArrayList<Data> allData, String m) {
		Data ret = new Data();
		for(Data data : allData) {
			// Check if it's max/min or first assignment of max
			if(allData.indexOf(data) == 0) {
				ret = data;
			}
			
			else if(m.equalsIgnoreCase("max") && ret.getValue() < data.getValue()) {
				ret = data;
			} 
			else if (m.equalsIgnoreCase("min") && ret.getValue() > data.getValue()) {
				ret = data;
			}
		}
			System.out.println("Max value dc=" + ret.getDC() + " at time " + ret.getTime());
		System.out.println();
		return ret.getValue();
	}

	/**
	 * This method converts the data into a CSV file that can be read into Excel.
	 * I used it to verify that my scatter plot was graphed accurately.
	 * @return void
	 */
	public static void createCSV(ArrayList<Data> data){
		try{
			PrintWriter writer = new PrintWriter("dataCenters.csv", "UTF-8");
			for(Data d : data) {
				writer.print(d.getTime() + ",");
				writer.print(d.getValue() + ",");
				writer.println(d.getDC()+"\n");
			}
			writer.close();
		} catch (IOException e) {
			System.err.println("Couldn't write to file");
		}
	}
}
