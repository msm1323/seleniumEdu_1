package ru.msm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RGS extends BaseClass{

    @Test
    public void applicationDMS_1() {

        closeAddFrame(By.xpath("//iframe[@class='flocktory-widget']"),
                By.xpath("//button[@class='CloseButton']"));

        // закрыть куки
        String cookiesClose = "//div[@class='btn btn-default text-uppercase']";
        WebElement cookiesBtnClose = driver.findElement(By.xpath(cookiesClose));
        cookiesBtnClose.click();

        //2. Выбрать Меню
        String menuButtonXPath = "//a[@data-toggle='dropdown' and contains(text(), 'Меню')]";
        WebElement menuButton = driver.findElement(By.xpath(menuButtonXPath));
        waitUtilElementToBeClickable(menuButton);
        menuButton.click();

        //3. Далее выбрать: Компаниям
        String comButtonXPath = "//div[@class='rgs-main-menu-category']//a[@href='https://www.rgs.ru/products/juristic_person/index.wbp']";
        WebElement comButton = driver.findElement(By.xpath(comButtonXPath));
        waitUtilElementToBeClickable(comButton);
        comButton.click();

        closeAddFrame(By.xpath("//iframe[@class='flocktory-widget']"),
                By.xpath("//div[@class='PushTip']//button[@class='PushTip-buttonItem']"));
        //закрыть уведомление разрешить/блокировать?

        //> Здоровье    (Страхование здоровья)
        String healthButtonXPath = "//div[@class='content-document']//a[@href='/products/juristic_person/health/index.wbp' " +
                "and contains(text(), 'здоровь')]";
        WebElement healthButton = driver.findElement(By.xpath(healthButtonXPath));
        waitUtilElementToBeClickable(healthButton);
        scrollToElementJs(healthButton);
        healthButton.click();

        switchToTabByText("ДМС для сотрудников");

        closeAddFrame(By.xpath("//iframe[@class='flocktory-widget']"),
                By.xpath("//div[@class='PushTip']//button[@class='PushTip-buttonItem']"));
        //закрыть уведомление разрешить/блокировать?

        //> Добровольное медицинское страхование
        String volHealthButtonXPath = "//a[@class='list-group-item adv-analytics-navigation-line4-link' and contains(text(), 'обровольное')]";
        WebElement volHealthButton = driver.findElement(By.xpath(volHealthButtonXPath));
        waitUtilElementToBeClickable(volHealthButton);
        volHealthButton.click();

        //4. Проверить наличие заголовка – Добровольное медицинское страхование
//        Assertions.assertEquals("Добровольное медицинское страхование в Росгосстрахе",
//                driver.getTitle(), "Заголовок отсутствует/не соответствует требуемому");
        String pageHeaderXPath = "//h1";
        waitUtilElementToBeVisible(By.xpath(pageHeaderXPath));
        WebElement pageHeader = driver.findElement(By.xpath(pageHeaderXPath));
        Assertions.assertEquals("Добровольное медицинское страхование",
                pageHeader.getText(),
                "Заголовок отсутствует/не соответствует требуемому");

        closeAddFrame(By.xpath("//iframe[@class='flocktory-widget']"),
                By.xpath("//div[@class='widget__close widget__close--ribbon']"));

        //5. Нажать на кнопку – Отправить заявку
        String sendRequestXPath = "//a[@data-product='CompanyVoluntaryMedicalInsurance' and contains(text(), 'Отправить заявку')]";
        WebElement sendRequestEl = driver.findElement(By.xpath(sendRequestXPath));
        sendRequestEl.click();

        //6. Проверить, что открылась страница, на которой присутствует текст – Заявка на добровольное медицинское страхование
        pageHeaderXPath = "//h4";   //или лучше создать новую.отдельную переменную?
        waitUtilElementToBeVisible(By.xpath(pageHeaderXPath));
        WebElement pageTitle = driver.findElement(By.xpath(pageHeaderXPath));
        Assertions.assertEquals("Заявка на добровольное медицинское страхование",
                pageTitle.getText(),
                "Заголовок отсутствует/не соответствует требуемому");

        //7. Заполнить поля: Имя, Фамилия, Отчество, Регион, Телефон, Эл. почта – qwertyqwerty, Комментарии, Я согласен на обработку
        //8. Проверить, что все поля заполнены введенными значениями
        String fieldXPath = "//form//*[@data-bind and @name='%s']";

        //  Фамилия
        fillInputField(String.format(fieldXPath, "LastName"), "Иванов");
        //  Имя
        fillInputField(String.format(fieldXPath, "FirstName"), "Иван");
        //  Отчество
        fillInputField(String.format(fieldXPath, "MiddleName"), "Иванович");

        //  Регион
        Select regionS = new Select(driver.findElement(By.xpath("//form//*[@data-bind and @name='Region']")));
        regionS.selectByVisibleText("Москва");

        boolean checkFlag = wait.until(ExpectedConditions.attributeContains(regionS.getWrappedElement(), "value", "77"));
        Assertions.assertTrue(checkFlag, "Поле было заполнено некорректно");

        //  Эл. почта
        fillInputField(String.format(fieldXPath, "Email"), "qwertyqwerty");

        //  Телефон
        String value = "4959999999";
        String valueAtt = String.format("+7 (%s) %s-%s-%s", value.substring(0,3), value.substring(3,6),
                value.substring(6,8), value.substring(8,10));
        fillInputField("//form//*[@data-bind and not(@name)]", value, valueAtt);

        //  Предпочитаемая дата контакта
        value = "18112021";
        valueAtt = String.format("%s.%s.%s", value.substring(0,2), value.substring(2,4), value.substring(4, 8));

        fillInputField("//form//*[@data-bind and @name='ContactDate']", value, valueAtt);

        //Комментарии
        fillInputField(String.format(fieldXPath, "Comment"), "Какой-то комментарий");

        //  Я согласен на обработку моих персональных данных...
        WebElement checkbox = driver.findElement(By.xpath("//form//*[@class='checkbox']"));
        checkbox.click();
        checkFlag = wait.until(ExpectedConditions.attributeContains(checkbox, "checked", "true"));
        Assertions.assertTrue(checkFlag, "Поле было заполнено некорректно");

        //9. Нажать Отправить
        WebElement send = driver.findElement(By.xpath("//form//button[@type='button']"));
        send.click();

        //10. Проверить, что у поля – Эл. почта присутствует сообщение об ошибке – Введите корректный email
        checkErrorMessageAtField(driver.findElement(By.xpath(String.format(fieldXPath, "Email"))), "Введите адрес электронной почты");

    }

    /**
     * Проверка ошибка именно у конкретного поля
     *
     * @param element      веб элемент (поле какое-то) которое не заполнили
     * @param errorMessage - ожидаемая ошибка под полем которое мы не заполнили
     */
    private void checkErrorMessageAtField(WebElement element, String errorMessage) {
        element = element.findElement(By.xpath("./..//span"));
        Assertions.assertEquals(errorMessage,
                element.getText(), "Проверка ошибки у поля не была пройдена");
    }

    /**
     * Заполнение полей определённым значений
     *
     * @param completeFieldXPath - XPath веб элемента (какого-то поля), которое планируется заполнить
     * @param value              - значение, которым заполнится веб элемент (какое-то поле)
     * @param valueAtt           - значение атрибута "value" веб элемента (какого-то поля)
     */
    private void fillInputField(String completeFieldXPath, String value, String valueAtt){
        WebElement element = driver.findElement(By.xpath(completeFieldXPath));
        scrollToElementJs(element);
        waitUtilElementToBeClickable(element);
        element.click();
        element.clear();
        element.click();
        element.sendKeys(value);
        boolean checkFlag = wait.until(ExpectedConditions.attributeContains(element, "value", valueAtt));
        Assertions.assertTrue(checkFlag, "Поле было заполнено некорректно");
    }

    private void fillInputField(String completeFieldXPath, String value) {
        fillInputField(completeFieldXPath, value, value);
    }

    private void switchToTabByText(String text) {
        String curTab = driver.getWindowHandle();
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        for (String tab : tabs) {
            if (!tab.equals(curTab)) {
                driver.switchTo().window(tab);
                if (driver.getTitle().contains(text)) {
                    return;
                }
            }
        }
        Assertions.fail("Вкладка " + text + " не найдена.");
    }

    private void closeAddFrame(By iframeBy, By by) {        //подходит только для одной вложенности
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        try {
            WebElement iframe = driver.findElement(iframeBy);
            driver.switchTo().frame(iframe);
            closeAdd(by);
        } catch (NoSuchElementException ignore) {

        } finally {
            driver.manage().timeouts().implicitlyWait(defaultWaitingTime, TimeUnit.SECONDS);
            driver.switchTo().parentFrame();
        }
    }

    private void closeAdd(By by) {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        try {
            WebElement addEl = driver.findElement(by);
            addEl.click();
        } catch (NoSuchElementException ignore) {

        } finally {
            driver.manage().timeouts().implicitlyWait(defaultWaitingTime, TimeUnit.SECONDS);
        }

    }

    /**
     * Скрол до элемента на js коде
     *
     * @param element - веб элемент, до которого нужно проскролить
     */
    private void scrollToElementJs(WebElement element) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Явное ожидание того что элемент станет кликабельный
     *
     * @param element - веб элемент до которого нужно проскролить
     */
    private void waitUtilElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Явное ожидание того что элемент станет видемым
     *
     * @param locator - локатор до веб элемент который мы ожидаем найти и который виден на странице
     */
    private void waitUtilElementToBeVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

}
