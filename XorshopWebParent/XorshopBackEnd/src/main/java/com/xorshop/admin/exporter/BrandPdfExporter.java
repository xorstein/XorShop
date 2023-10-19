package com.xorshop.admin.exporter;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Set;

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
import com.xorshop.admin.util.AbstractExporter;
import com.xorshop.common.entity.Brand;
import com.xorshop.common.entity.Category;
public class BrandPdfExporter extends AbstractExporter{

	public void export(List<Brand> listBrand, HttpServletResponse response) throws IOException {
		
		super.setResponseHeader(response, "application/pdf", ".pdf", "marque_");

		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);

		Paragraph paragraph = new Paragraph("Liste des marques", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(paragraph);

		PdfPTable table = new PdfPTable(3);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] {1.2f, 3.5f, 3.0f});

		writeTableHeader(table);
		writeTableData(table, listBrand);

		document.add(table);

		document.close();
	}
	
	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);		

		cell.setPhrase(new Phrase("ID", font));		
		table.addCell(cell);

		cell.setPhrase(new Phrase("Nom de la marque", font));		
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Catégories de la marque", font));		
		table.addCell(cell);

	}
	
	private void writeTableData(PdfPTable table, List<Brand> listBrand) {
		for (Brand brand : listBrand) {
			table.addCell(String.valueOf(brand.getId()));
			table.addCell(brand.getName());
			String categories="";
			Set<Category> list=brand.getCategories();
			for (Category category : list) {
			    categories += category.getName() + ",";
			}

			// Remove the trailing comma if it exists
			if (categories.endsWith(",")) {
			    categories = categories.substring(0, categories.length() - 1);
			}
			table.addCell(categories);
		}
	}
}