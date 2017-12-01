/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.cryptocurrency;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author masterulb
 */
public class Blockchain implements Serializable {

    private static final long serialVersionUID = 12358903454875L;
    private LinkedList<Block> listOfBlocks;

    /**
     *
     * @param listOfBlocks
     */
    public Blockchain(LinkedList<Block> listOfBlocks) {
        this.listOfBlocks = listOfBlocks;
    }

    /**
     *
     */
    public Blockchain() {
        this.listOfBlocks = new LinkedList<>();
    }

    /**
     *
     * @param block
     */
    public void addToBlockchain(Block block) {
        this.listOfBlocks.add(block);
    }

    /**
     * @return the listOfBlocks
     */
    public List<Block> getListOfBlocks() {
        return listOfBlocks;
    }

    /**
     * @param listOfBlocks the listOfBlocks to set
     */
    public void setListOfBlocks(LinkedList<Block> listOfBlocks) {
        this.listOfBlocks = listOfBlocks;
    }

    public Float getLastTransactionBySenderAddress(String address) {

        Transaction lastSenderTrans = null;
        Block lastBlock = null;
        Float lastAmount = 0f;

        Iterator lit = this.listOfBlocks.descendingIterator();
        System.out.println("Backward Iterations");
        while (lit.hasNext()) {
            Block b = (Block) lit.next();
            Iterator lit2 = b.getListOfTransactions().descendingIterator();
            while (lit2.hasNext()) {
                Transaction t = (Transaction) lit2.next();
                if (t.getStrSenderAddress().equals(address)) {
                    lastSenderTrans = t;
                    lastAmount = lastSenderTrans.getFltOutputSenderAmount();
                    System.out.println(lastAmount);
                    System.out.println("isa "+String.valueOf(lastSenderTrans.getFltInputSenderAmount()));
                    System.out.println("ora "+String.valueOf(lastSenderTrans.getFltOutputReceiverAmount()));
                    System.out.println("osa "+String.valueOf(lastSenderTrans.getFltOutputSenderAmount()));
                    System.out.println("adr "+String.valueOf(lastSenderTrans.getStrReceiver()));
                    System.out.println("ads "+String.valueOf(lastSenderTrans.getStrSenderAddress()));
                    System.out.println("t "+String.valueOf(lastSenderTrans.getTimeStamp()));
                    break;
                }
            }
            lastBlock = b;
            break;
        }
        for (int i = listOfBlocks.indexOf(lastBlock); i < listOfBlocks.size(); i++) {
            for (int j = lastBlock.getListOfTransactions().indexOf(lastSenderTrans) + 1; j < lastBlock.getListOfTransactions().size(); j++) {
                Transaction t = lastBlock.getListOfTransactions().get(i);
                if (t.getStrReceiver().equals(address)) {
                    lastAmount += t.getFltOutputReceiverAmount();
                    System.out.println(lastAmount);
                }
            }
        }

        return lastAmount;
    }
}
