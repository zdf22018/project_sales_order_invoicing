/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipd12.entity;

import java.math.BigDecimal;

/**
 *
 * @author 1095871
 */
public class OrderItem {
    private int id;
    private int orderId;
    private int productId;
    private BigDecimal quantity;
    private BigDecimal itemTotal;

    
    
    OrderItem(int id, int orderId, int productId, BigDecimal quantity, BigDecimal itemTotal){
            setId(id);
            setOrderId(orderId);
            setProductId(productId);
            setQuantity(quantity);
            setItemTotal(itemTotal);
            
    
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
     * @return the orderId
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the productId
     */
    public int getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * @return the quantity
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the ItemToal
     */
    public BigDecimal getItemTotal() {
        return itemTotal;
    }

    /**
     * @param ItemToal the ItemToal to set
     */
    public void setItemTotal(BigDecimal ItemToal) {
        this.itemTotal = ItemToal;
    }
    

    
    
}
