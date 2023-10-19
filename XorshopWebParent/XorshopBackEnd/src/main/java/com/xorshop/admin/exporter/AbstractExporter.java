package com.xorshop.admin.exporter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
public class AbstractExporter {
	
	public void setRsponseHeader( HttpServletResponse response,String content,String extention,String prefix) {
		// TODO Auto-generated method stub
		DateFormat dateformater=new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timestamp=dateformater.format(new Date());
		String filename=prefix+timestamp+extention;
		
		response.setContentType(content);
		
		String headerkey="Content-Disposition";
		String headervalue="attachement; fileName="+filename;
		response.setHeader(headerkey, headervalue);
		
	}

}
