package com.suntech.selfServiceKiosk.transaction.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SelfServiceKioskDateTimeConstants {

	public int getNumberOfDayInYear(TransactionReturnParameters transactionReturnParameters) throws ParseException {

		String strDate = transactionReturnParameters.getBizDate();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = dateFormatter.parse(strDate);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int reportingPeriodId = cal.get(Calendar.DAY_OF_YEAR);

		return reportingPeriodId;
	}

	public String getFiscalYear(TransactionReturnParameters transactionReturnParameters) {
		String date = transactionReturnParameters.getBizDate();

		String year = "";

		if (date.length() > 4) {
			year = date.substring(0, 4);
		}
		return year;
	}

	public String timePeriodIntervalPerCount(TransactionReturnParameters transactionReturnParameters) {
		String transactionStrat = transactionReturnParameters.getTransStart();

		String timePeriodIntervalPerHourCount = "";

		if (transactionStrat.length() > 11) {
			timePeriodIntervalPerHourCount = transactionStrat.substring(11, 13);
		}
		return timePeriodIntervalPerHourCount;
	}
	
	public int returnLineItemSequence(TransactionReturnParameters transactionReturnParameters)
	{	
		return transactionReturnParameters.getReturnLineItems().size();
	}

}
