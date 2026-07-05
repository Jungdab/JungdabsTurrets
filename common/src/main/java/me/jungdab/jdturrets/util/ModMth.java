package me.jungdab.jdturrets.util;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class ModMth {
    public static Vec3 getForwardPosition(Vec3 pos, Vec2 rot, double x, double y, double z) {
        float f = Mth.cos((rot.y + 90.0F) * ((float)Math.PI / 180F));
        float f1 = Mth.sin((rot.y + 90.0F) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(-rot.x * ((float)Math.PI / 180F));
        float f3 = Mth.sin(-rot.x * ((float)Math.PI / 180F));
        float f4 = Mth.cos((-rot.x + 90.0F) * ((float)Math.PI / 180F));
        float f5 = Mth.sin((-rot.x + 90.0F) * ((float)Math.PI / 180F));
        Vec3 vec31 = new Vec3(f * f2, f3, f1 * f2);
        Vec3 vec32 = new Vec3(f * f4, f5, f1 * f4);
        Vec3 vec33 = vec31.cross(vec32).scale(-1.0F);
        double d0 = vec31.x * z + vec32.x * y + vec33.x * x;
        double d1 = vec31.y * z + vec32.y * y + vec33.y * x;
        double d2 = vec31.z * z + vec32.z * y + vec33.z * x;
        return new Vec3(pos.x + d0, pos.y + d1, pos.z + d2);
    }

    public static Vec2 rotationFromDirection(Vec3 direction) {
        double x = direction.x;
        double y = direction.y;
        double z = direction.z;

        double xzLength = Math.sqrt(x * x + z * z);
        float pitch = (float) (Math.atan2(-y, xzLength) * (180.0D / Math.PI));
        float yaw = (float) (Math.atan2(x, z) * (180.0D / Math.PI));
        yaw = -yaw;
        return new Vec2(pitch, yaw);
    }
}
