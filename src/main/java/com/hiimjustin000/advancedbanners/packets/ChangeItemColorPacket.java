package com.hiimjustin000.advancedbanners.packets;

import com.hiimjustin000.advancedbanners.menus.DyeMachineMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChangeItemColorPacket
{
    private final int color;

    public ChangeItemColorPacket(int col)
    {
        color = col;
    }

    public ChangeItemColorPacket(FriendlyByteBuf buffer)
    {
        color = buffer.readVarInt();
    }

    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeVarInt(color);
    }

    public void handle(Supplier<NetworkEvent.Context> context)
    {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() ->
        {
            ServerPlayer player = ctx.getSender();
            if (player != null && player.containerMenu instanceof DyeMachineMenu menu)
                menu.changeColor(color);

            ctx.setPacketHandled(true);
        });
    }
}
