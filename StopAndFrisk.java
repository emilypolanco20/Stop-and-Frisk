import java.util.ArrayList;

/**
 * The StopAndFrisk class represents stop-and-frisk data, provided by
 * the New York Police Department (NYPD), that is used to compare
 * during when the policy was put in place and after the policy ended.
 * 
 * @author Tanvi Yamarthy
 * @author Vidushi Jindal
 */
public class StopAndFrisk {

    /*
     * The ArrayList keeps track of years that are loaded from CSV data file.
     * Each SFYear corresponds to 1 year of SFRecords. 
     * Each SFRecord corresponds to one stop and frisk occurrence.
     */ 
    private ArrayList<SFYear> database; 

    /*
     * Constructor creates and initializes the @database array
     * 
     * DO NOT update nor remove this constructor
     */
    public StopAndFrisk () {
        database = new ArrayList<>();
    }

    /*
     * Getter method for the database.
     * *** DO NOT REMOVE nor update this method ****
     */
    public ArrayList<SFYear> getDatabase() {
        return database;
    }

    /**
     * This method reads the records information from an input csv file and populates 
     * the database.
     * 
     * Each stop and frisk record is a line in the input csv file.
     * 
     * 1. Open file utilizing StdIn.setFile(csvFile)
     * 2. While the input still contains lines:
     *    - Read a record line (see assignment description on how to do this)
     *    - Create an object of type SFRecord containing the record information
     *    - If the record's year has already is present in the database:
     *        - Add the SFRecord to the year's records
     *    - If the record's year is not present in the database:
     *        - Create a new SFYear 
     *        - Add the SFRecord to the new SFYear
     *        - Add the new SFYear to the database ArrayList
     * 
     * @param csvFile
     */
    public void readFile ( String csvFile ) {

        // DO NOT remove these two lines
        StdIn.setFile(csvFile); // Opens the file
        StdIn.readLine();       // Reads and discards the header line

        // WRITE YOUR CODE HERE
        while (!StdIn.isEmpty()) {
            String[] recordEntries = StdIn.readLine().split(",");
            int year = Integer.parseInt(recordEntries[0]);

            String description = recordEntries[2];

            String gender = recordEntries[52];

            String race = recordEntries[66];

            String location = recordEntries[71];

            boolean arrested = recordEntries[13].equals("Y");

            boolean frisked = recordEntries[16].equals("Y");
    
            SFRecord newRecord = new SFRecord( description, arrested, frisked, 
            gender, race, location);
    
            // Check if the database already contains the year
            boolean yearExists = false;
            for (SFYear sfYear : database) {
                if (sfYear.getcurrentYear() == year) {
                    // Year exists, add the record to its designated year
                    sfYear.addRecord(newRecord);
                    yearExists = true;
                    break;
                }
            }
            // If year doesn't exist in the database, create a new SFYear and add the record
            if (!yearExists) {
                SFYear newYear = new SFYear(year);
                newYear.addRecord(newRecord);
                database.add(newYear);
            }
        }
    }

    /**
     * This method returns the stop and frisk records of a given year where 
     * the people that was stopped was of the specified race.
     * 
     * @param year we are only interested in the records of year.
     * @param race we are only interested in the records of stops of people of race. 
     * @return an ArrayList containing all stop and frisk records for people of the 
     * parameters race and year.
     */

    public ArrayList<SFRecord> populationStopped ( int year, String race ) {

        // WRITE YOUR CODE HERE
        ArrayList<SFRecord> records = new ArrayList<>();
        for (SFYear sfYear : database) {
            if (sfYear.getcurrentYear() == year) {
                for (SFRecord record : sfYear.getRecordsForYear()) {
                    if (record.getRace().equals(race)) {
                        records.add(record);
                    }
                }
                break;
            }
        }
        return records; // update the return value
    }

    /**
     * This method computes the percentage of records where the person was frisked and the
     * percentage of records where the person was arrested.
     * 
     * @param year we are only interested in the records of year.
     * @return the percent of the population that were frisked and the percent that
     *         were arrested.
     */
    public double[] friskedVSArrested ( int year ) {
        
        // WRITE YOUR CODE HERE
        double[] percentages = new double[2];
        int friskedCount = 0;
        int arrestedCount = 0;
        int totalRecords = 0;

        for (SFYear sfYear : database) {
            if (sfYear.getcurrentYear() == year) {
                for (SFRecord record : sfYear.getRecordsForYear()) {
                    if (record.getFrisked()) {
                        friskedCount++;
                    }
                    if (record.getArrested()) {
                        arrestedCount++;
                    }
                    totalRecords++;
                }
                break;
            }
        }

        if (totalRecords != 0) {
            percentages[0] = (friskedCount / (double) totalRecords) * 100;
            percentages[1] = (arrestedCount / (double) totalRecords) * 100;
        }

        return percentages; // update the return value
    }

