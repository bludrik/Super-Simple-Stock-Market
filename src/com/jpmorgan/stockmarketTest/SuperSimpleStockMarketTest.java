package com.jpmorgan.stockmarketTest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;
import com.jpmorgan.stockmarket.Indicator;
import com.jpmorgan.stockmarket.Market;
import com.jpmorgan.stockmarket.Stock;
import com.jpmorgan.stockmarket.StockType;
import com.jpmorgan.stockmarket.Trade;

import junit.framework.TestCase;

public class SuperSimpleStockMarketTest extends TestCase {
	
	private static final String STOCK_FILE = "StockTest.txt";
	
	private static final String TRADE_FILE = "Trades.txt";
	
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	
	private static final double DELTA = 1e-3;
	
	private ArrayList<DemoStock> demoStocks = null;
	
	private ArrayList<Trade> demoTrades = null;
	
	private double expectedVWeightedStockPrice;
	
	private double expectedGBCEAllShareIndex;
	
	private Date date = null;


	private class DemoStock {
		
		private Stock stock;
		
		private double testDividendYield;
		
		private double testPERatio;


		public DemoStock(Stock stock, double testDividendYield, double testPERatio) {
			
			this.stock = stock;
			this.testDividendYield = testDividendYield;
			this.testPERatio = testPERatio;
			
		}
		
		public Stock getStock() {
			return stock;
		}

		public double getTestDividendYield() {
			return testDividendYield;
		}

		public double getTestPERatio() {
			return testPERatio;
		}
		
	} // end of Inner Class DemoStock
	
	/**
	 * Test case to check GBCE All Share Index calculation for all stocks.
	 * 
	 */
	public void testGBCEAllShareIdx() {
		ArrayList<Stock> stocks = new ArrayList<Stock>();
		for(int i=0;i<demoStocks.size();i++) {
			stocks.add(demoStocks.get(i).getStock());
		}
		
		Market market = new Market(stocks);
		assertEquals(expectedGBCEAllShareIndex, market.GBCEAllShareIdx(), DELTA);
		
	}
	
	/**
	 * Test case to check Volume Weighted stock Price method of Stock Object.
	 * 
	 */
	public void testVolumeWeightedStockPrice() {
		Stock stock = new Stock("GIN", StockType.COMMON, 2, 100, 37.5 );
		
		//I change the time of the first trade. I want that it won't be in the last 15 minutes interval
		demoTrades.get(0).setTime(new Timestamp(date.getTime()-16*60*1000));
		stock.setTrades(demoTrades);
		assertEquals(expectedVWeightedStockPrice, stock.VWeightendStockPrice(), DELTA);
		
	}
	
	/**
	 * Test case to check recordTrade method of Stock Object.
	 * 
	 */
	public void testRecordTrade() {
		Stock stock = new Stock("GIN", StockType.COMMON, 2, 100, 37.5 );
		
		ArrayList<Trade> tradesExpList = new ArrayList<>();
		assertEquals(tradesExpList, stock.getTrades());
		
		//Date date = new Date();
		
		tradesExpList.add(new Trade(new Timestamp(date.getTime()), 100, Indicator.BUY, 37.5));
		stock.recordTrade(Indicator.BUY, 100);
		
		tradesExpList.add(new Trade(new Timestamp(date.getTime()), 50, Indicator.SELL, 37.5));
		stock.recordTrade(Indicator.SELL, 50);
		
		assertEquals(tradesExpList.size(), stock.getTrades().size());
		
		{
			Trade tradeActual = stock.getTrades().get(0);
			Trade tradeExp = tradesExpList.get(0);
			assertEquals(tradeExp.getQuantity(), tradeActual.getQuantity());
			assertEquals(tradeExp.getTradedPrice(), tradeActual.getTradedPrice());
			assertEquals(tradeExp.getIndicator(), tradeActual.getIndicator());
			assertTrue( ( tradeActual.getTime().getTime() - tradeExp.getTime().getTime() ) < 1000);
		}
		
		{
			Trade tradeActual = stock.getTrades().get(1);
			Trade tradeExp = tradesExpList.get(1);
			assertEquals(tradeExp.getQuantity(), tradeActual.getQuantity());
			assertEquals(tradeExp.getTradedPrice(), tradeActual.getTradedPrice());
			assertEquals(tradeExp.getIndicator(), tradeActual.getIndicator());
			assertTrue( ( tradeActual.getTime().getTime() - tradeExp.getTime().getTime() ) < 1000 );
		}
		
	}
	
