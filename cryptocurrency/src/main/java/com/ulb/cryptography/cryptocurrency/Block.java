/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.cryptocurrency;

import java.io.Serializable;
import java.util.LinkedList;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *
 * @author masterulb
 */
public class Block implements Serializable {

    private static final long serialVersionUID = 12358903454875L;

    private LinkedList<Transaction> listOfTransactions;
    private String strNonce;
    private String strHash;
    ////S/////
    private String prevHash;
    private int BlockID;
    ////S/////

    /**
     *
     * @param listOfTransactions
     * @param strNonce
     * @param strHash
     */
    public Block(LinkedList<Transaction> listOfTransactions, String strNonce, String strHash) {
        this.listOfTransactions = listOfTransactions;
        this.strNonce = strNonce;
        this.strHash = strHash;
    }

    /**
     *
     * @param strNonce
     * @param strHash
     */
    public Block(String strNonce, String strHash) {
        this.listOfTransactions = new LinkedList<>();
        this.strNonce = strNonce;
        this.strHash = strHash;
    }

    public Block() {
        this.listOfTransactions = new LinkedList<>();
        this.strNonce = null;
        this.strHash = null;
    }

    public Block(LinkedList<Transaction> listOfTransactions) {
        this.listOfTransactions = listOfTransactions;
        this.strNonce = "";
        this.strHash = "";
    }

    /**
     * @return the listOfTransactions
     */
    public LinkedList<Transaction> getListOfTransactions() {
        return listOfTransactions;
    }

    /**
     * @param listOfTransactions the listOfTransactions to set
     */
    public void setListOfTransactions(LinkedList<Transaction> listOfTransactions) {
        this.listOfTransactions = listOfTransactions;
    }

    /**
     * @return the strNonce
     */
    public String getStrNonce() {
        return strNonce;
    }

    /**
     * @param strNonce the strNonce to set
     */
    public void setStrNonce(String strNonce) {
        this.strNonce = strNonce;
    }

    /**
     * @return the strHash
     */
    public String getStrHash() {
        return strHash;
    }

    /**
     * @param strHash the strHash to set
     */
    public void setStrHash(String strHash) {
        this.strHash = strHash;
    }

    public String getprevHash() {
        return prevHash;
    }

    public void setprevHash(String prevHash) {
        this.prevHash = prevHash;
    }

    public int getBlockID() {
        return BlockID;
    }

    public void setBlockID(int BlockID) {
        this.BlockID = BlockID;
    }

    ////S////
    public String calcBlockHash() {
        String Hash = Integer.toString(this.BlockID) + this.strNonce + this.prevHash + this.listOfTransactions.toString();
        String sha256hex = DigestUtils.sha256Hex(Hash);
        this.strHash = sha256hex;
        return sha256hex;
    }

    public void createFirstBlock() {

        //Here we need to add transaction
        this.BlockID = 0;
        this.strNonce = "0";
        this.prevHash = "";
        this.strHash = this.calcBlockHash();

    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
