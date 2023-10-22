package com.xorshop.admin.report;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xorshop.admin.dto.ReportItemDTO;
import com.xorshop.admin.order.OrderRepository;
import com.xorshop.admin.util.MasterOrderReportServiceUtil;
import com.xorshop.admin.util.ReportType;
import com.xorshop.common.entity.order.Order;



@Service
public class MasterOrderReportService extends AbstractReportService{

	private static final Logger LOGGER = LoggerFactory.getLogger(MasterOrderReportService.class);
	
	private final OrderRepository repo;
	
	public MasterOrderReportService(OrderRepository repo) {
		super();
		this.repo = repo;
	}

	@Override
	protected List<ReportItemDTO> getReportDataByDateRangeInternal(Date startTime, Date endTime,
			ReportType reportType) {
		
		LOGGER.info("MasterOrderReportService | getReportDataByDateRange is called");
		
		List<Order> listOrders = repo.findByOrderTimeBetween(startTime, endTime);
		MasterOrderReportServiceUtil.printRawData(listOrders);

		List<ReportItemDTO> listReportItems = MasterOrderReportServiceUtil.createReportData(startTime, endTime, dateFormatter, reportType);

		System.out.println();

		MasterOrderReportServiceUtil.calculateSalesForReportData(listOrders, listReportItems, dateFormatter);
		
		MasterOrderReportServiceUtil.printReportData(listReportItems);
		
		return listReportItems;
	}
	

}
