/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 * aion-emu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-emu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.light.gameserver.dao;

import com.aionemu.commons.database.dao.DAO;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.model.gameobjects.player.title.TitleList;
import com.light.gameserver.model.gameobjects.player.title.Title;

/**
 * @author xavier
 */
public abstract class PlayerTitleListDAO implements DAO {

	@Override
	public final String getClassName() {
		return PlayerTitleListDAO.class.getName();
	}

	public abstract TitleList loadTitleList(int playerId);

	public abstract boolean storeTitles(Player player, Title entry);
	
	public abstract boolean removeTitle(int playerId, int titleId);

}
