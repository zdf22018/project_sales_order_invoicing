
package ipd12.zz;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import ipd12.entity.Invoice;
import ipd12.entity.OrderItem;
import ipd12.entity.SalesOrder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import java.io.IOException;

/**
 *
 * @author zhao
 */
public class Utils {

    public static final String LOGO = "img/logo.png";
    public static final String DEST = "invoice/";
    static PdfFont timesNewRoman = null;
    static PdfFont timesNewRomanBold = null;
        
    public static void createInvoicePdf(Invoice invoice) throws IOException {
        
        if(null == invoice || invoice.getId() <=0 ){
            return;
        }
        timesNewRoman = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        timesNewRomanBold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
        
        String fileName = "invoice_" + invoice.getCustomer().getName() + "_" + invoice.getId() + ".pdf";
        //Initialize PDF writer
        PdfWriter writer = new PdfWriter(DEST + fileName);
 
        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer); 
 
        // Initialize document
        PageSize ps = PageSize.A5;
        Document document = new Document(pdf, ps);
        //document.setMargins(20, 20, 20, 20);        
        
        //Set column parameters
        float offSet = 20;
        float columnWidth = (ps.getWidth() - offSet * 2 + 10) / 3;
        float columnHeight = 120;//ps.getHeight() - offSet * 2;

        //Define column areas
        Rectangle[] columns = {new Rectangle(offSet - 5, offSet+450, columnWidth, columnHeight),
                new Rectangle(offSet + columnWidth, offSet+450, columnWidth, columnHeight),
                new Rectangle(offSet + columnWidth * 2 + 5, offSet+450, columnWidth, columnHeight)};
        //document.setRenderer(new ColumnDocumentRenderer(document, columns));
        float y = offSet+500;
        columnHeight = 40;
        
        Rectangle rect10 = new Rectangle(offSet - 5, y, ps.getWidth()+50, columnHeight);        
        Paragraph p10 = new Paragraph()
                .setFont(timesNewRoman)
                .setFontSize(22)
                //.setBackgroundColor(com.itextpdf.kernel.color.Color.LIGHT_GRAY)
                .setMarginLeft(20)
                .setMarginTop(20)
                .add("INVOICE");                 
        p10.setNextRenderer(new ParagraphRenderer(p10) {
        @Override
        public List<Rectangle> initElementAreas(LayoutArea area) {
            List<Rectangle> list = new ArrayList<Rectangle>();
            list.add(rect10);
            return list;
        }
        });        
        document.add(p10); 
        
        columnHeight = 30;
        y =offSet+470;
        Rectangle rect11 = new Rectangle(offSet - 5, y, columnWidth, columnHeight);        
        Paragraph p11 = new Paragraph()
                .setFont(timesNewRoman)
                .setFontSize(10)
                .add("Billed to");
        
        p11.setNextRenderer(new ParagraphRenderer(p11) {
        @Override
        public List<Rectangle> initElementAreas(LayoutArea area) {
            List<Rectangle> list = new ArrayList<Rectangle>();
            list.add(rect11);
            return list;
        }
        });        
        document.add(p11);        
       
        Rectangle rect21 = new Rectangle(offSet + columnWidth, y, columnWidth, columnHeight);
        Paragraph p21 = new Paragraph()
                .setFont(timesNewRoman)
                .setFontSize(10)
                .add("Invoice Number");
        p21.setNextRenderer(new ParagraphRenderer(p21) {
        @Override
        public List<Rectangle> initElementAreas(LayoutArea area) {
            List<Rectangle> list = new ArrayList<Rectangle>();
            list.add(rect21);
            return list;
        }
        });        
        document.add(p21);
        
        Rectangle rect31 = new Rectangle(offSet + columnWidth * 2 + 5, y, columnWidth, columnHeight);
        Paragraph p31 = new Paragraph()
                .setFont(timesNewRoman)
                .setFontSize(10)
                .add("Invoice Total");
        p31.setNextRenderer(new ParagraphRenderer(p31) {
        @Override
        public List<Rectangle> initElementAreas(LayoutArea area) {
            List<Rectangle> list = new ArrayList<Rectangle>();
            list.add(rect31);
            return list;
        }
        });   
        document.add(p31);
        
        columnHeight = 30;
        y =offSet+450;
        Rectangle rect12 = new Rectangle(offSet - 5, y, columnWidth, columnHeight);        
        Paragraph p12 = new Paragraph()
                .setFont(timesNewRoman)
                .setFontSize(14)
                .add(invoice.getCustomer().getAddress());        
        p12.setNextRenderer(new ParagraphRenderer(p12) {
        @Override
        public List<Rectangle> initElementAreas(LayoutArea area) {
            List<Rectangle> list = new ArrayList<Rectangle>();
            list.add(rect12);
            return list;
        }
        });        
        document.add(p12);        
       
