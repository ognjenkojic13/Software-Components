package rs.ac.raf.notificationservice.service;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TemplateRendererTest {

    @Test
    void replacesKnownPlaceholders() {
        Map<String, String> data = new HashMap<>();
        data.put("username", "Pera");
        data.put("sessionTitle", "CS2 vece");

        String result = TemplateRenderer.render("Zdravo {{username}}, sesija '{{sessionTitle}}' pocinje.", data);

        assertEquals("Zdravo Pera, sesija 'CS2 vece' pocinje.", result);
    }

    @Test
    void leavesUnknownPlaceholdersUntouched() {
        String result = TemplateRenderer.render("Zdravo {{username}}", new HashMap<>());
        assertEquals("Zdravo {{username}}", result);
    }
}
