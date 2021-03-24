package joebenitus.joebenite;

import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolMaterial;

public class HoeBase extends HoeItem {

  public HoeBase(ToolMaterial material) {
    super(material, -4, 1.0f, new Item.Settings().group(ItemGroup.TOOLS));
    //TODO Auto-generated constructor stub
  }
  
}
