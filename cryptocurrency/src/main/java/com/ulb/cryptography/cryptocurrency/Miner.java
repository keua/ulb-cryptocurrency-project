/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.cryptocurrency;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author masterulb
 */
public class Miner {

    private Blockchain blockchain;
    private Block blockOfTransaction;
    private Wallet wallet;

    /**
     *
     * @param blockchain
     * @param BlockOfTransaction
     */
    public Miner(Blockchain blockchain, Block BlockOfTransaction) throws GeneralSecurityException, NoSuchAlgorithmException, IOException {
        this.blockchain = blockchain;
        this.blockOfTransaction = BlockOfTransaction;
        this.wallet = new Wallet();
        this.wallet.createAccount("miner");
    }

    /**
     *
     */
    public Miner() throws GeneralSecurityException, NoSuchAlgorithmException, IOException {
        this.blockchain = new Blockchain();
        this.blockOfTransaction = new Block();
        this.wallet = new Wallet();
        this.wallet = new Wallet();
        this.wallet.createAccount("miner");
    }

    /**
     *
     */
    public void reqestTransactionListToTheRealy() {

    }

    /**
     * @return the wallet
     */
    public Wallet getWallet() {
        return wallet;
    }

    /**
     * @param wallet the wallet to set
     */
    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
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
     * @return the blockOfTransaction
     */
    public Object getBlockOfTransaction() {
        return blockOfTransaction;
    }

    /**
     * @param BlockOfTransaction the blockOfTransaction to set
     */
    public void setBlockOfTransaction(Block BlockOfTransaction) {
        this.blockOfTransaction = BlockOfTransaction;
    }
}
