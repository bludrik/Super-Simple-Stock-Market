package com.jpmorgan.stockmarket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * Stock is a representation of the StockInterface.
 * @author ludrik
 *
 */
public class Stock implements StockInterface{
	
	private String stockSymbol;
	
	private StockType type;
	
	private int lastDividend;

	private int fixDividend;
	
	private int parValue;
	
	private double stockPrice;
	
	private ArrayList<Trade> trades;
	
	
	public void setTrades(ArrayList<Trade> trades) {
		this.trades = trades;
	}

	public ArrayList<Trade> getTrades() {
		return trades;
	}

	public double getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(double stockPrice) {
		this.stockPrice = stockPrice;
	}
	
	public Stock(String stockSymbol, StockType type, int lastDividend, int fixDividend, int parValue,
			double stockPrice) {
		super();
		this.stockSymbol = stockSymbol;
		this.type = type;
		this.lastDividend = lastDividend;
		this.fixDividend = fixDividend;
		this.parValue = parValue;
		this.stockPrice = stockPrice;
		
		trades = new ArrayList<Trade>();
		
	}

	public Stock(String stockSymbol, StockType type, int lastDividend, int parValue, double stockPrice) {
		this(stockSymbol, type, lastDividend, 0, parValue, stockPrice);
			
	}

	@Override
	public double dividendYield( double price ) throws Exception{
		if( price <= 0 ) {
			throw new Exception("Illegal argument at method Stock.dividendYield()");
		}
		
		if( type.equals(StockType.COMMON)) {
			return lastDividend/price;
			
		} else {
			return fixDividend*parValue/price;
			
		}
		
	}

	@Override
	public double PERatio(double price) throws ArithmeticException{		
		//double dividend = dividendYield(price);
		if (lastDividend != 0) {
			return price/lastDividend;
		} else {
			throw new ArithmeticException("Illegal operation");
		}

	}

	@Override
	public void recordTrade(Indicator indicator, int quantity) {
		Date date = new Date();
		Timestamp time = new Timestamp(date.getTime());
		
		Trade trade = new Trade(time, quantity, indicator, stockPrice);
		trades.add(trade);
		
	}

	@Override
	public double VWeightendStockPrice() {
		
		int sumQuantity = 0;
		double sumPriceXQuantity = 0;
		
		Date date = new Date();
		Timestamp time = new Timestamp(date.getTime() - (15 * 60 * 1000));
		
		Trade currentTrade;
		
		for(int i=trades.size()-1;i>=0;i--) {
			currentTrade = trades.get(i);
			if ( currentTrade.getTime().compareTo(time) != -1 ) {
				// the condition is that we have to be within (current time - 15 minutes)
				sumPriceXQuantity += currentTrade.getQuantity()*currentTrade.getTradedPrice();
				sumQuantity += currentTrade.getQuantity();
				
			} else {
				break;
				
			}
		}
		
		return sumPriceXQuantity/sumQuantity;
	
	}
	
	@Override
	public String toString() {
		return this.stockSymbol + " " + this.type + " " + this.lastDividend + " " + this.fixDividend +
				" " + this.parValue + " " + this.stockPrice;
		
	}
	
	
}
