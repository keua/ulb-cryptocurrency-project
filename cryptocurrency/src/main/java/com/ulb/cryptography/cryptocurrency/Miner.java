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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author masterulb
 */
public class Miner {

    private Blockchain blockchain;
    private Block blockOfTransaction;
    private Wallet wallet;
    private LinkedList<Transaction> notValidTransactions;

    /**
     *
     * @param blockchain
     * @param BlockOfTransaction
     * @throws java.security.GeneralSecurityException
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.IOException
     */
    public Miner(
            Blockchain blockchain, Block BlockOfTransaction
    ) throws GeneralSecurityException, NoSuchAlgorithmException, IOException {
        this.blockchain = blockchain;
        this.blockOfTransaction = BlockOfTransaction;
        this.wallet = new Wallet();
        this.wallet.createAccount("miner");
        this.notValidTransactions = new LinkedList<>();
    }

    /**
     *
     * @throws java.security.GeneralSecurityException
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.IOException
     */
    public Miner() throws GeneralSecurityException, NoSuchAlgorithmException, IOException {
        this.blockchain = new Blockchain();
        this.blockOfTransaction = new Block();
        this.wallet = new Wallet();
        this.wallet.createAccount("miner");
        this.notValidTransactions = new LinkedList<>();
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

    // this method create the new block to be mined.. list of transaction is the pending
    // transaction the miner get fro the relay... and the prevBlock is the last block on
    // the block chain cuz to create a new block we need the hash of the previous one
    public Block CreateNewBlock(LinkedList<Transaction> listOfTransactions) {
        //Thia is the block to be mined
        Block block = new Block();
        Block lastBlock = this.blockchain.getListOfBlocks().get(this.blockchain.getListOfBlocks().size() - 1);
        block.setBlockID(lastBlock.getBlockID() + 1);
        block.setListOfTransactions(listOfTransactions);
        //depending on how we'll create a transaction
        // A reward transaction has to be added here to the list of transactions
        // Add the reward transaction to the list
        //Here we generate a random string using the class RandomString for the nonce
        // Here I'm using 8 length string but we should use a longer one
        // we can also a random length string
        RandomString gen = new RandomString(8, ThreadLocalRandom.current());
        block.setStrNonce(gen.toString());
        block.setprevHash(lastBlock.getStrHash());
        return block;
    }

    //This the prove of work pretty simple
    // the difficulty is the number of zeros in the begining of the hash
    // the more the difficulty is the longer the time needed to find the hash
    // block is the new created block
    //the method return the mined block
    public Block ProveOfWork(Block block, int difficulty) {
        String diff = new String(new char[difficulty]).replace('\0', '0');
        System.out.println(diff);
        String hash;
        // String hash= block.calcBlockHash();
        do {
            // Here I'm using 8 length string but we should use a longer one I think
            RandomString gen = new RandomString(8, ThreadLocalRandom.current());
            block.setStrNonce(gen.toString());
            hash = block.calcBlockHash();
            System.out.println(hash);
            System.out.println(hash.substring(0, difficulty));
        } while (!hash.substring(0, difficulty).equals(diff));
        return block;
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
                this.getNotValidTransactions().add(transaction);
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
            if (!transaction.getTranType().equals("reward")) {
                if (transaction.getFltInputSenderAmount() == transaction.getFltOutputReceiverAmount() + transaction.getFltOutputSenderAmount()) {
                    sum = true;
                }
            }
            if ((b == 1) && (sum)) {
                validTransactions.add(transaction);
            } else {
                getNotValidTransactions().add(transaction);
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
            Float lastAmount = this.blockchain.getLastTransactionBySenderAddress(address);
            Float sum = 0f;
            for (Transaction transactionByAddres : transactionsByAddres) {
                sum += transactionByAddres.getFltInputSenderAmount();
                if (sum <= lastAmount) {
                    validTransactions.add(transactionByAddres);
                } else {
                    this.getNotValidTransactions().add(transactionByAddres);
                    sum = sum - transactionByAddres.getFltInputSenderAmount();
                }
            }

        }
        return validTransactions;
    }

    /**
     * @return the notValidTransactions
     */
    public LinkedList<Transaction> getNotValidTransactions() {
        return notValidTransactions;
    }

    /**
     * @param notValidTransactions the notValidTransactions to set
     */
    public void setNotValidTransactions(LinkedList<Transaction> notValidTransactions) {
        this.notValidTransactions = notValidTransactions;
    }
}
