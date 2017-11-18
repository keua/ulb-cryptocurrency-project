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
public class RelayNode {

    private String strAddress;
    private Blockchain blockChain;
    private Block minedBlock;
    private List<Transaction> transactionList;

    /**
     *
     * @param strAddress
     * @param blockChain
     */
    public RelayNode(String strAddress, Blockchain blockChain) {
        this.strAddress = strAddress;
        this.blockChain = blockChain;
        this.transactionList = new LinkedList<>();
    }

    /**
     *
     * @param strAddress
     */
    public RelayNode(String strAddress) {
        this.strAddress = strAddress;
        this.blockChain = new Blockchain();
        this.transactionList = new LinkedList<>();
    }

    /**
     *
     */
    public RelayNode() {
        this.strAddress = new String();
        this.blockChain = new Blockchain();
        this.transactionList = new LinkedList<>();
    }

    /**
     *
     */
    public void sendMineBlockToMasterNode() {

    }

    /**
     *
     */
    public void confirmMinedBlock() {

    }

    /**
     *
     */
    public void updateBlockchain() {

    }

    /**
     *
     */
    public void sendBlockToWallets() {

    }

    /**
     *
     */
    public void sendBlockchain() {

    }

    /**
     *
     */
    public void sendBlockToMiners() {

    }

    /**
     *
     */
    public void rewardMinerWithCoins() {

    }

    /**
     *
     */
    public void sendTransactionsToMiners() {

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
     * @return the blockChain
     */
    public Blockchain getBlockChain() {
        return blockChain;
    }

    /**
     * @param blockChain the blockChain to set
     */
    public void setBlockChain(Blockchain blockChain) {
        this.blockChain = blockChain;
    }

    /**
     * @return the minedBlock
     */
    public Block getMinedBlock() {
        return minedBlock;
    }

    /**
     * @param minedBlock the minedBlock to set
     */
    public void setMinedBlock(Block minedBlock) {
        this.minedBlock = minedBlock;
    }

    /**
     * @return the transactionList
     */
    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    /**
     * @param transactionList the transactionList to set
     */
    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
}
