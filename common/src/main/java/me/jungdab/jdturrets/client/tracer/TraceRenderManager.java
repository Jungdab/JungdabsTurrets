package me.jungdab.jdturrets.client.tracer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class TraceRenderManager {
    private static final List<ClientTracer> TRACERS = new ArrayList<>();

    // 패킷 핸들러에서 호출할 메서드
    public static void addTracer(Vec3 start, Vec3 end, long duration) {
        TRACERS.add(new ClientTracer(start, end, System.currentTimeMillis(),duration)); // 0.15초 동안 지속
    }

    public static void render(PoseStack poseStack, Camera camera) {
        TRACERS.removeIf(ClientTracer::isExpired);
        if (TRACERS.isEmpty()) return;

        Vec3 cameraPos = camera.getPosition();

        poseStack.pushPose();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        Matrix4f matrix = poseStack.last().pose();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();

        for(ClientTracer tracer : TRACERS) {
            Vec3 start = tracer.start();
            Vec3 end = tracer.end();
            float alpha = tracer.getAlpha();

            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

            // -------------------------------------------------------------
            // [핵심] 플레이어 카메라 방향을 반영한 빌보드 오프셋 계산
            // -------------------------------------------------------------
            Vec3 lineDir = end.subtract(start).normalize(); // 레이저 진행 방향
            Vec3 camToLine = start.subtract(cameraPos).normalize(); // 카메라에서 레이저를 바라보는 방향

            // 두 벡터의 외적을 구하면, 레이저 진행 방향과 카메라 시선 모두에 수직인 오프셋 벡터가 나옵니다.
            Vec3 right = lineDir.cross(camToLine).normalize();

            float thickness = 0.01f; // 궤적 두께
            Vec3 offset = right.scale(thickness);

            // 이제 이 p1~p4 사각형은 항상 카메라를 정면으로 바라봅니다.
            Vec3 p1 = start.subtract(offset);
            Vec3 p2 = start.add(offset);
            Vec3 p3 = end.add(offset);
            Vec3 p4 = end.subtract(offset);

            int r = 255, g = 255, b = 30;
            int a = (int)(alpha * 200);

            bufferBuilder.addVertex(matrix, (float)p1.x, (float)p1.y, (float)p1.z).setColor(r, g, b, a);
            bufferBuilder.addVertex(matrix, (float)p2.x, (float)p2.y, (float)p2.z).setColor(r, g, b, a);
            bufferBuilder.addVertex(matrix, (float)p3.x, (float)p3.y, (float)p3.z).setColor(r, g, b, a);
            bufferBuilder.addVertex(matrix, (float)p4.x, (float)p4.y, (float)p4.z).setColor(r, g, b, a);

            MeshData meshData = bufferBuilder.build();
            if (meshData != null) {
                BufferUploader.drawWithShader(meshData);
            }
        }

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        poseStack.popPose();
    }

//    @SubscribeEvent
//    public static void onRenderLevel(RenderLevelStageEvent event) {
//        // AFTER_PARTICLES 단계에서 그리는 것이 이펙트 처리에 좋습니다.
//        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
//
//        TRACERS.removeIf(ClientTracer::isExpired);
//
//        if (TRACERS.isEmpty()) return;
//
//        Minecraft mc = Minecraft.getInstance();
//        // 카메라의 실제 위치를 가져와 렌더링 원점을 맞춥니다.
//        Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();
//
//        PoseStack poseStack = event.getPoseStack();
//        poseStack.pushPose();
//        poseStack.translate(-camPos.x, -camPos.y, -camPos.z); // 월드 좌표계로 변환
//        Matrix4f matrix = poseStack.last().pose();
//
//        // 렌더링 설정 세팅 (반투명 레이저 세팅)
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        RenderSystem.disableCull();
//        RenderSystem.setShader(GameRenderer::getPositionColorShader);
//
//        Tesselator tesselator = Tesselator.getInstance();
//        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
//
//
//        buffer.addVertex(poseStack.last(), 0, 0, 0).setColor(1,1,1,1);
//        buffer.addVertex(poseStack.last(), 0, 5, 0).setColor(1,1,1,1);
//
//        for (ClientTracer tracer : TRACERS) {
//            float alpha = tracer.getAlpha();
//
//            buffer.addVertex(matrix, (float)tracer.start().x, (float)tracer.start().y, (float)tracer.start().z)
//                    .setColor(1.0f, 0.1f, 0.1f, alpha);
//            buffer.addVertex(matrix, (float)tracer.end().x, (float)tracer.end().y, (float)tracer.end().z)
//                    .setColor(1.0f, 0.1f, 0.1f, alpha);
//        }
//
//        BufferUploader.drawWithShader(buffer.buildOrThrow());
//
//        RenderSystem.enableCull();
//        RenderSystem.disableBlend();
//        poseStack.popPose();
//    }
}
