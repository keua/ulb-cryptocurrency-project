/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.cryptocurrency;

import java.io.IOException;
import java.io.PrintStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author masterulb
 */
public class ExecutionProcess {

    private static final PrintStream OUT = System.out;
    private static String outResult = "";

    /**
     * @param args the command line arguments
     * @throws java.security.GeneralSecurityException
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws GeneralSecurityException, NoSuchAlgorithmException, IOException {

        // Creating the Blockchain
        Blockchain blockchain = new Blockchain();

        // Creating the master node
        MasterNode masterNode = new MasterNode();
        masterNode.setBlockChain(blockchain);// ASIGN THE BLOCKCHAIN OT THE MASTERNODE

        // Creating 2 realy nodes
        RelayNode relayNode1 = new RelayNode("localhost:3000");
        RelayNode relayNode2 = new RelayNode("localhost:3001");

        // Creating 3 Wallets
        Wallet wallet1 = new Wallet();
        Wallet wallet2 = new Wallet();
        Wallet wallet3 = new Wallet();

        // Creating 2 Miners
        Miner miner1 = new Miner();
        Miner miner2 = new Miner();

        // Firt scenario : creating and account inside a wallet
        // require the password
        wallet1.createAccount("password");
        wallet1.getAccounts().get(0).getPrivateKey();

        String eprk = wallet1.getAccounts().get(0).getEncryptedPrivateKey();
        String pbk = Cryptography.savePublicKey(wallet1.getAccounts().get(0).getPubliKey());
        String addr = wallet1.getAccounts().get(0).getStrAddress();
        String prk = Cryptography.savePrivateKey(wallet1.getAccounts().get(0).getPrivateKey());
        String out = String.format("eprk: %s \n pbk: %s \n address: %s \n prk: %s", eprk, pbk, addr, prk);

        OUT.println(out);

        /*
        //1) INITIALISATION
        // Mater Node
        LinkedList<Transaction> transactions = new LinkedList<>();// we are inicializating the Transactions list.
        for (int i = 0; i < 3; i++) { // we are filling the transactions list.
            Transaction transaction
                    = new Transaction(
                            15, "192.0.1." + i, "192.0.2." + i, new Date()
                    );
            transactions.add(transaction);
        }
        Block block = new Block(transactions, "nonce", "hash");
        MasterNode masterNode = new MasterNode();
        masterNode.addBlockToChain(block);

        // testing that all works
        Block insertedBlock = masterNode.getBlockChain().getListOfBlocks().get(0);
        outResult
                = String.format(
                        "Hash: %s , Nonce: %s",
                        insertedBlock.getStrHash(),
                        insertedBlock.getStrNonce()
                );
        OUT.println(outResult);

        for (Transaction transaction : insertedBlock.getListOfTransactions()) {
            outResult
                    = String.format(
                            "Amount: %d, Address: %s, Receiver: %s",
                            transaction.getIntAmount(),
                            transaction.getStrAddress(),
                            transaction.getStrReceiver()
                    );
            OUT.println(outResult);
        }
        // Miner
        Wallet wallet = new Wallet("ADD001", "KEY0001", "PASSS001");// miner address
        Miner miner = new Miner();
        miner.setWallet(wallet);
         */
    }

}
