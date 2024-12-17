import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class VehicleDetailsPage {

    private WebDriver driver;

    //page elements
    By registrationField = By.name("vrm");
    By mileageField = By.name("mileage");
    By postcodeField = By.name("postcode");
    By valueMyCarButton = By.xpath("//button[@class='btn btn--spinner  btn--expand']");

    By errorMessage = By.cssSelector(".err.err__msg-box");
    By vehicleMake = By.xpath("//h3[@class='title-5 no-margin']");
    By vehicleModel = By.xpath("//h4[@class='no-margin']");
    By vehicleRegistration = By.xpath("//div[@class='vyv__number-plate']");
    By fuelType = By.xpath("//html/body/section/div/div/section/ol/li[2]/section/div/div[1]/ul/li[2]");
    By gearType = By.xpath("/html/body/section/div/div/section/ol/li[2]/section/div/div[1]/ul/li[3]");
    By vehicleImage = By.xpath("/html/body/section/div/div/section/ol/li[2]/section/div/div[1]/div/div[1]/div/img");


    public VehicleDetailsPage(WebDriver _driver){
        this.driver = _driver;
    }


    public void visitPage(){
        driver.get(Util.baseUrl);
        // Handle cookies
        handleCookies();
    }

    public void enterRegistrationNumber(String regNumber) {
        driver.findElement(registrationField).clear();
        driver.findElement(registrationField).sendKeys(regNumber);
    }

    public void blankMileageField(){
        driver.findElement(mileageField).clear();
        driver.findElement(mileageField).sendKeys(Keys.TAB);
    }

    public void blankPostcodeField(){
        driver.findElement(postcodeField).clear();
        driver.findElement(postcodeField).sendKeys(Keys.TAB);
    }

    public void enterMileage(String mileage){
        driver.findElement(mileageField).clear();
        driver.findElement(mileageField).sendKeys(mileage);
    }

    public void enterPostcode(String postcode) {
        driver.findElement(postcodeField).clear();
        driver.findElement(postcodeField).sendKeys(postcode);
    }

    public boolean valueMyCarButtonIsClickable(){
        return driver.findElement(valueMyCarButton).isEnabled();
    }

    public void clickValueMyCarButton(){
        driver.findElement(valueMyCarButton).click();
    }

    public boolean errorMessageIsDisplayed(){
        return driver.findElement(errorMessage).isDisplayed();
    }

    public String getVehicleRegistration(){
        return driver.findElement(vehicleRegistration).getText();
    }

    public String getVehicleMake(){
        return driver.findElement(vehicleMake).getText();
    }

    public String getVehicleModel(){
        return driver.findElement(vehicleModel).getText();
    }

    public String getFuelType(){
        return driver.findElement(fuelType).getText();
    }

    public String getGearType(){
        return driver.findElement(gearType).getText();
    }


    public void handleCookies() {
        List<WebElement> popups = driver.findElements(By.id("onetrust-accept-btn-handler"));
        if (!popups.isEmpty()) {
            popups.getFirst().click();
        }
    }

}

