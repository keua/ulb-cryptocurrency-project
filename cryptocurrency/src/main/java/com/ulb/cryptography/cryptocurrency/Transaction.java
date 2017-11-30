/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.cryptocurrency;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.util.Date;

/**
 *
 * @author masterulb
 */
public class Transaction implements Serializable {

    private static final long serialVersionUID = 12358903454875L;

    private Integer intAmount;
    private String strSenderAddress;
    private String strReceiver;
    private Date timeStamp;
    private byte[] transactionSigned;

    /**
     *
     * @param intAmount
     * @param strAddress
     * @param strReceiver
     * @param timeStamp
     */
    public Transaction(Integer intAmount, String strAddress, String strReceiver, Date timeStamp) {
        this.intAmount = intAmount;
        this.strSenderAddress = strAddress;
        this.strReceiver = strReceiver;
        this.timeStamp = timeStamp;
    }

    /**
     *
     */
    public Transaction() {
        this.intAmount = null;
        this.strSenderAddress = null;
        this.strReceiver = null;
        this.timeStamp = new Date();
    }

    /**
     * @return the transactionSigned
     */
    public byte[] getTransactionSigned() {
        return transactionSigned;
    }

    /**
     * @param transactionSigned the transactionSigned to set
     */
    public void setTransactionSigned(byte[] transactionSigned) {
        this.transactionSigned = transactionSigned;
    }

    /**
     *
     */
    public void createTransaction() {

    }

    /**
     *
     */
    public void signTransaction(PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        String concatenatedValues
                = this.timeStamp.toString()
                + this.intAmount
                + this.strReceiver
                + this.strSenderAddress;
        this.transactionSigned = Cryptography.DSASign(privateKey, concatenatedValues);
    }

    /**
     * @return the intAmount
     */
    public Integer getIntAmount() {
        return intAmount;
    }

    /**
     * @param intAmount the intAmount to set
     */
    public void setIntAmount(Integer intAmount) {
        this.intAmount = intAmount;
    }

    /**
     * @return the strSenderAddress
     */
    public String getStrSenderAddress() {
        return strSenderAddress;
    }

    /**
     * @param strSenderAddress the strSenderAddress to set
     */
    public void setStrSenderAddress(String strSenderAddress) {
        this.strSenderAddress = strSenderAddress;
    }

    /**
     * @return the strReceiver
     */
    public String getStrReceiver() {
        return strReceiver;
    }

    /**
     * @param strReceiver the strReceiver to set
     */
    public void setStrReceiver(String strReceiver) {
        this.strReceiver = strReceiver;
    }

    /**
     * @return the timeStamp
     */
    public Date getTimeStamp() {
        return timeStamp;
    }

    /**
     * @param timeStamp the timeStamp to set
     */
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
