package me.jungdab.jdturrets.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChipItem extends Item {

    private final Map<Attribute, AttributeModifier> modifiers;
    private final Component lore;

    public ChipItem(Properties properties, Component lore, Map.Entry<Attribute, AttributeModifier>... modifierEntry) {
        super(properties);

        this.lore = lore;
        this.modifiers = new HashMap<>();
        for(Map.Entry<Attribute, AttributeModifier> entry : modifierEntry) {
            this.modifiers.put(entry.getKey(), entry.getValue());
        }
    }

    public ChipItem(Properties properties, Component lore) {
        super(properties);

        this.lore = lore;
        this.modifiers = new HashMap<>();
    }

    public ChipItem(Properties properties) {
        super(properties);

        this.lore = null;
        this.modifiers = new HashMap<>();
    }

    public Map<Attribute, AttributeModifier> getModifiers() {
        return this.modifiers;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        if(this.lore != null) tooltipComponents.add(this.lore);
    }
}
