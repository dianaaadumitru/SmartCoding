package com.example.backend.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CourseNeededTime {
    public static double extractValueFromString(String input) {
        System.out.println("extracting..." + input);
        String pattern = "(\\d{1,2})(?::(\\d{1,2}))? hours";
        Pattern regex = Pattern.compile(pattern);

        Matcher matcher = regex.matcher(input);
        if (matcher.find()) {
            String hours = matcher.group(1);
            String minutes = matcher.group(2);

            int totalMinutes = 0;
            if (hours != null) {
                totalMinutes += Integer.parseInt(hours) * 60;
            }
            if (minutes != null) {
                totalMinutes += Integer.parseInt(minutes);
            }

            double totalTime = totalMinutes / 60.0;
            System.out.println("im here: " + totalTime);
            return totalTime;
        }
        return 0.0;
    }

    public static String convertDoubleIntoHoursAndMinutes(double givenTime) {
        int totalMinutes = (int) Math.round(givenTime * 60);
        int wholeHours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        return String.format("%02d:%02d", wholeHours, minutes);
    }
}
