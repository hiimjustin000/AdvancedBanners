package com.hiimjustin000.advancedbanners.packets;

import com.hiimjustin000.advancedbanners.listeners.DyePresetReloadListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DyePresetReloadPacket
{
    public DyePresetReloadPacket() {}

    public DyePresetReloadPacket(FriendlyByteBuf buffer)
    {
        int count = buffer.readVarInt();
        DyePresetReloadListener.PRESETS.clear();
        for (int i = 0; i < count; i++)
        {
            ItemStack stack = new ItemStack(DyePresetReloadListener.ADVANCED_DYE_ITEM);
            CompoundTag tag = buffer.readNbt();
            if (tag != null)
                stack.setTag(tag);
            DyePresetReloadListener.PRESETS.add(stack);
        }
    }

    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeVarInt(DyePresetReloadListener.PRESETS.size());
        DyePresetReloadListener.PRESETS.forEach(x -> buffer.writeNbt(x.getTag()));
    }

    public void handle(Supplier<NetworkEvent.Context> context)
    {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> ctx.setPacketHandled(true));
    }
}
