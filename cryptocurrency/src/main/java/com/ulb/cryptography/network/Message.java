/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.cryptography.network;

import java.io.Serializable;

/**
 *
 * @author masterulb
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 12358903454875L;
    private Object object;

    public Message() {
    }

    public Message(Object object) {
        this.object = object;
    }

    /**
     * @return the object
     */
    public Object getObject() {
        return object;
    }

    /**
     * @param object the object to set
     */
    public void setObject(Object object) {
        this.object = object;
    }
}
