/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.cryptocurrency;

import static com.ulb.cryptography.cryptocurrency.Cryptography.Ripemd160;
import static com.ulb.cryptography.cryptocurrency.Cryptography.savePrivateKey;
import static com.ulb.cryptography.cryptocurrency.Cryptography.savePublicKey;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.concurrent.ThreadLocalRandom;

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

    ////////
    private int AcountID;

/// Create new account
    public Account() {
    }

    public Account(String password) throws NoSuchAlgorithmException, GeneralSecurityException, IOException {
        KeyPair keyPair = Cryptography.genKeys();
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("this is the original Private key");
        System.out.println(savePrivateKey(privateKey));
        this.publiKey = keyPair.getPublic();
        String sha256hex = DigestUtils.sha256Hex(savePublicKey(this.publiKey));
        this.strAddress = Ripemd160(sha256hex);

        // hash password
        PasswordAuthentication pa = new PasswordAuthentication();
        char[] ca = password.toCharArray();
        this.hashPassword = pa.hash(ca);
        this.aesKey = Cryptography.FromPasstoAESkey(password);
        this.iv = "asdfqwertyuioplk".getBytes();// this can be changed, we need one IV per account
        this.encryptedPrivateKey = Cryptography.AESencrypt(this.aesKey, this.iv, Cryptography.savePrivateKey(privateKey));
        // Add public and encrypted private key to file
        int id = ThreadLocalRandom.current().nextInt(1, 50 + 1);
        File f = new File(Integer.toString(id));
        while (f.exists()) {
            id = ThreadLocalRandom.current().nextInt(1, 50 + 1);
        }
        String pubKey = savePublicKey(this.publiKey);
        String filename = Integer.toString(id);
        try (PrintWriter out = new PrintWriter(filename + ".txt")) {
            out.println("EncryptedPrivateKey=" + "\r\n" + this.encryptedPrivateKey + "\r\n" + "PublicKey=" + "\r\n" + pubKey);
        }
        try (
                Writer output = new BufferedWriter(new FileWriter("pass.txt", true))) {

            output.append(Integer.toString(id));
            output.append("\r\n" + this.hashPassword + "\r\n");
        }
        this.AcountID = id;

        this.savePublicInfo();

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

    public PrivateKey getPrivateKey(String AccID, String password) throws GeneralSecurityException, FileNotFoundException, IOException {

        String decrypted = null;
        try (BufferedReader br = new BufferedReader(new FileReader(AccID + ".txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                if (line.equals("EncryptedPrivateKey=")) {
                    decrypted = br.readLine();
                }
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        }
        this.aesKey = Cryptography.FromPasstoAESkey(password);
        this.iv = "asdfqwertyuioplk".getBytes();
        String priv = Cryptography.AESdecrypt(this.aesKey, this.iv, decrypted);
        PrivateKey privateKey = Cryptography.loadPrivateKey(priv);
        return privateKey;

    }

    public PublicKey getPublicKey(String AccID) throws GeneralSecurityException, FileNotFoundException, IOException {
        String decrypted = null;
        try (BufferedReader br = new BufferedReader(new FileReader(AccID + ".txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                if (line.equals("PublicKey=")) {
                    decrypted = br.readLine();
                }
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        }
        PublicKey pubkey = Cryptography.loadPublicKey(decrypted);
        return pubkey;

    }

    /**
     * @param hashPassword the hashPassword to set
     */
    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public boolean checkpass(String Accid, String password) throws FileNotFoundException, IOException {
        String PassHash = null;
        try (BufferedReader br = new BufferedReader(new FileReader("pass.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                if (line.equals(Accid)) {
                    PassHash = br.readLine();
                }
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        }
        PasswordAuthentication pa = new PasswordAuthentication();
        if (pa.authenticate(password.toCharArray(), PassHash)) {
            System.out.println("Match!");
            return true;
        } else {
            System.out.println("Wrong Password!");
            return false;
        }
    }

    /**
     * @return the AcountID
     */
    public int getAcountID() {
        return AcountID;
    }

    /**
     * @param AcountID the AcountID to set
     */
    public void setAcountID(int AcountID) {
        this.AcountID = AcountID;
    }

    public PrivateKey getPrivateKey() throws GeneralSecurityException {
        String decrypted = Cryptography.AESdecrypt(this.aesKey, this.iv, this.encryptedPrivateKey);
        PrivateKey privateKey = Cryptography.loadPrivateKey(decrypted);
        return privateKey;
    }

    private void savePublicInfo() throws FileNotFoundException, GeneralSecurityException {
        /*File file =  new File("public_repo");
        try (PrintWriter out = new PrintWriter(file)) {
            out.("address=" + this.strAddress + "\r\n" + savePublicKey(this.publiKey));
        }*/
        try (FileWriter fw = new FileWriter("public_repo", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println("address=" + this.strAddress + "\r\n" + savePublicKey(this.publiKey));
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

}
