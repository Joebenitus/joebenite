package joebenitus.joebenite;

import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolMaterial;

public class AxeBase extends AxeItem {

  public AxeBase(ToolMaterial material) {
    super(material, 7, -3.0f, new Item.Settings().group(ItemGroup.TOOLS));
    //TODO Auto-generated constructor stub
  }
  
}
