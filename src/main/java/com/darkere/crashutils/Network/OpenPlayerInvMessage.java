package com.darkere.crashutils.Network;

import com.darkere.crashutils.ClientEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class OpenPlayerInvMessage {
    Map<String, Integer> slotAmounts;
    String otherPlayerName;
    int windowID;

    public OpenPlayerInvMessage(int id, String playerName, Map<String, Integer> curios) {
        this.windowID = id;
        this.otherPlayerName = playerName;
        this.slotAmounts = curios;
    }

    public static void encode(OpenPlayerInvMessage data, FriendlyByteBuf buf) {
        buf.writeInt(data.windowID);
        buf.writeUtf(data.otherPlayerName);
        buf.writeInt(data.slotAmounts.size());
        data.slotAmounts.forEach((s, i) -> {
            buf.writeUtf(s);
            buf.writeInt(i);
        });

    }

    public static OpenPlayerInvMessage decode(FriendlyByteBuf buf) {
        Map<String, Integer> curios = new LinkedHashMap<>();
        int id = buf.readInt();
        String name = buf.readUtf();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            curios.put(buf.readUtf(), buf.readInt());
        }
        return new OpenPlayerInvMessage(id, name, curios);
    }

    public static boolean handle(OpenPlayerInvMessage data, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientEvents.openContainerAndScreen(data.windowID, data.otherPlayerName, data.slotAmounts);
        });
        return true;
    }
}
