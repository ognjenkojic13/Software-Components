package rs.ac.raf.notificationservice.service;

import java.util.Map;

public final class TemplateRenderer {

    private TemplateRenderer() {
    }

    public static String render(String template, Map<String, String> templateData) {
        if (template == null) {
            return "";
        }
        String result = template;
        if (templateData != null) {
            for (Map.Entry<String, String> entry : templateData.entrySet()) {
                result = result.replace("{{" + entry.getKey() + "}}", entry.getValue() == null ? "" : entry.getValue());
            }
        }
        return result;
    }
}
