package rs.ac.raf.userservice.service;

import org.junit.jupiter.api.Test;
import rs.ac.raf.userservice.entity.OrganizerTitle;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrganizerTitleTest {

    @Test
    void assignsThresholdsCorrectly() {
        assertEquals(OrganizerTitle.NEMA_TITULE, OrganizerTitle.fromSessionsOrganized(0));
        assertEquals(OrganizerTitle.NEMA_TITULE, OrganizerTitle.fromSessionsOrganized(9));
        assertEquals(OrganizerTitle.BARJAKTAR, OrganizerTitle.fromSessionsOrganized(10));
        assertEquals(OrganizerTitle.BARJAKTAR, OrganizerTitle.fromSessionsOrganized(24));
        assertEquals(OrganizerTitle.HAJDUK, OrganizerTitle.fromSessionsOrganized(25));
        assertEquals(OrganizerTitle.VOJVODA, OrganizerTitle.fromSessionsOrganized(50));
        assertEquals(OrganizerTitle.KNEZ, OrganizerTitle.fromSessionsOrganized(100));
        assertEquals(OrganizerTitle.KNEZ, OrganizerTitle.fromSessionsOrganized(250));
    }
}
