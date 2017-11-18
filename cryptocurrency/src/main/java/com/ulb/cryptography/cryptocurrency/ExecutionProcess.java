/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.cryptocurrency;

import java.io.PrintStream;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 * @author masterulb
 */
public class ExecutionProcess {
    
    private static final PrintStream OUT = System.out;
    private static String outResult = "";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creating part
        LinkedList<Transaction> transactions = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
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
        
        Wallet wallet = new Wallet("ADD001", "KEY0001", "PASSS001");// miner address
        Miner miner = new Miner();
        miner.setWallet(wallet);
        
        Transaction masternodePayTransaction
                = new Transaction(100, "ADDMASTERNODE", "ADD001", new Date());
        RelayNode relayNode = new RelayNode("192.168.4.1");

        // sending the transaction to the realy node
        masterNode
                .getWallet()
                .sendTransactionToRelayNode(masternodePayTransaction, relayNode);
        
        outResult
                = String.format(
                        "Transaction Amount: %s",
                        relayNode.getTransactionList().get(0).getIntAmount()
                );
        OUT.println(outResult);
        
    }
    
}