	/**
	 * Test case to check Dividend Yield calculation of Stock Object.
	 * It uses an ArrayList of DemoStocks to assert the result of Stock method dividendYield() and PERatio()
	 * 
	 */
	public void testDividendYield() {
		
		try {
			
			for(int i=1; i<demoStocks.size();i++) {
				DemoStock dStock = demoStocks.get(i);
				Stock actStock = dStock.getStock();
				
				assertEquals(dStock.getTestDividendYield(), actStock.dividendYield(actStock.getStockPrice()), DELTA);

			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
			
		} catch( Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * Test case to check P/E Ratio calculation of Stock Object.
	 * It uses an ArrayList of DemoStocks to assert the result of Stock method dividendYield() and PERatio()
	 * 
	 */
	public void testPERatio() {
		
		try {
			
			for(int i=1; i<demoStocks.size();i++) {
				DemoStock dStock = demoStocks.get(i);
				Stock actStock = dStock.getStock();
				
				assertEquals(dStock.getTestPERatio(), actStock.PERatio(actStock.getStockPrice()), DELTA);
							
			}
				
		} catch( Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * Setup method to create the test demoLists and generate the Date Object.
	 * 
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		date = new Date();
		demoStocks = createTestStockList();
		demoTrades = createTestTradeList();

		
	}
	
	/**
	 * It creates an Arraylist of DemoStock objects. It parses a file to populate the ArrayList.
	 * @return	ArrayList<DemoStock>
	 */
	private ArrayList<DemoStock> createTestStockList() throws IOException{
		ArrayList<DemoStock> dStockList = new ArrayList<DemoStock>();
		
		Path iFilePath = Paths.get(STOCK_FILE);
		Scanner scanner = null;
		try {
			
			scanner = new Scanner(iFilePath, ENCODING.name());
			while (scanner.hasNextLine()){
				
				String line = scanner.nextLine();
//		        System.out.println(line);
		        
				if( line.contains("#") ) {
					if( line.contains("geometricsMean")) {
						String[] expectedValue = line.split(":");
						expectedGBCEAllShareIndex = Double.parseDouble(expectedValue[1]);
					
					}
					continue;
		        } else {
		        	DemoStock dStock = splitTheLineStocks(line);
			        dStockList.add(dStock);
			        
		        }    
			}
			
		} finally {
			scanner.close();
		}
		
		
		return dStockList;
		
	}

	/**
	 * It creates an Arraylist of Trade objects. It parses a file to populate the ArrayList.
	 * @return	ArrayList<Trade>
	 */
	private ArrayList<Trade> createTestTradeList() throws IOException{
		ArrayList<Trade> dTradeList = new ArrayList<Trade>();
		
		Path iFilePath = Paths.get(TRADE_FILE);
		Scanner scanner = null;
		try {
			
			scanner = new Scanner(iFilePath, ENCODING.name());
			while (scanner.hasNextLine()){
				
				String line = scanner.nextLine();
		        
				if( line.contains("#") ) {
					if( line.contains("volumeWeightedStockPrice")) {
						String[] expectedValue = line.split(":");
						expectedVWeightedStockPrice = Double.parseDouble(expectedValue[1]);
					
					}
		        	continue;
		        
				} else {
		        	Trade dTrade = splitTheLineTrades(line);
			        dTradeList.add(dTrade);
			        
		        }    
			}
			
		} finally {
			if(scanner != null)
				scanner.close();
		}

		return dTradeList;
		
	}

	
	
	/**
	 * This method splits up a line. It creates a demoStock Object from the information.
	 * DemoStock contains a Stock Object and the expected values of Dividend Yield and P/E Ratio.
	 * @param line	it is the String which is needed to split
	 * @return	DemoStock Object
	 */
	private DemoStock splitTheLineStocks(String line) {
		DemoStock dStock = null;
		
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter("\t\t");
		
		if (scanner.hasNext()){
			
		      String stockSymbol = scanner.next();
		      StockType type = (scanner.next().equals("COMMON")) ? StockType.COMMON : StockType.PREFERRED;
		      int lastDividend = Integer.parseInt(scanner.next());
		      int fixDividend = Integer.parseInt(scanner.next());
		      int parValue = Integer.parseInt(scanner.next());
		      double price = Double.parseDouble(scanner.next());
		      double dividendYield = Double.parseDouble(scanner.next());
		      double peRatio = Double.parseDouble(scanner.next());
		      
		      Stock stock = new Stock(stockSymbol, type, lastDividend, fixDividend, parValue, price);
		      dStock = new DemoStock(stock, dividendYield, peRatio);
		
		}
		
		scanner.close();
		
		return dStock;
	}
	
	/**
	 * This method splits up a line. It creates a Trade Object from the information.
	 * @param line	it is the String which is needed to split
	 * @return	Trade Object
	 */
	private Trade splitTheLineTrades(String line) {
		Trade trade = null;
		
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter("\t\t");
		
		//Date date = new Date();
		if (scanner.hasNext()){
			
		      int quantity = Integer.parseInt(scanner.next());
		      double price = Double.parseDouble(scanner.next());
		      Indicator indicator = (scanner.next().equals("SELL")) ? Indicator.SELL : Indicator.BUY;
		      
		      
		      trade = new Trade(new Timestamp(date.getTime()),quantity, indicator, price);
		
		}
		
		scanner.close();
		
		return trade;
	}
	
}
