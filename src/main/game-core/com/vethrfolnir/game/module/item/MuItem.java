/**
 * Copyright (C) 2013-2014 Project-Vethrfolnir
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.vethrfolnir.game.module.item;

import com.vethrfolnir.game.entitys.GameObject;
import com.vethrfolnir.game.entitys.components.creature.CreatureMapping;
import com.vethrfolnir.game.entitys.components.inventory.Inventory;
import com.vethrfolnir.game.entitys.components.inventory.WindowType;
import com.vethrfolnir.game.entitys.components.player.PlayerMapping;
import com.vethrfolnir.game.network.mu.MuPackets;
import com.vethrfolnir.game.templates.item.ItemTemplate;

/**
 * @author Constantin
 *
 */
public class MuItem {
	
	private final int objectId;
	private final ItemTemplate template;
	private final int[] dataBuffer = new int[12];

	private int Skill = 0;
	private int Luck = 0;
	private int addOption = 0;
	
	private int excelentOption1 = 0; // Increase Mana per kill +8 | Increase Zen After Hunt +40%
	private int excelentOption2 = 0; // Increase hit points per kill +8 | Defense success rate +10%
	private int excelentOption3 = 0; // Increase attacking(wizardly)speed+7 | Reflect damage +5%
	private int excelentOption4 = 0; // Increase wizardly damage +2% | Damage Decrease +4%
	private int excelentOption5 = 0; // Increase Damage +level/20 | Increase MaxMana +4%
	private int excelentOption6 = 0; // Excellent Damage Rate +10% | Increase MaxHP +4%
	private int AllExcOptions = 0;
	
	private int Ancient = 0;
	private int Option380 = 0;
	private int harmonyType = 0;
	private int harmonyEnchant = 0;
	
	private int socketOption1 = 255;
	private int socketOption2 = 255;
	private int socketOption3 = 255;
	private int socketOption4 = 255;
	private int socketOption5 = 255;
	
	private GameObject owner;
	private int ownerId;
	private int slot = -1;
	private int itemLevel;
	private int durabilityCount;

	private WindowType location = WindowType.InventoryWindow;
	private Inventory inventory;
	private boolean needsRegen = true;
	private boolean isNew = true;
	private boolean sellable;
	private boolean isEquipped;
	
	public MuItem(int objectId, ItemTemplate template) {
		this.objectId = objectId;
		this.template = template;
		
		setDurabilityCount(template.MagDur + template.Dur); //XXX test purpose, one of them is 0
	}
	
	public String getName() {
		return template.Name;
	}
	
	public int getItemType() {
		return template.Type;
	}
	
	public int getItemIndex() {
		return template.index;
	}
	
	public int getItemId() {
		return template.id;
	}
	
	public int getUniqueId() {
		return template.uniqueId;
	}

	/**
	 * @return the objectId
	 */
	public int getObjectId() {
		return objectId;
	}
	
	/**
	 * @return the owner's character id referenced in the database
	 */
	public int getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(GameObject e) {
		this.owner = e;
		this.ownerId = e.get(PlayerMapping.PlayerState).getCharId();
		inventory = e.get(CreatureMapping.Inventory);
	}

	/**
	 * @return the template
	 */
	public ItemTemplate getTemplate() {
		return template;
	}

	/**
	 * @return
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * @return the skill
	 */
	public int getSkill() {
		return Skill;
	}
	
	/**
	 * @param skill the skill to set
	 */
	public void setSkill(int skill) {
		Skill = skill;
		needsRegen = true;
	}
	
	/**
	 * @return the luck
	 */
	public int getLuck() {
		return Luck;
	}
	
	/**
	 * @param luck the luck to set
	 */
	public void setLuck(int luck) {
		Luck = luck;
		needsRegen = true;
	}

	/**
	 * @return the ancient
	 */
	public int getAncient() {
		return Ancient;
	}
	
