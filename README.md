# Brick - Tokopedia Web Scrapping (Top 100 of Mobile Products) using Java and Selenium

<a href="https://selenium.dev"><img src="https://selenium.dev/images/selenium_logo_square_green.png" width="180" alt="Selenium"/></a>

The project is made by Jibran Fawaid

## Utilities
* Java: version "1.8.0_311"
* SpringBoot: version "2.6.1" 
* Lombok: version "1.18.20"
* Selenium: version "3.141.59" 
* ChromeDriver: "96.0.4664.45"
* Intellij 2021.3

## How it Works
1. I downloaded <a href="https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-java">Selenium</a> as dependencies of 
   this program. Based on what I have learned about <a href="https://www.tokopedia.com/p/handphone-tablet/handphone">Mobile Phone Products</a>'s page on Tokopedia, it needs a javascript to load every elements. Also, I already used a borderless web request like HtmlUnit and it doesn't work.
2. Before I start to scrape the web, I prepare a Configuration and ScraperService to initialize the <a href="https://chromedriver.chromium.org/downloads">WebDriver </a>
2. From the pattern that I have learned, we need to scrolldown the page to load every product
<img src="https://github.com/JibranFawaid/BrickTokopediaWebScrapper/blob/main/images/ScrollDown.png?raw=true"></img>
After I request <a href="https://www.tokopedia.com/p/handphone-tablet/handphone">Mobile Phone Products</a>'s page, I trigger the page with a javascript to scroll it down.
3. After every product loaded, This program collecting every Product's URL (Limited by 100).
4. All of these URL will be requested later to get every data we need.
<img src="https://github.com/JibranFawaid/BrickTokopediaWebScrapper/blob/main/images/Pass%20Data.png?raw=true"></img>
*i also prepared in case Promotional Product included*
5. Using FileWriter, every data will be stored in CSV file
<img src="https://github.com/JibranFawaid/BrickTokopediaWebScrapper/blob/main/images/File%20Writer.png?raw=true"></img>
6. Every product will be requested and the program will collecting the data that we needed. After that, the program will write it down in CSV file
<img src="https://github.com/JibranFawaid/BrickTokopediaWebScrapper/blob/main/images/Write%20Every%20Data.png?raw=true"></img>
7. Result of this program can be looked at result/top-100-phones.csv

<hr>
Sincerely,
Jibran Fawaid
