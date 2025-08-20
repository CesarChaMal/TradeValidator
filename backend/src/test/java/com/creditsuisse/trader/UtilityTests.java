package com.creditsuisse.trader;

import com.creditsuisse.trader.util.Utility;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilityTests {

    @Test
    public void testValidDateComparison() {
        assertTrue("First date should be before second date", 
                Utility.checkBeforeDate("2016-08-11", "2016-08-15"));
    }

    @Test
    public void testInvalidDateComparison() {
        assertFalse("First date should not be before second date", 
                Utility.checkBeforeDate("2016-08-15", "2016-08-11"));
    }

    @Test
    public void testSameDateComparison() {
        assertFalse("Same dates should return false", 
                Utility.checkBeforeDate("2016-08-11", "2016-08-11"));
    }

    @Test
    public void testWeekendDetectionSaturday() {
        assertTrue("Saturday should be detected as weekend", 
                Utility.isDateFallinWeekend("2016-08-13")); // Saturday
    }

    @Test
    public void testWeekendDetectionSunday() {
        assertTrue("Sunday should be detected as weekend", 
                Utility.isDateFallinWeekend("2016-08-14")); // Sunday
    }

    @Test
    public void testWeekdayDetection() {
        assertFalse("Monday should not be detected as weekend", 
                Utility.isDateFallinWeekend("2016-08-15")); // Monday
    }

    @Test
    public void testValidCurrencyISO4217() {
        assertTrue("USD should be valid ISO 4217 currency", 
                Utility.isValidCurrencyISO4217("USD"));
        assertTrue("EUR should be valid ISO 4217 currency", 
                Utility.isValidCurrencyISO4217("EUR"));
        assertTrue("GBP should be valid ISO 4217 currency", 
                Utility.isValidCurrencyISO4217("GBP"));
    }

    @Test
    public void testInvalidCurrencyISO4217() {
        assertFalse("INVALID should not be valid ISO 4217 currency", 
                Utility.isValidCurrencyISO4217("INVALID"));
        assertFalse("Empty string should not be valid currency", 
                Utility.isValidCurrencyISO4217(""));
        assertFalse("Null should not be valid currency", 
                Utility.isValidCurrencyISO4217(null));
    }

    @Test
    public void testCurrencyPairValidation() {
        // Test individual currencies, not pairs
        assertTrue("EUR should be valid currency", 
                Utility.isValidCurrencyISO4217("EUR"));
        assertTrue("USD should be valid currency", 
                Utility.isValidCurrencyISO4217("USD"));
    }
}