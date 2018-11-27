package com.creditcards;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SeleniumExcercise {
	public WebDriver driver;
	String url;
	Actions action;
	private static final String CHROME_DRIVER_PATH = "src\\main\\resources\\chromedriver_win32\\chromedriver.exe";
	private static final String CHROME_DRIVER_KEY = "webdriver.chrome.driver";
	private static final String INSTANCE_URL = "https://www.creditcards.com";
	private static final String LANDINGPAGE_TITLE = "Credit Cards - Compare Credit Card Offers at CreditCards.com";
	private static final String AIRLINECARDS_PAGE_TITLE = "Best Airline Miles Credit Cards of 2018 - CreditCards.com";
	private static final By CARD_CATEGORY_XPATH = By
			.xpath("//a[contains(@class, 'menu__item-label') and text() = 'Card category']");
	private static final String BLUE_COLOR_CODE = "rgba(21, 66, 248, 1)";
	private static final String CARDMATCH_PAGE_TITLE = "See Pre-Qualified Credit Card Offers in CardMatch - CreditCards.com";
	private static final String CARD_ISSUER_XPATH = "//a[contains(@class, 'menu__item-label') and text() = 'Card issuer']";
	private static final By AIRLINE_XPATH = By.xpath("//a[contains(@href,'/airline-miles/')]");
	private static final By AMEX_XPATH = By.xpath("//a[contains(@href,'/american-express/')]");
	private static final By FAIR_CSSSELECTOR = By.cssSelector("label[class='cardFinder__tab'][for='fair']");
	private static final By CARDMATCH_CSS = By.cssSelector("div[class$='cardMatch']");
	private static final By FIRSTNAME_NAME = By.name("name[first]");
	private static final By LASTNAME_NAME = By.name("name[last]");
	private static final By GETMATCHES_CSS = By.cssSelector("button[type='submit']");
	private static final By LOGO_CSS = By.cssSelector("a[href$='creditcards.com']");
	private static final By REURIED_XPATH=By.xpath("//div[contains(text(),'Required')]");
	
	@BeforeMethod
	public void setUp() {
		System.setProperty(CHROME_DRIVER_KEY, CHROME_DRIVER_PATH);
		driver = new ChromeDriver();
		// driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		//driver.manage().window().maximize();
		url = INSTANCE_URL;
		driver.get(url);
		action = new Actions(driver);
	}
	
	@AfterMethod
	public void cleanUp() {
		driver.quit();
	}
	
	@Test(priority=1)
	public void pageTitleTest() {
		String title = driver.getTitle();
		assertEquals(title, LANDINGPAGE_TITLE);
	}
	
	@Test(priority=2)
	public void cardIssuerDropDownTest() {		
		WebElement cardissuer = driver
				.findElement(By.xpath(CARD_ISSUER_XPATH));
		Action act = action.clickAndHold(cardissuer).build();
		act.perform();
		assertTrue(driver.findElement(AMEX_XPATH).isDisplayed());
		try {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File("reports/screenshot.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		act = action.release(cardissuer).build();
		act.perform();
	}
	
	@Test(priority=3)
	public void pageNavigationTest() {
		WebElement we = driver.findElement(CARD_CATEGORY_XPATH);
		action.moveToElement(we).perform();
		driver.findElement(AIRLINE_XPATH).click();

		String airlinetitle = driver.getTitle();
		assertEquals(airlinetitle, AIRLINECARDS_PAGE_TITLE);
		driver.navigate().back();
		assertEquals(driver.getTitle(), LANDINGPAGE_TITLE);
	}
	
	@Test(priority=4)
	public void cssColorValidationTest() {
		WebElement fairscore = driver.findElement(FAIR_CSSSELECTOR);
		action.moveToElement(fairscore).perform();
		assertEquals(BLUE_COLOR_CODE, fairscore.getCssValue("color"));
	}
	
	@Test(priority=5)
	public void cardMatchPageFormValidation() {
		WebElement fairscore = driver.findElement(FAIR_CSSSELECTOR);
		action.moveToElement(fairscore).perform();
		fairscore.click();		
		driver.findElement(CARDMATCH_CSS).click();
		driver.findElement(FIRSTNAME_NAME).sendKeys("FIRST NAME");
		driver.findElement(LASTNAME_NAME).sendKeys("LASTNAME");
		driver.findElement(GETMATCHES_CSS).submit();	
		assertTrue(driver.findElement(REURIED_XPATH).isDisplayed());
		driver.findElement(LOGO_CSS).click();
	}
	

	@Test(priority=6)
	public void logoLinkTest() {
		WebElement fairscore = driver.findElement(FAIR_CSSSELECTOR);
		action.moveToElement(fairscore).perform();
		fairscore.click();
		driver.findElement(CARDMATCH_CSS).click();
		assertEquals(driver.getTitle(), CARDMATCH_PAGE_TITLE);
		driver.findElement(LOGO_CSS).click();
		assertEquals(driver.getTitle(), LANDINGPAGE_TITLE);
	}

}