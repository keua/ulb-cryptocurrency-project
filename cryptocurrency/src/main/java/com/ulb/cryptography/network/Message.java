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
    private Object object2;

    public Message() {
        this.object = null;
        this.object2 = null;
    }

    public Message(Object object) {
        this.object = object;
        this.object2 = null;
    }

    public Message(Object object, Object object2) {
        this.object = object;
        this.object2 = object2;
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

    /**
     * @return the object2
     */
    public Object getObject2() {
        return object2;
    }

    /**
     * @param object2 the object2 to set
     */
    public void setObject2(Object object2) {
        this.object2 = object2;
    }
}
