package org.projects.machine_coding.url_shortner.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

@Service
@Slf4j
public class UrlShortnerService {

    // get the bytes from the url string
    private final static String test_url = "https://www.google.com/maps";

    // Base62 alphabet (62 characters)
    private static final char[] BASE62_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    UrlShortnerService() {
        convertToBytes();
    }
    public static void convertToBytes(){
        try {
            // get the hash for the string - SHA-256 algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashedByte = digest.digest(test_url.getBytes());
            log.info("hashed bytes from the string : "+ Arrays.toString(hashedByte));

            // shorten the hash for a fixed length -> 8
            byte[] shortenHashByte = new byte[8];
            // copy items from hash to shortenHash
            System.arraycopy(hashedByte,0, shortenHashByte, 0, 8);
            log.info("shorten hash : "+ Arrays.toString(shortenHashByte));

            // convert byte array to BASE62 string
            String shortenUrlKey = convertToBase62(shortenHashByte);


        } catch (Exception e) {
            log.error("Error while converting string to bytes : convertToBytes : "+ e);
        }
    }
    public static String convertToBase62(byte[] hashByte) {
        try {
            BigInteger bigInteger = new BigInteger(1, hashByte);
            log.info("Big Integer for hashed byte array : "+ bigInteger);
            StringBuilder shortUrl = new StringBuilder();

            // divide those big int to get shortencoded url
            while(bigInteger.compareTo(BigInteger.ZERO) > 0) {
                // get the modulo
                int remainder = bigInteger.mod(BigInteger.valueOf(62)).intValue();

                // add the char from base 62 char array
                shortUrl.append(BASE62_ALPHABET[remainder]);

                // divide and remove last digit from bigInteger
                bigInteger = bigInteger.divide(BigInteger.valueOf(62));
            }

            log.info("short url key is : "+ shortUrl.reverse().toString());
            return shortUrl.reverse().toString();
        } catch (Exception e) {
            log.error("Error while converting byte array to BASE 2 : "+ e);
            return "";
        }
    }


}
