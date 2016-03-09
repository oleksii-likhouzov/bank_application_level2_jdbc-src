package org.test.bankapp.util;

/**
 * Created by stalker on 06.03.16.
 */
public class StringUtility {
    /**
     * Проверяет корректность адреса электронной почты
     */
    public static boolean checkIsEmail(String email) {
        return email.matches(
                "^[A-Za-z\\.-0-9]{2,}@[A-Za-z\\.-0-9]{2,}\\.[A-Za-z]{2,3}$");
    }

    /**
     * Проверяет корректность адреса электронной почты
     */

    public static boolean checkIsPhone(String phone) {
        return phone.matches("^((8|\\+3)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)[\\d\\- ]{5,10}$");
    }

}
