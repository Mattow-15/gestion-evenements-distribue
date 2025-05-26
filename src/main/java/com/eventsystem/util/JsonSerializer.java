package com.eventsystem.util;

import com.eventsystem.model.Evenement;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonSerializer {
    private static final ObjectMapper mapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.eventsystem.model")
                .build();

        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
                .findAndRegisterModules();
    }

    public static void saveEvents(List<Evenement> evenements, String filePath) throws IOException {
        try {
            File file = new File(filePath);

            // Crée le dossier parent seulement si nécessaire
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                if (!parent.mkdirs()) {
                    throw new IOException("Impossible de créer le dossier: " + parent.getAbsolutePath());
                }
            }

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, evenements);
        } catch (IOException e) {
            throw new IOException("Échec de la sauvegarde JSON vers " + filePath + ": " + e.getMessage(), e);
        }
    }


    public static List<Evenement> loadEvents(String filePath) throws IOException {
        return mapper.readValue(new File(filePath), new TypeReference<List<Evenement>>() {});
    }
}