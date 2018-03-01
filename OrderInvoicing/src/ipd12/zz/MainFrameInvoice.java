/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipd12.zz;

import ipd12.dao.Database;
import ipd12.entity.Invoice;
import ipd12.entity.OrderItem;
import ipd12.entity.SalesOrder;
import java.awt.Component;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    DefaultTableModel modelInvoiceOrderlines = new DefaultTableModel();
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
                   System.out.println(jtInvoices.getSelectedRow());
                }  
            }); 
            
            jtOrderitems.getSelectionModel().addListSelectionListener(new ListSelectionListener() {  

                public void valueChanged(ListSelectionEvent e) {  
                   System.out.println(jtOrderitems.getSelectedRow());
                   System.out.println(jtOrderitems.getModel().getValueAt(jtOrderitems.getSelectedRow(), 0));
                }  
            }); 
            
            loadTableTitle();
            loadInvoices();
            loadInvoiceOrderlines();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Fatal error: can not connect to database.", "database error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            System.exit(1);
        }
    }
    
    private void loadTableTitle(){
        modelInvoices.addColumn("Id");
        modelInvoices.addColumn("CustomerId");
        modelInvoices.addColumn("CustomerName");
        modelInvoices.addColumn("Address");
        modelInvoices.addColumn("AmountBeforeTax");
        modelInvoices.addColumn("AmountTax");
        modelInvoices.addColumn("TotalAmount");
        modelInvoices.addColumn("Date");
        
        modelInvoiceOrderlines.addColumn("OrderId");
        modelInvoiceOrderlines.addColumn("ProductDescription");
        modelInvoiceOrderlines.addColumn("UnitPrice");
        modelInvoiceOrderlines.addColumn("Quantity");
        modelInvoiceOrderlines.addColumn("ItemTotal");
        
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
    
    private void loadInvoices(){
       
        try {
            modelInvoices.setRowCount(0);            
//            txtDateFrom.getText()
//            txtDateTo.getText()
            List<Invoice> invoices = db.getInvoices(txtCustomerName.getText(), null, null);
            for(Invoice invoice : invoices){
                modelInvoices.addRow(new Object[]{invoice.getId(),invoice.getTimestamp()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: unable to reload invoice(s)\n" + ex.getMessage(), "database error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void loadInvoiceOrderlines(){

        try {
            modelInvoiceOrderlines.setRowCount(0);
            
            int invoicesSelected = jtInvoices.getSelectedRows().length;
            if(0 == invoicesSelected || invoicesSelected > 1){
                System.out.println("0 row selected.");
                return;
            }
            
            List<SalesOrder> orders = db.getOrders("", Integer.parseInt(jtInvoices.getModel().getValueAt(jtInvoices.getSelectedRow(), 0).toString()), "", 0);
//            for(int i = 0; i < orders.size(); i++){
//                List<OrderItem> items = db.getOrderItemsByOrderId(orders.get(0).getId());
//                for(OrderItem item : orders.get(i).getItems()){
//                    modelInvoiceOrderlines.addRow(new Object[]{item.getId(),item.getItemTotal()});
//                }
//            }
            Object[][] data = {
                {new Integer(3), "Smith",
                    new Integer(3),new Integer(5),new Integer(5)},
                {new Integer(3), "Doe",
                    new Integer(3),new Integer(5),new Integer(5)}
            };
            for(Object da[] : data){
                modelInvoiceOrderlines.addRow(da);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: unable to reload order item(s)\n" + ex.getMessage(), "database error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
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

        filechooser = new javax.swing.JFileChooser();
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
        jLabel1 = new javax.swing.JLabel();
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

        jLabel7.setText("Orders");

        jLabel9.setText("Customer");

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
                    .addGroup(dlgIssueLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(dlgIssueLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(dlgIssueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dlgIssue_btnSave)
                            .addComponent(dlgIssue_btnCancel))
                        .addGap(30, 30, Short.MAX_VALUE))))
        );

        dlgOrders.setTitle("Query Sales Orders");
        dlgOrders.setModal(true);

        jLabel10.setText("Customer");

        jLabel11.setText("Order Id");

        jLabel12.setText("Status");

        dlgOrders_cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "complete", "not complete" }));

        dlgOrders_btnSearch.setText("Search");
        dlgOrders_btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dlgOrders_btnSearchActionPerformed(evt);
            }
        });

        jLabel13.setText("Orders");

        jLabel14.setText("OrderItem(s)");

        dlgOrders_jtOrders.setModel(dlgOrdersModel);
        jScrollPane2.setViewportView(dlgOrders_jtOrders);

        dlgOrders_jtItems.setModel(dlgOrdersItemsModel);
        jScrollPane3.setViewportView(dlgOrders_jtItems);

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
                .addContainerGap(30, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Invoicing System");
        setResizable(false);

        jLabel1.setText("Status");
        jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel1.setMaximumSize(new java.awt.Dimension(34, 18));
        jLabel1.setMinimumSize(new java.awt.Dimension(34, 18));
        jLabel1.setPreferredSize(new java.awt.Dimension(34, 18));
        getContentPane().add(jLabel1, java.awt.BorderLayout.PAGE_END);

        jLabel2.setText("Customer");

        jLabel3.setText("Date from");

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

        jtOrderitems.setModel(modelInvoiceOrderlines);
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
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jMenu1.setText("File");

        miExPDF.setText("Export selected invoice to PDF");
        jMenu1.add(miExPDF);

        miExCSV.setText("Export selected invoice(s) to CSV");
        jMenu1.add(miExCSV);

        miExCSVEmail.setText("Export selected invoice(s) to CSV&Email");
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
        
        
    }//GEN-LAST:event_dlgOrders_btnSearchActionPerformed

    private void dlgIssue_btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlgIssue_btnSearchActionPerformed
        
        
    }//GEN-LAST:event_dlgIssue_btnSearchActionPerformed

    private void dlgIssue_btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlgIssue_btnSaveActionPerformed
        
    }//GEN-LAST:event_dlgIssue_btnSaveActionPerformed

    private void miExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExitActionPerformed
        
        System.exit(0);
    }//GEN-LAST:event_miExitActionPerformed

    private void menuQueryOrdesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuQueryOrdesMouseClicked
        
        dlgOrders.pack();
        dlgOrders.setLocationRelativeTo(SwingUtilities.getWindowAncestor((Component) evt.getSource()));
        dlgOrders.setVisible(true);
    }//GEN-LAST:event_menuQueryOrdesMouseClicked

    private void menuIssueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuIssueMouseClicked
        
        dlgIssue.pack();
        dlgIssue.setLocationRelativeTo(SwingUtilities.getWindowAncestor((Component) evt.getSource()));
        dlgIssue.setVisible(true);
    }//GEN-LAST:event_menuIssueMouseClicked

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        
        loadInvoices();
        loadInvoiceOrderlines();
    }//GEN-LAST:event_btnSearchActionPerformed

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
    private javax.swing.JDialog dlgOrders;
    private javax.swing.JButton dlgOrders_btnSearch;
    private javax.swing.JComboBox<String> dlgOrders_cbStatus;
    private javax.swing.JTable dlgOrders_jtItems;
    private javax.swing.JTable dlgOrders_jtOrders;
    private javax.swing.JTextField dlgOrders_txtCustomerName;
    private javax.swing.JTextField dlgOrders_txtOrderId;
    private javax.swing.JFileChooser filechooser;
    private javax.swing.JLabel jLabel1;
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
