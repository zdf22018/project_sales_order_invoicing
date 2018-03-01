
package ipd12.dao;

import ipd12.entity.Customer;
import ipd12.entity.Invoice;
import ipd12.entity.OrderItem;
import ipd12.entity.OrderStatus;
import ipd12.entity.Product;
import ipd12.entity.SalesOrder;
import ipd12.entity.TaxCode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import jdk.net.SocketFlow;

/**
 *
 * @author Jian Zhao
 */
public class Database {
//    private final String dbURL = "//azuredf.database.windows.net";
//    private final String databaseName = "project";
//    private final String username = "zdf2018";
//    private final String password = "zoe20178@";
    
    private final String dbURL = "//zhaojiandb.database.windows.net";
    private final String databaseName = "dbtest";
    private final String username = "jeanzhao";
    private final String password = "Jean170905";
    
    private Connection conn;
    
    public Database() throws SQLException {
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }catch(ClassNotFoundException ex){
            //exception chaining
            throw new SQLException("Driver not found.", ex);
        }
        
        String connectionString = String.format("jdbc:sqlserver:%s:1433;database=%s;user=%s;password=%s;", dbURL, databaseName, username, password);
        conn = DriverManager.getConnection(connectionString);
    }
    
    public List<Invoice> getInvoices() throws SQLException{
        return getInvoices("", null, null);
    }
    
    public List<Invoice> getInvoices(String customerName, java.util.Date from, java.util.Date to) throws SQLException{
        List<Invoice> invoices = new ArrayList<>();
        StringBuilder sbSql = new StringBuilder();
        StringBuilder sbWhere = new StringBuilder();
        sbSql.append("inv.id, inv.timestamp, inv.amountBeforeTax, inv.amountTax, inv.totalAmount, inv.payment ");       
        sbSql.append("from invoices inv ");
        if(!customerName.isEmpty() || null != from || null != to){
            if(!customerName.isEmpty()){
                sbSql.append("left join orders o on inv.id=o.invoiceId ");
                sbSql.append("left join customers c on o.customerId=c.id ");
                sbWhere.append("c.name like '%");
                sbWhere.append(customerName);
                sbWhere.append("%' ");
            }
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if(null != from){
                if(sbWhere.length() > 0){
                    sbWhere.append(" and ");
                }
                sbWhere.append(" inv.timestamp >='");
                sbWhere.append(df.format(from));
                sbWhere.append(" 00:00:00");
                sbWhere.append("' ");
            }
             if(null != to){
                if(sbWhere.length() > 0){
                    sbWhere.append(" and ");
                }
                sbWhere.append(" inv.timestamp <='");
                sbWhere.append(df.format(to));
                sbWhere.append(" 23:59:59");
                sbWhere.append("' ");
            }
        }
        if(sbWhere.length() > 0){
            sbSql.insert(0, "select distinct ");
            sbSql.append(" where ");            
            sbSql.append(sbWhere);
        }
        else{
            sbSql.insert(0, "select distinct top 10 ");
        }
        sbSql.append("order by inv.timestamp desc");
        System.out.println(sbSql.toString());
        try(
                Statement st = conn.createStatement();
                ResultSet rt = st.executeQuery(sbSql.toString());
            ){
            while(rt.next()){
                List<SalesOrder> saleOrders = getOrders("", rt.getInt(1),"",0);
                //Invoice invoice = new Invoice(rt.getInt(1),rt.getDate(2),rt.getBigDecimal(3), rt.getBigDecimal(4), rt.getBigDecimal(5), rt.getBigDecimal(6), saleOrders);
                Invoice invoice = new Invoice(rt.getInt(1),rt.getDate(2), (saleOrders.size() > 0 ? saleOrders.get(0) : null));
                invoices.add(invoice);
            }
        }
        
        return invoices;
    }
    
     public List<SalesOrder> getOrders() throws SQLException{
        return getOrders("", 0, "", 0);
     }
    
    public List<SalesOrder> getOrders(String customerName, int invoiceId, String status, int orderId) throws SQLException{
        List<SalesOrder> orders = new ArrayList<>();
        StringBuilder sbSql = new StringBuilder();
        StringBuilder sbWhere = new StringBuilder();
        sbSql.append("id, customerId, timestamp, amountBeforeTax, amountTax, totalAmount, status, invoiceId ");
        sbSql.append("from orders ");
       
         if(!customerName.isEmpty() || 0 != invoiceId || !status.isEmpty() || 0!= orderId){
            if(!customerName.isEmpty()){
                sbWhere.append("customerId in(select id from customer where name like '%");
                sbWhere.append(customerName);
                sbWhere.append("%') ");
            }
            
            if(0 != invoiceId){
                if(sbWhere.length() > 0){
                    sbWhere.append(" and ");
                }
                sbWhere.append(" invoiceId =");
                sbWhere.append(invoiceId);
            }
            
             if(!status.isEmpty()){
                 if(sbWhere.length() > 0){
                    sbWhere.append(" and ");
                }
                sbWhere.append("status = '");
                sbWhere.append(status);
                sbWhere.append("' ");
            }
            
            if(0 != orderId){
                if(sbWhere.length() > 0){
                    sbWhere.append(" and ");
                }
                sbWhere.append(" id =");
                sbWhere.append(orderId);
            }
            
        }
        if(sbWhere.length() > 0){
            sbSql.insert(0, "select ");
            sbSql.append(" where ");            
            sbSql.append(sbWhere);
        }
        else{
            sbSql.insert(0, "select top 10 ");
        }
        sbSql.append("order by timestamp desc");
        System.out.println(sbSql.toString());
        try(
                Statement st = conn.createStatement();
                ResultSet rt = st.executeQuery(sbSql.toString());
            ){
            while(rt.next()){
                List <OrderItem> items = getOrderItemsByOrderId(rt.getInt(1));
                SalesOrder order = new SalesOrder(rt.getInt(1),rt.getInt(2),rt.getDate(3),rt.getBigDecimal(4), rt.getBigDecimal(5), rt.getBigDecimal(6),OrderStatus.valueOf(rt.getString(7)), items);
                orders.add(order);
            }
        }
        
        return orders;
    }
    
    public SalesOrder getOrderById(int id) throws SQLException{
        SalesOrder order = null;
        StringBuilder sbSql = new StringBuilder();
        sbSql.append("select id, customerId, timestamp, amountBeforeTax, amountTax, totalAmount, status, invoiceId ");
        sbSql.append("from orders ");
        sbSql.append("where id = ");
        sbSql.append(id);      
      
        System.out.println(sbSql.toString());
        
        try(
                Statement st = conn.createStatement();
                ResultSet rt = st.executeQuery(sbSql.toString());
            ){
            if(rt.next()){
                List <OrderItem> items = getOrderItemsByOrderId(rt.getInt(1));
                order = new SalesOrder(rt.getInt(1), rt.getInt(2), rt.getDate(3), rt.getBigDecimal(4), rt.getBigDecimal(5), rt.getBigDecimal(6), OrderStatus.valueOf(rt.getString(7)), items);
            }
        }
        
        return order;
    }
    
    public List<OrderItem> getOrderItemsByOrderId(int orderId) throws SQLException{
        List<OrderItem> items = new ArrayList<>();
        StringBuilder sbSql = new StringBuilder();
        sbSql.append("select id, orderId, productId, quantity, lineTotal ");
        sbSql.append("from orderItems where orderId=");
        sbSql.append(orderId);       
        
        System.out.println(sbSql.toString());
        try(
                Statement st = conn.createStatement();
                ResultSet rt = st.executeQuery(sbSql.toString());
            ){
            while(rt.next()){
                OrderItem item = new OrderItem(rt.getInt(1),rt.getInt(2),rt.getInt(3),rt.getBigDecimal(4), rt.getBigDecimal(5));
                items.add(item);
            }
        }
        
        return items;
    }
    
    public Product getProductById(int id) throws SQLException{
        Product product = null;
        StringBuilder sbSql = new StringBuilder();
        sbSql.append("select id, description, unitPrice");
        sbSql.append("from product ");
        sbSql.append("where id = ");
        sbSql.append(id);      
      
        System.out.println(sbSql.toString());
        
        try(
                Statement st = conn.createStatement();
                ResultSet rt = st.executeQuery(sbSql.toString());
            ){
            if(rt.next()){                
                product = new Product(rt.getInt(1), rt.getString(2), rt.getBigDecimal(3));
            }
            else{
                throw new SQLException("Not found product id=" + id);
            }
        }
        
        return product;
    }
    
    public Customer getCustomerById(int id) throws SQLException{
        Customer customer = null;
        StringBuilder sbSql = new StringBuilder();
        sbSql.append("select id, name, address, taxCode, creditLimit, email ");
        sbSql.append("from customers ");
        sbSql.append("where id = ");
        sbSql.append(id);      
      
        System.out.println(sbSql.toString());
        
        try(
                Statement st = conn.createStatement();
                ResultSet rt = st.executeQuery(sbSql.toString());
            ){
            if(rt.next()){                
                customer = new Customer(rt.getInt(1), rt.getString(2), rt.getString(3), TaxCode.valueOf(rt.getString(4)), rt.getBigDecimal(5), rt.getString(6));
            }
            else{
                throw new SQLException("Not found product id=" + id);
            }
        }
        
        return customer;
    }
}
