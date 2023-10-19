package com.xorshop.admin.exporter;
import java.awt.Color;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.xorshop.common.entity.Customer;
public class CustomerPdfExporter extends AbstractExporter{

public void export(List<Customer> listCustomer, HttpServletResponse response) throws IOException {
		
		super.setRsponseHeader(response, "application/pdf", ".pdf", "customers_");

		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);

		Paragraph paragraph = new Paragraph("List of Customer", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(paragraph);

		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] {1.2f, 3.5f, 3.0f, 3.0f, 3.0f, 1.7f});

		writeTableHeader(table);
		writeTableData(table, listCustomer);

		document.add(table);

		document.close();
	}
	
	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);		

		cell.setPhrase(new Phrase("Client Id", font));		
		table.addCell(cell);

		cell.setPhrase(new Phrase("Prénom", font));		
		table.addCell(cell);

		cell.setPhrase(new Phrase("Nom", font));		
		table.addCell(cell);

		cell.setPhrase(new Phrase("E-mail", font));		
		table.addCell(cell);		

		cell.setPhrase(new Phrase("Ville", font));		
		table.addCell(cell);

		cell.setPhrase(new Phrase("Province", font));		
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Pays", font));		
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Status", font));		
		table.addCell(cell);
	}
	
	private void writeTableData(PdfPTable table, List<Customer> listCustomer) {
		for (Customer customer : listCustomer) {
			table.addCell(String.valueOf(customer.getId()));
			table.addCell(customer.getFirstName());
			table.addCell(customer.getLastName());
			table.addCell(customer.getEmail());
			table.addCell(customer.getCity());
			table.addCell(customer.getState());
			table.addCell(customer.getCountry().getName());
			table.addCell(String.valueOf(customer.isEnabled()));
		}
	}
}

