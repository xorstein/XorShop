package com.xorshop.admin.exporter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.xorshop.common.entity.Customer;



public class CustomerCsvExporter extends AbstractExporter{

public void export(List<Customer> listCustomer, HttpServletResponse response) throws IOException {
		
		super.setRsponseHeader(response, "text/csv", ".csv", "customers_");
		
		Writer writer = new OutputStreamWriter(response.getOutputStream(), "utf-8");
		writer.write('\uFEFF');
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(writer, 
				CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = {"Client ID", "Prénom", "Nom", "E-mail",  "Ville", "Province", "Pays" , "Status"};
		String[] fieldMapping = {"id", "firstName", "lastName", "email",  "city", "state" , "country" , "enabled"};

		csvWriter.writeHeader(csvHeader);

		for (Customer customer : listCustomer) {
			csvWriter.write(customer, fieldMapping);
		}

		csvWriter.close();
	}
}

