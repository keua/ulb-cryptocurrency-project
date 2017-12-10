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
        LinkedList<Block> temp = (LinkedList<Block>) this.listOfBlocks.clone();

        Iterator lit = temp.descendingIterator();
        System.out.println("The blockchain has " + temp.size() + " blocks");
        while (lit.hasNext()) {
            Block b = (Block) lit.next();
            Iterator lit2 = b.getListOfTransactions().descendingIterator();
            System.out.println(b.toString());
            while (lit2.hasNext()) {
                Transaction t = (Transaction) lit2.next();
                System.out.println(t.toString());
                if (t.getStrSenderAddress().equals(address)) {
                    lastSenderTrans = t;
                    lastAmount = lastSenderTrans.getFltOutputSenderAmount();
                    System.out.println("Ive found the last Amount this address sent " + lastAmount);
                    lastBlock = b;
                    break;
                }
            }
            if (lastBlock != null) {
                break;
            }
        }
        if (temp.indexOf(lastBlock) > -1) { // if not exists mean the addres dont have any transaction yet
            System.out.println("we will start in the block no. " + temp.indexOf(lastBlock));
            for (int i = temp.indexOf(lastBlock); i < temp.size(); i++) {
                Block b = temp.get(i);
                if (i == temp.indexOf(lastBlock)) {
                    System.out.println("In the transaction no. " + b.getListOfTransactions().indexOf(lastSenderTrans));
                    for (int j = b.getListOfTransactions().indexOf(lastSenderTrans) + 1; j < b.getListOfTransactions().size(); j++) {
                        Transaction t = b.getListOfTransactions().get(j);
                        if (t.getStrReceiver().equals(address)) {
                            lastAmount += t.getFltOutputReceiverAmount();
                            System.out.println(lastAmount);
                        }
                    }
                } else {
                    for (int j = 0; j < b.getListOfTransactions().size(); j++) {
                        Transaction t = b.getListOfTransactions().get(j);
                        if (t.getStrReceiver().equals(address)) {
                            lastAmount += t.getFltOutputReceiverAmount();
                            System.out.println(lastAmount);
                        }
                    }
                }

            }
        }

        return lastAmount;
    }
}
