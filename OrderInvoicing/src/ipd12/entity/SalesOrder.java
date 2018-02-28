/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipd12.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author 1095871
 */
public class SalesOrder {
    
     private int id;
    private int customerId;
    private Date timestamp;
    private BigDecimal amountBeforeTax;
    private BigDecimal amountTax;
    private BigDecimal totalAmount;
    private Status status;
    
    enum Status{
        complete, notcomplete
    }
    
    
    SalesOrder(int id, int customerId, Date timestamp, BigDecimal amountBeforeTax, BigDecimal amountTax, BigDecimal totalAmount, Status status){
            setId(id);
            setCustomerId(customerId);
            setTimestamp(timestamp);
            setAmountBeforeTax(amountBeforeTax);
            setAmountTax(amountTax);
            setTotalAmount(totalAmount);
            setStatus(status);
    
    }
    
    SalesOrder(int customerId,BigDecimal amountBeforeTax, BigDecimal amountTax, BigDecimal totalAmount, Status status){
            setId(id);
            setCustomerId(customerId);
            setTimestamp(timestamp);
            setAmountBeforeTax(amountBeforeTax);
            setAmountTax(amountTax);
            setTotalAmount(totalAmount);
            setStatus(status);
    
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the amountBeforeTax
     */
    public BigDecimal getAmountBeforeTax() {
        return amountBeforeTax;
    }

    /**
     * @param amountBeforeTax the amountBeforeTax to set
     */
    public void setAmountBeforeTax(BigDecimal amountBeforeTax) {
        this.amountBeforeTax = amountBeforeTax;
    }

    /**
     * @return the amountTax
     */
    public BigDecimal getAmountTax() {
        return amountTax;
    }

    /**
     * @param amountTax the amountTax to set
     */
    public void setAmountTax(BigDecimal amountTax) {
        this.amountTax = amountTax;
    }

    /**
     * @return the totalAmount
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }
   
    
    
    
    
    
    
}
