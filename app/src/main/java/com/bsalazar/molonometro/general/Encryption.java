package com.bsalazar.molonometro.general;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by rrodriguez on 28/09/2015.
 */
class Encryption {

    private final String KEY_ENCRYPTION = "SHA-1";
    private final String KEY_SPEC = "AES";
    private final String DATA_ENCRYPTION = "AES/ECB/PKCS5Padding";
    private final String ENCODING = "UTF-8";

    private final int blockSize = 16;

    private SecretKeySpec skeySpec;
    private Cipher cipher;

    public Encryption(byte [] keyraw) throws Exception {
        if(keyraw == null){
            byte[] bytesOfMessage = "".getBytes(ENCODING);
            MessageDigest md = MessageDigest.getInstance(KEY_ENCRYPTION);
            keyraw = md.digest(bytesOfMessage);
        }

        keyraw = Arrays.copyOf(keyraw, blockSize);
        skeySpec = new SecretKeySpec(keyraw, KEY_SPEC);
        cipher = Cipher.getInstance(DATA_ENCRYPTION);
    }

    public Encryption(String passphrase) throws Exception {
        byte[] bytesOfMessage = passphrase.getBytes(ENCODING);
        MessageDigest md = MessageDigest.getInstance(KEY_ENCRYPTION);
        byte[] thedigest = md.digest(bytesOfMessage);

        thedigest = Arrays.copyOf(thedigest, blockSize);
        skeySpec = new SecretKeySpec(thedigest, KEY_SPEC);
        cipher = Cipher.getInstance(DATA_ENCRYPTION);
    }

    public byte[] encrypt (byte[] plaintext) throws Exception {
        //returns byte array encrypted with key

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        return cipher.doFinal(plaintext);
    }

    public byte[] encrypt (String plaintext) throws Exception {
        //returns byte array encrypted with key

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        return cipher.doFinal(plaintext.getBytes(ENCODING));
    }

    public String decrypt (byte[] ciphertext) throws Exception {
        //returns byte array decrypted with key
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] plaintext = cipher.doFinal(ciphertext);

        return new String(plaintext, ENCODING);
    }

    public String decrypt (String ciphertext) throws Exception {
        //returns byte array decrypted with key
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] plaintext = cipher.doFinal(ciphertext.getBytes(ENCODING));

        return new String(plaintext, ENCODING);
    }
}
