package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
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
import static ru.netology.delivery.data.DataGenerator.generateDate;

class DeliveryTest {
    

    @BeforeEach
    void setupAll() {
        Configuration.holdBrowserOpen = true;
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
        $x("//div[@data-test-id='success-notification']/div[@class='notification__content']").shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15)).shouldBe(Condition.visible);

        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + secondMeetingDate);
        $x("//*[@class='button__text']").click();
        $x("//div[@data-test-id='replan-notification']/div/button").click();
        $x("//div[@class='notification__content']").shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15)).shouldBe(Condition.visible);;

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

        $x("//div[@data-test-id='success-notification']/div[@class='notification__content']").shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15)).shouldBe(Condition.visible);

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

        $x("//*[@data-test-id='city']").shouldHave(Condition.text("Доставка в выбранный город недоступна"),
                Duration.ofSeconds(15)).shouldBe(Condition.visible);

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

        $x("//*[@data-test-id='city']").shouldHave(Condition.text("Поле обязательно для заполнения"),
                Duration.ofSeconds(15)).shouldBe(Condition.visible);

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

        $x("//span[contains(@data-test-id,'date')]//span[@class='input__sub']").shouldHave(Condition.text("Заказ на выбранную дату невозможен"),
                Duration.ofSeconds(15)).shouldBe(Condition.visible);

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

        $x("//div[@data-test-id='success-notification']/div[@class='notification__content']").shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate),
                Duration.ofSeconds(15)).shouldBe(Condition.visible);

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

        $x("//span[contains(@data-test-id,'date')]//span[@class='input__sub']").shouldHave(Condition.text("Неверно введена дата"),
                Duration.ofSeconds(15)).shouldBe(Condition.visible);

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

        $x("//span[contains(@data-test-id,'name')]/span/span[contains(@class,'input__sub')]").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."),
                Duration.ofSeconds(15)).shouldBe(Condition.visible);

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

        $x("//span[contains(@data-test-id,'name')]/span/span[contains(@class,'input__sub')]").shouldHave(Condition.text("Поле обязательно для заполнения"),
                Duration.ofSeconds(15)).shouldBe(Condition.visible);

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

        $x("//span[contains(@data-test-id,'phone')]/span/span[@class='input__sub']").shouldHave(Condition.text("Поле обязательно для заполнения"),
                Duration.ofSeconds(15)).shouldBe(Condition.visible);

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

        $x("//label[contains(@class,'input_invalid')]/span[contains(@role,'presentation')]").shouldHave(Condition.text("Я соглашаюсь с условиями обработки и использования моих персональных данных"),
                Duration.ofSeconds(15)).shouldBe(Condition.visible);

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

        $x("//span[contains(@class,'input_invalid')]/span/span[contains(@class,'input__sub')]").shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."),
                Duration.ofSeconds(15)).shouldBe(Condition.visible);
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

        $x("//div[@data-test-id='success-notification']/div[@class='notification__content']").
                shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate),
                        Duration.ofSeconds(15)).shouldBe(Condition.visible);

    }


}
