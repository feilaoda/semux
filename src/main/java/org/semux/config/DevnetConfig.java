/**
 * Copyright (c) 2017-2018 The Semux Developers
 *
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.semux.config;

import static org.semux.net.Capability.SEM_TESTNET;

import org.semux.Network;
import org.semux.net.CapabilitySet;

public class DevnetConfig extends AbstractConfig {

    public DevnetConfig(String dataDir) {
        super(dataDir, Network.DEVNET, Constants.DEVNET_VERSION);
        this.netMaxInboundConnectionsPerIp = Integer.MAX_VALUE;
        minTransactionFee = 0;
        minDelegateBurnAmount = 0;
    }

    @Override
    public CapabilitySet capabilitySet() {
        return CapabilitySet.of(SEM_TESTNET);
    }
}
