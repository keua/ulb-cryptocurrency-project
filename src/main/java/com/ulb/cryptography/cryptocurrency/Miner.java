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
public class Miner {

    private Blockchain blockchain;
    private Object BlockOfTransaction;

    /**
     *
     * @param blockchain
     * @param BlockOfTransaction
     */
    public Miner(Blockchain blockchain, Object BlockOfTransaction) {
        this.blockchain = blockchain;
        this.BlockOfTransaction = BlockOfTransaction;
    }

    /**
     *
     */
    public void processTransactions() {

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

    /**
     * @return the BlockOfTransaction
     */
    public Object getBlockOfTransaction() {
        return BlockOfTransaction;
    }

    /**
     * @param BlockOfTransaction the BlockOfTransaction to set
     */
    public void setBlockOfTransaction(Object BlockOfTransaction) {
        this.BlockOfTransaction = BlockOfTransaction;
    }
}
