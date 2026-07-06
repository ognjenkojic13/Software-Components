package rs.ac.raf.userservice.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AttendanceCalculatorTest {

    @Test
    void returnsFullPercentageWhenNoConcludedSessions() {
        assertEquals(100.0, AttendanceCalculator.calculatePercentage(0, 0));
    }

    @Test
    void calculatesPercentageFromAttendedAndLeft() {
        assertEquals(75.0, AttendanceCalculator.calculatePercentage(3, 1));
    }

    @Test
    void returnsZeroWhenNeverAttended() {
        assertEquals(0.0, AttendanceCalculator.calculatePercentage(0, 2));
    }
}
