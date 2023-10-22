package com.xorshop.admin.exporter;


import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.xorshop.common.entity.Customer;



public class CustomerExcelExporter extends AbstractExporter{

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	

	public CustomerExcelExporter() {
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Users");
		XSSFRow row = sheet.createRow(0);

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);

		createCell(row, 0, "Client Id", cellStyle);
		createCell(row, 1, "Prénom", cellStyle);
		createCell(row, 2, "Nom", cellStyle);
		createCell(row, 3, "E-mail", cellStyle);
		createCell(row, 4, "Ville", cellStyle);
		createCell(row, 5, "Province", cellStyle);
		createCell(row, 6, "Pays", cellStyle);
		createCell(row, 7, "Status", cellStyle);
	}

	private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {
		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);

		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}

		cell.setCellStyle(style);		
	}

	public void export(List<Customer> listCustomers, HttpServletResponse response) throws IOException {
		super.setRsponseHeader(response, "application/octet-stream", ".xlsx", "customers_");

		writeHeaderLine();
		writeDataLines(listCustomers);

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();


	}

	private void writeDataLines(List<Customer> listCustomers) {
		int rowIndex = 1;

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		cellStyle.setFont(font);

		for (Customer customer : listCustomers) {
			XSSFRow row = sheet.createRow(rowIndex++);
			int columnIndex = 0;

			createCell(row, columnIndex++, customer.getId(), cellStyle);
			createCell(row, columnIndex++, customer.getFirstName(), cellStyle);
			createCell(row, columnIndex++, customer.getLastName(), cellStyle);
			createCell(row, columnIndex++, customer.getEmail(), cellStyle);
			createCell(row, columnIndex++, customer.getCity(), cellStyle);
			createCell(row, columnIndex++, customer.getState(), cellStyle);
			createCell(row, columnIndex++, customer.getCountry().getName(), cellStyle);
			createCell(row, columnIndex++, customer.isEnabled(), cellStyle);
		}
	}
}

