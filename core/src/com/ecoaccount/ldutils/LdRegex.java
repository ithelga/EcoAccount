package com.ecoaccount.ldutils;

import java.util.regex.Pattern;

/**
 * Created by Lion's DaMajectim on 17.04.2020
 * Updated on 08.05.2020
 */

public class LdRegex {

    private static final String ENG_NUM_SPACE_PATTERN = "^[A-Za-z0-9 ]+$";

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static Pattern emailPattern, engNumSpacePattern;

    public static boolean checkEmail(String str) {
        if (emailPattern == null) emailPattern = Pattern.compile(EMAIL_PATTERN);
        return emailPattern.matcher(str).matches();
    }
    public static boolean checkEngNumSpace(String str) {
        if (engNumSpacePattern == null) engNumSpacePattern = Pattern.compile(ENG_NUM_SPACE_PATTERN);
        return engNumSpacePattern.matcher(str).matches();
    }
}
