package quickcarpet.mixin.core;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import quickcarpet.QuickCarpet;
import quickcarpet.QuickCarpetServer;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onPlayerConnect(MinecraftServer server, ClientConnection client, ServerPlayerEntity player, CallbackInfo ci) {
        QuickCarpet.getInstance().onPlayerConnect(player);
    }

    @Inject(method = "onCustomPayload", at = @At("HEAD"))
    private void processCustomPacket(CustomPayloadC2SPacket packet, CallbackInfo ci) {
        NetworkThreadUtils.forceMainThread(packet, (ServerPlayPacketListener) this, this.player.getWorld());
        QuickCarpetServer.getInstance().getPluginChannelManager().process(this.player, packet);
    }

    @Inject(method = "onDisconnected", at = @At("HEAD"))
    private void onPlayerDisconnect(Text reason, CallbackInfo ci) {
        QuickCarpet.getInstance().onPlayerDisconnect(this.player);
    }
}