    /**
     * This method keeps track of the fraction of Black females, Black males,
     * White females and White males that were stopped for any reason.
     * Drawing out the exact table helps visualize the gender bias.
     * 
     * @param year we are only interested in the records of year.
     * @return a 2D array of percent of number of White and Black females
     *         versus the number of White and Black males.
     */
    public double[][] genderBias ( int year ) {

        // WRITE YOUR CODE HERE
        double blackMales = 0.0;
        double whiteMales = 0.0;
        double blackFemales = 0.0;
        double whiteFemales = 0.0;
        double totalBlack = 0.0;
        double totalWhite = 0.0;

        // Access the specific year in the database array
        for (int i = 0; i < database.size(); i++)
        {
            if (database.get(i).getcurrentYear() == year)
            {
                ArrayList<SFRecord> records = (database.get(i)).getRecordsForYear();

                for (int j = 0; j < records.size(); j++)
                {
                    if (records.get(j).getRace().equals("B"))
                    {
                        totalBlack += 1;
                        if (records.get(j).getGender().equals("F")) 
                        {
                            blackFemales += 1;
                        }
                        else if(records.get(j).getGender().equals("M")) 
                        {
                            blackMales += 1;
                        }
                    }
                    else if(records.get(j).getRace().equals("W")) 
                    {
                        totalWhite += 1;
                        if (records.get(j).getGender().equals("F"))
                        {
                            whiteFemales += 1;
                        }
                        else if(records.get(j).getGender().equals("M"))
                        {
                            whiteMales += 1;
                        }
                    }
                }
            }
        }

        // Calculate percentages
        double[][] bias = new double[2][3];
        bias[0][0] = (blackFemales / totalBlack) * 0.5 * 100; // Black Female Percentage
        bias[0][1] = (whiteFemales / totalWhite) * 0.5 * 100; // White Female Percentage
        bias[0][2] = bias[0][0] + bias[0][1]; // Total Female Percentage
        bias[1][0] = (blackMales / totalBlack) * 0.5 * 100; // Black Male Percentage
        bias[1][1] = (whiteMales / totalWhite) * 0.5 * 100; // White Male Percentage
        bias[1][2] = bias[1][0] + bias[1][1]; // Total Male Percentage

        return bias; // update the return value
    }

    /**
     * This method checks to see if there has been increase or decrease 
     * in a certain crime from year 1 to year 2.
     * 
     * Expect year1 to preceed year2 or be equal.
     * 
     * @param crimeDescription
     * @param year1 first year to compare.
     * @param year2 second year to compare.
     * @return 
     */

    public double crimeIncrease ( String crimeDescription, int year1, int year2 ) {
        
        // WRITE YOUR CODE HERE

        double crimeCountYear1 = 0;
        double crimeCountYear2 = 0;
        double year1CrimeTotal = 0;
        double year2CrimeTotal = 0;

        for (int i = 0; i < database.size(); i++)
        {
            if (database.get(i).getcurrentYear() == year1)
            {
                ArrayList<SFRecord> year1Records = (database.get(i)).getRecordsForYear();
                for (int j = 0; j < year1Records.size(); j++)
                {
                    year1CrimeTotal += 1;
                    if (year1Records.get(j).getDescription().indexOf(crimeDescription) != -1)
                    {
                        crimeCountYear1 += 1;
                    }
                }
            }
        }

        for (int x = 0; x < database.size(); x++)
        {
            if (database.get(x).getcurrentYear() == year2)
            {
                ArrayList<SFRecord> year2Records = (database.get(x)).getRecordsForYear();
                for (int y = 0; y < year2Records.size(); y++)
                {
                    year2CrimeTotal += 1;
                    if (year2Records.get(y).getDescription().indexOf(crimeDescription) != -1)
                    {
                        crimeCountYear2 += 1;
                    }
                }
            }
        }
    
        // Calculate the percentage change
        double year1CrimePercentage = ((crimeCountYear1/year1CrimeTotal) * 100);
        double year2CrimePercentage = ((crimeCountYear2/year2CrimeTotal) * 100);
        double percentageChange = year2CrimePercentage - year1CrimePercentage;
    
        return percentageChange; // update the return value
    }

    /**
     * This method outputs the NYC borough where the most amount of stops 
     * occurred in a given year. This method will mainly analyze the five 
     * following boroughs in New York City: Brooklyn, Manhattan, Bronx, 
     * Queens, and Staten Island.
     * 
     * @param year we are only interested in the records of year.
     * @return the borough with the greatest number of stops
     */
    public String mostCommonBorough ( int year ) {

        // WRITE YOUR CODE HERE
        
        int BrooklynCounter = 0;
        int ManhattanCounter = 0;
        int BronxCounter = 0;
        int QueensCounter = 0;
        int StatenIslandCounter = 0;

        int[] counts = {BrooklynCounter, ManhattanCounter, BronxCounter, QueensCounter, StatenIslandCounter};
        String[] boroughs = {"Brooklyn", "Manhattan", "Bronx", "Queeens", "Staten Island"};


        for (int i = 0; i < database.size(); i++)
        {
            if (database.get(i).getcurrentYear() == year)
            {
                ArrayList<SFRecord> records = (database.get(i)).getRecordsForYear();
                for (int j = 0; j < records.size(); j++)
                {
                    for (int x = 0; x < 5; x++)
                    {
                        if (records.get(j).getLocation().equalsIgnoreCase(boroughs[x]))
                        {
                            counts[x] += 1;
                        }
                    }
                }
            }
        }

        int biggestCountNumber = 0;
        int largestIndex = 0;
        for (int i = 0; i < counts.length; i++)
        {
            if (counts[i] > biggestCountNumber)
            {
                biggestCountNumber = counts[i];
                largestIndex = i;
            }
        }
        

        return boroughs[largestIndex]; // update the return value
    }

}