	/**
	 * @return the option380
	 */
	public int getOption380() {
		return Option380;
	}
	
	/**
	 * @return the addOption
	 */
	public int getAddOption() {
		return addOption;
	}

	/**
	 * @param addOption the addOption to set
	 */
	public void setAddOption(int addOption) {
		this.addOption = addOption;
		needsRegen = true;
	}

	/**
	 * @return the excelentOption1
	 */
	public int getExcelentOption1() {
		return excelentOption1;
	}

	/**
	 * @param excelentOption1 the excelentOption1 to set
	 */
	public void setExcelentOption1(int excelentOption1) {
		this.excelentOption1 = excelentOption1;
		needsRegen = true;
	}

	/**
	 * @return the excelentOption2
	 */
	public int getExcelentOption2() {
		return excelentOption2;
	}

	/**
	 * @param excelentOption2 the excelentOption2 to set
	 */
	public void setExcelentOption2(int excelentOption2) {
		this.excelentOption2 = excelentOption2;
		needsRegen = true;
	}

	/**
	 * @return the excelentOption3
	 */
	public int getExcelentOption3() {
		return excelentOption3;
	}

	/**
	 * @param excelentOption3 the excelentOption3 to set
	 */
	public void setExcelentOption3(int excelentOption3) {
		this.excelentOption3 = excelentOption3;
		needsRegen = true;
	}

	/**
	 * @return the excelentOption4
	 */
	public int getExcelentOption4() {
		return excelentOption4;
	}

	/**
	 * @param excelentOption4 the excelentOption4 to set
	 */
	public void setExcelentOption4(int excelentOption4) {
		this.excelentOption4 = excelentOption4;
		needsRegen = true;
	}

	/**
	 * @return the excelentOption5
	 */
	public int getExcelentOption5() {
		return excelentOption5;
	}

	/**
	 * @param excelentOption5 the excelentOption5 to set
	 */
	public void setExcelentOption5(int excelentOption5) {
		this.excelentOption5 = excelentOption5;
		needsRegen = true;
	}

	/**
	 * @return the excelentOption6
	 */
	public int getExcelentOption6() {
		return excelentOption6;
	}

	/**
	 * @param excelentOption6 the excelentOption6 to set
	 */
	public void setExcelentOption6(int excelentOption6) {
		this.excelentOption6 = excelentOption6;
		needsRegen = true;
	}

	/**
	 * @return the getAllExcOptions
	 */
	public int getAllExcOptions() {
		return AllExcOptions;
	}

	/**
	 * @param getAllExcOptions the getAllExcOptions to set
	 */
	public void setAllExcOptions(int getAllExcOptions) {
		this.AllExcOptions = getAllExcOptions;
		needsRegen = true;
	}

	/**
	 * @return the harmonyType
	 */
	public int getHarmonyType() {
		return harmonyType;
	}

	/**
	 * @param harmonyType the harmonyType to set
	 */
	public void setHarmonyType(int harmonyType) {
		this.harmonyType = harmonyType;
		needsRegen = true;
	}

	/**
	 * @return the harmonyEnchant
	 */
	public int getHarmonyEnchant() {
		return harmonyEnchant;
	}

	/**
	 * @param harmonyEnchant the harmonyEnchant to set
	 */
	public void setHarmonyEnchant(int harmonyEnchant) {
		this.harmonyEnchant = harmonyEnchant;
		needsRegen = true;
	}

	/**
	 * @return the socketOption1
	 */
	public int getSocketOption1() {
		return socketOption1;
	}

	/**
	 * @param socketOption1 the socketOption1 to set
	 */
	public void setSocketOption1(int socketOption1) {
		this.socketOption1 = socketOption1;
		needsRegen = true;
	}

	/**
	 * @return the socketOption2
	 */
	public int getSocketOption2() {
		return socketOption2;
	}

