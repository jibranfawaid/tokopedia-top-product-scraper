package com.webscrape.webscrapetokped.configuration;

import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class SeleniumConfiguration
{
    @PostConstruct
    void postConstruct()
    {
        System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriver\\chromedriver.exe");
    }
    @Bean
    public ChromeDriver driver()
    {
        return new ChromeDriver();
    }
}
