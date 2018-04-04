/**
 * Copyright (c) 2017-2018 The Semux Developers
 *
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.semux.gui.panel;

import org.semux.Kernel;
import org.semux.Network;
import org.semux.config.Config;
import org.semux.core.PendingManager;
import org.semux.core.Transaction;
import org.semux.core.TransactionType;
import org.semux.crypto.CryptoException;
import org.semux.crypto.Hex;
import org.semux.crypto.Key;
import org.semux.gui.Action;
import org.semux.gui.SemuxGui;
import org.semux.gui.SwingUtil;
import org.semux.gui.model.WalletAccount;
import org.semux.gui.model.WalletModel;
import org.semux.message.GuiMessages;
import org.semux.util.Bytes;
import org.semux.util.exception.UnreachableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BurningPanel extends JPanel implements ActionListener {

    private static final Logger logger = LoggerFactory.getLogger(BurningPanel.class);

    private static final long serialVersionUID = 1L;

    private transient SemuxGui gui;
    private transient WalletModel model;
    private transient Kernel kernel;

    private transient Config config;

    private JTextField txtAmount;
    private JTextField txtData;

    public BurningPanel(SemuxGui gui, JFrame frame) {
        this.gui = gui;
        this.model = gui.getModel();
        this.model.addListener(this);

        this.kernel = gui.getKernel();
        this.config = kernel.getConfig();

        setBorder(new LineBorder(Color.LIGHT_GRAY));



//        JLabel lblTo = new JLabel(GuiMessages.get("To") + ":");
//        lblTo.setHorizontalAlignment(SwingConstants.RIGHT);


        JLabel lblAmount = new JLabel(GuiMessages.get("Amount") + ":");
        lblAmount.setHorizontalAlignment(SwingConstants.RIGHT);

        txtAmount = SwingUtil.textFieldWithCopyPastePopup();
        txtAmount.setName("txtAmount");
        txtAmount.setColumns(10);
        txtAmount.setActionCommand(Action.SEND.name());
        txtAmount.addActionListener(this);

        JLabel lblFee = new JLabel(GuiMessages.get("Fee") + ":");
        lblFee.setHorizontalAlignment(SwingConstants.RIGHT);
        lblFee.setToolTipText(GuiMessages.get("FeeTip", SwingUtil.formatValue(config.minTransactionFee())));


        JLabel lblData = new JLabel(GuiMessages.get("Data") + ":");
        lblData.setHorizontalAlignment(SwingConstants.RIGHT);
        lblData.setToolTipText(GuiMessages.get("DataTip"));

        txtData = SwingUtil.textFieldWithCopyPastePopup();
        txtData.setName("txtData");
        txtData.setColumns(10);
        txtData.setActionCommand(Action.SEND.name());
        txtData.addActionListener(this);
        txtData.setToolTipText(GuiMessages.get("DataTip"));

        JLabel lblSem1 = new JLabel("SEM");

//        JLabel lblSem2 = new JLabel("SEM");

        JButton btnSend = new JButton(GuiMessages.get("Send"));
        btnSend.setName("btnSend");
        btnSend.addActionListener(this);
        btnSend.setActionCommand(Action.SEND.name());

        JButton btnClear = new JButton(GuiMessages.get("Clear"));
        btnClear.setName("btnClear");
        btnClear.addActionListener(this);
        btnClear.setActionCommand(Action.CLEAR.name());

        // @formatter:off
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(62)
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
//                                        .addComponent(lblTo)
                                        .addComponent(lblAmount)
//                                        .addComponent(lblFee)
                                        .addComponent(lblData))
                                .addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addComponent(btnClear)
                                                .addGap(10)
                                                .addComponent(btnSend))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
//                                                        .addComponent(selectFrom, 0, 400, Short.MAX_VALUE)
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                                                        .addComponent(txtAmount, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                                                                        .addComponent(txtData))
                                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                                .addGap(12)
                                                                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                                                                        .addComponent(lblSem1)
//                                                                                        .addComponent(lblSem2)
                                                                                ))
//                                                                        .addGroup(groupLayout.createSequentialGroup()
//                                                                                .addPreferredGap(ComponentPlacement.RELATED)
//                                                                                .addComponent(rdbtnText)
//                                                                                .addPreferredGap(ComponentPlacement.RELATED)
//                                                                                .addComponent(rdbtnHex))
                                                                ))
//                                                        .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
//                                                                .addComponent(txtTo, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
//                                                                .addGap(18)
//                                                                .addComponent(btnAddressBook))
                                                )
                                                .addGap(59))))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
//                                .addGap(18)
//                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
//                                        .addComponent(lblFrom)
//                                        .addComponent(selectFrom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
// )
                                .addGap(18)
//                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
//                                        .addComponent(lblTo)
//                                        .addComponent(txtTo, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(btnAddressBook))
                                .addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblAmount)
                                        .addComponent(txtAmount, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblSem1))
                                .addGap(18)
//                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
//                                        .addComponent(lblFee)
//                                        .addComponent(txtFee, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(lblSem2))
//                                .addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblData)
                                        .addComponent(txtData, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(rdbtnText)
//                                        .addComponent(rdbtnHex)
                                )
                                .addGap(18)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(btnSend)
                                        .addComponent(btnClear))
                                .addContainerGap(30, Short.MAX_VALUE)
                                )
        );
        setLayout(groupLayout);
        // @formatter:on

        refresh();
        clear();
    }


    public long getAmountText() throws ParseException {
        return SwingUtil.parseValue(txtAmount.getText().trim());
    }

    public void setAmountText(long a) {
        txtAmount.setText(SwingUtil.formatValue(a, false));
    }


    public String getDataText() {
        return txtData.getText().trim();
    }

    public void setDataText(String dataText) {
        txtData.setText(dataText.trim());
    }

    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        Action action = Action.valueOf(e.getActionCommand());

        switch (action) {
        case REFRESH:
            refresh();
            break;
        case SEND:
            send();
            break;
        case CLEAR:
            clear();
            break;
        case SHOW_ADDRESS_BOOK:
            showAddressBook();
            break;
        default:
            throw new UnreachableException();
        }
    }

    /**
     * Refreshes the GUI.
     */
    protected void refresh() {
    }

    /**
     * Sends transaction.
     */
    protected void send() {
        try {
            WalletAccount acc = getSelectedAccount();
            long value = getAmountText();
            String data = getDataText();

            // decode0x recipient address

            byte[] to = Hex.decode0x(getToText());


            if (acc == null) {
                showErrorDialog(GuiMessages.get("SelectAccount"));
            } else if (value <= -100000L) {
                showErrorDialog(GuiMessages.get("EnterValidValue"));
            } else if (Bytes.of(data).length > config.maxTransactionDataSize(TransactionType.TRANSFER)) {
                showErrorDialog(
                        GuiMessages.get("InvalidData", config.maxTransactionDataSize(TransactionType.TRANSFER)));
            } else {
                int ret = JOptionPane.showConfirmDialog(this,
                        GuiMessages.get("TransferInfo", SwingUtil.formatValue(value), ""),
                        GuiMessages.get("ConfirmTransfer"), JOptionPane.YES_NO_OPTION);
                if (ret == JOptionPane.YES_OPTION) {
                    PendingManager pendingMgr = kernel.getPendingManager();

                    byte[] rawData = Bytes.of(data);

                    Network network = kernel.getConfig().network();
                    TransactionType type = TransactionType.TRANSFER;
                    byte[] from = acc.getKey().toAddress();
                    long nonce = pendingMgr.getNonce(from);
                    long timestamp = System.currentTimeMillis();
                    Transaction tx = new Transaction(network, type, to, value, 0, nonce, timestamp, rawData);
                    tx.sign(acc.getKey());

                    logger.info("transaction info:"+tx.toString()+ " signature:"+Hex.encode(tx.getSignature().toBytes()));

                    if(data.equals("1")) {
                        sendTransaction(pendingMgr, tx);
                    }
                }
            }
        } catch (ParseException | CryptoException ex) {
            showErrorDialog(GuiMessages.get("EnterValidValue"));
        }
    }

    public String getToText() {
        return "0000000000000000000000000000000000000000".trim();
    }


    /**
     * Clears all input fields.
     */
    protected void clear() {
        setAmountText(0);
        setDataText("");
    }

    /**
     * Shows the address book.
     */
    protected void showAddressBook() {
        gui.getAddressBookDialog().setVisible(true);
    }

    /**
     * Returns the selected account.
     * 
     * @return
     */
    protected WalletAccount getSelectedAccount() {
        int idx = 0;
        return (idx == -1) ? null : model.getAccounts().get(idx);
    }

    /**
     * Adds a transaction to the pending manager.
     * 
     * @param pendingMgr
     * @param tx
     */
    protected void sendTransaction(PendingManager pendingMgr, Transaction tx) {
        PendingManager.ProcessTransactionResult result = pendingMgr.addTransactionSync(tx);
        if (result.error == null) {
            JOptionPane.showMessageDialog(
                    this,
                    GuiMessages.get("TransactionSent", 30),
                    GuiMessages.get("SuccessDialogTitle"),
                    JOptionPane.INFORMATION_MESSAGE);
            clear();
            model.fireUpdateEvent();
        } else {
            logger.error(GuiMessages.get("TransactionFailed", result.error.toString()), result.error);
            showErrorDialog(GuiMessages.get("TransactionFailed", result.error.toString()));
        }
    }

    /**
     * Shows an error dialog.
     * 
     * @param message
     */
    protected void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                GuiMessages.get("ErrorDialogTitle"),
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Represents an item in the account drop list.
     */
    protected static class AccountItem {
        WalletAccount account;
        String name;

        public AccountItem(WalletAccount a) {
            Optional<String> alias = a.getName();

            this.account = a;
            this.name = Hex.PREF + account.getKey().toAddressString() + ", " // address
                    + (alias.map(s -> s + ", ").orElse("")) // alias
                    + SwingUtil.formatValue(account.getAvailable()); // available
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}