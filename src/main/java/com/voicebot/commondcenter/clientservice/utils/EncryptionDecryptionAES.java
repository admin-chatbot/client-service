package com.voicebot.commondcenter.clientservice.utils;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class EncryptionDecryptionAES {
    private final Cipher cipher;

    private final SecretKey secretKey;

    public static final String CRYPTOR_KEY = "voicebot";
    public EncryptionDecryptionAES() throws NoSuchAlgorithmException, NoSuchPaddingException {
        byte[] keyByte = Base64.getDecoder().decode(CRYPTOR_KEY);
        secretKey = new SecretKeySpec(keyByte, "AES");
        System.out.println(secretKey);

        cipher = Cipher.getInstance("AES"); //SunJCE provider AES algorithm, mode(optional) and padding schema(optional)

    }

    public static void main(String[] args) throws Exception {
        /*
         create key
         If we need to generate a new key use a KeyGenerator
         If we have existing plaintext key use a SecretKeyFactory
        */
        EncryptionDecryptionAES encryptionDecryptionAES = new EncryptionDecryptionAES();

        String plainText = "J1tendr@";
        System.out.println("Plain Text Before Encryption: " + plainText);

        String encryptedText = encryptionDecryptionAES.encrypt(plainText );
        System.out.println("Encrypted Text After Encryption: " + encryptedText);

        String decryptedText = encryptionDecryptionAES.decrypt(encryptedText);
        System.out.println("Decrypted Text After Decryption: " + decryptedText);
    }

    public  String encrypt(String plainText )
            throws Exception {
        byte[] plainTextByte = plainText.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainTextByte);
        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedText = encoder.encodeToString(encryptedByte);
        return encryptedText;
    }

    public  String decrypt(String encryptedText )
            throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        String decryptedText = new String(decryptedByte);
        return decryptedText;
    }
}
