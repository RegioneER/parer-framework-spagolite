/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.sacerlog.util;

import java.math.BigDecimal;

/**
 *
 * @author Iacolucci_M
 */
public class TransactionLogContext {
    private BigDecimal transactionId;

    public TransactionLogContext(BigDecimal transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getTransactionId() {
        return transactionId;
    }

    @Override
    public String toString() {
        return transactionId.toPlainString();
    }

}
