import exceptions.InvalidAnalysisState;
import exceptions.InvalidStockSymbolException;
import exceptions.StockTickerConnectionError;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;


import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class StockQuoteAnalyzerTest {

    @Mock
    private StockQuoteGeneratorInterface generatorMock;
    @Mock
    private StockTickerAudioInterface audioMock;

    private StockQuoteAnalyzer analyzer;

    @BeforeMethod
    public void setUp() throws Exception {
        generatorMock = mock(StockQuoteGeneratorInterface.class);
        audioMock = mock(StockTickerAudioInterface.class);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        generatorMock = null;
        audioMock = null;
    }

    // invalid name of symbol
    @Test(expectedExceptions = InvalidStockSymbolException.class)
    public void constructorShouldThrowExceptionWhenSymbolIsInvalid() throws Exception {
        analyzer = new StockQuoteAnalyzer("ZZZZZZZZZ", generatorMock, audioMock);
    }

    // invalid stockQuoteSource
    @Test(expectedExceptions = NullPointerException.class)
    public void constructorShouldThrowExceptionWhenQuoteSourceIsNull() throws Exception {
        analyzer = new StockQuoteAnalyzer("GOOG", null, audioMock);
    }

    // when we cannot call
    @Test(expectedExceptions = StockTickerConnectionError.class)
    public void refreshShouldThrowStockTickerConnectionErrorWhenUnableToConnect() throws Exception {
        analyzer = new StockQuoteAnalyzer("GOOG", generatorMock, audioMock);
        doThrow(new StockTickerConnectionError("Unable to connect with Stock Ticker Source.")).when(generatorMock).getCurrentQuote();
        // when = arrange
        // then = assert
        // or verify
        analyzer.refresh();
    }

    // when we can call
    @Test
    public void refreshShouldThrowShouldSetValuesWhenConnected() throws Exception {
        analyzer = new StockQuoteAnalyzer("GOOG", generatorMock, audioMock);
    }

    @Test
    public void playAppropriateAudioShouldReturnHappyMusicWhenChangeIsGreaterThanZero() throws Exception {
        StockQuote quote = new StockQuote("GOOG", 5, 5, 5);
        when(generatorMock.getCurrentQuote()).thenReturn(quote);
        analyzer = new StockQuoteAnalyzer("GOOG", generatorMock, audioMock);
        analyzer.refresh();
        analyzer.playAppropriateAudio();
        verify(audioMock, times(1)).playHappyMusic();
    }

    @Test
    public void playAppropriateAudioShouldReturnSadMusicWhenChangeIsGreaterThanZero() throws Exception {
        StockQuote quote = new StockQuote("GOOG", 5, 5, -4);
        when(generatorMock.getCurrentQuote()).thenReturn(quote);
        analyzer = new StockQuoteAnalyzer("GOOG", generatorMock, audioMock);
        analyzer.refresh();
        analyzer.playAppropriateAudio();
        verify(audioMock, times(1)).playSadMusic();
    }

    @Test
    public void playAppropriateAudioShouldReturnErrorMusicWhenChangeIsGreaterThanZero() throws Exception {
        StockQuote quote = new StockQuote("GOOG", 5, 5, -4);
        when(generatorMock.getCurrentQuote()).thenReturn(quote);
        analyzer = new StockQuoteAnalyzer("GOOG", generatorMock, audioMock);
        analyzer.playAppropriateAudio();
        verify(audioMock, times(1)).playErrorMusic();
    }


    @Test
    public void testOne() throws Exception {
        StockQuote quote = new StockQuote("GOOG", 0, 0, 0);
        when(generatorMock.getSymbol()).thenReturn("GOOG");

        String sym = quote.getSymbol();
        assertEquals("GOOG", sym);

    }

    @Test
    public void testTwo() throws Exception {
        StockQuote quote = new StockQuote("AAPL", 0,0,0);
        when(generatorMock.getCurrentQuote()).thenReturn(quote);

        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);

    }

    @Test
    public void getSymbolShouldReturnValidSymbol() throws Exception {
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        String symbol = analyzer.getSymbol();
        assertEquals(symbol,"AAPL");
    }

    @Test
    public void getCurrentPriceShouldReturnCorrectValue() throws Exception {
        StockQuote quote = new StockQuote("AAPL",3,10,5);
        when(generatorMock.getCurrentQuote()).thenReturn(quote);
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        analyzer.refresh();
        double price = analyzer.getCurrentPrice();
        assertEquals(quote.getLastTrade(), price, 0.001);
    }

    @Test (expectedExceptions = InvalidAnalysisState.class)
    public void getCurrentPriceShouldThrowExceptionIfNoStockQuoteIsLoaded() throws Exception {
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        analyzer.getCurrentPrice();
    }

    @Test
    public void getPreviousCloseReturnsCorrectValue() throws Exception {
        StockQuote quote = new StockQuote("AAPL",3,10,5);
        when(generatorMock.getCurrentQuote()).thenReturn(quote);
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        analyzer.refresh();
        double price = analyzer.getPreviousClose();
        assertEquals(quote.getClose(), price, 0.001);
    }

    @Test (expectedExceptions = InvalidAnalysisState.class)
    public void getPreviousCloseeShouldThrowExceptionIfNoStockQuoteIsLoaded() throws Exception {
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        analyzer.getPreviousClose();
    }

    @Test
    public void getChangeSinceCloseReturnsCorrectValue() throws Exception {
        StockQuote quote = new StockQuote("AAPL",3,10,5);
        when(generatorMock.getCurrentQuote()).thenReturn(quote);
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        analyzer.refresh();
        double price = analyzer.getChangeSinceClose();
        assertEquals(quote.getChange() - quote.getClose(), price, 0.001);
    }

    @Test (expectedExceptions = InvalidAnalysisState.class)
    public void getChangeSinceCloseShouldThrowExceptionIfNoStockQuoteIsLoaded() throws Exception {
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        analyzer.getChangeSinceClose();
    }

    @Test
    public void getPercentChangeSinceCloseReturnsCorrectValue() throws Exception {
        StockQuote quote = new StockQuote("AAPL",3,10,5);
        when(generatorMock.getCurrentQuote()).thenReturn(quote);
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        analyzer.refresh();
        double price = analyzer.getPercentChangeSinceClose();
        assertEquals((quote.getChange() / quote.getClose()) * 100, price, 0.01);
    }

    @Test (expectedExceptions = InvalidAnalysisState.class)
    public void getPercentChangeSinceCloseShouldThrowExceptionIfNoStockQuoteIsLoaded() throws Exception {
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        analyzer.getPercentChangeSinceClose();
    }

    @Test
    public void getChangeSinceLastCheckShouldReturnCorrectValue() throws Exception {
        StockQuote quote = new StockQuote("AAPL",3,10,5);
        when(generatorMock.getCurrentQuote()).thenReturn(quote);
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        analyzer.refresh();
        StockQuote quote2 = new StockQuote("AAPL",4,11,6);
        when(generatorMock.getCurrentQuote()).thenReturn(quote2);
        analyzer.refresh();
        double price = analyzer.getChangeSinceLastCheck();
        assertEquals(1.0, price, 0.01);
    }

    @Test (expectedExceptions = InvalidAnalysisState.class)
    public void getChangeSinceLastCheckShouldThrowExceptionIfThereHaveNotBeenTwoChecks() throws Exception {
        analyzer = new StockQuoteAnalyzer("AAPL", generatorMock, audioMock);
        analyzer.getChangeSinceLastCheck();
    }
}