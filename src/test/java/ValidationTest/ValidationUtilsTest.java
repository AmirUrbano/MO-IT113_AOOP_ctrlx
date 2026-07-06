package ValidationTest;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import com.mycompany.motorphpayroll_OOP.ValidationUtils;
/**
 *
 * @author Amir
 */
public class ValidationUtilsTest {

    @Test
    @DisplayName("Validation: Verify Employee ID Length and Bounds")
    public void testEmployeeIdValidation() {
        assertTrue(ValidationUtils.isValidEmployeeId("10001"), "Exact 5-digit numerical configurations must be valid");
        assertTrue(ValidationUtils.isValidEmployeeId("00001"), "Leading zeros inside 5-digit blocks should be valid");
        assertFalse(ValidationUtils.isValidEmployeeId("123"), "Under-length numerical formats must be evaluated as false");
        assertFalse(ValidationUtils.isValidEmployeeId("123456"), "Over-length numerical formats must be evaluated as false");
        assertFalse(ValidationUtils.isValidEmployeeId("ABCDE"), "Alphabetic character strings must be evaluated as false");
        assertFalse(ValidationUtils.isValidEmployeeId(null), "Null pointers must be handled safely returning false");
    }

    @Test
    @DisplayName("Validation: Verify Month Cardinality Bounds")
    public void testMonthValidation() {
        assertTrue(ValidationUtils.isValidMonth(1), "Lower boundary validation for January must be true");
        assertTrue(ValidationUtils.isValidMonth(6), "Mid-range validation for June must be true");
        assertTrue(ValidationUtils.isValidMonth(12), "Upper boundary validation for December must be true");
        assertFalse(ValidationUtils.isValidMonth(0), "Zero boundary entries must be evaluated as false");
        assertFalse(ValidationUtils.isValidMonth(13), "Values exceeding normal calendar bounds must be false");
        assertFalse(ValidationUtils.isValidMonth(-5), "Negative integers must be safely handled as false");
    }

    @Test
    @DisplayName("Validation: Verify Date Isolation Constraints")
    public void testDateValidation() {
        assertTrue(ValidationUtils.isValidDate("2026-07-06"), "Standard ISO-8601 date format (yyyy-MM-dd) must return true");
        assertFalse(ValidationUtils.isValidDate("07/06/2026"), "Slash-separated alternative formats must be rejected by default parser");
        assertFalse(ValidationUtils.isValidDate("invalid-string"), "Plain alphanumeric labels should evaluate cleanly as false");
    }

    @Test
    @DisplayName("Validation: Verify Military Time Processing")
    public void testTimeValidation() {
        assertTrue(ValidationUtils.isValidTime("14:30"), "Standard 24-hour pattern formats (H:mm) must evaluate as true");
        assertTrue(ValidationUtils.isValidTime("9:15"), "Single digit hour formats should process correctly");
        assertFalse(ValidationUtils.isValidTime("25:00"), "Hour integers exceeding global limits must evaluate as false");
        assertFalse(ValidationUtils.isValidTime("12:61"), "Minute components exceeding structural rules must evaluate as false");
        assertFalse(ValidationUtils.isValidTime(null), "Null fields inside temporal validation must safely yield false");
    }

    @Test
    @DisplayName("Validation: Verify Government Statutory Formats")
    public void testGovernmentIdFormats() {
        // --- SSS ---
        assertTrue(ValidationUtils.isValidSSS("12-1234567-1"), "Standard hyphenated SSS format must return true");
        assertFalse(ValidationUtils.isValidSSS("1234567890"), "Raw numerical digit groups for SSS must return false");
        
        // --- TIN ---
        assertTrue(ValidationUtils.isValidTIN("123-456-789-000"), "Standard quadruple-segment TIN configuration must return true");
        assertFalse(ValidationUtils.isValidTIN("123-456-789"), "Incomplete segments for TIN formatting must yield false");
        
        // --- Phone ---
        assertTrue(ValidationUtils.isValidPhone("123-456-789"), "Standard triple-digit segmented phone patterns must return true");
        assertFalse(ValidationUtils.isValidPhone("123-45"), "Truncated contact entries must return false");
        
        // --- Pag-IBIG ---
        assertTrue(ValidationUtils.isValidPagIbig("123456789012"), "Standard continuous 12-digit Pag-IBIG structures must return true");
        assertFalse(ValidationUtils.isValidPagIbig("12345"), "Under-length Pag-IBIG values must evaluate as false");
        
        // --- PhilHealth ---
        assertTrue(ValidationUtils.isValidPhilHealth("123456789012"), "Standard continuous 12-digit PhilHealth sequences must return true");
        assertFalse(ValidationUtils.isValidPhilHealth("ABC-123"), "Alphanumeric string parameters for PhilHealth must return false");
    }

    @Test
    @DisplayName("Validation: Verify Employee Age Rule Restrictions")
    public void testAgeThresholdValidation() {
        // Core Business Rule Check
        assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtils.validateAge("01/01/2020");
        }, "Sub-18 minor lifecycle dates must explicitly throw an IllegalArgumentException");
        
        // Parsing Failure Redirection Check
        assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtils.validateAge("2026-07-06");
        }, "Malformed pattern entries violating MM/dd/yyyy structures must throw an IllegalArgumentException");
    }
}