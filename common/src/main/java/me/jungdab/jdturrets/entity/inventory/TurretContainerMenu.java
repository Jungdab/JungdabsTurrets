package me.jungdab.jdturrets.entity.inventory;

import me.jungdab.jdturrets.init.ModItems;
import me.jungdab.jdturrets.init.ModMenuTypes;
import me.jungdab.jdturrets.item.ChipItem;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class TurretContainerMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData containerData;

    public TurretContainerMenu(int containerId, Inventory pInv) {
        this(containerId, pInv, new SimpleContainer(7), new SimpleContainerData(9), 0);
    }

    public TurretContainerMenu(int containerId, Inventory pInv, Container tContainer, ContainerData containerData, int type) {
        super(ModMenuTypes.TURRET_CONTAINER_MENU.get(), containerId);
        this.container = tContainer;
        this.containerData = containerData;
        this.containerData.set(5, type);

        for(int i1 = 0; i1 < 3; i1++) {
            this.addSlot(new ChipSlot(tContainer, i1,63 + i1 * 18, 75));
        }

        for(int i1 = 0; i1 < 4; i1++) {
            this.addSlot(new AmmoSlot(tContainer, i1+3, 153 + i1 * 18, 75));
        }

        for(int k = 0; k < 3; ++k) {
            for(int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(pInv, l + k * 9 + 9,63 + l * 18, 105 + k * 18));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(pInv, i1,63 + i1 * 18, 163));
        }

        this.addDataSlots(this.containerData);
    }

    public float getData(int idx) {
        return this.containerData.get(idx) / 10f;
    }

    public int getTurretType() {
        return this.containerData.get(5);
    }

    public int getTurretId() {
        return this.containerData.get(8);
    }

    public boolean isTarget(int idx) {
        int targetFilter = this.containerData.get(6);
        return (targetFilter & (1 << idx)) != 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 7) {
                if (!this.moveItemStackTo(itemstack1, 7, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 7, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    private static class ChipSlot extends Slot {
        public ChipSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() instanceof ChipItem;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }

    private static class AmmoSlot extends Slot {
        public AmmoSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(ModItems.TURRET_AMMO.get());
        }

        @Override
        public int getMaxStackSize() {
            return 100;
        }
    }
}
