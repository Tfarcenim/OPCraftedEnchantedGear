package tfar.opcraftedenchantedgear.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.functions.EnchantRandomly;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.opcraftedenchantedgear.OPCraftedEnchantedGear;

@Mixin(WorkbenchContainer.class)
public class WorkBenchContainerMixin {

	private static final ThreadLocal<PlayerEntity> playerEntityThreadLocal = new ThreadLocal<>();

	@Inject(method = "updateCraftingResult",at = @At("HEAD"))
	private static void capturePlayer(int id, World world, PlayerEntity player, CraftingInventory inventory, CraftResultInventory inventoryResult, CallbackInfo ci) {
		playerEntityThreadLocal.set(player);
	}

	@Redirect(at = @At(value = "INVOKE",target = "Lnet/minecraft/item/crafting/ICraftingRecipe;getCraftingResult(Lnet/minecraft/inventory/IInventory;)Lnet/minecraft/item/ItemStack;"),
			method = "updateCraftingResult")
	private static ItemStack init(ICraftingRecipe iCraftingRecipe, IInventory inv) {
		return OPCraftedEnchantedGear.enchantOutputedGear(playerEntityThreadLocal.get(),(CraftingInventory)inv,iCraftingRecipe);
	}
}
