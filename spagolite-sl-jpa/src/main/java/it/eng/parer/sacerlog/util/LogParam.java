/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

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
public class LogParam {
    private String nomeApplicazione;
    private String nomeUtente;
    private String nomePagina;
    private String nomeAzione;
    private String nomeComponenteSoftware;
    private String nomeTipoOggetto;
    private BigDecimal idOggetto;
    private TransactionLogContext transactionLogContext;

    public String getNomeApplicazione() {
        return nomeApplicazione;
    }

    public void setNomeApplicazione(String nomeApplicazione) {
        this.nomeApplicazione = nomeApplicazione;
    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    public String getNomePagina() {
        return nomePagina;
    }

    public void setNomePagina(String nomePagina) {
        this.nomePagina = nomePagina;
    }

    public String getNomeAzione() {
        return nomeAzione;
    }

    public void setNomeAzione(String nomeAzione) {
        this.nomeAzione = nomeAzione;
    }

    public BigDecimal getIdOggetto() {
        return idOggetto;
    }

    public void setIdOggetto(BigDecimal idOggetto) {
        this.idOggetto = idOggetto;
    }

    public String getNomeComponenteSoftware() {
        return nomeComponenteSoftware;
    }

    public void setNomeComponenteSoftware(String nomeComponenteSoftware) {
        this.nomeComponenteSoftware = nomeComponenteSoftware;
    }

    public String getNomeTipoOggetto() {
        return nomeTipoOggetto;
    }

    public void setNomeTipoOggetto(String nomeTipoOggetto) {
        this.nomeTipoOggetto = nomeTipoOggetto;
    }

    public LogParam(String nomeApplicazione, String nomeUtente, String nomePagina, String nomeAzione,
            BigDecimal idOggetto) {
        this.nomeApplicazione = nomeApplicazione;
        this.nomeUtente = nomeUtente;
        this.nomePagina = nomePagina;
        this.nomeAzione = nomeAzione;
        this.idOggetto = idOggetto;
    }

    public LogParam(String nomeApplicazione, String nomeUtente, String nomePagina, String nomeAzione) {
        this.nomeApplicazione = nomeApplicazione;
        this.nomeUtente = nomeUtente;
        this.nomePagina = nomePagina;
        this.nomeAzione = nomeAzione;
    }

    public LogParam(String nomeApplicazione, String nomeUtente, String nomePagina) {
        this.nomeApplicazione = nomeApplicazione;
        this.nomeUtente = nomeUtente;
        this.nomePagina = nomePagina;
    }

    public LogParam(String nomeApplicazione, String nomeUtente) {
        this.nomeApplicazione = nomeApplicazione;
        this.nomeUtente = nomeUtente;
    }

    public LogParam(String nomeApplicazione) {
        this.nomeApplicazione = nomeApplicazione;
    }

    public LogParam() {
    }

    public TransactionLogContext getTransactionLogContext() {
        return transactionLogContext;
    }

    public void setTransactionLogContext(TransactionLogContext transactionLogContext) {
        this.transactionLogContext = transactionLogContext;
    }

    public boolean isTransactionActive() {
        return this.transactionLogContext == null ? false : true;
    }

    @Override
    public String toString() {
        return "LogParam{" + "nomeApplicazione=" + nomeApplicazione + ", nomeUtente=" + nomeUtente + ", nomePagina="
                + nomePagina + ", nomeAzione=" + nomeAzione + ", nomeComponenteSoftware=" + nomeComponenteSoftware
                + ", nomeTipoOggetto=" + nomeTipoOggetto + ", idOggetto=" + idOggetto + ", ctx="
                + this.transactionLogContext + '}';
    }

}
