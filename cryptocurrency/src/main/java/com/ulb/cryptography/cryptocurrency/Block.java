/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.cryptocurrency;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author masterulb
 */
public class Block {

    private List<Transaction> listOfTransactions;
    private String strNonce;
    private String strHash;

    /**
     *
     * @param listOfTransactions
     * @param strNonce
     * @param strHash
     */
    public Block(List<Transaction> listOfTransactions, String strNonce, String strHash) {
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
        this.strNonce = new String();
        this.strHash = new String();
    }

    /**
     * @return the listOfTransactions
     */
    public List<Transaction> getListOfTransactions() {
        return listOfTransactions;
    }

    /**
     * @param listOfTransactions the listOfTransactions to set
     */
    public void setListOfTransactions(List<Transaction> listOfTransactions) {
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
}
