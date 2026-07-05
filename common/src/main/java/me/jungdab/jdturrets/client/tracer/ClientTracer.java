package me.jungdab.jdturrets.client.tracer;

import net.minecraft.world.phys.Vec3;

public record ClientTracer(
        Vec3 start,
        Vec3 end,
        long spawnTime,
        long duration
) {
    public boolean isExpired() {
        return System.currentTimeMillis() - spawnTime > duration;
    }

    public float getAlpha() {
        long elapsed = System.currentTimeMillis() - spawnTime;
        return 1.0f - ((float) elapsed / duration);
    }
}
