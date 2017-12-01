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
public class MasterNode {

    private Blockchain blockChain;
    private Block block;
    private Account account;

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

}
