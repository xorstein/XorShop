package com.xorshop.admin.exporter;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import com.xorshop.admin.util.AbstractExporter;
import com.xorshop.common.entity.User;

public class UserPDFExporter extends AbstractExporter{

	public void exporter(List<User> list, HttpServletResponse response) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		super.setResponseHeader(response, "application/pdf", ".pdf", "users_");
		Document document= new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		
		
		Font font=FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);
		Paragraph paragraph= new Paragraph("Liste des utilisateurs",font);
		paragraph.setAlignment(paragraph.ALIGN_CENTER);
		document.add(paragraph);		
		
		PdfPTable pdftable= new PdfPTable(8);
		pdftable.setWidthPercentage(100f);
		pdftable.setSpacingBefore(10);
		pdftable.setWidths(new float[] {1f,1f,2.8f,2.8f,2.8f,4.0f,3.5f,1.7f});
		writeTableHead(pdftable);
		writeTableData(list,pdftable);
		document.add(pdftable);
		document.close();
		
		
		
		
	}

	private void writeTableData(List<User> list, PdfPTable pdftable) {
		// TODO Auto-generated method stub
		int i=1;
		for (User user:list) {
			String Status = user.isEnabled() ? "Actif" : "Inactif";
			pdftable.addCell(String.valueOf(i));
			pdftable.addCell(String.valueOf(user.getId()));
			pdftable.addCell(user.getUsername());
			pdftable.addCell(user.getLastname());
			pdftable.addCell(user.getFirstname());
			pdftable.addCell(user.getEmailid());
			pdftable.addCell(user.getRoles().toString());
			pdftable.addCell(Status);
			i++;
			
		}
		
	}

	private void writeTableHead(PdfPTable pdftable) {
		// TODO Auto-generated method stub
		PdfPCell cell= new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		Font font=FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setColor(Color.WHITE);
		cell.setPhrase(new Phrase("#",font));
	    pdftable.addCell(cell);
		cell.setPhrase(new Phrase("Id",font));
	    pdftable.addCell(cell);
	    cell.setPhrase(new Phrase("Pseudo",font));
	    pdftable.addCell(cell);
	    cell.setPhrase(new Phrase("Nom",font));
	    pdftable.addCell(cell);
	    cell.setPhrase(new Phrase("Prénom",font));
	    pdftable.addCell(cell);
	    cell.setPhrase(new Phrase("E-mail",font));
	    pdftable.addCell(cell);
	    cell.setPhrase(new Phrase("Accés",font));
	    pdftable.addCell(cell);
	    cell.setPhrase(new Phrase("Status",font));
	    pdftable.addCell(cell);
		
	}

}
