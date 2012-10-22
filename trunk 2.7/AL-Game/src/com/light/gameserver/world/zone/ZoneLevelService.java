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
package com.light.gameserver.world.zone;

import com.light.gameserver.model.TaskId;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.utils.ThreadPoolManager;
import com.light.gameserver.world.World;

/**
 * @author ATracer
 */
public class ZoneLevelService {

	private static final long DROWN_PERIOD = 2000;

	/**
	 * Check water level (start drowning) and map death level (die)
	 */
	public static void checkZoneLevels(Player player) {
		World world = World.getInstance();
		float z = player.getZ();

		if (player.getLifeStats().isAlreadyDead())
			return;

		if (z < world.getWorldMap(player.getWorldId()).getDeathLevel()) {
			player.getController().die();
			return;
		}

		// TODO need fix character height
		float playerheight = player.getPlayerAppearance().getHeight() * 1.6f;
		if (z < world.getWorldMap(player.getWorldId()).getWaterLevel() - playerheight)
			startDrowning(player);
		else
			stopDrowning(player);
	}

	/**
	 * @param player
	 */
	private static void startDrowning(Player player) {
		if (!isDrowning(player))
			scheduleDrowningTask(player);
	}

	/**
	 * @param player
	 */
	private static void stopDrowning(Player player) {
		if (isDrowning(player))
			player.getController().cancelTask(TaskId.DROWN);

	}

	/**
	 * @param player
	 * @return
	 */
	private static boolean isDrowning(Player player) {
		return player.getController().getTask(TaskId.DROWN) == null ? false : true;
	}

	/**
	 * @param player
	 */
	private static void scheduleDrowningTask(final Player player) {
		player.getController().addTask(TaskId.DROWN, ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				int value = Math.round(player.getLifeStats().getMaxHp() / 10);
				// TODO retail emotion, attack_status packets sending
				if (!player.getLifeStats().isAlreadyDead()) {
					if (!player.isInvul()) {
						player.getLifeStats().reduceHp(value, null);
						player.getLifeStats().sendHpPacketUpdate();
					}
				}
				else
					stopDrowning(player);
			}
		}, 0, DROWN_PERIOD));
	}
}