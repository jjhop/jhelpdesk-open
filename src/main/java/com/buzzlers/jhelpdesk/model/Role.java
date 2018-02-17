package com.buzzlers.jhelpdesk.model;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <p>
 * Role użytkowników w systemie. Dla wygody programisty zawiera statyczne
 * wartości dla wszystkich ról oraz metody pozwalające na wygodne używanie
 * jej obiektów (np. zapisywanie ról w bazie za pomocą liczb i odtwarzenie
 * tychże z zapisanych liczba - szczegóły w dokumentacji poszczególnych metod).
 * </p>
 */
public enum Role {

    /**
     * Rola CLIENT. Użytkownik w tej roli może przede wszystkim zgłaszać błędy. Może także
     * w ograniczony sposób korzystać z pozostałych części systemu, nie może jednak edytować
     * nic poza częścią swoich danych osobowych.
     */
    CLIENT(1, "role.client"),

    /**
     * Rola TICKETKILLER. Uzytkownik w tej roli może zgłaszać problemy w swoim imieniu, oraz
     * w imieniu innych użytkowników. Może także w pewnym zakresie edytować zgłoszenia
     * (np. przypisywać je do rozwiązania przez siebie ale nie przez innych użytkowników).
     * Może także edytować wiele innych elementów systemu.
     */
    TICKETKILLER(10, "role.ticketkiller"),

    /**
     * Rola MANAGER. Użytkownik z nieograniczonymi uprawnieniami w systemie. Może zmieniać role
     * innych użytkowników, przypisywać zgłoszenia do rozwiązania przez dowolnego użytkownika
     * z rolą TICKETKILLER. W pełni może edytować wszystkie obiekty w systemie.
     */
    MANAGER(100, "role.manager");

    /**
     * Liczba, za pomocą której rola może być przedstawiana w bazie danych.
     */
    private final int code;

    /**
     * Nazwa roli.
     */
    private final String roleName;

    /**
     * <p>
     * Odtwarza rolę z podanego identyfikatora. Identyfikator musi mieć wartość jedną z trzech:
     * 1, 10 lub 100. Dla każdej innej wartości zgłasza wyjątk RuntimeException.
     * </p>
     * <p>
     * W bazie danych spójność powinna być zachowywana poprzez zdefiniowanie domeny z podanymi
     * wartościami lub zastosowanie mechanizmu sprawdzania wartości przed zapisaniem użytkownika
     * za pomocą CHECK lub TRIGGERa.
     * </p>
     */
    public static Role fromInt(int code) {
        switch (code) {
            case 1:
                return CLIENT;
            case 10:
                return TICKETKILLER;
            case 100:
                return MANAGER;
            default:
                throw new RuntimeException("Nieznana rola. [" + code + "]");
        }
    }

    Role(int code, String roleName) {
        this.code = code;
        this.roleName = roleName;
    }

    public int toInt() {
        return this.getRoleCode();
    }

    public int getRoleCode() {
        return code;
    }

    public String getRoleName(Locale locale) {
        ResourceBundle names = ResourceBundle.getBundle("role", locale);
        return names.getString(roleName);
    }

}
