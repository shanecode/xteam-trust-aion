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

import java.util.Date;
import java.util.concurrent.Future;

import com.aionemu.commons.database.dao.DAOManager;
import com.light.gameserver.configs.main.GSConfig;
import com.light.gameserver.dao.PlayerPunishmentsDAO;
import com.light.gameserver.model.TaskId;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.network.aion.serverpackets.SM_CAPTCHA;
import com.light.gameserver.network.aion.serverpackets.SM_QUIT_RESPONSE;
import com.light.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.light.gameserver.network.chatserver.ChatServer;
import com.light.gameserver.services.teleport.TeleportService;
import com.light.gameserver.utils.PacketSendUtility;
import com.light.gameserver.utils.ThreadPoolManager;
import com.light.gameserver.world.World;
import com.light.gameserver.world.WorldMapType;
import com.light.gameserver.global.additions.MessagerAddition;

/**
 * @author lord_rex, Cura, nrg
 */
public class PunishmentService {
	
	/**
	 * This method will handle unbanning a character
	 * 
	 * @param player
	 * @param state
	 * @param delayInMinutes
	 */
	public static void unbanChar(int playerId) {
		DAOManager.getDAO(PlayerPunishmentsDAO.class).unpunishPlayer(playerId, PunishmentType.CHARBAN);
	}
	
	/**
	 * This method will handle banning a character
	 * 
	 * @param player
	 * @param state
	 * @param delayInMinutes
	 */
	public static void banChar(int playerId, int dayCount, String reason) {
		DAOManager.getDAO(PlayerPunishmentsDAO.class).punishPlayer(playerId, PunishmentType.CHARBAN, calculateDuration(dayCount), reason);
		
		//if player is online - kick him
		Player player = World.getInstance().findPlayer(playerId);
		if(player != null)
		  player.getClientConnection().close(new SM_QUIT_RESPONSE(), false);
	}
	
	/**
	 * Calculates the timestamp when a given number of days is over
	 * 
	 * @param dayCount
	 * @return timeStamp
	 */
	@SuppressWarnings("deprecation")
	public static long calculateDuration(int dayCount) {
		if(dayCount == 0)
			return Integer.MAX_VALUE; //int because client handles this with seconds timestamp in int
		
		//since the current day counts as first day we have to cut one day off
		dayCount -= 1;
		
		Date now = new Date(); 
    Date ban = new Date(); 
    ban.setDate(now.getDate()+dayCount); 
    ban.setHours(23); 
    ban.setMinutes(59); 
    ban.setSeconds(59); 
    return ban.getTime()-System.currentTimeMillis();
	}

	/**
	 * This method will handle moving or removing a player from prison
	 * 
	 * @param player
	 * @param state
	 * @param delayInMinutes
	 */
	public static void setIsInPrison(Player player, boolean state, long delayInMinutes, String reason, String mb) 
	{
		stopPrisonTask(player, false);
		if (state) 
		{
			long prisonTimer = player.getPrisonTimer();
			if (delayInMinutes > 0) 
			{
				prisonTimer = delayInMinutes * 60000L;
				schedulePrisonTask(player, prisonTimer);
				PacketSendUtility.sendMessage(player, "�� ���� ����������� � ������ ��  " + delayInMinutes
					+ " ���.\n���� �� ������� �� ����, ����� ��������� ��������������, � ������������ ����� ���������� ����� � ����.");
			}
			
			if (GSConfig.ENABLE_CHAT_SERVER)
				ChatServer.getInstance().sendPlayerLogout(player);

			player.setStartPrison(System.currentTimeMillis());
			MessagerAddition.announceAll(mb, 0);
			TeleportService.teleportToPrison(player);
			DAOManager.getDAO(PlayerPunishmentsDAO.class).punishPlayer(player, PunishmentType.PRISON, reason);
		}
		else 
		{
			PacketSendUtility.sendMessage(player, "�� ��������� �� ������. ����� ��������� �������.");
			MessagerAddition.announceAll("�������� "+ player.getName() +" ������� �� ������. ����� ��������� �������.", 0);
			
			if (GSConfig.ENABLE_CHAT_SERVER)
				PacketSendUtility.sendMessage(player, "To use global chats again relog!");
				
			player.setPrisonTimer(0);

			TeleportService.moveToBindLocation(player, true);

			DAOManager.getDAO(PlayerPunishmentsDAO.class).unpunishPlayer(player.getObjectId(), PunishmentType.PRISON);
		}
	}

