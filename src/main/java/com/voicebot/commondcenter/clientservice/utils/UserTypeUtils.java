package com.voicebot.commondcenter.clientservice.utils;

import com.voicebot.commondcenter.clientservice.enums.UserType;
import org.apache.commons.lang3.StringUtils;

public class UserTypeUtils {

    public static boolean isSuperAdmin(String userType) {
        return StringUtils.equalsIgnoreCase(userType, UserType.SUPER_ADMIN.name());
    }

    public static boolean isSuperAdmin(UserType userType) {
        return StringUtils.equalsIgnoreCase(userType.name(), UserType.SUPER_ADMIN.name());
    }

    public static boolean isClientAdmin(String userType) {
        return StringUtils.equalsIgnoreCase(userType, UserType.SUPER_ADMIN.name());
    }

    public static boolean isClientAdmin(UserType userType) {
        return StringUtils.equalsIgnoreCase(userType.name(), UserType.SUPER_ADMIN.name());
    }
}
