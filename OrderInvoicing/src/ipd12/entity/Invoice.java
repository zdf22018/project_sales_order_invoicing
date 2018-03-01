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
public class Invoice {

    /**
     * @return the salesOrder
     */
    
   private int id;
   private Date timestamp;
    //private BigDecimal amountBeforeTax;
   // private BigDecimal amountTax;
    //private BigDecimal totalAmount;
    private BigDecimal payment;
    private SalesOrder salesOrder;
   
 
   
    
    Invoice(int id, Date timestamp, SalesOrder salesOrder){
            setId(id);
            setTimestamp(timestamp);
            setSalesOrder(salesOrder);
    
    }
    
    Invoice(Date timestamp, SalesOrder salesOrder){
           
            setTimestamp(timestamp);
            setSalesOrder(salesOrder);
    
    }

    Invoice(Date timestamp, SalesOrder salesOrder, BigDecimal payment){
           
            setTimestamp(timestamp);
            setSalesOrder(salesOrder);
            setPayment(payment);
    
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

    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    /**
     * @param salesOrder the salesOrder to set
     */
    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }
    /**
     * @return the amountBeforeTax
     */
    
    /**
     * @return the payment
     */
    public BigDecimal getPayment() {
        return payment;
    }

    /**
     * @param payment the payment to set
     */
    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }
    
    
}
