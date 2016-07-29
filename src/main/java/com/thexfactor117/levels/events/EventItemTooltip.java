package com.thexfactor117.levels.events;

import org.lwjgl.input.Keyboard;

import com.thexfactor117.levels.handlers.ConfigHandler;
import com.thexfactor117.levels.leveling.Ability;
import com.thexfactor117.levels.leveling.AbilityHelper;
import com.thexfactor117.levels.leveling.Experience;
import com.thexfactor117.levels.leveling.Rarity;
import com.thexfactor117.levels.utils.NBTHelper;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author TheXFactor117
 *
 */
public class EventItemTooltip 
{
	/**
	 * Gets called whenever the tooltip for an item needs to appear.
	 * @param event
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void addInformation(ItemTooltipEvent event)
	{
		ItemStack stack = event.getItemStack();
		Item item = stack.getItem();

		if (item != null)
		{
			if (item instanceof ItemSword || item instanceof ItemBow || item instanceof ItemArmor)
			{
				NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);
				
				if (nbt != null)
				{	
					Rarity rarity = Rarity.getRarity(nbt);
					String exp;
					
					if (Experience.getLevel(nbt) == ConfigHandler.MAX_LEVEL_CAP) exp = I18n.format("levels.experience.max");
					else exp = Experience.getExperience(nbt) + " / " + Experience.getMaxLevelExp(Experience.getLevel(nbt));
					
					event.getToolTip().add("");
					event.getToolTip().add(TextFormatting.GOLD + "=========================");
					event.getToolTip().add(rarity.getColor() + TextFormatting.ITALIC + I18n.format("levels.rarity." + rarity.ordinal()));
				
					if (Experience.getLevel(nbt) == 6) event.getToolTip().add("Level:" + TextFormatting.ITALIC + "Max");
					else event.getToolTip().add("Level: " + Experience.getLevel(nbt));
					
					event.getToolTip().add("Experience: " + exp);
					
					if (!ConfigHandler.DURABILITY) event.getToolTip().add("Durability: " + TextFormatting.ITALIC + "Unlimited");
					else event.getToolTip().add("Durability: " + (stack.getMaxDamage() - stack.getItemDamage()) + " / " + stack.getMaxDamage());
					
					event.getToolTip().add("");
					
					if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
					{
						event.getToolTip().add(TextFormatting.ITALIC + "Abilities");
						AbilityHelper abilityHelper;
						
						if (item instanceof ItemArmor) abilityHelper = AbilityHelper.ARMOR;
						else abilityHelper = AbilityHelper.WEAPON;
							
						for (Ability ability : abilityHelper.getAbilities())
						{	
							if (ability.hasAbility(nbt))
							{
								int tier = ability.getTier(nbt);
								String index = "undefined";
								
								switch (tier)
								{
									case 1: 
										index = "I";
										break;
									case 2:
										index = "II";
										break;
									case 3:
										index = "III";
										break;
								}
								
								event.getToolTip().add(ability.getColor() + I18n.format("levels.ability." + ability.toString().toLowerCase()) + " " + index);
							}
						}
					}
					else event.getToolTip().add(TextFormatting.ITALIC + "Abilities (Shift)");
					
					event.getToolTip().add(TextFormatting.GOLD + "=========================");
					event.getToolTip().add("");
				}
			}
		}
	}
}
