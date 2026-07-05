package me.jungdab.jdturrets.client.screen;

import me.jungdab.jdturrets.Constants;
import me.jungdab.jdturrets.entity.AbstractTurret;
import me.jungdab.jdturrets.entity.inventory.TurretContainerMenu;
import me.jungdab.jdturrets.init.ModEntities;
import me.jungdab.jdturrets.network.C2STurretConfigPacket;
import me.jungdab.jdturrets.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TurretContainerScreen extends AbstractContainerScreen<TurretContainerMenu> {
    private static final Map<Integer, AbstractTurret> DUMMY_TURRETS = new HashMap<>();

    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/gui/turret_container.png");

    private float xMouse;
    private float yMouse;
    private boolean placed = false;

    private CheckBox playerCheckBox;
    private CheckBox friendlyCheckBox;
    private CheckBox hostileCheckBox;
    private CheckBox turretCheckBox;

    private Component ownerLabel;

    public TurretContainerScreen(TurretContainerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.imageWidth = 231;
        this.imageHeight = 187;

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
                dummyTurret.getAnimatableInstanceCache().getManagerForId(0).getAnimationControllers().get("Turret").forceAnimationReset();
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
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.xMouse = (float)mouseX;
        this.yMouse = (float)mouseY;
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float v, int i, int i1) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        int type = this.menu.getTurretType();
        if(type == 0) return;

        if(DUMMY_TURRETS.containsKey(type)) {
            AbstractTurret dummyTurret = DUMMY_TURRETS.get(type);


            InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, leftPos+89, topPos+60, 30, (leftPos+89) - this.xMouse, (topPos+60) - this.yMouse, dummyTurret);
        } else {
            AbstractTurret dummyTurret = getDummyTurret(type, this.minecraft);
            if(dummyTurret != null) {
                DUMMY_TURRETS.put(type, dummyTurret);
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);

        guiGraphics.drawString(this.font, String.format("%.1f", menu.getData(0)) + "/" + String.format("%.1f", menu.getData(1)), 157, 23, 4210752, false);
        guiGraphics.drawString(this.font, String.format("%.1f", menu.getData(2)), 157, 35, 4210752, false);
        guiGraphics.drawString(this.font, String.format("%.1f", menu.getData(3)), 157, 47, 4210752, false);
        guiGraphics.drawString(this.font, String.format("%.1f", menu.getData(4)), 157, 58, 4210752, false);

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
            guiGraphics.drawString(this.font, this.ownerLabel, this.imageWidth-w-8, this.titleLabelY, 4210752, false);
        }
    }

    private static AbstractTurret getDummyTurret(int type, Minecraft mc) {
        if(mc == null) return null;

        ClientLevel level = mc.level;
        if(level == null) return null;

        return switch (type) {
            case 1 -> ModEntities.BASIC_TURRET.get().create(level);
            case 2 -> ModEntities.SNIPER_TURRET.get().create(level);
            case 3 -> ModEntities.TANK_TURRET.get().create(level);
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
        public void onClick(double mouseX, double mouseY) {
            super.onClick(mouseX, mouseY);

            this.isChecked = !this.isChecked;
            this.onClick.accept(this);
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
            float u = isHovered ? 32 : 0;
            u += isChecked ? 0 : 16;

            guiGraphics.blit(TEXTURE, this.getX(), this.getY(), u, 187, this.getWidth(), this.getHeight(), 256, 256);

            if(isHovered) {
                guiGraphics.renderTooltip(Minecraft.getInstance().font, this.getMessage(), i, i1);
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

        }
    }
}
