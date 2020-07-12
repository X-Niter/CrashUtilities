package com.darkere.crashutils.Network;

import com.darkere.crashutils.DataStructures.DataHolder;
import com.darkere.crashutils.DataStructures.EntityData;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class EntityDataMessage {
    EntityData list;

    public EntityDataMessage(EntityData list) {
        this.list = list;
    }


    public static void encode(EntityDataMessage data, PacketBuffer buf) {
        NetworkTools.writeRLWPMap(data.list.getMap(), buf);
    }

    public static EntityDataMessage decode(PacketBuffer buf) {
        return new EntityDataMessage(new EntityData(NetworkTools.readRLWPMap(buf)));
    }

    public static void handle(EntityDataMessage data, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DataHolder.addEntityData(data.list);

        });
        ctx.get().setPacketHandled(true);
    }
}