	/**
	 * This method will stop the prison task
	 * 
	 * @param playerObjId
	 */
	public static void stopPrisonTask(Player player, boolean save) {
		Future<?> prisonTask = player.getController().getTask(TaskId.PRISON);
		if (prisonTask != null) {
			if (save) {
				long delay = player.getPrisonTimer();
				if (delay < 0)
					delay = 0;
				player.setPrisonTimer(delay);
			}
			player.getController().cancelTask(TaskId.PRISON);
		}
	}

	/**
	 * This method will update the prison status
	 * 
	 * @param player
	 */
	public static void updatePrisonStatus(final Player player) {
		if (player.isInPrison()) {
			long prisonTimer = player.getPrisonTimer();
			if (prisonTimer > 0) {
				schedulePrisonTask(player, prisonTimer);
				int timeInPrison = Math.round(prisonTimer / 60000);

				if (timeInPrison <= 0)
					timeInPrison = 1;

				PacketSendUtility.sendMessage(player, "You are still in prison for " + timeInPrison + " minute"
					+ (timeInPrison > 1 ? "s" : "") + ".");

				player.setStartPrison(System.currentTimeMillis());
			}
			
			if (player.getWorldId() != WorldMapType.DF_PRISON.getId() && player.getWorldId() != WorldMapType.DE_PRISON.getId()) {
				PacketSendUtility.sendMessage(player, "You will be teleported to prison in one minute!");
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						TeleportService.teleportToPrison(player);
					}
				}, 60000);
			}				
		}
	}

	/**
	 * This method will schedule a prison task
	 * 
	 * @param player
	 * @param prisonTimer
	 */
	private static void schedulePrisonTask(final Player player, long prisonTimer) {
		player.setPrisonTimer(prisonTimer);
		player.getController().addTask(TaskId.PRISON, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				setIsInPrison(player, false, 0, "", "");
			}
		}, prisonTimer));
	}

	/**
	 * This method will handle can or cant gathering
	 * 
	 * @param player
	 * @param captchaCount
	 * @param state
	 * @param delay
	 * @author Cura
	 */
	public static void setIsNotGatherable(Player player, int captchaCount, boolean state, long delay) {
		stopGatherableTask(player, false);

		if (state) {
			if (captchaCount < 3) {
				PacketSendUtility.sendPacket(player, new SM_CAPTCHA(captchaCount + 1, player.getCaptchaImage()));
			}
			else {
				player.setCaptchaWord(null);
				player.setCaptchaImage(null);
			}

			player.setGatherableTimer(delay);
			player.setStopGatherable(System.currentTimeMillis());
			scheduleGatherableTask(player, delay);
			DAOManager.getDAO(PlayerPunishmentsDAO.class).punishPlayer(player, PunishmentType.GATHER, "Possible gatherbot");
		}
		else {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400269));
			player.setCaptchaWord(null);
			player.setCaptchaImage(null);
			player.setGatherableTimer(0);
			player.setStopGatherable(0);
			DAOManager.getDAO(PlayerPunishmentsDAO.class).unpunishPlayer(player.getObjectId(), PunishmentType.GATHER);
		}
	}

	/**
	 * This method will stop the gathering task
	 * 
	 * @param player
	 * @param save
	 * @author Cura
	 */
	public static void stopGatherableTask(Player player, boolean save) {
		Future<?> gatherableTask = player.getController().getTask(TaskId.GATHERABLE);

		if (gatherableTask != null) {
			if (save) {
				long delay = player.getGatherableTimer();
				if (delay < 0)
					delay = 0;
				player.setGatherableTimer(delay);
			}
			player.getController().cancelTask(TaskId.GATHERABLE);
		}
	}

	/**
	 * This method will update the gathering status
	 * 
	 * @param player
	 * @author Cura
	 */
	public static void updateGatherableStatus(Player player) {
		if (player.isNotGatherable()) {
			long gatherableTimer = player.getGatherableTimer();

			if (gatherableTimer > 0) {
				scheduleGatherableTask(player, gatherableTimer);
				player.setStopGatherable(System.currentTimeMillis());
			}
		}
	}

	/**
	 * This method will schedule a gathering task
	 * 
	 * @param player
	 * @param gatherableTimer
	 * @author Cura
	 */
	private static void scheduleGatherableTask(final Player player, long gatherableTimer) {
		player.setGatherableTimer(gatherableTimer);
		player.getController().addTask(TaskId.GATHERABLE, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				setIsNotGatherable(player, 0, false, 0);
			}
		}, gatherableTimer));
	}

	/**
	 * PunishmentType
	 * 
	 * @author Cura
	 */
	public enum PunishmentType {
		PRISON,
		GATHER,
		CHARBAN
	}
}