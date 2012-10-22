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

import com.light.gameserver.model.stats.calc.Stat2;
import com.light.gameserver.model.stats.calc.StatOwner;
import com.light.gameserver.model.stats.container.StatEnum;

/**
 * @author ATracer
 */
public interface IStatFunction extends Comparable<IStatFunction>{

	StatEnum getName();

	boolean isBonus();

	int getPriority();

	int getValue();

	boolean validate(Stat2 stat, IStatFunction statFunction);

	void apply(Stat2 stat);
	
	StatOwner getOwner();
}