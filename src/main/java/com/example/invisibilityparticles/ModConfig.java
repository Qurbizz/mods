package com.example.invisibilityparticles;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    public static int particleAmount = 20;
    private static final File configFile = new File("config/invisibility_particles.json");

    public static void loadConfig() {
        try {
            if (!configFile.getParentFile().exists()) {
                configFile.getParentFile().mkdirs();
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            if (configFile.exists()) {
                try (FileReader reader = new FileReader(configFile)) {
                    ModConfig loaded = gson.fromJson(reader, ModConfig.class);
                    if (loaded != null) {
                        particleAmount = loaded.particleAmount;
                    }
                }
            } else {
                saveConfig();
            }
        } catch (Exception e) {
            particleAmount = 20; // fallback
        }
    }

    public static void saveConfig() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(configFile)) {
                gson.toJson(new ModConfig(), writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
