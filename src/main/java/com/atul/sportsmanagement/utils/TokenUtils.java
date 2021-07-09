package com.atul.sportsmanagement.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TokenUtils {
    public static String generateToken(String encode){
        return Base64.getEncoder().encodeToString(encode.getBytes(StandardCharsets.UTF_8)).replaceAll("=","!");

    }
    public static String[] parseUserIdAndTeamId(String token){
        byte[] decodedBytes = Base64.getUrlDecoder().decode(token);
        String decodedUrl = new String(decodedBytes);
        return decodedUrl.split("!",2);
    }
    public static String parseTeamId(String token){
        byte[] decodedBytes = Base64.getUrlDecoder().decode(token);
        return new String(decodedBytes);
    }
}
