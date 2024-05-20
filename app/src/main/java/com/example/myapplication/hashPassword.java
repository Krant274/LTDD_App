package com.example.myapplication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class hashPassword {
    public static String hashedPassword(String password){
        try {
            // Tạo đối tượng MessageDigest với thuật toán MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Cập nhật dữ liệu đầu vào
            md.update(password.getBytes());

            // Băm dữ liệu
            byte[] byteData = md.digest();

            // Chuyển đổi byte array thành dạng hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : byteData) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
