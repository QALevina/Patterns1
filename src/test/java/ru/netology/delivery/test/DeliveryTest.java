package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;
import java.util.Locale;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.delivery.data.DataGenerator.generateDate;

class DeliveryTest {

    private static Faker faker;

    @BeforeAll
    static void setUpAll() {

        faker = new Faker(new Locale("ru"));
    }

    @BeforeEach
    void setupAll() {
        //Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");


    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 3;
        var firstMeetingDate = generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = generateDate(daysToAddForSecondMeeting);


        $x("//*[@placeholder='Город']").setValue(validUser.getCity());
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + firstMeetingDate);
        $x("//*[@name='name']").setValue(validUser.getName());
        $x("//*[@name='phone']").setValue(validUser.getPhone());
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $x("//form").should(Condition.visible, Duration.ofSeconds(15));

        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + secondMeetingDate);
        $x("//*[@class='button__text']").click();
        $x("//div[@data-test-id='replan-notification']/div/button").click();


        String planningDateText = $x("//div[@class='notification__content']").getText();
        assertEquals("Встреча успешно запланирована на " + secondMeetingDate, planningDateText);
    }

    @Test
    void happyPath() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 3;
        var firstMeetingDate = generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = generateDate(daysToAddForFirstMeeting);

        $x("//*[@placeholder='Город']").setValue(validUser.getCity());
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + firstMeetingDate);
        $x("//*[@name='name']").setValue(validUser.getName());
        $x("//*[@name='phone']").setValue(validUser.getPhone());
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $x("//form").should(Condition.visible, Duration.ofSeconds(15));

        String text = $x("//*[@class='notification__title']").getText();
        assertEquals("Успешно!", text);

        String planningDateText = $x("//div[@class='notification__content']").getText();
        assertEquals("Встреча успешно запланирована на " + firstMeetingDate, planningDateText);

    }


    @Test
    void invalidCity() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 3;
        var firstMeetingDate = generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = generateDate(daysToAddForSecondMeeting);

        $x("//*[@placeholder='Город']").doubleClick().sendKeys(Keys.DELETE + "Moc");
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + firstMeetingDate);
        $x("//*[@name='name']").setValue(validUser.getName());
        $x("//*[@name='phone']").setValue(validUser.getPhone());
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        String text = $x("//*[@data-test-id='city']").getText();
        assertEquals("Доставка в выбранный город недоступна", text);

    }

    @Test
    void invalidNoCity() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 3;
        var firstMeetingDate = generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = generateDate(daysToAddForSecondMeeting);

        $x("//*[@placeholder='Город']").setValue("");
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + firstMeetingDate);
        $x("//*[@name='name']").setValue(validUser.getName());
        $x("//*[@name='phone']").setValue(validUser.getPhone());
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();

        String text = $x("//*[@data-test-id='city']").getText();
        assertEquals("Поле обязательно для заполнения", text);

    }

    @Test
    void selectPastDate() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 2;
        var firstMeetingDate = generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = generateDate(daysToAddForSecondMeeting);

        $x("//*[@placeholder='Город']").setValue(validUser.getCity());
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + firstMeetingDate);
        $x("//*[@name='name']").setValue(validUser.getName());
        $x("//*[@name='phone']").setValue(validUser.getPhone());
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();

        String text = $x("//span[contains(@data-test-id,'date')]//span[@class='input__sub']").getText();
        assertEquals("Заказ на выбранную дату невозможен", text);

    }

    @Test
    void selectFutureDate() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = generateDate(daysToAddForSecondMeeting);


        $x("//*[@placeholder='Город']").setValue(validUser.getCity());
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + firstMeetingDate);
        $x("//*[@name='name']").setValue(validUser.getName());
        $x("//*[@name='phone']").setValue(validUser.getPhone());
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $x("//form").should(Condition.visible, Duration.ofSeconds(15));

        String planningDateText = $x("//div[@data-test-id='success-notification']/div[@class='notification__content']").getText();
        assertEquals("Встреча успешно запланирована на " + firstMeetingDate, planningDateText);

    }

    @Test
    void invalidNoDate() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = generateDate(daysToAddForSecondMeeting);

        $x("//*[@placeholder='Город']").setValue(validUser.getCity());
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE);
        $x("//*[@name='name']").setValue(validUser.getName());
        $x("//*[@name='phone']").setValue(validUser.getPhone());
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();

        String text = $x("//span[contains(@data-test-id,'date')]//span[@class='input__sub']").getText();
        assertEquals("Неверно введена дата", text);

    }

    @Test
    void invalidName() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = generateDate(daysToAddForSecondMeeting);

        $x("//*[@placeholder='Город']").setValue(validUser.getCity());
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + firstMeetingDate);
        $x("//*[@name='name']").setValue("Elena");
        $x("//*[@name='phone']").setValue(validUser.getPhone());
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();

        String text = $x("//span[contains(@data-test-id,'name')]/span/span[contains(@class,'input__sub')]").getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text);
    }

    @Test
    void invalidNoName() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = generateDate(daysToAddForSecondMeeting);


        $x("//*[@placeholder='Город']").setValue(validUser.getCity());
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + firstMeetingDate);
        $x("//*[@name='name']").setValue("");
        $x("//*[@name='phone']").setValue(validUser.getPhone());
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();

        String text = $x("//span[contains(@data-test-id,'name')]/span/span[contains(@class,'input__sub')]").getText();
        assertEquals("Поле обязательно для заполнения", text);
    }


    @Test
    void invalidNoPhone() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = generateDate(daysToAddForSecondMeeting);


        $x("//*[@placeholder='Город']").setValue(validUser.getCity());
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + firstMeetingDate);
        $x("//*[@name='name']").setValue(validUser.getName());
        $x("//*[@name='phone']").setValue("");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        String text = $x("//span[contains(@data-test-id,'phone')]/span/span[@class='input__sub']").getText();
        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void invalidNoClikCheckBox() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = generateDate(daysToAddForSecondMeeting);


        $x("//*[@placeholder='Город']").setValue(validUser.getCity());
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + firstMeetingDate);
        $x("//*[@name='name']").setValue(validUser.getName());
        $x("//*[@name='phone']").setValue(validUser.getPhone());
        $x("//*[@class='button__text']").click();

        String text = $x("//label[contains(@class,'input_invalid')]/span[contains(@role,'presentation')]").getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных", text);


    }

    // !!! These tests are falling

    @Test
    void invalidPhone() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = generateDate(daysToAddForSecondMeeting);

        $x("//*[@placeholder='Город']").setValue(validUser.getCity());
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + firstMeetingDate);
        $x("//*[@name='name']").setValue(validUser.getName());
        $x("//*[@name='phone']").setValue("+999");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        String text = $x("//span[contains(@class,'input_invalid')]/span/span[contains(@class,'input__sub')]").getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }


    @Test
    void invalidExistingRussianLetter() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 3;
        var firstMeetingDate = generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = generateDate(daysToAddForFirstMeeting);

        $x("//*[@placeholder='Город']").setValue(validUser.getCity());
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + firstMeetingDate);
        $x("//*[@name='name']").setValue("Семён");
        $x("//*[@name='phone']").setValue(validUser.getPhone());
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $x("//form").should(Condition.visible, Duration.ofSeconds(15));



        String planningDateText = $x("//div[@data-test-id='success-notification']/div[@class='notification__content']").getText();
        assertEquals("Встреча успешно запланирована на " + firstMeetingDate, planningDateText);

    }


}
