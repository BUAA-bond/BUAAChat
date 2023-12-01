package com.BUAAChat.Server.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Encrypt {

    private static String hexCharSet = "0123456789ABCDEF";

    /**
     * generate a random salt string (HEX).
     * @param len length of salt
     * @return salt string
     */
    public static String randomSalt(int len){
        
        if( len <= 0 ) return "";

        Random rand = new Random();
        StringBuilder salt = new StringBuilder();
        for( int i=0; i<len; i++ ){
            salt.append( hexCharSet.charAt(rand.nextInt(16)) );
        }

        return salt.toString();
    }

    /**
     * append salt to string.
     * @param str string to append salt
     * @param len length of salt
     * @return result string
     */
    public static String appendSalt(String str,int len){
        if( str == null ) return null;
        return str + randomSalt(len);
    }

    /**
     * parse byte[] to hex string.
     * @param by byte[] to be parsed
     * @return result hex string
     */
    public static String byteToHex(byte[] by){
        StringBuffer sb = new StringBuffer();
        for (byte b : by) {
            String str = Integer.toHexString(b & 0xFF).toUpperCase();
            if(str.length()<2) sb.append('0');
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * to append salt and encrypt using SHA-256.
     * @param str string to encrypt
     * @param salt salt
     * @return encrypted string.
     */
    public static String encryptString(String str,String salt){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String str_salt = str + salt ;
        String res = byteToHex( md.digest(str_salt.getBytes()) );

        return res;
    }

}
