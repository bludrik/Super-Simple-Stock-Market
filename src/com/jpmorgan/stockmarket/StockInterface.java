package com.jpmorgan.stockmarket;

/**
 * StockInterface is an interface for Stock operations.
 * @author ludrik
 *
 */
public interface StockInterface {
	
	/**
	 * Calculates Dividend Yield on stock
	 * @param price
	 * @return dividend Yield of stock
	 * @throws Exception
	 */
	public abstract double dividendYield( double price) throws Exception;
	
	/**
	 * Calculates P/E Ratio on stock
	 * @param price
	 * @return P/E Ratio of stock
	 * @throws Exception
	 */
	public abstract double PERatio( double price) throws Exception;
	
	/**
	 * Record a trade on a stock.
	 * @param indicator
	 * @param quantity
	 */
	public abstract void recordTrade(Indicator indicator, int quantity);
	
	/**
	 * Calculates the Volume Weighted Stock Price based on the trades in past 15 minutes 
	 * @return Volume Weighted Stock Price
	 */
	public abstract double VWeightendStockPrice();
	
}
