package rs.ac.raf.userservice.service;

public final class AttendanceCalculator {

    private AttendanceCalculator() {
    }

    public static double calculatePercentage(int sessionsAttended, int sessionsLeft) {
        int concluded = sessionsAttended + sessionsLeft;
        if (concluded == 0) {
            return 100.0;
        }
        return (sessionsAttended * 100.0) / concluded;
    }
}
