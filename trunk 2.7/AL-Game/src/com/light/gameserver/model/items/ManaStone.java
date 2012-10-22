/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.light.gameserver.model.items;

import java.util.List;

import com.light.gameserver.dataholders.DataManager;
import com.light.gameserver.model.gameobjects.PersistentState;
import com.light.gameserver.model.stats.calc.functions.StatFunction;
import com.light.gameserver.model.templates.item.ItemTemplate;

/**
 * @author ATracer
 */
public class ManaStone extends ItemStone {

	private List<StatFunction> modifiers;

	public ManaStone(int itemObjId, int itemId, int slot, PersistentState persistentState) {
		super(itemObjId, itemId, slot, persistentState);

		ItemTemplate stoneTemplate = DataManager.ITEM_DATA.getItemTemplate(itemId);
		if (stoneTemplate != null && stoneTemplate.getModifiers() != null) {
			this.modifiers = stoneTemplate.getModifiers();
		}
	}

	/**
	 * @return modifiers
	 */
	public List<StatFunction> getModifiers() {
		return modifiers;
	}

	public StatFunction getFirstModifier() {
		return (modifiers != null && modifiers.size() > 0) ? modifiers.get(0) : null;
	}

}
