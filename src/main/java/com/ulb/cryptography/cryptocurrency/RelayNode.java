/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.cryptocurrency;

import java.util.List;

/**
 *
 * @author masterulb
 */
public class RelayNode {

    private String strAddress;
    private Blockchain blockChain;
    private List<Object> blocksOfTransactions;

    /**
     *
     * @param strAddress
     * @param blockChain
     * @param blocksOfTransactions
     */
    public RelayNode(String strAddress, Blockchain blockChain, List<Object> blocksOfTransactions) {
        this.strAddress = strAddress;
        this.blockChain = blockChain;
        this.blocksOfTransactions = blocksOfTransactions;
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
     * @return the blocksOfTransactions
     */
    public List<Object> getBlocksOfTransactions() {
        return blocksOfTransactions;
    }

    /**
     * @param blocksOfTransactions the blocksOfTransactions to set
     */
    public void setBlocksOfTransactions(List<Object> blocksOfTransactions) {
        this.blocksOfTransactions = blocksOfTransactions;
    }
}
