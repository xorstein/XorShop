package com.xorshop.admin.exporter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.Iterator;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.xorshop.admin.util.AbstractExporter;
import com.xorshop.common.entity.Brand;
import com.xorshop.common.entity.Category;
public class BrandExcelExporter extends AbstractExporter{

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	

	public BrandExcelExporter() {
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Brand");
		XSSFRow row = sheet.createRow(0);

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);

		createCell(row, 0, "Id", cellStyle);
		createCell(row, 1, "Nom de la marque", cellStyle);
		createCell(row, 2, "Catégories de la marque", cellStyle);

	}

	private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {
		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);

		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof String){
			cell.setCellValue((String) value);
		} if (value instanceof Set){
			String text = "";
			Iterator itr = ((Set) value).iterator();
		    while (itr.hasNext()) {
		        text += " " + itr.next();
		    }
		    
		    cell.setCellValue(text);
		}

		cell.setCellStyle(style);		
	}

	public void export(List<Brand> listBrands, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/octet-stream", ".xlsx", "marque_");

		writeHeaderLine();
		writeDataLines(listBrands);

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();


	}

	private void writeDataLines(List<Brand> listBrands) {
		int rowIndex = 1;

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		cellStyle.setFont(font);

		for (Brand brand : listBrands) {
			XSSFRow row = sheet.createRow(rowIndex++);
			int columnIndex = 0;

			createCell(row, columnIndex++, brand.getId(), cellStyle);
			createCell(row, columnIndex++, brand.getName(), cellStyle);
			String categories="";
			Set<Category> list=brand.getCategories();
			for (Category category : list) {
			    categories += " Id: "+category.getId()+" - Nom: "+category.getName() + ",";
			}

			// Remove the trailing comma if it exists
			if (categories.endsWith(",")) {
			    categories = categories.substring(0, categories.length() - 1);
			}
			createCell(row, columnIndex++, categories, cellStyle);
		}
	}
}
