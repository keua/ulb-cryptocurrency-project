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

import static com.ulb.cryptography.cryptocurrency.Cryptography.AESdecrypt;
import static com.ulb.cryptography.cryptocurrency.Cryptography.AESencrypt;
import static com.ulb.cryptography.cryptocurrency.Cryptography.DSASign;
import static com.ulb.cryptography.cryptocurrency.Cryptography.DSAVerify;
import static com.ulb.cryptography.cryptocurrency.Cryptography.FromPasstoAESkey;
import static com.ulb.cryptography.cryptocurrency.Cryptography.Ripemd160;
import static com.ulb.cryptography.cryptocurrency.Cryptography.genKeys;
import static com.ulb.cryptography.cryptocurrency.Cryptography.loadPrivateKey;
import static com.ulb.cryptography.cryptocurrency.Cryptography.savePrivateKey;
import static com.ulb.cryptography.cryptocurrency.Cryptography.savePublicKey;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
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
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.codec.digest.DigestUtils;


import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.util.encoders.Hex;

public class Cryptography {

    
    ///////////////    AES    ////////////////////////
    ///Derive a AES symmetric encryption key from a password using PBKDF2.
    public static byte[] FromPasstoAESkey(String psw) throws NoSuchAlgorithmException, InvalidKeySpecException{
      //  SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        String str = "78578E5A5D63CB06";
        byte[] salt = str.getBytes();
       // SecureRandom random = new SecureRandom();
       // byte[] salt = new byte[16];
       // random.nextBytes(salt);
        char[] password = psw.toCharArray();
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
        
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = f.generateSecret(spec).getEncoded();
       // Base64.Encoder enc = Base64.getEncoder();
      //  SecretKey tmp = factory.generateSecret(spec);
       // SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        return hash;
    }
    
    public static  byte[] createAESiv()
    {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
        return bytes;
    }
    
    public static String AESencrypt(byte[] key, byte[] initVector, String value) {
        try {
            //IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            IvParameterSpec iv = new IvParameterSpec(initVector);
           // SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
          //  System.out.println("encrypted string: "
          //          + Base64.encodeBase64String(encrypted));

            return Base64.encodeBase64String(encrypted);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
        }

        return null;
    }

    public static String AESdecrypt(byte[] key, byte[] initVector, String encrypted) {
        try {
             IvParameterSpec iv = new IvParameterSpec(initVector);
             SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
           // IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
           // SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
        }

        return null;
    }
///////////////////   DSA    /////////////////////////    
// create DSA keys
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

    
/////////////   Methods to vonvert publickey<>string and privatekey<>string ////////////////
    public static PrivateKey loadPrivateKey(String key64) throws GeneralSecurityException {
   // byte[] clear = base64Decode(key64);
    byte[] clear=Base64.decodeBase64(key64);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
    KeyFactory fact = KeyFactory.getInstance("DSA");
    PrivateKey priv = fact.generatePrivate(keySpec);
    Arrays.fill(clear, (byte) 0);
    return priv;
}


public static PublicKey loadPublicKey(String stored) throws GeneralSecurityException {
    //byte[] data = base64Decode(stored);
    byte[] data=Base64.decodeBase64(stored);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
    KeyFactory fact = KeyFactory.getInstance("DSA");
    return fact.generatePublic(spec);
}

public static String savePrivateKey(PrivateKey priv) throws GeneralSecurityException {
    KeyFactory fact = KeyFactory.getInstance("DSA");
    PKCS8EncodedKeySpec spec = fact.getKeySpec(priv,
            PKCS8EncodedKeySpec.class);
    byte[] packed = spec.getEncoded();
   // String key64 = base64Encode(packed);
   String key64 = Base64.encodeBase64String(packed);

    Arrays.fill(packed, (byte) 0);
    return key64;
}


public static String savePublicKey(PublicKey publ) throws GeneralSecurityException {
    KeyFactory fact = KeyFactory.getInstance("DSA");
    X509EncodedKeySpec spec = fact.getKeySpec(publ,
            X509EncodedKeySpec.class);
   // return base64Encode(spec.getEncoded());
    return Base64.encodeBase64String(spec.getEncoded());
}
//////////////////////////////////////    
//not used/ maybe will be used
    public static PublicKey convertArrayToPubKey(byte encoded[],
            String algorithm) throws Exception {
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

        return pubKey;
    }    
    
    
//not used/ maybe will be used
    public static PrivateKey convertArrayToPriKey(byte encoded[],
            String algorithm) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PrivateKey priKey = keyFactory.generatePrivate(keySpec);
        return priKey;
    }
//////////////////////////////////
    
////    pass private key //////    
    public static byte[] DSASign(PrivateKey privateKey, String message) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
      //  PrivateKey privateKey = keyPair.getPrivate();
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
    
///////////////    Ripemd160   //////////////////////////////////
// public static String Ripemd160(PublicKey pubkey) throws IOException, NoSuchAlgorithmException {
    public static String Ripemd160(String pubkey) throws IOException, NoSuchAlgorithmException {    
        //byte[] r = pubkey.toString().getBytes("UTF-8");
        byte[] r = pubkey.getBytes("UTF-8");
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
    //generate ECDSA keypair (Not sure we're using it or not!)
    /*
    public  static KeyPair generateECDSAKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDsA", "SC");
    ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
    keyGen.initialize(ecSpec, new SecureRandom());
    return keyGen.generateKeyPair();   
}
 */   
// Hash/verify Password   
//First Hash password and store it.
//when the password is entered verify the password with the hash
//then get AES key from the hash
//and use it to encrypt/decrypt
 public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, IOException, InvalidKeySpecException, InvalidAlgorithmParameterException, GeneralSecurityException
 {
  //Create DSA key pairs 
   KeyPair k=genKeys();
   /////  Use of convert methods ////
  //  String pubKey = savePublicKey(k.getPublic());
  //  PublicKey pubSaved = loadPublicKey(pubKey);
  //  System.out.println(k.getPublic()+"\n"+pubSaved);
  //convert private key to string to encrypt it
    String privKey = savePrivateKey(k.getPrivate());
    PrivateKey privSaved = loadPrivateKey(privKey);
   // System.out.println(k.getPrivate()+"\n"+privSaved);
   
   // Sign the message
   byte[] signature= DSASign(privSaved,"Transaction");
   
   //create password hash
   PasswordAuthentication p= new PasswordAuthentication();  
   String password="Hi";  
   char[] b=password.toCharArray();
   String hash= p.hash(b);  
   
   // Authenticate password/hash
   if(p.authenticate("Hi".toCharArray(), hash))
 
   {
   //use AES to encrypt/decrypt using a key derived from the password hash
   //derive secret key from password hash    
   byte[] s=FromPasstoAESkey(hash);
   System.out.print("\n"+s+"\n");
   // generate IV for AES algo
   byte[] iv="qwertyuiasdfghjk".getBytes();
   //encrypt private key using AES lago
   String encrypted =AESencrypt(s,iv , privKey); 
   //decrypt private key using AES lag
   String decrypted=AESdecrypt(s, iv, encrypted);
   //convert the decrypted string to private key
   PrivateKey newprv = loadPrivateKey(decrypted);
   System.out.print("\n"+newprv+"\n");
   
   }
   //Verify DSA signature
   int ver=DSAVerify(k.getPublic(),signature,"Transaction");
   System.out.print(ver);
   
   /// Derive address from Public Key
   String sha256hex = DigestUtils.sha256Hex(savePublicKey(k.getPublic()));
   String address= Ripemd160(sha256hex);
   System.out.print(address);
 }
     
}
