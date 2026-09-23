package me.jungdab.jdturrets.client.screen;

import me.jungdab.jdturrets.Constants;
import me.jungdab.jdturrets.entity.AbstractTurret;
import me.jungdab.jdturrets.entity.inventory.TurretContainerMenu;
import me.jungdab.jdturrets.init.ModEntities;
import me.jungdab.jdturrets.network.C2STurretConfigPacket;
import me.jungdab.jdturrets.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TurretContainerScreen extends AbstractContainerScreen<TurretContainerMenu> {
    private static final Map<Integer, AbstractTurret> DUMMY_TURRETS = new HashMap<>();

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/turret_container.png");

    private float xMouse;
    private float yMouse;
    private boolean placed = false;

    private CheckBox playerCheckBox;
    private CheckBox friendlyCheckBox;
    private CheckBox hostileCheckBox;
    private CheckBox turretCheckBox;

    private Component ownerLabel;

    public TurretContainerScreen(TurretContainerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 231, 187);

        this.titleLabelX = 63;
        this.inventoryLabelX = 63;
        this.inventoryLabelY = 95;
    }


    private void sendTargetFilterConfigToServer() {
        int data = 0;

        data |= playerCheckBox.isChecked ? 1 : 0;
        data |= friendlyCheckBox.isChecked ? 1 << 1 : 0;
        data |= hostileCheckBox.isChecked ? 1 << 2 : 0;
        data |= turretCheckBox.isChecked ? 1 << 3 : 0;

        Services.NETWORK.sendToServer(new C2STurretConfigPacket(
                this.menu.getTurretId(),
                C2STurretConfigPacket.TARGET_FILTER,
                data
        ));
    }

    @Override
    protected void init() {
        super.init();

        this.playerCheckBox = new CheckBox( this.leftPos + 29, this.topPos + 7, Component.translatable("gui.check_box.target_player"), checkBox -> sendTargetFilterConfigToServer());
        this.friendlyCheckBox = new CheckBox(this.leftPos + 29, this.topPos + 27, Component.translatable("gui.check_box.target_friendly"), checkBox -> sendTargetFilterConfigToServer());
        this.hostileCheckBox = new CheckBox(this.leftPos + 29, this.topPos + 47, Component.translatable("gui.check_box.target_hostile"), checkBox -> sendTargetFilterConfigToServer());
        this.turretCheckBox = new CheckBox(this.leftPos + 29, this.topPos + 67, Component.translatable("gui.check_box.target_turret"), checkBox -> sendTargetFilterConfigToServer());

        this.addRenderableWidget(this.playerCheckBox);
        this.addRenderableWidget(this.friendlyCheckBox);
        this.addRenderableWidget(this.hostileCheckBox);
        this.addRenderableWidget(this.turretCheckBox);
    }

    @Override
    protected void containerTick() {
        int type = this.menu.getTurretType();
        if(type == 0) return;
        if(DUMMY_TURRETS.containsKey(type)) {
            AbstractTurret dummyTurret = DUMMY_TURRETS.get(type);
            if(!placed) {
                dummyTurret.getAnimatableInstanceCache().getManagerForId(dummyTurret.getId()).getAnimationControllers().get("Turret").reset();
                dummyTurret.tickCount = 0;
                placed = true;
                return;
            }
            if(dummyTurret != null) dummyTurret.tickCount++;
        }

        this.playerCheckBox.isChecked = this.menu.isTarget(AbstractTurret.TARGET_PLAYER);
        this.friendlyCheckBox.isChecked = this.menu.isTarget(AbstractTurret.TARGET_FRIENDLY_MOB);
        this.hostileCheckBox.isChecked = this.menu.isTarget(AbstractTurret.TARGET_HOSTILE_MOB);
        this.turretCheckBox.isChecked = this.menu.isTarget(AbstractTurret.TARGET_TURRET);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.xMouse = (float)mouseX;
        this.yMouse = (float)mouseY;
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);
        this.extractTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);

        int type = this.menu.getTurretType();
        if(type == 0) return;

        if(DUMMY_TURRETS.containsKey(type)) {
            AbstractTurret dummyTurret = DUMMY_TURRETS.get(type);
            extractAnimatedPreview(graphics, dummyTurret, partialTick);
        } else {
            AbstractTurret dummyTurret = getDummyTurret(type, this.minecraft);
            if(dummyTurret != null) {
                DUMMY_TURRETS.put(type, dummyTurret);
            }
        }
    }

    private void extractAnimatedPreview(GuiGraphicsExtractor graphics, AbstractTurret turret, float partialTick) {
        int x0 = this.leftPos + 63;
        int y0 = this.topPos + 18;
        int x1 = this.leftPos + 115;
        int y1 = this.topPos + 70;
        float xAngle = (float) Math.atan(((x0 + x1) / 2.0F - this.xMouse) / 40.0F);
        float yAngle = (float) Math.atan(((y0 + y1) / 2.0F - this.yMouse) / 40.0F);
        Quaternionf xRotation = new Quaternionf().rotateX(yAngle * 20.0F * ((float) Math.PI / 180.0F));
        Quaternionf rotation = new Quaternionf().rotateZ((float) Math.PI).mul(xRotation);

        // The vanilla inventory helper forces partialTick=1, which pauses GeckoLib 5.5.2 animations.
        var renderer = this.minecraft.getEntityRenderDispatcher().getRenderer(turret);
        var renderState = renderer.createRenderState(turret, partialTick);
        renderState.shadowPieces.clear();
        renderState.outlineColor = 0;
        if (renderState instanceof LivingEntityRenderState livingState) {
            livingState.bodyRot = 180.0F + xAngle * 20.0F;
            livingState.yRot = xAngle * 20.0F;
            livingState.xRot = -yAngle * 20.0F;
            livingState.boundingBoxWidth /= livingState.scale;
            livingState.boundingBoxHeight /= livingState.scale;
            livingState.scale = 1.0F;
        }
        Vector3f translation = new Vector3f(0.0F, renderState.boundingBoxHeight / 2.0F + 0.2F, 0.0F);
        graphics.entity(renderState, 30, translation, rotation, xRotation, x0, y0, x1, y1);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        super.extractLabels(guiGraphics, mouseX, mouseY);

        guiGraphics.text(this.font, String.format("%.1f", menu.getData(0)) + "/" + String.format("%.1f", menu.getData(1)), 157, 23, 0xFF404040, false);
        guiGraphics.text(this.font, String.format("%.1f", menu.getData(2)), 157, 35, 0xFF404040, false);
        guiGraphics.text(this.font, String.format("%.1f", menu.getData(3)), 157, 47, 0xFF404040, false);
        guiGraphics.text(this.font, String.format("%.1f", menu.getData(4)), 157, 58, 0xFF404040, false);

        if(this.ownerLabel == null) {
            int id = this.menu.getTurretId();
            if(id == 0) return;
            ClientLevel cl = Minecraft.getInstance().level;
            if(cl == null) return;
            Entity e = cl.getEntity(id);
            if(!(e instanceof AbstractTurret t)) return;
            this.ownerLabel = Component.literal(t.getOwnerName());
        }
        else {
            int w = this.font.width(this.ownerLabel);
            guiGraphics.text(this.font, this.ownerLabel, this.imageWidth-w-8, this.titleLabelY, 0xFF404040, false);
        }
    }

    private static AbstractTurret getDummyTurret(int type, Minecraft mc) {
        if(mc == null) return null;

        ClientLevel level = mc.level;
        if(level == null) return null;

        return switch (type) {
            case 1 -> ModEntities.BASIC_TURRET.get().create(level, EntitySpawnReason.LOAD);
            case 2 -> ModEntities.SNIPER_TURRET.get().create(level, EntitySpawnReason.LOAD);
            case 3 -> ModEntities.TANK_TURRET.get().create(level, EntitySpawnReason.LOAD);
            default -> null;
        };
    }

    private static class CheckBox extends AbstractWidget {

        public boolean isChecked;
        private final Consumer<CheckBox> onClick;

        public CheckBox(int x, int y, Component title, Consumer<CheckBox> onClick) {
            super(x, y, 16, 16, title);

            this.onClick = onClick;
        }

        @Override
        public void onClick(MouseButtonEvent event, boolean doubleClick) {
            super.onClick(event, doubleClick);

            this.isChecked = !this.isChecked;
            this.onClick.accept(this);
        }

        @Override
        protected void extractWidgetRenderState(GuiGraphicsExtractor guiGraphics, int i, int i1, float v) {
            float u = isHovered ? 32 : 0;
            u += isChecked ? 0 : 16;

            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.getX(), this.getY(), u, 187, this.getWidth(), this.getHeight(), 256, 256);

            if(isHovered) {
                guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, this.getMessage(), i, i1);
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

        }
    }
}