	/**
	 * @param socketOption2 the socketOption2 to set
	 */
	public void setSocketOption2(int socketOption2) {
		this.socketOption2 = socketOption2;
		needsRegen = true;
	}

	/**
	 * @return the socketOption3
	 */
	public int getSocketOption3() {
		return socketOption3;
	}

	/**
	 * @param socketOption3 the socketOption3 to set
	 */
	public void setSocketOption3(int socketOption3) {
		this.socketOption3 = socketOption3;
		needsRegen = true;
	}

	/**
	 * @return the socketOption4
	 */
	public int getSocketOption4() {
		return socketOption4;
	}

	/**
	 * @param socketOption4 the socketOption4 to set
	 */
	public void setSocketOption4(int socketOption4) {
		this.socketOption4 = socketOption4;
		needsRegen = true;
	}

	/**
	 * @return the socketOption5
	 */
	public int getSocketOption5() {
		return socketOption5;
	}

	/**
	 * @param socketOption5 the socketOption5 to set
	 */
	public void setSocketOption5(int socketOption5) {
		this.socketOption5 = socketOption5;
		needsRegen = true;
	}

	/**
	 * @return the itemLevel
	 */
	public int getItemLevel() {
		return itemLevel;
	}

	/**
	 * @param itemLevel the itemLevel to set
	 */
	public void setItemLevel(int itemLevel) {
		this.itemLevel = itemLevel;
		needsRegen = true;
	}

	/**
	 * @return the durabilityCount
	 */
	public int getDurabilityCount() {
		return durabilityCount;
	}

	/**
	 * @param durabilityCount the durabilityCount to set
	 */
	public void setDurabilityCount(int durabilityCount) {
		this.durabilityCount = durabilityCount;

		if(owner != null)
			owner.sendPacket(MuPackets.ExDurabilityChange, this);
		
		needsRegen = true;
	}

	public void resetDurability(boolean b) {
		// TODO
	}

	/**
	 * @return the location
	 */
	public WindowType getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(WindowType location) {
		this.location = location;
	}

	/**
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * @return the dataBuffer
	 */
	public int[] getDataBuffer() {
		return dataBuffer;
	}

	/**
	 * @param slot the slot to set
	 */
	public void setSlot(int slot) {
		this.slot = slot;
	}
	
	public boolean isAncient() {
		return getAncient() > 0 ? true : false;
	}

	/**
	 * @param isAncient the isAncient to set
	 */
	public void setIsAncient() {
		Ancient = 1;
		needsRegen = true;
		
		if(owner != null)
			owner.sendPacket(MuPackets.ExItemLevelUpdate, this);
	}
	
	public boolean isExcelent() {
		return getAllExcOptions() == 0 ? false : true;
	}
	
	public void setOption380() {
		Option380 = 128;
		needsRegen = true;
		
		if(owner != null)
			owner.sendPacket(MuPackets.ExItemLevelUpdate, this);
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return template.X;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return template.Y;
	}

	/**
	 * @return
	 */
	public int[] toCode() {
		if(needsRegen) {
			ItemUtils.genItem(this);
			needsRegen  = false;
		}
		return dataBuffer;
	}

	public void forceRegen() {
		needsRegen = true;
	}
	
	/**
	 * @param isNew the isNew to set
	 */
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	
	/**
	 * @return
	 */
	public boolean isNew() {
		return isNew;
	}

	/**
	 * @return
	 */
	public boolean isSellable() {
		return sellable;
	}

	/**
	 * @param sellable the sellable to set
	 */
	public void setSellable(boolean sellable) {
		this.sellable = sellable;
	}

	/**
	 * @return
	 */
	public boolean isEquipped() {
		return isEquipped;
	}
	
	/**
	 * @param isEquipped the isEquipped to set
	 */
	public void setEquipped(boolean isEquipped) {
		this.isEquipped = isEquipped;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MuItem["+getName()+"/"+getObjectId()+"]";
	}
}
