/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.light.gameserver.model.stats.calc.functions;

import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.model.stats.calc.Stat2;
import com.light.gameserver.model.stats.container.StatEnum;
import com.light.gameserver.model.templates.item.ArmorType;

/**
 * @author ATracer (based on Mr.Poke ArmorMasteryModifier)
 */
public class StatArmorMasteryFunction extends StatRateFunction {

	private final ArmorType armorType;

	public StatArmorMasteryFunction(ArmorType armorType, StatEnum name, int value, boolean bonus) {
		super(name, value, bonus);
		this.armorType = armorType;
	}

	@Override
	public void apply(Stat2 stat) {
		Player player = (Player) stat.getOwner();
		if (player.getEquipment().isArmorEquipped(armorType))
			super.apply(stat);
	}
}