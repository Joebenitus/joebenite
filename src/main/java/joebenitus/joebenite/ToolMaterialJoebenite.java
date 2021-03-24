package joebenitus.joebenite;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class ToolMaterialJoebenite implements ToolMaterial {

  @Override
  public float getAttackDamage() {
    // TODO Auto-generated method stub
    return 4.0f;
  }

  @Override
  public int getDurability() {
    return 2700;
  }

  @Override
  public int getEnchantability() {
    // TODO Auto-generated method stub
    return 15;
  }

  @Override
  public int getMiningLevel() {
    // TODO Auto-generated method stub
    return 5;
  }

  @Override
  public float getMiningSpeedMultiplier() {
    // TODO Auto-generated method stub
    return 12.0f;
  }

  @Override
  public Ingredient getRepairIngredient() {
    // TODO Auto-generated method stub
    return Ingredient.ofItems(Joebenite.JOEBENITE);
  }
  
}
