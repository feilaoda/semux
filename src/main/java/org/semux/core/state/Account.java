/**
 * Copyright (c) 2017-2018 The Semux Developers
 *
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.semux.core.state;

import org.semux.crypto.Hex;
import org.semux.util.SimpleDecoder;
import org.semux.util.SimpleEncoder;

public class Account {

    private byte[] address;
    private long available; //coin avaible
    private long incensePieceAvailable;
    private long incenseAvailable;

    private long locked;
    private long nonce;

    /**
     * Creates an account instance.
     * 
     * @param address
     * @param available
     * @param locked
     * @param nonce
     */
    @Deprecated
    public Account(byte[] address, long available, long locked, long nonce) {
        this.address = address;
        this.available = available;
        this.incenseAvailable = 0;
        this.incensePieceAvailable = 0;
        this.locked = locked;
        this.nonce = nonce;
    }

    public Account(byte[] address, long available, long incensePieceAvailable, long incenseAvailable, long locked, long nonce) {
        this.address = address;
        this.available = available;
        this.incenseAvailable = incenseAvailable;
        this.incensePieceAvailable = incensePieceAvailable;
        this.locked = locked;
        this.nonce = nonce;
    }

    /**
     * Serializes this account into byte array.
     * 
     * @return
     */
    public byte[] toBytes() {
        SimpleEncoder enc = new SimpleEncoder();
        enc.writeLong(available);
        enc.writeLong(locked);
        enc.writeLong(nonce);
        enc.writeLong(incensePieceAvailable);
        enc.writeLong(incenseAvailable);

        return enc.toBytes();
    }

    /**
     * Parses an account from byte array.
     * 
     * @param address
     * @param bytes
     * @return
     */
    public static Account fromBytes(byte[] address, byte[] bytes) {
        SimpleDecoder dec = new SimpleDecoder(bytes);
        long available = dec.readLong();
        long locked = dec.readLong();
        long nonce = dec.readLong();

        long incensePieceAvailable = dec.readLong();
        long incenseAvailable = dec.readLong();


        return new Account(address, available, incensePieceAvailable, incenseAvailable, locked, nonce);
    }

    /**
     * Returns the address of this account.
     * 
     * @return
     */
    public byte[] getAddress() {
        return address;
    }

    /**
     * Returns the available balance of this account.
     * 
     * @return
     */
    public long getAvailable() {
        return available;
    }

    /**
     * Sets the available balance of this account.
     * 
     * @param available
     */
    void setAvailable(long available) {
        this.available = available;
    }

    /**
     * Returns the locked balance of this account.
     * 
     * @return
     */
    public long getLocked() {
        return locked;
    }

    /**
     * Sets the locked balance of this account.
     * 
     * @param locked
     */
    void setLocked(long locked) {
        this.locked = locked;
    }

    /**
     * Gets the nonce of this account.
     * 
     * @return
     */
    public long getNonce() {
        return nonce;
    }

    /**
     * Sets the nonce of this account.
     * 
     * @param nonce
     */
    void setNonce(long nonce) {
        this.nonce = nonce;
    }


    public long getIncensePieceAvailable() {
        return incensePieceAvailable;
    }

    public void setIncensePieceAvailable(long incensePieceAvailable) {
        this.incensePieceAvailable = incensePieceAvailable;
    }

    public long getIncenseAvailable() {
        return incenseAvailable;
    }

    public void setIncenseAvailable(long incenseAvailable) {
        this.incenseAvailable = incenseAvailable;
    }

    @Override
    public String toString() {
        return "Account [address=" + Hex.encode(address) + ", available=" + available + ", incense piece available=" + incensePieceAvailable  + ", incense available=" + incenseAvailable + ", locked=" + locked
                + ", nonce=" + nonce + "]";
    }


}
