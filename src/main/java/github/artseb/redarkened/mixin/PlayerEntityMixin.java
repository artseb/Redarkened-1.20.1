package github.artseb.redarkened.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {
	@Unique
	private String artsebs_mod$classId;

	protected PlayerEntityMixin(EntityType<? extends Entity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
	private void onWriteNbt(NbtCompound nbt, CallbackInfo ci) {
		if (artsebs_mod$classId != null) {
			nbt.putString("RedarkenedClass", artsebs_mod$classId);
		}
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
	private void onReadNbt(NbtCompound nbt, CallbackInfo ci) {
		if (nbt.contains("RedarkenedClass")) {
			artsebs_mod$classId = nbt.getString("RedarkenedClass");
		} else {
			artsebs_mod$classId = null;
		}
	}
}