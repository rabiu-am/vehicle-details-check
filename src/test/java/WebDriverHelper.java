import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class WebDriverHelper {

    private WebDriver driver;
    public WebDriverWait wait;
    private static Duration waitTime = Duration.ofSeconds(5);


    public WebDriver initializeDriver(){
        //WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(Util.waitTime));

        return driver;
    }


    public void shutdownDriver(){
        if(driver != null) {
            driver.quit();
        }
    }

//    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//            wait.until(ExpectedConditions.visibilityOfElementLocated();

    public WebElement waitForExpectedElement(final By by, Duration waitTimeInSeconds) {
        try {
            wait = new WebDriverWait(driver, waitTimeInSeconds);
            return wait.until(visibilityOfElementLocated(by));
        } catch (NoSuchElementException | TimeoutException e) {
            // LOG.info(e.getMessage());
            return null;
        }
    }

    public <T> T waitFor(ExpectedCondition<T> condition) {
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        return wait.until(condition);
    }

}
