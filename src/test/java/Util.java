import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static String inputFilePath = "src/test/resources/car_input_v2.txt";
    public static String outputFilePath = "src/test/resources/car_output_v2.txt";
    public static String baseUrl = "https://www.motors.co.uk/sell-my-car/car-valuation/";

    public static int waitTime = 10;
    //String browserName = "chrome";

    /**
     * this method reads data from a file
     * @param path: path to the file
     * @return a String containing the lines of the file
     */
    public static String retrieveFileContent(String path) throws IOException {

        StringBuilder fileContent = new StringBuilder();

        //reading from file
        FileInputStream inputStream = new FileInputStream(path);
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = fileReader.readLine()) != null) {
            fileContent.append(line).append("\n");
        }
        fileReader.close();
        return fileContent.toString();
    }


    /**
     * this method extracts UK car registration numbers from a file
     * @param path: path to file
     * @return a List containing valid car reg numbers
     */
    public static List<String> extractVehicleRegs(String path) throws IOException {

        //get the data
        String dataFromFile = retrieveFileContent(path);

        //to store valid reg numbers
        List<String> validVehicleRegs = new ArrayList<>();

        //extract vehicle regs based on UK pattern eg AB17 CDE OR ab12cde
        String vehicleRegistrationPattern = "([a-zA-z]{2}[0-9]{2}\\s[a-zA-z]{3})|([a-zA-z]{2}[0-9]{2}[a-zA-z]{3})";
        Pattern pattern = Pattern.compile(vehicleRegistrationPattern);
        Matcher matcher = pattern.matcher(dataFromFile);

        while(matcher.find()){
            validVehicleRegs.add(matcher.group());
        }

        return validVehicleRegs;
    }


    /**
     * this method prepares test data
     * by converting a list of registration numbers gotten from input file to 2-dimensional array
     * @param path: path to file
     * @return a 2-dimensional array containing valid car reg numbers
     */
    public static Object[][] getTestData(String path) throws IOException {
        //get the list of vehicle registrations
        List<String> vehicleRegList = extractVehicleRegs(path);

        //update the List items to have Vehicle registrations without spacing
        List<String> updatedList = new ArrayList<>();
        for (String s : vehicleRegList) {
            String joinedRegNumber = String.join("", s.split(" "));
            updatedList.add(joinedRegNumber);
        }

        //convert the updated list to object[][]
        Object[][] result = new Object[updatedList.size()][1];

        for (int i = 0; i < updatedList.size(); i++){
            result[i][0] = updatedList.get(i);
        }
        return result;
    }


    /**
     * this method extracts a csv structured data from a file
     * @param path: path to file to be read
     * @return a List of maps containing the data
     */
    public static List<Map<String,String>> csvToList(String path) throws IOException, CsvValidationException {

        List<Map<String, String>> carsList = new ArrayList<>();

        CSVReader csvReader = new CSVReader(new FileReader(path));
        String[] headers = csvReader.readNext();    //get header
        String[] values;

        while ((values = csvReader.readNext()) != null) {
            if (values.length == 1) continue;   //skip empty lines

            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                map.put(headers[i], values[i]);
            }
            carsList.add(map);
        }
        csvReader.close();
        return carsList;
    }
}
