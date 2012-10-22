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
package com.light.gameserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.light.gameserver.configs.main.CustomConfig;
import com.light.gameserver.dataholders.DataManager;
import com.light.gameserver.model.gameobjects.Creature;
import com.light.gameserver.model.gameobjects.Npc;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.light.gameserver.model.items.storage.StorageType;
import com.light.gameserver.model.templates.CubeExpandTemplate;
import com.light.gameserver.network.aion.serverpackets.SM_CUBE_UPDATE;
import com.light.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.light.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.light.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 * @author Simple
 * @reworked Luzien
 */
public class CubeExpandService {

	private static final Logger log = LoggerFactory.getLogger(CubeExpandService.class);

	private static final int MIN_EXPAND = 0;
	private static final int MAX_EXPAND = CustomConfig.BASIC_CUBE_SIZE_LIMIT;

	/**
	 * Shows Question window and expands on positive response
	 * 
	 * @param player
	 * @param npc
	 */
	public static void expandCube(final Player player, Npc npc) {
		final CubeExpandTemplate expandTemplate = DataManager.CUBEEXPANDER_DATA.getCubeExpandListTemplate(npc.getNpcId());

		if (expandTemplate == null) {
			log.error("Cube Expand Template could not be found for Npc ID: " + npc.getObjectId());
			return;
		}
	
		if (npcCanExpandLevel(expandTemplate, player.getNpcExpands() + 1) && canExpand(player)) {
			/**
			 * Check if player is allowed to expand by buying
			 */
			if (player.getNpcExpands() >= CustomConfig.NPC_CUBE_EXPANDS_SIZE_LIMIT) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_EXTEND_INVENTORY_CANT_EXTEND_MORE);
				return;
			}
			/**
			 * Check if our player can pay the cubic expand price
			 */
			final int price = getPriceByLevel(expandTemplate, player.getNpcExpands() + 1);

			RequestResponseHandler responseHandler = new RequestResponseHandler(npc) {

				@Override
				public void acceptRequest(Creature requester, Player responder) {
					if (price > player.getInventory().getKinah()) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_WAREHOUSE_EXPAND_NOT_ENOUGH_MONEY);
						return;
					}
					expand(responder, true);
					player.getInventory().decreaseKinah(price);
				}

				@Override
				public void denyRequest(Creature requester, Player responder) {
					// nothing to do
				}
			};

			boolean result = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_WAREHOUSE_EXPAND_WARNING,
				responseHandler);
			if (result) {
				PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_WAREHOUSE_EXPAND_WARNING, 0,
					String.valueOf(price)));
			}
		}
		else
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300430));
	}

	/**
	 * Expands the cubes
	 * 
	 * @param player
	 * @param isNpcExpand TODO
	 */
	public static void expand(Player player, boolean isNpcExpand) {
		if (!canExpand(player))
			return;
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300431, "9")); // 9 Slots added
		if(isNpcExpand)
			player.setNpcExpands(player.getNpcExpands() + 1);
		else
			player.setQuestExpands(player.getQuestExpands() + 1);
		PacketSendUtility.sendPacket(player, SM_CUBE_UPDATE.cubeSize(StorageType.CUBE, player));
	}

	/**
	 * @param player
	 * @return
	 */
	public static boolean canExpand(Player player) {
		return validateNewSize(player.getNpcExpands() + player.getQuestExpands() + 1);
	}

	/**
	 * Checks if new player cube is not max
	 * 
	 * @param level
	 * @return true or false
	 */
	private static boolean validateNewSize(int level) {
		// check min and max level
		if (level < MIN_EXPAND || level > MAX_EXPAND)
			return false;
		return true;
	}

	/**
	 * Checks if npc can expand level
	 * 
	 * @param clist
	 * @param level
	 * @return true or false
	 */
	private static boolean npcCanExpandLevel(CubeExpandTemplate clist, int level) {
		// check if level exists in template
		if (!clist.contains(level))
			return false;
		return true;
	}

	/**
	 * The guy who created cube template should blame himself :) One day I will rewrite them
	 * 
	 * @param clist
	 * @param level
	 * @return
	 */
	private static int getPriceByLevel(CubeExpandTemplate clist, int level) {
		return clist.get(level).getPrice();
	}
}