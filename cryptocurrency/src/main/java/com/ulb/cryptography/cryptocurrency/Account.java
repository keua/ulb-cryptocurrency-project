/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.cryptocurrency;

import static com.ulb.cryptography.cryptocurrency.Cryptography.Ripemd160;
import static com.ulb.cryptography.cryptocurrency.Cryptography.savePrivateKey;
import static com.ulb.cryptography.cryptocurrency.Cryptography.savePublicKey;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author masterulb
 */
public class Account {

    private String strAddress;
    private String encryptedPrivateKey;
    private PublicKey publiKey;
    private String hashPassword;
    private byte[] aesKey;
    private byte[] iv;

    public Account(String password) throws NoSuchAlgorithmException, GeneralSecurityException, IOException {
        KeyPair keyPair = Cryptography.genKeys();
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("this is the original one");
        System.out.println(savePrivateKey(privateKey));
        System.out.println("this is the original one");
        this.publiKey = keyPair.getPublic();
        String sha256hex = DigestUtils.sha256Hex(savePublicKey(this.publiKey));
        this.strAddress = Ripemd160(sha256hex);

        // hash password
        PasswordAuthentication pa = new PasswordAuthentication();
        char[] ca = password.toCharArray();
        this.hashPassword = pa.hash(ca);
        // use here the this.strPassword
        this.aesKey = Cryptography.FromPasstoAESkey(this.hashPassword);
        this.iv = "asdfqwertyuioplk".getBytes();// this can be changed, we need one IV per account
        this.encryptedPrivateKey = Cryptography.AESencrypt(this.aesKey, this.iv, Cryptography.savePrivateKey(privateKey));
    }

    /**
     * @return the strAddress
     */
    public String getStrAddress() {
        return strAddress;
    }

    /**
     * @param strAddress the strAddress to set
     */
    public void setStrAddress(String strAddress) {
        this.strAddress = strAddress;
    }

    /**
     * @return the publiKey
     */
    public PublicKey getPubliKey() {
        return publiKey;
    }

    /**
     * @param publiKey the publiKey to set
     */
    public void setPubliKey(PublicKey publiKey) {
        this.publiKey = publiKey;
    }

    /**
     * @return the encryptedPrivateKey
     */
    public String getEncryptedPrivateKey() {
        return encryptedPrivateKey;
    }

    /**
     * @param encryptedPrivateKey the encryptedPrivateKey to set
     */
    public void setEncryptedPrivateKey(String encryptedPrivateKey) {
        this.encryptedPrivateKey = encryptedPrivateKey;
    }

    public PrivateKey getPrivateKey() throws GeneralSecurityException {
        String decrypted = Cryptography.AESdecrypt(this.aesKey, this.iv, this.encryptedPrivateKey);
        PrivateKey privateKey = Cryptography.loadPrivateKey(decrypted);
        return privateKey;
    }

    /**
     * @param hashPassword the hashPassword to set
     */
    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }
}
