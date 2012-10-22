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
package com.light.gameserver.dao;

import com.aionemu.commons.database.dao.DAO;
import com.light.gameserver.model.gameobjects.player.Player;

/**
 * @author ATracer
 */
public abstract class PlayerSettingsDAO implements DAO {

	/**
	 * Returns unique identifier for PlayerUiSettingsDAO
	 * 
	 * @return unique identifier for PlayerUiSettingsDAO
	 */
	@Override
	public final String getClassName() {
		return PlayerSettingsDAO.class.getName();
	}

	/**
	 * @param playerId
	 * @param data
	 */
	public abstract void saveSettings(final Player player);

	/**
	 * @param playerId
	 */
	public abstract void loadSettings(final Player player);
}
