package com.suntech.selfServiceKiosk.transaction.utils;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@XmlRootElement
public class NewModel {
 
	private TransactionReturnParameters transactionReturnParameters;
}
