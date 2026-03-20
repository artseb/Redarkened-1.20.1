package github.artseb.redarkened.mixin;

import github.artseb.redarkened.system.classes.ClassApplier;
import github.artseb.redarkened.system.classes.PlayerClassStorage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
	@Inject(method = "copyFrom", at = @At("RETURN"))
	private void onCopyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
		Identifier classId = PlayerClassStorage.getClass(oldPlayer);
		if (classId != null) {
			ServerPlayerEntity thisPlayer = (ServerPlayerEntity) (Object) this;
			PlayerClassStorage.setClass(thisPlayer, classId);
			ClassApplier.applyClass(thisPlayer, classId);
		}
	}
}