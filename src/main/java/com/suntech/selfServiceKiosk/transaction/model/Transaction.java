package com.suntech.selfServiceKiosk.transaction.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TR_TRN")
public class Transaction 
{
	@Id
	@Column(name = "AI_TRN")
	private int transNo;
}
