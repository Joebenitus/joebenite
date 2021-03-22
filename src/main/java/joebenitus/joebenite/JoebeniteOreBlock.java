package joebenitus.joebenite;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.util.math.MathHelper;

public class JoebeniteOreBlock extends Block {

  public JoebeniteOreBlock(Settings settings) {
    super(settings);
    //TODO Auto-generated constructor stub
  }

  protected int getExperienceWhenMined(java.util.Random random) {
    return MathHelper.nextInt(random, 2, 5);
  }
  
  public void onStacksDropped(net.minecraft.block.BlockState state, net.minecraft.server.world.ServerWorld world, net.minecraft.util.math.BlockPos pos, net.minecraft.item.ItemStack stack) {
    super.onStacksDropped(state, world, pos, stack);
      if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) == 0) {
        int i = this.getExperienceWhenMined(world.random);
        if (i > 0) {
          this.dropExperience(world, pos, i);
        }
      }
  }

}