package com.hiimjustin000.advancedbanners.packets;

import com.hiimjustin000.advancedbanners.listeners.BannerPresetReloadListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BannerPresetReloadPacket
{
    public BannerPresetReloadPacket() {}

    public BannerPresetReloadPacket(FriendlyByteBuf buffer)
    {
        int count = buffer.readVarInt();
        BannerPresetReloadListener.PRESETS.clear();
        for (int i = 0; i < count; i++)
        {
            ItemStack stack = new ItemStack(BannerPresetReloadListener.ADVANCED_BANNER_ITEM);
            CompoundTag tag = buffer.readNbt();
            if (tag != null)
                stack.setTag(tag);
            BannerPresetReloadListener.PRESETS.add(stack);
        }
    }

    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeVarInt(BannerPresetReloadListener.PRESETS.size());
        BannerPresetReloadListener.PRESETS.forEach(x -> buffer.writeNbt(x.getTag()));
    }

    public void handle(Supplier<NetworkEvent.Context> context)
    {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> ctx.setPacketHandled(true));
    }
}
