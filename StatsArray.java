
/**
 * A data structure created to calculate statistical functions.
 * The purpose of this structure is to increase developer efficiency when calculating
 * these functions while also handling and manipulating large amounts of data. If called with no
 * parameters,the program assumes that the user wishes to use a 31 size int array to keep 
 * track of data over a one month period. The structure stresses having a faster O(n) over
 * conserving space.
 * @author Mike
 *
 * Provided statistics functions:
 * add, numElements, mode, min, max, range, length, sum, average
 * 
 * I have chosen to include these functions since this structure was created 
 * to help me with my personal stock analyzing project. For this reason there is no sort function.
 * Other functions may be added in later.
 * 
 * This version only accepts integers into the array.
 */
public class StatsArray{
	private int[] array;
	private int[] countArray = new int[100]; //counter array to calculate the mode, limits numbers up to 99.
	private int modeCount = 0;	//Keeps track of how often the mode number has occurred.
	private int min = -1;
	private int max = 0;
	private int range = 0;
	private int length;			//Length of the array
	private double average = 0;
	private int numElements = 0; 		//Keeps track of where the latest number has been put in.
	private int sum;
	private int mode = -1; 		//-1 is uninitialized. There should never be a -1 in the modeArray.
	private double stdDev = 0.0;
	
	/**
	 * Default Constructor
	 */
	public StatsArray(){		//default array, 31 is used for the most number of days in a month. 
		array = new int[31];
		length = 31;
	}
	
	/**
	 * Create an array of custom size.
	 * @param size - the length of the array
	 */
	public StatsArray(int size){
		array = new int[size];
		length = size;
	}
	
	/**
	 * This constructor is used whenever the user already has a pre-made array
	 * and wishes to use the statistical functions without having to rewrite code.
	 * @param newArray - the array to turn into a StatsArray.
	 */
	public StatsArray(int[] newArray){
		if(newArray!=null)
		{
			array = newArray;
			length = newArray.length; 	//if given an array, assume the array does not need to grow.
			numElements = length;
			
			min = newArray[0];
			max = newArray[0];
			sum+= newArray[0];
			for(int i = 1; i<length; i++)
			{
				sum+= newArray[i]; 		//assuming every cell should be included.
				if(newArray[i]<min)
					min = newArray[i];
				if(newArray[i]>max)
					max = newArray[i];
			}
			range = max - min;
			average = (double)sum/length;
			calculateMode();
			calculateStdDev();
		}
		else
		{
			System.out.println("Error! StatsArray handed null");
			System.exit(1); //StatsArray should never be handed null
		}
	}
	
	/**
	 * Add a number to the array. All variables that need to be recalculated
	 * upon addition (min, max, range, median, etc) are done after the addition.
	 * 
	 * Adding itself is O(1), as well as calculating min, max, range, and mode.
	 * @param var - the number to be added.
	 */
	public void add(int var) {
		if(numElements<length) {
			array[numElements] = var;
			numElements++;
			if(var<min || min==-1)
			{
				min = var;
				range = max - min;
			}
			else if(var>max)
			{
				max = var;
				range = max - min;
			}
			sum+=var;
			average = (double) sum/numElements; //Only counting content up until numElements, not the entire array.
			calculateNewMode(var);
			calculateStdDev();
		}
		else
		{  
			System.out.println("Error! Array out of space.");
			System.exit(1); //we don't want to give the user errored data.
		}
	}
	
	/**
	 * Provides a function to add an array of numbers at once.
	 * 
	 * Each O(1) statistical function then takes O(1) but run n times, so O(n)
	 * @param var - the array to be added
	 */
	public void add(int[] var)
	{
		if((var.length+numElements)<length)
		{
			for(int i: var)
				add(i);
		}
		else
		{
			System.out.println("Error! Add rejected. Array out of space.");
			System.exit(1); //user may be expecting rejected array to factor in the data.
		}
		System.out.println("The array: "+array.toString());
	}
	
	/**
	 * Calculates the most common number in the array using a counter array to count the number
	 * of occurrences of each number. mode itself keeps track of the most common number.
	 * 
	 * This function takes O(n)
	 */
	private void calculateMode()
	{
		for(int i : array)
		{
			countArray[i]++;
			if(modeCount<countArray[i]) //ties are chosen based on the last updated number.
			{							//in the future, I may return an array of all equal modes.
				modeCount = countArray[i];
				mode = i;
			}
		}
	}
	
	/**
	 * Only used when a new number is added. Uses a O(1) add and check.
	 */
	private void calculateNewMode(int newNumber)
	{
		countArray[newNumber]++;
		if(modeCount<countArray[newNumber]) 
		{
			modeCount = countArray[newNumber];
			mode = newNumber;
		}
	}
	
	//Returns the mode
	public int getMode()
	{
		if(modeCount==1) //most occurrences of any number is 1, so no mode
		{
			return -1;
		}
		return mode;
	}
	
	//Returns the number of added ints to the array
	public int validSize()
	{
		return numElements;
	}
		
	/**
	 * Calculates the standard deviation of the array. Takes O(n).
	 * Assumes the average has already been calculated.
	 */
	private void calculateStdDev()
	{
		stdDev = 0.00;
		for(int i = 0; i<numElements; i++)
		{
			stdDev+= Math.pow((double) array[i]-average,2);
		}
		stdDev = stdDev/numElements;
		stdDev = Math.pow(stdDev,0.5);
	}
	
	//Returns the standard deviation
	public double getStdDev()
	{
		return stdDev;
	}
	
	//The min number in the array
	public int min()
	{
		return min;
	}
	
	//The max number in the array
	public int max()
	{
		return max;
	}
	
	//The difference between the largest and smallest number in the array.
	public int range()
	{
		return range;
	}
	
	//The size of the array, in other words the max number of elements the array can hold.
	public int length()
	{
		return length;
	}
	
	//The total sum of the values in the array.
	public int sum()
	{
		return sum;
	}
	
	//The average of all the values in the array.
	public double average()
	{
		return average;
	}	
	
	/**
	 * toString function. Will only print the numbers that the user added, not any excess zeros.
	 */
	public String toString()
	{
		String arrayString = "[";
		for(int i = 0; i<numElements; i++)
		{
			arrayString+=array[i];
			if(i+1<numElements)
				arrayString+=", ";
		}
		arrayString+="]";
		return arrayString;
	}
}
