/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.cryptocurrency;

/**
 *
 * @author masterulb
 */
public class Transaction {

    private Integer intAmount;
    private String strAddress;
    private String strReceiver;

    /**
     *
     * @param intAmount
     * @param strAddress
     * @param strReceiver
     */
    public Transaction(Integer intAmount, String strAddress, String strReceiver) {
        this.intAmount = intAmount;
        this.strAddress = strAddress;
        this.strReceiver = strReceiver;
    }

    /**
     *
     */
    public void createTransaction() {

    }

    /**
     *
     */
    public void signTransaction() {

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
}
