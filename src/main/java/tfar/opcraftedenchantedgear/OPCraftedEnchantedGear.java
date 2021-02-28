package tfar.opcraftedenchantedgear;

import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(OPCraftedEnchantedGear.MODID)
public class OPCraftedEnchantedGear {
    // Directly reference a log4j logger.

    public static final String MODID = "opcraftedenchantedgear";

    public OPCraftedEnchantedGear() {
    }

    private static final Random rand = new Random();

    public static ItemStack enchantOutputedGear(PlayerEntity player, CraftingInventory craftingInventory, ICraftingRecipe iCraftingRecipe) {
        ItemStack old = iCraftingRecipe.getCraftingResult(craftingInventory);
        List<EnchantmentData> list = getEnchantmentList(old, 50);

        for (EnchantmentData enchantmentdata : list) {
            old.addEnchantment(enchantmentdata.enchantment, enchantmentdata.enchantmentLevel);
        }

        ListNBT enchTag = old.getEnchantmentTagList();
        if (!enchTag.isEmpty()) {
            for (INBT inbt : enchTag) {
                CompoundNBT compoundNBT = (CompoundNBT)inbt;
                compoundNBT.putInt("lvl",compoundNBT.getInt("lvl") * 64);
            }
        }
        return old;
    }

    private static List<EnchantmentData> getEnchantmentList(ItemStack stack, int level) {
        List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(rand, stack, level, false);
        if (stack.getItem() == Items.BOOK && list.size() > 1) {
            list.remove(rand.nextInt(list.size()));
        }
        return list;
    }
}
