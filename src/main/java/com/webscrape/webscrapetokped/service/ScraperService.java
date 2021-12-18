package com.webscrape.webscrapetokped.service;

import lombok.AllArgsConstructor;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileWriter;

@Service
@AllArgsConstructor
public class ScraperService {
    public static final String URL = "https://www.tokopedia.com/p/handphone-tablet/handphone?page=";

    private ChromeDriver driver;

    @PostConstruct
    void postConstruct() throws InterruptedException, IOException {
        scrape("100");
    }

    public void scrape(final String value) throws InterruptedException, IOException {
        List <String> urlList = new ArrayList<String>();    // Initialize List for Every Product's URL (String)
        int pageNumber = 1;                                 // Page Number
        while(urlList.size() < Integer.parseInt(value)){    // Loop until the amount of the required product reached
            driver.get(URL + pageNumber);                   // Request to Tokopedia's Page

            Thread.sleep(5000);                        // Delay 5 seconds to make sure page full loaded

            //Since Tokopedia only load the page after the user scroll down the page, here is the scroll down script
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,60000)");
            Thread.sleep(2000);
            js.executeScript("window.scrollBy(0,60000)");
            Thread.sleep(2000);
            js.executeScript("window.scrollBy(0,60000)");
            Thread.sleep(2000);

            // Initialize List for Every Product's URL (WebElement)
            final List<WebElement> wordlist = driver.findElements(By.xpath("//div[@class='css-bk6tzz e1nlzfl3']/a"));

            // Store every URL from WebElement to urlList
            for (int i = 0; i < wordlist.size(); i++){
                if(urlList.size() < Integer.parseInt(value)){
                    String linkUrl = wordlist.get(i).getAttribute("href");
                    Map<String, String> query = getQueryMap(linkUrl);

                    // This is if Promotional Product included
                    // if (query.get("r") != null) {
                    //    urlList.add(decodeValue(query.get("r")));
                    // } else{
                    //    urlList.add(linkUrl);
                    // }
                    if (query.get("r") == null) {
                        System.out.println(linkUrl);
                        urlList.add(linkUrl);
                    }
                }
            }
            pageNumber++;
        }

        // Total Product
        System.out.println("total items: "+urlList.size());

        // Initialize File and Filewriter for the CSV
        File file = new File("C:\\WebScrapTop100PhoneTokped\\top-100-phones.csv");
        file.getParentFile().mkdirs();
        FileWriter top100Phones = new FileWriter(file);
        top100Phones.write("id,name,description,image_link,price,rating,merchant\n");

        // Iterating every Product's URL
        for (int i = 0; i < urlList.size(); i++) {
            // Store Product's URL to a variable (productUrl)
            String productUrl = urlList.get(i);

            // An Exception in case there is an error from product's Page
            Boolean staleElement = true;
            while(staleElement){
                try{
                    System.out.println("Collecting Product Number " + (i+1) + " Data");

                    // Request to Product's Page
                    driver.get(productUrl);

                    // Make sure web fully loaded and we able to interact with some button on this page
                    WebDriverWait wait = new WebDriverWait(driver, 600);
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='css-k2r3u4-unf-btn e1ggruw00']")));

                    // Scroll down to make sure page fully loaded
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("window.scrollBy(0,60000)");

                    Thread.sleep(2000);

                    // Get every data we needed by Its class name, element name, and else
                    String title = driver.findElementByClassName("css-1wtrxts").getText();
                    String description = driver.findElement(By.xpath("//div[@class='css-1k1relq']//div")).getText().replace('"', ' ');
                    String imageLink = driver.findElement(By.xpath("//div[@class='css-cbnyzd active']/div[2]/img")).getAttribute("src");
                    String price = driver.findElement(By.xpath("//div[@class='css-aqsd8m']/div[@class='price']")).getText().replace("Rp", "").replace(".","");
                    String rating = driver.findElement(By.xpath("//div[@class='css-7fidm1']/div[@class='items']/div[2]//span[@class='main']")).getText();
                    String store = driver.findElement(By.xpath("//div[@class='css-12gb68h']//h2")).getText();

                    // Prepare for the CSV
                    top100Phones.write(i + "," + appendDQ(title) + "," + appendDQ(description) + "," + appendDQ(imageLink) + "," + appendDQ(price) + "," + appendDQ(rating) + "," + appendDQ(store) + "\n");
                    top100Phones.flush();
                    staleElement = false;
                } catch(Exception e){
                    staleElement = true;
                    // This is if we want to skip the error product
                    // System.out.println("Product No " + (i+1) + " Skipped");
                    // i++;
                    // System.out.println(e.getMessage());
                    driver.navigate().refresh();
                }
            }
        }

        // Write it on CSV
        top100Phones.write("id,name,link\n");
    }

    // Split encrypted link
    public static Map<String, String> getQueryMap(String query) {
        String[] firstProcess = query.split("\\?");
        String[] params = firstProcess[1].split("&");
        Map<String, String> map = new HashMap<String, String>();

        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }

        return map;
    }

    // URL Decoding
    public static String decodeValue(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    // Put \ for long text on CSV
    private static String appendDQ(String str) {
        return "\"" + str + "\"";
    }
}
