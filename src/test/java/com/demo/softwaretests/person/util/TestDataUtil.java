package com.demo.softwaretests.person.util;

import com.demo.softwaretests.person.entity.Person;

import java.util.List;

public class TestDataUtil {

    private TestDataUtil() {}

    private static final Person richard;
    private static final Person guenther;
    private static final Person lilliane;
    private static final Person bianca;

    static {
        richard = new Person();
        richard.setFullName("Richard Rüdiger");
        richard.setEmailAddress("richard.ruediger@gmail.com");
        richard.setAge(41);

        guenther = new Person();
        guenther.setFullName("Günther Grandiger");
        guenther.setEmailAddress("guenther.grandiger@gmail.com");
        guenther.setAge(55);

        lilliane = new Person();
        lilliane.setFullName("Lilliane Langdorf");
        lilliane.setEmailAddress("lilliane.langdorf@icloud.com");
        lilliane.setAge(29);

        bianca = new Person();
        bianca.setFullName("Bianca Bambus");
        bianca.setEmailAddress("bianca.bambus@yahoo.com");
        bianca.setAge(22);
    }

    public static List<Person> listOfRichard() {
        return List.of(richard);
    }

    public static List<Person> listOfBianca() {
        return List.of(bianca);
    }

    public static List<Person> listOfRichardAndLilliane() {
        return List.of(richard, lilliane);
    }

    public static List<Person> listOfRichardAndGuenther() {
        return List.of(richard, guenther);
    }

    public static List<Person> listOfRichardAndGuentherAndLilliane() {
        return List.of(richard, guenther, lilliane);
    }
}
