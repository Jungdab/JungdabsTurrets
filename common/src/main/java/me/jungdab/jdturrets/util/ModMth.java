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

        // 1. 수평 거리(X와 Z 축의 합성 벡터 길이)를 구합니다.
        double xzLength = Math.sqrt(x * x + z * z);

        // 2. Pitch(상하 회전각) 계산
        // 마인크래프트에서 위를 바라보면 -, 아래를 바라보면 + 입니다.
        // -y와 수평 거리를 이용해 아크탄젠트를 구한 뒤 각도로 변환합니다.
        float pitch = (float) (Math.atan2(-y, xzLength) * (180.0D / Math.PI));

        // 3. Yaw(좌우 회전각) 계산
        // 마인크래프트의 Yaw 기준점에 맞추기 위해 atan2(x, z)를 사용하고
        // 라디안을 각도로 바꾼 뒤 마인크래프트 방위 체계에 맞게 조정합니다.
        float yaw = (float) (Math.atan2(x, z) * (180.0D / Math.PI));
        yaw = -yaw; // 방향 부호 반전

        // 필요하다면 값을 반환 형식에 맞게 처리 (예: float 배열 또는 커스텀 객체)
        return new Vec2(pitch, yaw);
    }
}
