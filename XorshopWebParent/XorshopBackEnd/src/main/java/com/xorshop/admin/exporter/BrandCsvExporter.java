package com.xorshop.admin.exporter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.xorshop.common.entity.Brand;
import com.xorshop.common.entity.Category;

import jakarta.servlet.http.HttpServletResponse;

public class BrandCsvExporter extends AbstractExporter {

	public void export(List<Brand> listBrands, HttpServletResponse response) 
			throws IOException {
		
		super.setRsponseHeader(response, "text/csv", ".csv", "marque_");

		Writer writer = new OutputStreamWriter(response.getOutputStream(), "utf-8");
		writer.write('\uFEFF');
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(writer, 
				CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = {"Id", "Nom de la marque", "Catégories de la marque"};
		String[] fieldMapping = {"id", "name", "logo"};

		csvWriter.writeHeader(csvHeader);

		for (Brand brand : listBrands) {
		    // Convert the Set<Category> to a comma-separated string
		    String categoriesString = brand.getCategories()
		        .stream()
		        .map(Category::getName)
		        .collect(Collectors.joining(", "));

		    // Set the categories string in the Brand object
		    brand.setLogo(categoriesString);

		    // Write the Brand object to the CSV
		    csvWriter.write(brand, fieldMapping);
		}


		csvWriter.close();
		
		
	}
	
	
}