package com.ecoaccount.ldutils;

import com.ecoaccount.ldutils.ui.LdText;

import java.util.Calendar;

/**
 * Created by Lion's DaMajectim on 24.08.2020
 * Updated on 18.12.2020
 */

public class LdDate {

    private int day, month, year;

    public LdDate(LdDate date) { this(date.getDay(), date.getMonth(), date.getYear()); }
    public LdDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() { return day; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
    public int getDayOfWeek() { return getDayOfWeek(day, month, year); }
    public int getDayOfYear() { return getDayOfYear(day, month, year); }
    public int getDayInYear() { return getDayInYear(year); }


    public void set(LdDate date) {
        set(date.getDay(), date.getMonth(), date.getYear());
    }
    public void set(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }
    public void setDay(int day) { this.day = day; }
    public void setMonth(int month) { this.month = month; }
    public void setYear(int year) { this.year = year; }

    public void addDay(int days) {
        day += days;
        int daysInMonth = getDaysInMonth(month, year);
        if (day < 1) {
            addMonth(-1);
            day += getDaysInMonth(month, year);
        }
        else if (day > daysInMonth) {
            day -= daysInMonth;
            addMonth(1);
        }
    }
    public void addMonth(int months) {
        month += months;
        if (month < 1) {
            month += 12;
            year--;
        }
        else if (month > 12) {
            month -= 12;
            year++;
        }
    }
    public void addYear(int years) { year += years; }

    public boolean less(LdDate date) { return compare(date) == -1; }
    public boolean more(LdDate date) { return compare(date) == 1; }
    public boolean equal(LdDate date) { return compare(date) == 0; }
    public int compare(LdDate date) {
        if (date.getYear() < year) return 1;
        else if (date.getYear() > year) return -1;
        else if (date.getMonth() < month) return 1;
        else if (date.getMonth() > month) return -1;
        else if (date.getDay() < day) return 1;
        else if (date.getDay() > day) return -1;
        else return 0;
    }

    @Override
    public String toString() {
        return LdText.format("@.@.@", LdText.zeros(day), LdText.zeros(month), year);
    }


    private static Calendar calendar;

    public static void init() {
        calendar = Calendar.getInstance();
    }

    public static int getDaysInMonth(int month, int year) {
        switch (month) {
            case 2: return ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) ? 29 : 28;
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
        }
        return 0;
    }

    public static int getDayOfWeek(int dayOfYear) {
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
        return calendar.get(Calendar.DAY_OF_WEEK);
//        return 0;
    }

    public static int getDayOfWeek(int day, int month, int year) {
        setDate(day, month - 1, year);
        return calendar.get(Calendar.DAY_OF_WEEK);
//        return 0;
    }

    public static int getDayOfYear(int day, int month, int year) {
        setDate(day, month - 1, year);
        return calendar.get(Calendar.DAY_OF_YEAR);
//        return 0;
    }

    public static int getDayInYear(int year) {
        return getDayOfYear(31, 12, year);
//        return 0;
    }

    private static void setDate(int day, int month, int year) {
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
    }

    public static LdDate parse(String date) {
//        System.out.println("new parse: " + date);
        String[] s;
        if (date.contains(".")) s = date.split("\\.");
        else if (date.contains("-")) s = date.split("-");
        else return null;
//        System.out.println("array " + s[0] + " " + s[1] + " " + s[2]);
        if (s[0].length() == 4) return new LdDate(Integer.parseInt(s[2]), Integer.parseInt(s[1]), Integer.parseInt(s[0]));
        else return new LdDate(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]));
    }
}
