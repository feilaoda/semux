/**
 * Copyright (c) 2017-2018 The Semux Developers
 *
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.semux;

public enum CoinType {

    INCENSECOIN((byte) 0, "BIC"),

    INCENSE((byte) 1, "BICI"),
    INCENSEPIECE((byte) 2, "BICIP");


    CoinType(byte id, String label) {
        this.id = id;
        this.label = label;
    }

    byte id;
    String label;

    public byte id() {
        return id;
    }

    public String label() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
