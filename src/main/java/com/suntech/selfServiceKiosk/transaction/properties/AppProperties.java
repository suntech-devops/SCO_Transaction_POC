package com.suntech.selfServiceKiosk.transaction.properties;

import org.springframework.beans.factory.annotation.Value;

import lombok.Data;

@Data
public class AppProperties {

	
	@Value("${transaction.queue.path}")
	private String supplierCompanyId;
}
