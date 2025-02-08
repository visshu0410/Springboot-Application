package com.vishalgound.Springboot.Application.Utils;

import com.vishalgound.Springboot.Application.constants.CommonConstants;

public class CommonUtils {
    public static String makeKey(String a, String b) {
        return a + CommonConstants.DOLLAR + b;
    }
}
