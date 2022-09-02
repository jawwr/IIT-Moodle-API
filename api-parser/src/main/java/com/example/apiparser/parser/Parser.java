package com.example.apiparser.parser;

import com.example.apiparser.models.Event;
import com.example.apiparser.models.Mark;
import com.example.apiparser.models.UserInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Parser {
    WebDriver driver;
    ChromeOptions options = new ChromeOptions();

    {
        options.addArguments("incognito");
        options.addArguments("headless");
        options.addArguments("disable-extensions");
        options.addArguments("start-maximized");
        options.addArguments("disable-infobars");
    }

    private void entry(String login, String password) {
        driver = new ChromeDriver(options);
        driver.get("https://eu.iit.csu.ru/login/");

        String xPathUsername = "//*[@id=\"username\"]";
        String xPathPassword = "//*[@id=\"password\"]";

        driver.findElement(By.xpath(xPathUsername)).sendKeys(login);
        driver.findElement(By.xpath(xPathPassword)).sendKeys(password);

        var entryBtn = driver.findElement(By.id("loginbtn"));
        entryBtn.click();
    }

    public UserInfo parseUserInfo(String login, String password) {
        entry(login, password);

        var userDetailBtnXpath = "//*[@id=\"action-menu-1-menu\"]/a[2]";
        var btn = driver.findElement(By.xpath(userDetailBtnXpath));
        var href = btn.getDomProperty("href");

        driver.get(href);

        String[] userFullName = driver.findElement(By.className("page-header-headings")).getText().split(" ");
        String group = driver.findElement(By.xpath("//*[@id=\"region-main\"]/div/div/div/section[1]/div/ul/li[6]/dl/dd"))
                .getText();

        UserInfo user = new UserInfo();
        user.setLogin(login);
        user.setPassword(password);
        user.setName(userFullName[1]);
        user.setSurname(userFullName[0]);
        user.setGroupName(group);

        driver.close();

        return user;
    }

    public List<Event> parseEvents(String login, String password, String groupName) {
        entry(login, password);

        String calendarText = driver.findElement(By.tagName("body"))
                .findElement(By.className("calendarwrapper"))
                .findElement(By.tagName("tbody"))
                .getText();

        driver.close();

        return validateEvents(calendarText, groupName);
    }

    private List<Event> validateEvents(String calendar, String group) {
        List<Event> events = new ArrayList<>();
        var days = calendar.split("\n");
        for (int i = 0; i < days.length - 1; i++) {
            if (i % 2 == 0 || days[i + 1].contains("No events")) {
                continue;
            }
            events.add(new Event(days[i + 1], days[i], "", group));
        }
        return events;
    }

    public List<Mark> parseMarks(String login, String password) {
        entry(login, password);

        driver.get("https://eu.iit.csu.ru/grade/report/overview/index.php");

        var table = driver.findElement(By.xpath("//*[@id=\"overview-grade\"]")).findElement(By.tagName("tbody"));

        var courseName = table.findElements(By.tagName("a"));
        var courseMarks = table.findElements(By.className("c1"));

        List<Mark> marks = new ArrayList<>();

        for (int i = 0; i < courseName.size(); i++) {
            marks.add(new Mark(courseName.get(i).getText(), courseMarks.get(i).getText()));
        }

        driver.close();

        return marks;
    }
}
