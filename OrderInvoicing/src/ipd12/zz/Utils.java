
package ipd12.zz;



import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import static com.itextpdf.kernel.pdf.PdfName.Color;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;


import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;



import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.HorizontalAlignment;
import ipd12.entity.Invoice;
import ipd12.entity.OrderItem;
import ipd12.entity.SalesOrder;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author zhao
 */
public class Utils {

    public static final String LOGO = "img/logo.png";
    public static final String DEST = "invoice/";
        
    public static void createInvoicePdf(Invoice invoice) throws IOException {
        
        if(null == invoice || invoice.getId() <=0 ){
            return;
        }
        
        String fileName = "invoice_" + invoice.getCustomer().getName() + "_" + invoice.getId() + ".pdf";
        //Initialize PDF writer
        PdfWriter writer = new PdfWriter(DEST + fileName);
 
        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer); 
 
        // Initialize document
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(20, 20, 20, 20);
        
        //title
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        Image logo = new Image(ImageDataFactory.create(LOGO));
        Paragraph title = new Paragraph("INVOICE").setFont(font).add("                    ").add(logo);        
        document.add(title);
        
        Paragraph subtitle = new Paragraph(invoice.getCustomer().getName()).add("                                                   ").add(invoice.getTotalAmount().toString());
        document.add(subtitle);
        
        Paragraph invoiceNo = new Paragraph("Invoice Number       ").add(Integer.toString(invoice.getId()));
        document.add(invoiceNo);
        
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Paragraph issueDate = new Paragraph("Date Of Issue       ").add(df.format(invoice.getTimestamp()));
        document.add(issueDate);
        
        Table table = new Table(new float[]{10, 5, 5, 5});
        table.setWidthPercent(100);
        
        String line = "Description;Unit Price;Qty;Amount";
        process(table, line, bold, true);
        for(SalesOrder order : invoice.getSalesOrder()) {
            System.out.println(null == order ? "nullerer" : order.getId());
            for(OrderItem item : order.getItems()){
                line = String.format("%s;%.2f;%.2f;%.2f", item.getProduct().getDescription(),item.getProduct().getUnitPrice(),item.getQuantity(),item.getItemTotal());
                process(table, line, font, false);
            }
        }
        document.add(table);
        
        Paragraph subTotal = new Paragraph("Subtotal      ").add(String.format("%.2f", invoice.getAmountBeforeTax()));
        document.add(subTotal);
        
         Paragraph tax = new Paragraph("Tax      ").add(String.format("%.2f", invoice.getAmountTax()));
        document.add(tax);
        //Close document
        document.close();
    }
    
    public static void process(Table table, String line, PdfFont font, boolean isHeader) {
        StringTokenizer tokenizer = new StringTokenizer(line, ";");
        while (tokenizer.hasMoreTokens()) {
            if (isHeader) {
                table.addHeaderCell(new Cell().setHeight(25).add(new Paragraph(tokenizer.nextToken()).setFont(font)));
            } else {
                table.addCell(new Cell().setHeight(20).add(new Paragraph(tokenizer.nextToken()).setFont(font)));
            }
        }
    }
    
    
 
   
    
    
}
