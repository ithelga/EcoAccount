package com.ecoaccount.ldutils;

import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by Lion's DaMajectim on 19.04.2018
 * Updated on 17.04.2021
 */

public class LdTime {

    public static String getTimeHMS(long time) { return getTimeHMS(time, TimeUtils.millis()); }
    public static String getTimeHMS(long time, long time2) {
        time -= time2;
        if (time < 0) return "00:00:00";
        int sec = (int) time / 1000;
        return String.format("%02d:%02d:%02d", (sec / 3600), (sec / 60) % 60, sec % 60);
    }

    public static String getTimeHM(long time) { return getTimeHM(time, TimeUtils.millis()); }
    public static String getTimeHM(long time, long time2) {
        time -= time2;
        if (time < 0) return "00:00";
        int sec = (int) time / 1000;
        return String.format("%02d:%02d", (sec / 3600), (sec / 60) % 60);
    }

    public static String getTimeMSM(long time) { return getTimeMSM(time, TimeUtils.millis()); }
    public static String getTimeMSM(long time, long time2) {
        time -= time2;
        if (time < 0) return "00:00.0";
        int sec = (int) time / 1000;
        return String.format("%02d:%02d.%d", (sec / 60), sec % 60, (time - sec * 1000) / 100);
    }

    public static String getTimeSM(long time) {
        time -= TimeUtils.millis();
        if (time < 0) return "00.0";
        int sec = (int) time / 1000;
        return String.format("%d.%d", sec, (time - sec * 1000) / 100);
    }

    public static String getTimeMS(long time) {
        time -= TimeUtils.millis();
        if (time < 0) return "00:00";
        int sec = (int) time / 1000;
        return String.format("%02d:%02d", sec / 60, sec % 60);
    }

    public static String getPassedTimeMSM(long time) {
        time = TimeUtils.millis() - time;
        if (time < 0) return "00:00.0";
        int sec = (int) time / 1000;
        return String.format("%02d:%02d.%d", (sec / 60), sec % 60, (time - sec * 1000) / 100);
    }
}
