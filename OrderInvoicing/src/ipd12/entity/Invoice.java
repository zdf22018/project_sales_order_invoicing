/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipd12.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author 1095871
 */
public class Invoice {

    /**
     * @return the amountBeforeTax
     */
    
   private int id;
   private java.sql.Date timestamp;
   private BigDecimal amountBeforeTax;
    private BigDecimal amountTax;
   private BigDecimal totalAmount;
    private BigDecimal payment;
    private List <SalesOrder> salesOrder;
    private Customer customer;
   
 
   public Invoice(){
   }
    
   
   
    public Invoice(int id, java.sql.Date timestamp, List salesOrder, Customer customer){
            setId(id);
            setTimestamp(timestamp);
            this.salesOrder=salesOrder;
            this.customer =customer;
    
    }
    public Invoice(int id, java.sql.Date timestamp, List salesOrder){
            setId(id);
            setTimestamp(timestamp);
            this.salesOrder=salesOrder;
            
    
    }
    public Invoice(java.sql.Date timestamp, List salesOrder, Customer customer){
           
           setTimestamp(timestamp);
            this.salesOrder=salesOrder;
            this.customer =customer;
    }

    public Invoice(java.sql.Date timestamp, List salesOrder, BigDecimal payment){
           
            setTimestamp(timestamp);
            this.salesOrder=salesOrder;
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
    public void setTimestamp(java.sql.Date timestamp) {
        this.timestamp = timestamp;
    }

   

   
    public BigDecimal getPayment() {
        return payment;
    }

    /**
     * @param payment the payment to set
     */
    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    /**
     * @return the salesOrder
     */
    public List <SalesOrder> getSalesOrder() {
        return salesOrder;
    }

    /**
     * @param salesOrder the salesOrder to set
     */
    public void setSalesOrder(List <SalesOrder> salesOrder) {
        this.salesOrder = salesOrder;
    }
    
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
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the salesOrder
     */
    
    
}
