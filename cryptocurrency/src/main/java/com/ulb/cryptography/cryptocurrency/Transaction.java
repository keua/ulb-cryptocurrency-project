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
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.Date;

/**
 *
 * @author masterulb
 */
public class Transaction implements Serializable {

    /**
     * @return the TranType
     */
    public String getTranType() {
        return TranType;
    }

    /**
     * @param TranType the TranType to set
     */
    public void setTranType(String TranType) {
        this.TranType = TranType;
    }

    private static final long serialVersionUID = 12358903454875L;

    private Float fltInputSenderAmount;
    private Float fltOutputReceiverAmount;
    private Float fltOutputSenderAmount;
    private String strSenderAddress;
    private String strReceiver;
    private Date timeStamp;
    private byte[] transactionSigned;
    private String TranType;

    /**
     *
     * @param inputSenderAmount
     * @param outputReceiverAmount
     * @param outputSenderAmount
     * @param strAddress
     * @param strReceiver
     * @param timeStamp
     */
    public Transaction(
            Float inputSenderAmount,
            Float outputReceiverAmount,
            Float outputSenderAmount,
            String strAddress,
            String strReceiver,
            Date timeStamp
    ) {
        this.fltInputSenderAmount = inputSenderAmount;
        this.fltOutputReceiverAmount = outputReceiverAmount;
        this.fltOutputSenderAmount = outputSenderAmount;
        this.strSenderAddress = strAddress;
        this.strReceiver = strReceiver;
        this.timeStamp = timeStamp;
    }

    /**
     *
     */
    public Transaction() {
        this.fltInputSenderAmount = null;
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
     * @param privateKey
     */
    public void signTransaction(PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        String concatenatedValues
                = this.timeStamp.toString()
                + this.fltInputSenderAmount
                + this.strReceiver
                + this.strSenderAddress
                + this.fltOutputReceiverAmount
                + this.fltOutputSenderAmount
                + this.getTranType();
        this.transactionSigned = Cryptography.DSASign(privateKey, concatenatedValues);
    }

    /**
     * @return the fltInputSenderAmount
     */
    public Float getFltInputSenderAmount() {
        return fltInputSenderAmount;
    }

    /**
     * @param fltInputSenderAmount the fltInputSenderAmount to set
     */
    public void setFltInputSenderAmount(Float fltInputSenderAmount) {
        this.fltInputSenderAmount = fltInputSenderAmount;
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

    /**
     * @return the fltOutputReceiverAmount
     */
    public Float getFltOutputReceiverAmount() {
        return fltOutputReceiverAmount;
    }

    /**
     * @param fltOutputReceiverAmount the fltOutputReceiverAmount to set
     */
    public void setFltOutputReceiverAmount(Float fltOutputReceiverAmount) {
        this.fltOutputReceiverAmount = fltOutputReceiverAmount;
    }

    /**
     * @return the fltOutputSenderAmount
     */
    public Float getFltOutputSenderAmount() {
        return fltOutputSenderAmount;
    }

    /**
     * @param fltOutputSenderAmount the fltOutputSenderAmount to set
     */
    public void setFltOutputSenderAmount(Float fltOutputSenderAmount) {
        this.fltOutputSenderAmount = fltOutputSenderAmount;
    }
}
