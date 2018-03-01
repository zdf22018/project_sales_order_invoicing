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
public class Customer {

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    private int id;
    private String name;
    private String address;
    private TaxCode taxCode;
    private BigDecimal creditLimit;
    private String email;
    
    
    public Customer(){
    
    }
    
    public Customer(int id, String name, String address, TaxCode taxCode, BigDecimal creditLimit, String email){
            setId(id);
            setName(name);
            setAddress(address);
            setTaxCode(taxCode);
            setCreditLimit(creditLimit);
            setEmail(email);
            
    }
    
    public Customer(String name, String address, TaxCode taxCode, BigDecimal creditLimit, String email){
            
            setName(name);
            setAddress(address);
            setTaxCode(taxCode);
            setCreditLimit(creditLimit);
            setEmail(email);
            
    }
    
    Customer(String name, String address, TaxCode taxCode){
            
            setName(name);
            setAddress(address);
            setTaxCode(taxCode);
            setEmail(email);
            
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the taxCode
     */
    public TaxCode getTaxCode() {
        return taxCode;
    }

    /**
     * @param taxCode the taxCode to set
     */
    public void setTaxCode(TaxCode taxCode) {
        this.taxCode = taxCode;
    }

    /**
     * @return the creditLimit
     */
    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    /**
     * @param creditLimit the creditLimit to set
     */
    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }
    
}
