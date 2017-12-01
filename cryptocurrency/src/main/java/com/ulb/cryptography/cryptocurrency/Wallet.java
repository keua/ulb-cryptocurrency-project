/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.cryptocurrency;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author masterulb
 */
public class Wallet {

    private Blockchain blockchain;
    private List<Account> accounts;

    /**
     *
     * @param blockchain
     * @param accounts
     */
    public Wallet(Blockchain blockchain, List<Account> accounts) {
        this.blockchain = blockchain;
        this.accounts = accounts;
    }

    /**
     *
     */
    public Wallet() {
        this.blockchain = new Blockchain();
        this.accounts = new LinkedList<>();
    }

    /**
     *
     */
    public void addBlockToWallet() {

    }

    /**
     *
     * @param transaction
     * @param relayNode
     */
    public void sendTransactionToRelayNode(Transaction transaction, RelayNode relayNode) {
        relayNode.getTransactionList().add(transaction);
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
     * @return the accounts
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * @param accounts the accounts to set
     */
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public String createAccount(String password) throws GeneralSecurityException, NoSuchAlgorithmException, IOException {
        Account account = new Account(password);
        this.accounts.add(account);
        return String.valueOf(account.getAcountID()) + "," + account.getStrAddress();
    }

    public String login(String user, String password) {
        int id = Integer.parseInt(user);
        for (Account account : this.accounts) {
            if (id == account.getAcountID()) {
                try {
                    account.checkpass(user, password);
                    return account.getStrAddress();
                } catch (IOException ex) {
                    Logger.getLogger(Wallet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return null;
    }
}
