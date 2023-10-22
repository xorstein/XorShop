package com.xorshop.admin.exporter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.xorshop.admin.util.AbstractExporter;
import com.xorshop.common.entity.User;

public class UserExcelExporter extends AbstractExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	public UserExcelExporter() {
		workbook = new XSSFWorkbook();
	}

	private void writeHeader() {
		sheet = workbook.createSheet("users");
		XSSFRow row = sheet.createRow(0);
		XSSFCellStyle cellstyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();

		font.setBold(true);
		font.setFontHeight(16);
		cellstyle.setFont(font);
		createCell(row, 0, "#", cellstyle);
		createCell(row, 1, "Id", cellstyle);
		createCell(row, 2, "Nom d'utilisateur", cellstyle);
		createCell(row, 3, "Nom", cellstyle);
		createCell(row, 4, "Prénom", cellstyle);
		createCell(row, 5, "E-mail", cellstyle);
		createCell(row, 6, "Accés", cellstyle);
		createCell(row, 7, "Status", cellstyle);
		

	}

	public void createCell(XSSFRow row, int Columnindex, Object value, CellStyle style) {
		XSSFCell cell = row.createCell(Columnindex);
        sheet.autoSizeColumn(Columnindex);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/octet-stream", ".xlsx", "users_");

		writeHeader();
		writeDataLines(listUsers);

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();


	}

	private void writeDataLines(List<User> list) {
		// TODO Auto-generated method stub
		int rowindex=1;
		XSSFCellStyle cellstyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		cellstyle.setFont(font);
		int i=1;
		for(User user:list ){
			XSSFRow row=sheet.createRow(rowindex++);
			int columnindex=0;
			String Status = user.isEnabled() ? "Actif" : "Inactif";
			createCell(row, columnindex++, i, cellstyle);
			createCell(row, columnindex++, user.getId(), cellstyle);
			createCell(row, columnindex++, user.getUsername(), cellstyle);
			createCell(row, columnindex++, user.getLastname(), cellstyle);
			createCell(row, columnindex++, user.getFirstname(), cellstyle);
			createCell(row, columnindex++, user.getEmailid(), cellstyle);
			createCell(row, columnindex++, user.getRoles().toString(), cellstyle);
			createCell(row, columnindex++, Status, cellstyle);
			i++;
		}
		
	}

}
