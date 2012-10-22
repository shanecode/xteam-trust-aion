/*
 * This file is part of aion-unique <aion-unique.com>.
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
package com.light.gameserver.model.templates.npc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import com.light.gameserver.model.gameobjects.state.CreatureSeeState;

/**
 * @author ATracer
 */
@XmlType(name = "rating")
@XmlEnum
public enum NpcRating {
	JUNK(CreatureSeeState.NORMAL),
	NORMAL(CreatureSeeState.NORMAL),
	ELITE(CreatureSeeState.SEARCH1),
	HERO(CreatureSeeState.SEARCH2),
	LEGENDARY(CreatureSeeState.SEARCH2);

	private final CreatureSeeState congenitalSeeState;

	private NpcRating(CreatureSeeState congenitalSeeState) {
		this.congenitalSeeState = congenitalSeeState;
	}

	public CreatureSeeState getCongenitalSeeState() {
		return congenitalSeeState;
	}
}
