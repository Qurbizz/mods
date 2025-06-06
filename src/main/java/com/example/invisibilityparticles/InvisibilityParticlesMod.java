package com.example.invisibilityparticles;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class InvisibilityParticlesMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModConfig.loadConfig();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client == null || client.player == null || client.world == null || !client.isRunning()) return;

            StatusEffectInstance invis = client.player.getStatusEffect(StatusEffects.INVISIBILITY);
            if (invis != null && invis.getDuration() > 0) {
                spawnInvisibilityParticles(client);
            }
        });

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof GameOptionsScreen gos) {
                screen.addDrawableChild(new SliderWidget(10, 10, 150, 20, Text.literal("Частицы невидимости: " + ModConfig.particleAmount)) {
                    @Override
                    protected void updateMessage() {
                        setMessage(Text.literal("Частицы невидимости: " + ModConfig.particleAmount));
                    }

                    @Override
                    protected void applyValue() {
                        ModConfig.particleAmount = (int)(value * 100);
                        updateMessage();
                        ModConfig.saveConfig();
                    }

                    @Override
                    public double getValue() {
                        return ModConfig.particleAmount / 100.0;
                    }
                });
            }
        });
    }

    private void spawnInvisibilityParticles(MinecraftClient client) {
        ClientWorld world = client.world;
        int count = Math.min(ModConfig.particleAmount, 100);
        for (int i = 0; i < count; i++) {
            double offsetX = (world.random.nextDouble() - 0.5);
            double offsetY = world.random.nextDouble();
            double offsetZ = (world.random.nextDouble() - 0.5);
            world.addParticle(ParticleTypes.CLOUD,
                    client.player.getX() + offsetX,
                    client.player.getY() + offsetY,
                    client.player.getZ() + offsetZ,
                    0.0, 0.0, 0.0);
        }
    }
}
