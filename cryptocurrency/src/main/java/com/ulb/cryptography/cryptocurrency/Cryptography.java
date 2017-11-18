/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.cryptocurrency;

/**
 *
 * @author TOSHIBA
 */
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.util.encoders.Hex;

public class Cryptography {

    public static String AESencrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string: "
                    + Base64.encodeBase64String(encrypted));

            return Base64.encodeBase64String(encrypted);
        } catch (UnsupportedEncodingException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
        }

        return null;
    }

    public static String AESdecrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (UnsupportedEncodingException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
        }

        return null;
    }

    public static KeyPair genKeys() throws NoSuchAlgorithmException {

        KeyPairGenerator pairgen = KeyPairGenerator.getInstance("DSA");
        SecureRandom random = new SecureRandom();
        pairgen.initialize(KEYSIZE, random);
        KeyPair keyPair = pairgen.generateKeyPair();

        return keyPair;
    }

    public static PublicKey getPublicKey(KeyPair keyPair) {
        return keyPair.getPublic();
    }

    public static PrivateKey getPrivateKey(KeyPair keyPair) {
        return keyPair.getPrivate();
    }

    public static PublicKey convertArrayToPubKey(byte encoded[],
            String algorithm) throws Exception {
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

        return pubKey;
    }

    public static PrivateKey convertArrayToPriKey(byte encoded[],
            String algorithm) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PrivateKey priKey = keyFactory.generatePrivate(keySpec);
        return priKey;
    }

    public static byte[] DSASign(KeyPair keyPair, String message) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        PrivateKey privateKey = keyPair.getPrivate();
        Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
        signature.initSign(privateKey);
        byte[] bytes = message.getBytes();
        signature.update(bytes);
        byte[] digitalSignature = signature.sign();
        return digitalSignature;
    }

    public static int DSAVerify(PublicKey pubkey, byte[] signature, String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature verifyalg = Signature.getInstance("DSA");
        verifyalg.initVerify(pubkey);
        verifyalg.update(message.getBytes());
        if (!verifyalg.verify(signature)) {
            return 0;
        } else {
            return 1;
        }
    }
    private static final int KEYSIZE = 512;

    public static String Ripemd160(PublicKey pubkey) throws IOException, NoSuchAlgorithmException {
        byte[] r = pubkey.toString().getBytes("UTF-8");
        RIPEMD160Digest d = new RIPEMD160Digest();
        d.update(r, 0, r.length);

        byte[] o = new byte[d.getDigestSize()];
        d.doFinal(o, 0);
        org.apache.commons.io.output.ByteArrayOutputStream byteArrayOutputStream
                = new org.apache.commons.io.output.ByteArrayOutputStream();
        Hex.encode(o, byteArrayOutputStream);
        String digestStr = byteArrayOutputStream.toString("UTF8");
        return digestStr;
    }

    /*
 public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, IOException
 {
  KeyPair keyPair = genKeys();
  PublicKey publicKey = keyPair.getPublic();
  PrivateKey privateKey = keyPair.getPrivate();
  System.out.print(publicKey); 
  System.out.print(privateKey); 
  String message="fdfdfdfdfd";
  byte[] signature= DSASign(keyPair,message);
  System.out.print("**************************");
  System.out.print(signature);
  int ver=DSAVerify(publicKey,signature,message);
  System.out.print("&&&&&&&&&&&&&&&&&&");
  System.out.print(ver);
  System.out.print("&&&&&&&&&&&&&&&&&&");
  System.out.print(Ripemd160(publicKey));
 }
     */
}
