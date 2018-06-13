package pl.altkom.shop;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.altkom.shop.model.Product;
import pl.altkom.shop.repo.ProductRepo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CoreConfig.class })
// @ActiveProfiles(Profiles.TEST)
public class ProductAcceptanceTest {

	@Inject
	ProductRepo repo;
	private ChromeDriver driver;

	@Rule
	public TestWatcher watchman = new TestWatcher() {
		@Override
		protected void failed(Throwable e, Description description) {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			try {
				FileUtils.copyFile(scrFile, new File("c:\\tmp\\" + description.getMethodName() + ".png"));
			} catch (IOException e1) {
				throw new RuntimeException(e);
			}
		}

		@Override
		protected void finished(Description description) {
			driver.quit();
		}
	};

	@Before
	public void setup() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\Mirek\\Downloads\\chromedriver_win32(3)\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost:8060/spring-shop/");
	}

	@Test
	@IfProfileValue(name = "os.name", value = "Windows 7")
	public void shouldShowProductList() throws Exception {
		// given
		WebElement username = driver.findElement(By.name("username"));
		WebElement password = driver.findElement(By.name("password"));
		username.sendKeys("user");
		password.sendKeys("user");
		password.submit();

		// when
		driver.findElement(By.linkText("Products list")).click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		// then
		List<WebElement> findElements = driver.findElements(By.tagName("tr"));
		assertThat(findElements).isNotEmpty();

	}

	@Test
	@IfProfileValue(name = "os.name", value = "Windows 7")
	public void shouldDeleteProduct() throws Exception {
		// given
		WebElement username = driver.findElement(By.name("username"));
		WebElement password = driver.findElement(By.name("password"));
		username.sendKeys("user");
		password.sendKeys("user");
		password.submit();

		Product product = new Product("SSD" + System.currentTimeMillis(), "Szybki", 10, BigDecimal.TEN);
		repo.insert(product);
		Long afterInsertCount = repo.count();

		driver.findElement(By.linkText("Products list")).click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		// when
		driver.findElement(By.cssSelector("a[href*='" + product.getId() + "/delete']")).click();

		// then
		assertThat(repo.count()).isEqualTo(afterInsertCount - 1);

	}

}
