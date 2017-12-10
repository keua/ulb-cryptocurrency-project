/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.cryptocurrency;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author masterulb
 */
public class MasterNode {

    private Blockchain blockChain;
    private Block block;
    private Account account;
    private LinkedList<Transaction> notValidTransactions;

    /**
     *
     * @param blockChain
     * @param block
     */
    public MasterNode(Blockchain blockChain, Block block) {
        this.blockChain = blockChain;
        this.block = block;
    }

    /**
     *
     */
    public MasterNode() {
        this.blockChain = new Blockchain();
        this.block = new Block();
        this.notValidTransactions = new LinkedList<>();
    }

    /**
     *
     * @param block
     */
    public void addBlockToChain(Block block) {
        this.blockChain.addToBlockchain(block);
    }

    /**
     *
     */
    public void sendConfirmationToRelayNode() {

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
     * @return the block
     */
    public Block getBlock() {
        return block;
    }

    /**
     * @param block the block to set
     */
    public void setBlock(Block block) {
        this.block = block;
    }

    /**
     * @return the account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean checkBlock(Block newBlock) throws IOException, FileNotFoundException, GeneralSecurityException {
        String newHash = newBlock.calcBlockHash();
        Block lastBlock = this.blockChain.getListOfBlocks().get(this.blockChain.getListOfBlocks().size() - 1);
        
        System.out.println("masternode new Hash "+newHash);
        System.out.println("last block hash "+lastBlock.getStrHash());
        System.out.println("new block id "+newBlock.getBlockID());
        System.out.println("last block id "+lastBlock.getBlockID());
        System.out.println("new block previous hash "+newBlock.getprevHash());
        System.out.println("miner new hash "+newBlock.getStrHash());

        //check that the new block is the last block in the chain
        if (lastBlock.getBlockID() + 1 != newBlock.getBlockID()) {
            return false;
        } else //verify previous block hash
        if (!lastBlock.getStrHash().equals(newBlock.getprevHash())) {
            return false;
        } else //verify this node hash
        if (!newHash.equals(newBlock.getStrHash())) {
            return false;
        }
        
        System.out.println("First validations good");
        
        this.validateReceiverAddress(newBlock.getListOfTransactions());
        this.validateSignature(newBlock.getListOfTransactions());
        this.validateAmount(newBlock.getListOfTransactions());
        //check all block's transactions... Not sure if we need this
        //   for(Transaction t:newBlock.getListOfTransactions()){
        //     this.checkTran(t);
        // }
        // Check if the sum of output transactions are equal the sum of input transactions + mining reward 

        if (this.notValidTransactions.isEmpty()) {
            return true;
        }
        
        return false;

    }

    public PublicKey getPublicKeyFromAddress(String address) throws FileNotFoundException, IOException, GeneralSecurityException {
        String publicKey = null;
        try (BufferedReader br = new BufferedReader(new FileReader("public_repo"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                if (line.equals("address=" + address)) {
                    publicKey = br.readLine();
                }
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        }
        PublicKey pubkey = Cryptography.loadPublicKey(publicKey);
        return pubkey;
    }

    public LinkedList<Transaction> validateReceiverAddress(LinkedList<Transaction> transactions) throws IOException, FileNotFoundException, GeneralSecurityException {
        LinkedList<Transaction> validTransactions = new LinkedList<>();
        for (Transaction transaction : transactions) {
            PublicKey pk = this.getPublicKeyFromAddress(transaction.getStrReceiver());
            if (pk == null) {
                this.notValidTransactions.add(transaction);
            } else {
                validTransactions.add(transaction);
            }

        }
        return validTransactions;
    }

    public LinkedList validateSignature(LinkedList<Transaction> transactions) throws IOException, FileNotFoundException, GeneralSecurityException {
        LinkedList<Transaction> validTransactions = new LinkedList<>();
        for (Transaction transaction : transactions) {
            String concatenatedValues
                    = transaction.getTimeStamp().toString()
                    + transaction.getFltInputSenderAmount()
                    + transaction.getStrReceiver()
                    + transaction.getStrSenderAddress()
                    + transaction.getFltOutputReceiverAmount()
                    + transaction.getFltOutputSenderAmount()
                    + transaction.getTranType();
            //add the rest values
            PublicKey pk = this.getPublicKeyFromAddress(transaction.getStrSenderAddress());
            int b = Cryptography.DSAVerify(pk, transaction.getTransactionSigned(), concatenatedValues);
            boolean sum = false;
            if (transaction.getFltInputSenderAmount() == transaction.getFltOutputReceiverAmount() + transaction.getFltOutputSenderAmount()) {
                sum = true;
            }
            if ((b == 1) && (sum)) {
                validTransactions.add(transaction);
            } else {
                notValidTransactions.add(transaction);
            }
        }
        return validTransactions;
    }

    public LinkedList<Transaction> validateAmount(LinkedList<Transaction> transactions) {

        LinkedList<Transaction> validTransactions = new LinkedList<>();
        HashMap<String, LinkedList<Transaction>> hm = new HashMap();
        for (Transaction transaction : transactions) {
            if (hm.containsKey(transaction.getStrSenderAddress())) {
                hm.get(transaction.getStrSenderAddress()).add(transaction);
            } else {
                hm.put(transaction.getStrSenderAddress(), new LinkedList<>());
                hm.get(transaction.getStrSenderAddress()).add(transaction);
            }
        }
        for (Map.Entry<String, LinkedList<Transaction>> entry : hm.entrySet()) {
            String address = entry.getKey();
            LinkedList<Transaction> transactionsByAddres = entry.getValue();
            Transaction rewardedTransaction = new Transaction();
            Float lastAmount = this.blockChain.getLastTransactionBySenderAddress(address);
            Float sum = 0f;
            for (Transaction transactionByAddres : transactionsByAddres) {
                if (!transactionByAddres.getTranType().equals("reward")) {
                    sum += transactionByAddres.getFltInputSenderAmount();
                    if (sum <= lastAmount) {
                        validTransactions.add(transactionByAddres);
                    } else {
                        this.notValidTransactions.add(transactionByAddres);
                        sum = sum - transactionByAddres.getFltInputSenderAmount();
                    }
                } else {
                    rewardedTransaction = transactionByAddres;
                }
            }

            if (rewardedTransaction.getFltOutputSenderAmount() == validTransactions.size() * 1.5f) {
                validTransactions.add(rewardedTransaction);
            } else {
                this.notValidTransactions.add(rewardedTransaction);
            }
        }
        return validTransactions;
    }

}
