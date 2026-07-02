package me.jungdab.jdturrets.mixin;

import me.jungdab.jdturrets.init.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

    @Shadow
    @Final
    private DataSlot cost;

    public AnvilMenuMixin(@Nullable MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(type, containerId, playerInventory, access);
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    public void createInfinityAmmoChip(CallbackInfo ci) {
        ItemStack itemstack = this.inputSlots.getItem(0);
        if(!itemstack.is(ModItems.CHIP_BASE.get()) | itemstack.getCount() != 1) return;

        ItemStack itemstack2 = this.inputSlots.getItem(1);
        ItemEnchantments enchantments = itemstack2.get(DataComponents.STORED_ENCHANTMENTS);
        if(enchantments == null) return;

        if(!itemstack2.is(Items.ENCHANTED_BOOK) || enchantments.getLevel(this.player.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.INFINITY)) <= 0) return;

        this.cost.set(8);
        this.resultSlots.setItem(0, ModItems.INFINITY_AMMO_CHIP.get().getDefaultInstance());

        ci.cancel();
    }
}
