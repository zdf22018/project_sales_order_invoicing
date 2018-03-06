/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipd12.zz;

import ipd12.dao.Database;
import ipd12.entity.Invoice;
import ipd12.entity.OrderItem;
import ipd12.entity.OrderStatus;
import ipd12.entity.SalesOrder;
import java.awt.Component;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Transport;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author 1796129
 */
public class MainFrameInvoice extends javax.swing.JFrame {

    DefaultTableModel modelInvoices = new DefaultTableModel();
    DefaultTableModel modelInvoiceOrderItems = new DefaultTableModel();
    DefaultTableModel dlgIssueOrdersModel = new DefaultTableModel();
    DefaultTableModel dlgOrdersModel = new DefaultTableModel();
    DefaultTableModel dlgOrdersItemsModel = new DefaultTableModel();

    private Database db;

    /**
     * Creates new form MainFrameInvoice
     */
    public MainFrameInvoice() {

        try {
            db = new Database();

            initComponents();
            jtInvoices.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {
                    //I want something to happen before the row change is triggered on the UI.
                    loadInvoiceOrderlines();
                    //System.out.println(jtInvoices.getSelectedRow());
                }
            });

            jtOrderitems.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {
                    //System.out.println(jtOrderitems.getSelectedRow());
                    //System.out.println(jtOrderitems.getModel().getValueAt(jtOrderitems.getSelectedRow(), 0));
                }
            });

            dlgOrders_jtOrders.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {
                    //I want something to happen before the row change is triggered on the UI.
                    loadOrderItems();
                    //System.out.println(dlgOrders_jtOrders.getSelectedRow());
                }
            });
            dlgOrders_jtItems.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {
                    //System.out.println(dlgOrders_jtItems.getSelectedRow());
                    //System.out.println(dlgOrders_jtItems.getModel().getValueAt(dlgOrders_jtItems.getSelectedRow(), 0));
                }
            });

            loadTableTitle();
            loadInvoices();
            loadInvoiceOrderlines();
            lbStatus.setText(jtInvoices.getRowCount() + "" + " invoices displayed");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Fatal error: can not connect to database.", "database error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private void loadTableTitle() {
        modelInvoices.addColumn("Id");
        modelInvoices.addColumn("CustomerId");
        modelInvoices.addColumn("CustomerName");
        modelInvoices.addColumn("Address");
        modelInvoices.addColumn("AmountBeforeTax");
        modelInvoices.addColumn("AmountTax");
        modelInvoices.addColumn("TotalAmount");
        modelInvoices.addColumn("Date");

        modelInvoiceOrderItems.addColumn("OrderId");
        modelInvoiceOrderItems.addColumn("ProductDescription");
        modelInvoiceOrderItems.addColumn("UnitPrice");
        modelInvoiceOrderItems.addColumn("Quantity");
        modelInvoiceOrderItems.addColumn("ItemTotal");

        dlgIssueOrdersModel.addColumn("Id");
        dlgIssueOrdersModel.addColumn("CustomerName");
        dlgIssueOrdersModel.addColumn("Date");
        dlgIssueOrdersModel.addColumn("AmountBeforeTax");
        dlgIssueOrdersModel.addColumn("AmountTax");
        dlgIssueOrdersModel.addColumn("TotalAmount");

        dlgOrdersModel.addColumn("Id");
        dlgOrdersModel.addColumn("CustomerName");
        dlgOrdersModel.addColumn("Date");
        dlgOrdersModel.addColumn("TotalAmount");

        dlgOrdersItemsModel.addColumn("ProductId");
        dlgOrdersItemsModel.addColumn("ProductDescription");
        dlgOrdersItemsModel.addColumn("UnitPrice");
        dlgOrdersItemsModel.addColumn("Quantity");
        dlgOrdersItemsModel.addColumn("ItemTotal");
    }

    private void loadInvoices() {

        try {
            modelInvoices.setRowCount(0);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date dateFrom = null;
            java.util.Date dateTo = null;
            if (!txtDateFrom.getText().isEmpty()) {
                dateFrom = df.parse(txtDateFrom.getText());
            }
            if (!txtDateTo.getText().isEmpty()) {
                dateTo = df.parse(txtDateTo.getText());
            }
            List<Invoice> invoices = db.getInvoices(txtCustomerName.getText(), dateFrom, dateTo);
            for (Invoice invoice : invoices) {
                modelInvoices.addRow(new Object[]{
                    invoice.getId(),
                    invoice.getCustomer().getId(), invoice.getCustomer().getName(), invoice.getCustomer().getAddress(),
                    invoice.getAmountBeforeTax(), invoice.getAmountTax(), invoice.getTotalAmount(), df.format(invoice.getTimestamp())
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: unable to reload invoice(s)\n" + ex.getMessage(), "database error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Error: converting date(should be yyyy-MM-dd):\n" + ex.getMessage(), "date error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadInvoiceOrderlines() {

        try {
            modelInvoiceOrderItems.setRowCount(0);

            int invoicesSelected = jtInvoices.getSelectedRows().length;
            if (0 == invoicesSelected || invoicesSelected > 1) {
                //System.out.println("0 row selected.");
                return;
            }
            Object invoiceId = jtInvoices.getModel().getValueAt(jtInvoices.getSelectedRow(), 0);
            List<SalesOrder> orders = db.getOrders(Integer.parseInt(invoiceId.toString()));
            for (int i = 0; i < orders.size(); i++) {
                List<OrderItem> items = orders.get(i).getItems();
                for (OrderItem item : items) {
                    modelInvoiceOrderItems.addRow(new Object[]{
                        orders.get(i).getId(),
                        item.getProduct().getDescription(), item.getProduct().getUnitPrice(),
                        item.getQuantity(), item.getItemTotal()
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: unable to reload order item(s)\n" + ex.getMessage(), "database error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadOrdersForIssue() {

        try {
            dlgIssueOrdersModel.setRowCount(0);

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            List<SalesOrder> orders = db.getOrders(dlgIssue_txtCustomerName.getText(), "notcomplete");
            for (SalesOrder order : orders) {
                dlgIssueOrdersModel.addRow(new Object[]{
                    order.getId(),
                    order.getCustomer().getName(), df.format(order.getTimestamp()),
                    order.getAmountBeforeTax(), order.getAmountTax(), order.getTotalAmount()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: unable to reload order(s)\n" + ex.getMessage(), "database error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadOrders() {

        try {
            dlgOrdersModel.setRowCount(0);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            List<SalesOrder> orders = db.getOrders();
            for (SalesOrder order : orders) {
                dlgOrdersModel.addRow(new Object[]{
                    order.getId(),
                    order.getCustomer().getName(),
                    df.format(order.getTimestamp()),
                    order.getTotalAmount(),});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: unable to reload order(s)\n" + ex.getMessage(), "database error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } //catch (ParseException ex) {
        // JOptionPane.showMessageDialog(null, "Error: converting date(should be yyyy-MM-dd):\n" + ex.getMessage(), "date error", JOptionPane.ERROR_MESSAGE);
        //  ex.printStackTrace();
        // }
    }

    private void loadOrdersWithSearch() {

        try {
            dlgOrdersModel.setRowCount(0);
            String customerName = "";
            String orderStatus = "";
            int orderId = 0;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            customerName = dlgOrders_txtCustomerName.getText();

            if (dlgOrders_cbStatus.getSelectedItem() != null && !dlgOrders_cbStatus.getSelectedItem().equals("---")) {
                orderStatus = dlgOrders_cbStatus.getSelectedItem().toString();
            }
            if (!dlgOrders_txtOrderId.getText().isEmpty()) {
                orderId = Integer.parseInt(dlgOrders_txtOrderId.getText());
            }

            List<SalesOrder> orders = db.getOrders(customerName, orderStatus, orderId);
            for (SalesOrder order : orders) {
                dlgOrdersModel.addRow(new Object[]{
                    order.getId(),
                    order.getCustomer().getName(),
                    df.format(order.getTimestamp()),
                    order.getTotalAmount(),});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: unable to reload order(s)\n" + ex.getMessage(), "database error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } //catch (ParseException ex) {
        // JOptionPane.showMessageDialog(null, "Error: converting date(should be yyyy-MM-dd):\n" + ex.getMessage(), "date error", JOptionPane.ERROR_MESSAGE);
        //  ex.printStackTrace();
        // }
    }

    private void loadOrderItems() {

        try {
            dlgOrdersItemsModel.setRowCount(0);

            int orderS = dlgOrders_jtOrders.getSelectedRows().length;
            System.out.println(orderS);
            if (0 == orderS || orderS > 1) {
                System.out.println("0 row selected.");
                return;
            }
            Object orderId = dlgOrders_jtOrders.getModel().getValueAt(dlgOrders_jtOrders.getSelectedRow(), 0);
            System.out.println(orderId);

            //List<SalesOrder> orders = db.getOrders(Integer.parseInt(orderId.toString()));
            //System.out.println (orders);
            //for(int i = 0; i < orders.size(); i++){
            List<OrderItem> items = db.getOrderItemsByOrderId(Integer.parseInt(orderId.toString()));
            //  List<OrderItem> items = db.getOrderItemsByOrderId(orders.get(i).getId());
            //System.out.println(orders.get(i).getId());
            System.out.println(items);
            for (OrderItem item : items) {
                dlgOrdersItemsModel.addRow(new Object[]{
                    //orders.get(i).getId(),
                    item.getProduct().getId(),
                    item.getProduct().getDescription(), item.getProduct().getUnitPrice(),
                    item.getQuantity(), item.getItemTotal()
                });
            }
            //}
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: unable to reload order item(s)\n" + ex.getMessage(), "database error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void exportCSV() { // to export selected invoice to csv using invoice+invoiceId as file name
        try {
            Object invoiceId = modelInvoices.getValueAt(jtInvoices.getSelectedRow(), 0);
            Invoice invoice = db.getInvoiceById(Integer.parseInt(invoiceId.toString()));
            File file = new File("export_invoices/csv/invoice_" +invoice.getCustomer().getName()+"_"+ invoiceId.toString() + ".csv");
            try (PrintWriter out = new PrintWriter(new FileWriter(file))) {

                for (SalesOrder order : invoice.getSalesOrder()) { //iterate through the SalesOrder list
                    out.printf("%d;%d;%s;%.1f;%.1f;%.1f;\n", invoice.getId(), invoice.getCustomer().getId(), invoice.getCustomer().getName(),
                            invoice.getAmountBeforeTax(), invoice.getAmountTax(), invoice.getTotalAmount());
                    out.printf("%d\n", order.getId());
                    for (OrderItem item : order.getItems()) {

                        out.printf("%s;%.2f;%.2f;%.2f\n", item.getProduct().getDescription(), item.getProduct().getUnitPrice(), item.getQuantity(), item.getItemTotal());
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(MainFrameInvoice.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error saving data to file:\n" + ex.getMessage(),
                    "File saving error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        dlgIssue = new javax.swing.JDialog();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        dlgIssue_txtCustomerName = new javax.swing.JTextField();
        dlgIssue_btnSearch = new javax.swing.JButton();
        dlgIssue_btnCancel = new javax.swing.JButton();
        dlgIssue_btnSave = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        dlgIssue_jtOrders = new javax.swing.JTable();
        dlgOrders = new javax.swing.JDialog();
        dlgOrders_txtCustomerName = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        dlgOrders_txtOrderId = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        dlgOrders_cbStatus = new javax.swing.JComboBox<>();
        dlgOrders_btnSearch = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        dlgOrders_jtOrders = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        dlgOrders_jtItems = new javax.swing.JTable();
        dlgOrderStatus = new javax.swing.JLabel();
        lbStatus = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtDateFrom = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtDateTo = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jtInvoices = new javax.swing.JTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        jtOrderitems = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        miExPDF = new javax.swing.JMenuItem();
        miExCSV = new javax.swing.JMenuItem();
        miExCSVEmail = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miExit = new javax.swing.JMenuItem();
        menuIssue = new javax.swing.JMenu();
        menuQueryOrdes = new javax.swing.JMenu();

        dlgIssue.setTitle("Issue Invoice");
        dlgIssue.setModal(true);
        dlgIssue.setResizable(false);
        dlgIssue.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                dlgIssueComponentShown(evt);
            }
        });

        jLabel7.setText("Orders");

        jLabel9.setText("Customer name");

        dlgIssue_btnSearch.setText("Search");
        dlgIssue_btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dlgIssue_btnSearchActionPerformed(evt);
            }
        });

        dlgIssue_btnCancel.setText("Cancel");
        dlgIssue_btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dlgIssue_btnCancelActionPerformed(evt);
            }
        });

        dlgIssue_btnSave.setText("Save");
        dlgIssue_btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dlgIssue_btnSaveActionPerformed(evt);
            }
        });

        dlgIssue_jtOrders.setModel(dlgIssueOrdersModel);
        dlgIssue_jtOrders.setRowHeight(20);
        jScrollPane1.setViewportView(dlgIssue_jtOrders);

        javax.swing.GroupLayout dlgIssueLayout = new javax.swing.GroupLayout(dlgIssue.getContentPane());
        dlgIssue.getContentPane().setLayout(dlgIssueLayout);
        dlgIssueLayout.setHorizontalGroup(
            dlgIssueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgIssueLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel7)
                .addGroup(dlgIssueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgIssueLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 610, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(30, Short.MAX_VALUE))
                    .addGroup(dlgIssueLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dlgIssue_txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(dlgIssue_btnSearch)
                        .addGap(38, 38, 38))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgIssueLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(dlgIssue_btnCancel)
                .addGap(33, 33, 33)
                .addComponent(dlgIssue_btnSave)
                .addGap(269, 269, 269))
        );
        dlgIssueLayout.setVerticalGroup(
            dlgIssueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgIssueLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(dlgIssueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(dlgIssue_txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dlgIssue_btnSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgIssueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, dlgIssueLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(dlgIssueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dlgIssue_btnSave)
                            .addComponent(dlgIssue_btnCancel))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dlgOrders.setTitle("Query Sales Orders");
        dlgOrders.setModal(true);
        dlgOrders.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                dlgOrdersComponentShown(evt);
            }
        });

        jLabel10.setText("Customer name");

        jLabel11.setText("Order Id");

        jLabel12.setText("Status");

        dlgOrders_cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---", "complete", "notcomplete" }));

        dlgOrders_btnSearch.setText("Search");
        dlgOrders_btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dlgOrders_btnSearchActionPerformed(evt);
            }
        });

        jLabel13.setText("Orders");

        jLabel14.setText("OrderItem(s)");

        dlgOrders_jtOrders.setModel(dlgOrdersModel);
        dlgOrders_jtOrders.setRowHeight(20);
        jScrollPane2.setViewportView(dlgOrders_jtOrders);

        dlgOrders_jtItems.setModel(dlgOrdersItemsModel);
        dlgOrders_jtItems.setRowHeight(20);
        jScrollPane3.setViewportView(dlgOrders_jtItems);

        dlgOrderStatus.setText("Status");

        javax.swing.GroupLayout dlgOrdersLayout = new javax.swing.GroupLayout(dlgOrders.getContentPane());
        dlgOrders.getContentPane().setLayout(dlgOrdersLayout);
        dlgOrdersLayout.setHorizontalGroup(
            dlgOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgOrdersLayout.createSequentialGroup()
                .addGroup(dlgOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgOrdersLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel13))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgOrdersLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel14)))
                .addGroup(dlgOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgOrdersLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dlgOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE)
                            .addComponent(jScrollPane3))
                        .addContainerGap(30, Short.MAX_VALUE))
                    .addGroup(dlgOrdersLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dlgOrders_txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dlgOrders_txtOrderId, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dlgOrders_cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dlgOrders_btnSearch)
                        .addGap(38, 38, 38))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgOrdersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dlgOrderStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        dlgOrdersLayout.setVerticalGroup(
            dlgOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgOrdersLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(dlgOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dlgOrders_txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(dlgOrders_txtOrderId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(dlgOrders_cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dlgOrders_btnSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dlgOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(dlgOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dlgOrderStatus)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Invoicing System");
        setResizable(false);

        lbStatus.setText("Status");
        lbStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lbStatus.setMaximumSize(new java.awt.Dimension(34, 18));
        lbStatus.setMinimumSize(new java.awt.Dimension(34, 18));
        lbStatus.setPreferredSize(new java.awt.Dimension(34, 18));
        getContentPane().add(lbStatus, java.awt.BorderLayout.PAGE_END);

        jLabel2.setText("Customer name");

        jLabel3.setText("Date(yyyy-MM-dd) from");

        jLabel4.setText("to");

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        jLabel5.setText("Invoices");

        jLabel6.setText("OrderItem(s)");

        jScrollPane8.setPreferredSize(new java.awt.Dimension(452, 222));

        jtInvoices.setModel(modelInvoices);
        jtInvoices.setRowHeight(20);
        jtInvoices.setRowMargin(2);
        jtInvoices.setSelectionBackground(new java.awt.Color(102, 102, 102));
        jtInvoices.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jScrollPane8.setViewportView(jtInvoices);

        jtOrderitems.setModel(modelInvoiceOrderItems);
        jtOrderitems.setRowHeight(20);
        jScrollPane9.setViewportView(jtOrderitems);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(btnSearch))
                    .addComponent(jScrollPane9)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 682, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jMenu1.setText("File");

        miExPDF.setText("Export selected invoice to PDF");
        miExPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExPDFActionPerformed(evt);
            }
        });
        jMenu1.add(miExPDF);

        miExCSV.setText("Export selected invoice(s) to CSV");
        miExCSV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExCSVActionPerformed(evt);
            }
        });
        jMenu1.add(miExCSV);

        miExCSVEmail.setText("Export selected invoice(s) to CSV&Email");
        miExCSVEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExCSVEmailActionPerformed(evt);
            }
        });
        jMenu1.add(miExCSVEmail);
        jMenu1.add(jSeparator1);

        miExit.setText("Exit");
        miExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExitActionPerformed(evt);
            }
        });
        jMenu1.add(miExit);

        jMenuBar1.add(jMenu1);

        menuIssue.setText("Issue Invoice");
        menuIssue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuIssueMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuIssue);

        menuQueryOrdes.setText("Sales Orders");
        menuQueryOrdes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuQueryOrdesMouseClicked(evt);
            }
        });
        jMenuBar1.add(menuQueryOrdes);

        setJMenuBar(jMenuBar1);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void dlgIssue_btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlgIssue_btnCancelActionPerformed

        dlgIssue.setVisible(false);
    }//GEN-LAST:event_dlgIssue_btnCancelActionPerformed

    private void dlgOrders_btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlgOrders_btnSearchActionPerformed

        loadOrdersWithSearch();
        dlgOrderStatus.setText(dlgOrders_jtOrders.getRowCount() + "" + " orders displayed");
    }//GEN-LAST:event_dlgOrders_btnSearchActionPerformed

    private void dlgIssue_btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlgIssue_btnSearchActionPerformed

        loadOrdersForIssue();
    }//GEN-LAST:event_dlgIssue_btnSearchActionPerformed

    private void dlgIssue_btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlgIssue_btnSaveActionPerformed

        if (0 == dlgIssue_jtOrders.getSelectedRowCount()) {
            JOptionPane.showMessageDialog(this, "Warning: please select order(s)\n", "Issure invoice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {

            int[] selectedRows = dlgIssue_jtOrders.getSelectedRows();
            List<SalesOrder> orders = new ArrayList<>();
            BigDecimal amountBeforeTax = BigDecimal.valueOf(0);
            BigDecimal amountTax = BigDecimal.valueOf(0);
            BigDecimal totalAmount = BigDecimal.valueOf(0);
            for (int i = 0; i < selectedRows.length; i++) {
                SalesOrder order = new SalesOrder();
                try {
                    Object orderId = dlgIssueOrdersModel.getValueAt(selectedRows[i], 0);
                    order = db.getOrderById(Integer.parseInt(orderId.toString()));
                    System.out.println("selected order id = " + orderId);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error executing SQL query:\n" + ex.getMessage(), "database error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                    return;
                }

                // calculate amount for invoice
                amountBeforeTax = amountBeforeTax.add(order.getAmountBeforeTax());
                amountTax = amountTax.add(order.getAmountTax());
                totalAmount = totalAmount.add(order.getTotalAmount());

                order.setStatus(OrderStatus.complete);
                orders.add(order);
            }

            Invoice invoice = new Invoice();
            invoice.setTimestamp(new Date(new java.util.Date().getTime()));
            invoice.setAmountBeforeTax(amountBeforeTax);
            invoice.setAmountTax(amountTax);
            invoice.setTotalAmount(totalAmount);
            invoice.setSalesOrder(orders);

            db.addInvoice(invoice);
            loadOrdersForIssue();
            loadInvoices();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error executing SQL query(insert):\n" + ex.getMessage(), "database error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_dlgIssue_btnSaveActionPerformed

    private void miExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExitActionPerformed

        System.exit(0);
    }//GEN-LAST:event_miExitActionPerformed

    private void menuQueryOrdesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuQueryOrdesMouseClicked

        //loadOrders();
        //loadOrderItems();
        dlgOrderStatus.setText(dlgOrders_jtOrders.getRowCount() + "" + " orders displayed");
        dlgOrders.pack();
        dlgOrders.setLocationRelativeTo(SwingUtilities.getWindowAncestor((Component) evt.getSource()));
        dlgOrders.setVisible(true);

    }//GEN-LAST:event_menuQueryOrdesMouseClicked

    private void menuIssueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuIssueMouseClicked

        System.out.println("issue menu click.");
        dlgIssue.pack();
        dlgIssue.setLocationRelativeTo(SwingUtilities.getWindowAncestor((Component) evt.getSource()));
        dlgIssue.setVisible(true);
    }//GEN-LAST:event_menuIssueMouseClicked

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed

        loadInvoices();
        loadInvoiceOrderlines();
        lbStatus.setText(jtInvoices.getRowCount() + "" + " invoices displayed");
    }//GEN-LAST:event_btnSearchActionPerformed

    private void dlgIssueComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_dlgIssueComponentShown

        loadOrdersForIssue();
        System.out.println("dlgIssue component shown.");
    }//GEN-LAST:event_dlgIssueComponentShown

    private void miExPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExPDFActionPerformed

        try {
            int invoicesSelected = jtInvoices.getSelectedRows().length;
            if (0 == invoicesSelected || invoicesSelected > 1) {
                JOptionPane.showMessageDialog(this, "Warning: please select order(ONLY one line)\n", "Export pdf for invoice", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Object invoiceId = modelInvoices.getValueAt(jtInvoices.getSelectedRow(), 0);
            Invoice invoice = db.getInvoiceById(Integer.parseInt(invoiceId.toString()));

            Utils.createInvoicePdf(invoice);
            
            JOptionPane.showMessageDialog(this, "Exported pdf file of the invoice successfully.", "Export pdf", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error executing SQL query:\n" + ex.getMessage(), "database error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error export pdf:\n" + ex.getMessage(), "export pdf error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_miExPDFActionPerformed

    private void dlgOrdersComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_dlgOrdersComponentShown

        loadOrders();
    }//GEN-LAST:event_dlgOrdersComponentShown

    private void miExCSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExCSVActionPerformed
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                String path = file.getAbsolutePath();
                if (!path.matches(".+\\.[A-Za-z0-9]{1,20}")) {
                    System.out.println("no extension match");
                    file = new File(path + ".csv");
                }
                try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
                    Object invoiceId = modelInvoices.getValueAt(jtInvoices.getSelectedRow(), 0);

                    Invoice invoice = db.getInvoiceById(Integer.parseInt(invoiceId.toString()));
                    for (SalesOrder order : invoice.getSalesOrder()) { //iterate through the SalesOrder list
                        out.printf("%d;%d;%s;%.1f;%.1f;%.1f;\n", invoice.getId(), invoice.getCustomer().getId(), invoice.getCustomer().getName(),
                                invoice.getAmountBeforeTax(), invoice.getAmountTax(), invoice.getTotalAmount());
                        out.printf("%d\n", order.getId());
                        for (OrderItem item : order.getItems()) {

                            out.printf("%s;%.2f;%.2f;%.2f\n", item.getProduct().getDescription(), item.getProduct().getUnitPrice(), item.getQuantity(), item.getItemTotal());
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(MainFrameInvoice.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error saving data to file:\n" + ex.getMessage(),
                        "File saving error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_miExCSVActionPerformed

    private void miExCSVEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExCSVEmailActionPerformed
        exportCSV();
        String email = "";
        String fileName = "";
        String filePath = new File("").getAbsolutePath() + "\\export_invoices\\csv\\";
        final String user = "zdfmontreal13@gmail.com";//change accordingly  
        final String password = "zoe20178";//change accordingly  

        try {
            Object invoiceId = modelInvoices.getValueAt(jtInvoices.getSelectedRow(), 0);
            Invoice invoice = db.getInvoiceById(Integer.parseInt(invoiceId.toString()));
            email = invoice.getCustomer().getEmail();
            fileName = "invoice_" +invoice.getCustomer().getName()+"_"+ invoiceId.toString() + ".csv";

            // File file = new File("C:\\Users\\ITC\\Documents\\project_sales_order_invoicing\\docs" +fileName+ ".csv");
        } catch (SQLException ex) {
            Logger.getLogger(MainFrameInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(email);
        MailWithAttachment.send(user, password, email, "invoice", "please check invoice",filePath + fileName, fileName);

    }//GEN-LAST:event_miExCSVEmailActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrameInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrameInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrameInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrameInvoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrameInvoice().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JDialog dlgIssue;
    private javax.swing.JButton dlgIssue_btnCancel;
    private javax.swing.JButton dlgIssue_btnSave;
    private javax.swing.JButton dlgIssue_btnSearch;
    private javax.swing.JTable dlgIssue_jtOrders;
    private javax.swing.JTextField dlgIssue_txtCustomerName;
    private javax.swing.JLabel dlgOrderStatus;
    private javax.swing.JDialog dlgOrders;
    private javax.swing.JButton dlgOrders_btnSearch;
    private javax.swing.JComboBox<String> dlgOrders_cbStatus;
    private javax.swing.JTable dlgOrders_jtItems;
    private javax.swing.JTable dlgOrders_jtOrders;
    private javax.swing.JTextField dlgOrders_txtCustomerName;
    private javax.swing.JTextField dlgOrders_txtOrderId;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JTable jtInvoices;
    private javax.swing.JTable jtOrderitems;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JMenu menuIssue;
    private javax.swing.JMenu menuQueryOrdes;
    private javax.swing.JMenuItem miExCSV;
    private javax.swing.JMenuItem miExCSVEmail;
    private javax.swing.JMenuItem miExPDF;
    private javax.swing.JMenuItem miExit;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtDateFrom;
    private javax.swing.JTextField txtDateTo;
    // End of variables declaration//GEN-END:variables
}
