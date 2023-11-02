package com.suntech.selfServiceKiosk.transaction.utils;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component

public class SelfServiceKioskTaxLineItmBrkpRequest {
	
	@JsonProperty("TX_BRKUP_TX_CD")
	private String TX_BRKUP_TX_CD;
	
	@JsonProperty("TX_BRKUP_TX_CD_DSCR")
	private String TX_BRKUP_TX_CD_DSCR;
	
	@JsonProperty("TX_BRKUP_TX_RT")
	private double TX_BRKUP_TX_RT;
	
	@JsonProperty("TX_BRKUP_TXBL_AMT")
	private double TX_BRKUP_TXBL_AMT;
	
	@JsonProperty("TX_BRKUP_TX_AMT")
	private double TX_BRKUP_TX_AMT;
	
	@JsonProperty("TX_FCT")
	private BigDecimal TX_FCT;
	
	@JsonProperty("TXBL_FCT")
	private BigDecimal TXBL_FCT;
	
	
	private Date lstRcrdModfTimestamp;
	
	
	private Date crtRcrdModfTimestamp;
	

}
