package yoon.hw;

import org.checkerframework.checker.units.qual.C;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class Main {

    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver"; //드라이버 ID
    public static final String WEB_DRIVER_PATH = "C:\\Users\\82106\\chromedriver_win32\\chromedriver.exe"; //드라이버 경로

    public ClickThread thread;


    static class ClickThread extends Thread {
        WebDriver driver;

        ClickThread(WebDriver dri) {
            this.driver = dri;
        }
        public void run() {
            System.out.println("Start");

            while (true) {
                try {
                    Thread.sleep(10000);
                    driver.findElement(By.id("btnTimer")).click();
                }catch (Exception e) {

                }
            }
        }
    }


    public static void main(String[] args) {
        ClickThread thread;

        try {
            System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //크롬 설정을 담은 객체 생성
        ChromeOptions options = new ChromeOptions();
        //브라우저가 눈에 보이지 않고 내부적으로 돈다.
        //설정하지 않을 시 실제 크롬 창이 생성되고, 어떤 순서로 진행되는지 확인할 수 있다.
//        options.addArguments("headl ess");
        options.addArguments("disable-infobars");
        options.addArguments("enable-automation");
        options.addArguments("useAutomationExtension");

        WebDriver driver = new ChromeDriver(options);

        //이동을 원하는 url
        String url = "https://sugang.konkuk.ac.kr/";

        //WebDriver을 해당 url로 이동한다.
        driver.get(url);
        driver.switchTo().frame("Main");


        try {Thread.sleep(1000);} catch (InterruptedException e) {}


//#stdNo
        driver.findElement(By.id("stdNo")).sendKeys("dudwls143");
        driver.findElement(By.id("pwd")).sendKeys("@dudwlsdl12");

        driver.findElement(By.className("btn-login")).click();

        try {Thread.sleep(1000);} catch (InterruptedException e) {}

        driver.switchTo().frame("coreMain");

//        thread = new ClickThread(driver);
        Thread thread1 = new Thread(()->{
            while (true) {
                while (true) {
                    try {
                        Thread.sleep(10000);
                        driver.findElement(By.id("btnTimer")).click();
                    }catch (Exception e) {

                    }
                }
            }
        });
        thread1.start();
//        thread.run();



//        System.out.println(driver.getPageSource());

        driver.findElement(By.id("menu_search")).click();
        try {Thread.sleep(1000);} catch (InterruptedException e) {}



        driver.findElement(By.xpath("//*[@id=\"sForm\"]/table/tbody/tr[1]/td[2]/label[2]")).click();
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        js.executeScript("arguments[0].style.display = 'block';")



        Select cb = new Select(driver.findElement(By.id("pSustMjCd")));
        cb.selectByValue("127114");
        try {Thread.sleep(100);} catch (InterruptedException e) {}


        driver.findElement(By.id("btnSearch")).click();

        try {Thread.sleep(1000);} catch (InterruptedException e) {}


//            driver.findElement(By.xpath("/html/body/div[2]/main/div/div/div/div[1]/div[2]/div/div[3]/div[3]/div/table/tbody/tr[2]/td[18]/button")).click();

//        for (int i = 2; i <= 68 ; i++) {
//            driver.findElement(By.xpath("/html/body/div[2]/main/div/div/div/div[1]/div[2]/div/div[3]/div[3]/div/table/tbody/tr["+i+"]/td[18]/button")).click();
//            try {Thread.sleep(100);} catch (InterruptedException e) {}
//
//
//        }


//        try {Thread.sleep(100000);} catch (InterruptedException e) {}

//        System.out.println(driver.findElement(By.id("gridLecture")).getText());





//        driver.findElement(By.xpath("//*[@id=\"sTabs\"]/li[4]")).click();

//        System.out.println(driver.findElement(By.id("gridRegisteredCredits")).getText());


        //브라우저 이동시 생기는 로드시간을 기다린다.
        //HTTP응답속도보다 자바의 컴파일 속도가 더 빠르기 때문에 임의적으로 1초를 대기한다.


//        System.out.println(driver.findElement(By.className("nav-menu")).getText());


        //class="nav" 인 모든 태그를 가진 WebElement리스트를 받아온다.
        //WebElement는 html의 태그를 가지는 클래스이다.
//        List<WebElement> el1 = driver.findElements(By.className("nav"));
//
//        for (int i = 0; i < el1.size(); i++) {
//            //nav 클래스의 객체 중 "뉴스"라는 텍스트를 가진 WebElement를 클릭한다.
//            System.out.println(el1.get(i).getText());
//            if(el1.get(i).getText().equals("뉴스")) {
//                el1.get(i).click();
//                break;
//            }
//        }
//
//        //1초 대기
//        try {Thread.sleep(1000);} catch (InterruptedException e) {}

        //버튼을 클릭했기 때문에 브라우저는 뉴스창으로 이동돼 있다.
        //이동한 뉴스 창의 IT/과학 뉴스 헤드라인을 가져온다.

//
//        //iT/과학뉴스를 담은 div
//        WebElement el2 = driver.findElement(By.id("section_it"));
//
//        //div속에서 strong태그를 가진 모든 element를 받아온다.
//        List<WebElement> el3 = el2.findElements(By.tagName("strong"));
//
//        int count = 0;
//        for (int i = 0; i < el3.size(); i++) {
//            //뉴스의 제목을 모두 출력한다.
//            System.out.println(++count + "번 뉴스: "+ el3.get(i).getText());
//        }
//
//
//        try {
//            //드라이버가 null이 아니라면
//            if(driver != null) {
//                //드라이버 연결 종료
//                driver.close(); //드라이버 연결 해제
//
//                //프로세스 종료
//                driver.quit();
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }



    }


}
