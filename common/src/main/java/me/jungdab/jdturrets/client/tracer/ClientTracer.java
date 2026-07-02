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

    // 전체 생존 시간에 따른 투명도 (점점 흐려짐)
    public float getAlpha() {
        long elapsed = System.currentTimeMillis() - spawnTime;
        return 1.0f - ((float) elapsed / duration);
    }
}
