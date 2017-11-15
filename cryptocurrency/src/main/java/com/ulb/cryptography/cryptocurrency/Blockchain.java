/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.cryptocurrency;

import java.util.List;

/**
 *
 * @author masterulb
 */
public class Blockchain {

    private List<Block> listOfBlocks;

    /**
     *
     * @param listOfBlocks
     */
    public Blockchain(List<Block> listOfBlocks) {
        this.listOfBlocks = listOfBlocks;
    }

    /**
     *
     * @param block
     */
    public void addToBlockchain(Block block) {

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
    public void setListOfBlocks(List<Block> listOfBlocks) {
        this.listOfBlocks = listOfBlocks;
    }
}
