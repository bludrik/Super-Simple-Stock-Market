package com.jpmorgan.stockmarket;
import java.util.ArrayList;

public class Market {

	private ArrayList<Stock> stocks;
	
	
	public ArrayList<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(ArrayList<Stock> stocks) {
		this.stocks = stocks;
	}

	public Market() {
		stocks = new ArrayList<Stock>();
		
	}
	
	public Market(ArrayList<Stock> list) {
		stocks = list;
		
	}
	
	/**
	 * Calculates the GBCE All Share Index, using the geometric mean of all stocks
	 * @return	GBCE All Share Index
	 */
	public double GBCEAllShareIdx() {
		double geometricMean;
		double sumOfLogarithms = 0;
		for(int i=0;i<stocks.size();i++) {
			sumOfLogarithms += Math.log(stocks.get(i).getStockPrice());
			
		}
		
		geometricMean = Math.exp(sumOfLogarithms/stocks.size()); 
		
		return geometricMean;
		
	}

}
