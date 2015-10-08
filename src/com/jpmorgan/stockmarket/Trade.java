package com.jpmorgan.stockmarket;
import java.sql.Timestamp;

/**
 * Trade class for Stock.
 * @author ludrik
 *
 */
public class Trade {

	private Timestamp time;
	
	private int quantity;
	
	private Indicator indicator;	// buy or sell
	
	private double tradedPrice;

	public Trade(Timestamp time, int quantity, Indicator indicator, double tradedPrice) {
		super();
		this.time = time;
		this.quantity = quantity;
		this.indicator = indicator;
		this.tradedPrice = tradedPrice;
		
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Indicator getIndicator() {
		return indicator;
	}

	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

	public double getTradedPrice() {
		return tradedPrice;
	}

	public void setTradedPrice(double tradedPrice) {
		this.tradedPrice = tradedPrice;
	}
	
	
	
}
