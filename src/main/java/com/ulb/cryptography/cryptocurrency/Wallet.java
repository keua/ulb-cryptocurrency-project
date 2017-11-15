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
public class Wallet {

    private String strAddress;
    private String strPrivateKey;
    private String strPassword;
    private Blockchain blockchain;

    /**
     *
     * @param strAddress
     * @param strPrivateKey
     * @param strPassword
     * @param blockchain
     */
    public Wallet(String strAddress, String strPrivateKey, String strPassword, Blockchain blockchain) {
        this.strAddress = strAddress;
        this.strPrivateKey = strPrivateKey;
        this.strPassword = strPassword;
        this.blockchain = blockchain;
    }

    /**
     *
     */
    public void addBlockToWallet() {

    }

    /**
     *
     */
    public void sendTransactionToRelayNode() {

    }

    /**
     *
     */
    public void createAddress() {

    }

    /**
     *
     */
    public void authentificateUser() {

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
     * @return the strPrivateKey
     */
    public String getStrPrivateKey() {
        return strPrivateKey;
    }

    /**
     * @param strPrivateKey the strPrivateKey to set
     */
    public void setStrPrivateKey(String strPrivateKey) {
        this.strPrivateKey = strPrivateKey;
    }

    /**
     * @return the strPassword
     */
    public String getStrPassword() {
        return strPassword;
    }

    /**
     * @param strPassword the strPassword to set
     */
    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    /**
     * @return the blockchain
     */
    public Blockchain getBlockchain() {
        return blockchain;
    }

    /**
     * @param blockchain the blockchain to set
     */
    public void setBlockchain(Blockchain blockchain) {
        this.blockchain = blockchain;
    }
}
