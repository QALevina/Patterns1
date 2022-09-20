package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;
import org.junit.jupiter.api.BeforeAll;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;



public class DataGenerator {


    private static Faker faker = new Faker(new Locale("ru"));

    private DataGenerator() {
    }

    public static String generateDate(int shift) {
        String date = LocalDate.now().plusDays(shift).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return date;
    }

    public static String generateCity(String locale) {
        var random = new SecureRandom();
        var list = Arrays.asList("Белгород", "Брянск", "Владимир", "Воронеж", "Иваново", "Калуга", "Кострома", "Курск", "Липецк", "Москва", "Орел", "Рязань", "Смоленск", "Тамбов", "Тверь", "Тула", "Ярославль",
                "Архангельск", "Вологда", "Калининград", "Петрозаводск", "Сыктывкар", "Санкт-Петербург", "Мурманск", "Салехард", "Великий Новгород", "Псков", "Санкт-Петербург", "Уфа", "Киров", "Йошкар-Ола", "Саранск", "Нижний Новгород",
                "Оренбург", "Пенза", "Пермь", "Самара", "Саратов", "Казань", "Ижевск", "Ульяновск", "Чебоксары", "Курган", "Екатеринбург", "Тюмень", "Ханты-мансийск", "Челябиннск", "Салехард", "Горно-Алтайск",
                "Барнаул", "Улан-Удэ", "Чита", "Иркутск", "Кемерово", "Красноярск", "Новосибирск", "Омск", "Томск", "Кызыл", "Абакан", "Благовещенск", "Биробиджан", "Петропавловск-Камчатский", "Магадан", "Владивосток",
                "Якутск", "Южно-Сахалинск", "Хабаровск", "Анадырь", "Майкоп", "Астрахань", "Волгоград", "Элиста", "Краснодар", "Ростов", "Махачкала", "Магас", "Нальчик", "Черкесск", "Владивкавказ", "Ставрополь", "Грозный");
        var randomElement = list.get(random.nextInt(list.size()));
        String city = randomElement;
        //String city = faker.address().cityName();
        return city;
    }

    public static String generateName(String locale) {
        String name = faker.name().fullName();
        return name;
    }

    public static String generatePhone(String locale) {
        String phone = faker.phoneNumber().phoneNumber();
        return phone;
    }



    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
            Faker faker = new Faker(new Locale(locale));
            return new UserInfo(generateCity(locale), generateName(locale),generatePhone(locale));
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;

    }



}
