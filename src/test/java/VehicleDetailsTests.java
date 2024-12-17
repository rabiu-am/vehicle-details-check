import com.opencsv.exceptions.CsvValidationException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class VehicleDetailsTests {

    private WebDriver driver;
    private WebDriverHelper webDriverHelper;
    private VehicleDetailsPage vehicleDetailsPage;


    @DataProvider(name = "testData")
    public Object[][] provideTestData() throws IOException {
        return Util.getTestData(Util.inputFilePath);
    }

    @BeforeMethod
    public void setUp() {
        webDriverHelper = new WebDriverHelper();
        driver = webDriverHelper.initializeDriver();
        // Initialize Page Object
        vehicleDetailsPage = new VehicleDetailsPage(driver);
    }


    /**
     * Verify vehicle details without entering mileage details
     * expected: 'value my car' button is clickable and then vehicle details are retrieved
     * assuming "just enter your registration number and we'll identify your car" as stated on the page
     */
    @Test(dataProvider = "testData", priority = 0)
    public void getVehicleDetailsWithOnlyRegistrationFieldFilled(String licenceNumber) {
        vehicleDetailsPage.visitPage();
        vehicleDetailsPage.enterRegistrationNumber(licenceNumber);
        vehicleDetailsPage.blankMileageField();
        vehicleDetailsPage.blankPostcodeField();

        assert vehicleDetailsPage.valueMyCarButtonIsClickable() : "Value my car button is not enabled when mileage and postcode are blank.";
    }


    /**
     * Verify vehicle details by entering all fields
     * result: page updates to display correct make, model, and registration of the vehicle
     * otherwise error message is displayed if invalid details
     * @throws IOException, CsvValidationException
    */
    @Test(dataProvider = "testData", priority = 1)
    public void getVehicleDetailsWithAllFormFieldsFilled(String licenceNumber) throws CsvValidationException, IOException {
        vehicleDetailsPage.visitPage();
        vehicleDetailsPage.enterRegistrationNumber(licenceNumber);
        vehicleDetailsPage.enterMileage("1000");
        vehicleDetailsPage.enterPostcode("TS198UF");
        vehicleDetailsPage.clickValueMyCarButton();

        /*
         * If vehicle registration is invalid, Error Message is present. Code in try block will be executed
         * If Vehicle registration is valid,  Error Message is NOT present. An Exception is thrown and code in catch block is executed
         */
        try{
            assert vehicleDetailsPage.errorMessageIsDisplayed() : "Invalid details entered and error message is NOT displayed";

        }catch (NoSuchElementException e) {
            //wait for vehicle details to load
            webDriverHelper.waitFor(ExpectedConditions.visibilityOfElementLocated(vehicleDetailsPage.vehicleImage));

            //vehicle details from web page
            String extractedVehicleRegistration = vehicleDetailsPage.getVehicleRegistration();
            String extractedVehicleMake = vehicleDetailsPage.getVehicleMake();
            String extractedVehicleModel = vehicleDetailsPage.getVehicleModel();

            //extract the vehicle make alone
            extractedVehicleMake = extractedVehicleMake.contains(" ") ? extractedVehicleMake.split(" ")[0] : extractedVehicleMake;
            //update full model details
            extractedVehicleModel += "\n" + vehicleDetailsPage.getFuelType() + "\n" + vehicleDetailsPage.getGearType();


            //get list of the expected vehicle details to compare
            List<Map<String, String>> expectedOutput = Util.csvToList(Util.outputFilePath);

            // set boolean flags to use for asserting the vehicle details match
            boolean registrationMatch = false;
            boolean makeMatch = false;
            boolean modelMatch = false;

            for (Map<String, String> row : expectedOutput) {
                //get  expected Model string and break into tokens for comparison
                List<String> expectedModelTokens = Arrays.asList(row.get("MODEL").split("\\s+"));

                //split extracted Model string into tokens to compare
                List<String> extractedModelTokens = new ArrayList<>(
                        Arrays.asList(extractedVehicleModel.split("\\s+")));
//                extractedModelTokens.add(fuelType);
//                extractedModelTokens.add(gearType);

                if (row.get("REGISTRATION").equalsIgnoreCase(extractedVehicleRegistration)) {
                    registrationMatch = true;
                }
                if (row.get("MAKE").equalsIgnoreCase(extractedVehicleMake)) {
                    makeMatch = true;
                }
                if (expectedModelTokens.containsAll(extractedModelTokens)) {
                    modelMatch = true;
                }

                // Break early if all matches are found
                if (registrationMatch && makeMatch && modelMatch) {
                    break;
                }
            }

            // Assertions
            assert registrationMatch : "Vehicle Registration does not match any entry in the CSV.";
            assert makeMatch : "Vehicle Make does not match any entry in the CSV.";
            assert modelMatch : "Vehicle Model details do not match entry in the CSV.";
        }

    }

    @AfterMethod
    public void tearDown() {
        webDriverHelper.shutdownDriver();
    }
}



