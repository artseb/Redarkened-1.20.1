package github.artseb.redarkened.mixin;

import github.artseb.redarkened.system.classes.ClassApplier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private float modifyDamage(float amount, DamageSource source) {
		if ((Object) this instanceof PlayerEntity player) {
			float defense = ClassApplier.getDefenseModifier(player);
			if (defense != 0f) {
				amount *= (1.0f - defense);
			}
		}
		return amount;
	}
}