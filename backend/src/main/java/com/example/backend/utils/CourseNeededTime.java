package com.example.backend.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CourseNeededTime {
    public static double extractValueFromString(String input) {
        System.out.println("extracting..." + input);
        String pattern = "(\\d+\\.\\d+)";
        Pattern regex = Pattern.compile(pattern);

        Matcher matcher = regex.matcher(input);
        if (matcher.find()) {
            String match = matcher.group();
            System.out.println("im here: " + match);
            return Double.parseDouble(match);
        }
        return 0.0;
    }

    public static String convertDoubleIntoHoursAndMinutes(double givenTime) {
        int wholeHours = (int) givenTime;
        int minutes = (int)  Math.round(((givenTime - wholeHours) * 100));

        if (minutes >= 60) {
            wholeHours += 1;
            minutes -= 60;
        }
        return String.format("%02d:%02d", wholeHours, minutes);
    }
}