        Rectangle rect22 = new Rectangle(offSet + columnWidth, y, columnWidth, columnHeight);
        Paragraph p22 = new Paragraph()
                .setFont(timesNewRoman)
                .setFontSize(14)
                .add(Integer.toString(invoice.getId()));       
        p22.setNextRenderer(new ParagraphRenderer(p22) {
        @Override
        public List<Rectangle> initElementAreas(LayoutArea area) {
            List<Rectangle> list = new ArrayList<Rectangle>();
            list.add(rect22);
            return list;
        }
        });        
        document.add(p22);
        
        Rectangle rect32 = new Rectangle(offSet + columnWidth * 2 + 5, y, columnWidth, columnHeight);
        Paragraph p32 = new Paragraph()
                .setFont(timesNewRoman)
                .setFontSize(14)
                .add("$" + invoice.getTotalAmount().toString());
        p32.setNextRenderer(new ParagraphRenderer(p32) {
        @Override
        public List<Rectangle> initElementAreas(LayoutArea area) {
            List<Rectangle> list = new ArrayList<Rectangle>();
            list.add(rect32);
            return list;
        }
        });   
        document.add(p32);      
        
        y = offSet+410;
        columnHeight = 20;
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Rectangle rect50 = new Rectangle(offSet - 5, y, ps.getWidth(), columnHeight);    
        Paragraph p50 = new Paragraph()
                .setFont(timesNewRoman)
                .setFontSize(14)
                .add("Date Of Issue       ").add(df.format(invoice.getTimestamp()));                 
        p50.setNextRenderer(new ParagraphRenderer(p50) {
        @Override
        public List<Rectangle> initElementAreas(LayoutArea area) {
            List<Rectangle> list = new ArrayList<Rectangle>();
            list.add(rect50);
            return list;
        }
        });        
        document.add(p50); 
        
        // detail info
        y = offSet+400;
        Table table = new Table(new float[]{10, 5, 5, 5});
        table.setWidthPercent(100);
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        String line = "Description;Unit Price;Qty;Amount";
        process(table, line, bold, true);
        float lineCount = 0;
        for(SalesOrder order : invoice.getSalesOrder()) {
            lineCount += order.getItems().size();
            for(OrderItem item : order.getItems()){
                line = String.format("%s;%.2f;%.2f;%.2f", item.getProduct().getDescription(),item.getProduct().getUnitPrice(),item.getQuantity(),item.getItemTotal());
                process(table, line, font, false);
            }
        }
        table.addCell(new Cell(1,2).setHeight(20).setTextAlignment(TextAlignment.RIGHT).add(new Paragraph("Subtotal:").setFont(font)));
        table.addCell(new Cell(1,2).setHeight(20).setTextAlignment(TextAlignment.RIGHT).add(new Paragraph(String.format("$%.2f", invoice.getAmountBeforeTax())).setFont(font)));
        table.addCell(new Cell(1,2).setHeight(20).setTextAlignment(TextAlignment.RIGHT).add(new Paragraph("Tax:").setFont(font)));
        table.addCell(new Cell(1,2).setHeight(20).setTextAlignment(TextAlignment.RIGHT).add(new Paragraph(String.format("$%.2f", invoice.getAmountBeforeTax())).setFont(font)));
        
        float tableH = (lineCount + 3) * 25; // 25: line height
        //System.out.println(tableH);
        table.setFixedPosition(offSet, y - tableH , ps.getWidth() - 2 * offSet);
        document.add(table);
        
        //Close document
        document.close();
    }
    
    public static void addArticle(
        Document doc, String title, String author,  String text)//Image img,
        throws IOException {
        Paragraph p1 = new Paragraph(title)
                .setFont(timesNewRomanBold)
                .setFontSize(14);
        doc.add(p1);
        //doc.add(img);
        Paragraph p2 = new Paragraph()
                .setFont(timesNewRoman)
                .setFontSize(7)
                //.setFontColor(Color..GRAY)
                .add(author);
        doc.add(p2);
        Paragraph p3 = new Paragraph()
                .setFont(timesNewRoman)
                .setFontSize(10)
                .add(text);
        doc.add(p3);
    }
    
    public static void process(Table table, String line, PdfFont font, boolean isHeader) {
        StringTokenizer tokenizer = new StringTokenizer(line, ";");
        while (tokenizer.hasMoreTokens()) {
            if (isHeader) {
                table.addHeaderCell(new Cell().setHeight(20).add(new Paragraph(tokenizer.nextToken()).setFont(font)));
            } else {
                table.addCell(new Cell().setHeight(20).add(new Paragraph(tokenizer.nextToken()).setFont(font)));
            }
        }
    }
}
