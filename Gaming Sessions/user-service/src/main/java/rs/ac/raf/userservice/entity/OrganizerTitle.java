package rs.ac.raf.userservice.entity;

public enum OrganizerTitle {
    NEMA_TITULE(0),
    BARJAKTAR(10),
    HAJDUK(25),
    VOJVODA(50),
    KNEZ(100);

    private final int minSessionsOrganized;

    OrganizerTitle(int minSessionsOrganized) {
        this.minSessionsOrganized = minSessionsOrganized;
    }

    public static OrganizerTitle fromSessionsOrganized(int sessionsOrganized) {
        OrganizerTitle result = NEMA_TITULE;
        for (OrganizerTitle title : values()) {
            if (sessionsOrganized >= title.minSessionsOrganized) {
                result = title;
            }
        }
        return result;
    }
}
