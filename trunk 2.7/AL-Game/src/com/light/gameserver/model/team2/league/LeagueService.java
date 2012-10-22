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
package com.light.gameserver.model.team2.league;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.callbacks.util.GlobalCallbackHelper;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.model.team2.alliance.PlayerAlliance;
import com.light.gameserver.model.team2.alliance.callback.PlayerAllianceDisbandCallback;
import com.light.gameserver.model.team2.league.events.LeagueDisbandEvent;
import com.light.gameserver.model.team2.league.events.LeagueEnteredEvent;
import com.light.gameserver.model.team2.league.events.LeagueInvite;
import com.light.gameserver.model.team2.league.events.LeagueLeftEvent;
import com.light.gameserver.model.team2.league.events.LeagueLeftEvent.LeaveReson;
import com.light.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.light.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.light.gameserver.utils.PacketSendUtility;
import com.google.common.base.Preconditions;

/**
 * @author ATracer
 */
public class LeagueService {

	private static final Logger log = LoggerFactory.getLogger(LeagueService.class);

	private static final Map<Integer, League> leagues = new ConcurrentHashMap<Integer, League>();

	static {
		GlobalCallbackHelper.addCallback(new AllianceDisbandListener());
	}

	public static final void inviteToLeague(final Player inviter, final Player invited) {
		if (canInvite(inviter, invited)) {
			LeagueInvite invite = new LeagueInvite(inviter, invited);
			if (invited.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_REQUEST_LEAGUE_INVITE, invite)) {
				PacketSendUtility.sendPacket(inviter,
					SM_SYSTEM_MESSAGE.STR_UNION_INVITE_HIM(invited.getName(), invited.getPlayerAlliance2().size()));
				PacketSendUtility.sendPacket(invited, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_REQUEST_LEAGUE_INVITE, 0,
					inviter.getName()));
			}
		}
	}

	public static final boolean canInvite(Player inviter, Player invited) {
		return inviter.isInAlliance2() && invited.isInAlliance2() && inviter.getPlayerAlliance2().isLeader(inviter);
	}

	public static final League createLeague(Player inviter, Player invited) {
		PlayerAlliance alliance = inviter.getPlayerAlliance2();
		Preconditions.checkNotNull(alliance, "Alliance can not be null");
		League newLeague = new League(new LeagueMember(alliance, 0));
		leagues.put(newLeague.getTeamId(), newLeague);
		addAlliance(newLeague, alliance);
		return newLeague;
	}

	/**
	 * Add alliance to league
	 */
	public static final void addAlliance(League league, PlayerAlliance alliance) {
		Preconditions.checkNotNull(league, "League should not be null");
		league.onEvent(new LeagueEnteredEvent(league, alliance));
	}

	public static final void addAllianceToLeague(League league, PlayerAlliance alliance) {
		league.addMember(new LeagueMember(alliance, league.size()));
	}

	/**
	 * Remove alliance from league (normal leave)
	 */
	public static final void removeAlliance(PlayerAlliance alliance) {
		if (alliance != null) {
			League league = alliance.getLeague();
			Preconditions.checkNotNull(league, "League should not be null");
			league.onEvent(new LeagueLeftEvent(league, alliance));
		}
	}

	/**
	 * Remove alliance from league (expel)
	 */
	public static final void expelAlliance(Player expelledPlayer, Player expelGiver) {
		Preconditions.checkNotNull(expelledPlayer, "Expelled player should not be null");
		Preconditions.checkNotNull(expelGiver, "ExpelGiver player should not be null");
		Preconditions.checkArgument(expelGiver.isInLeague(), "Expelled player should be in league");
		Preconditions.checkArgument(expelledPlayer.isInLeague(), "ExpelGiver should be in league");
		Preconditions.checkArgument(expelGiver.getPlayerAlliance2().getLeague().isLeader(expelGiver.getPlayerAlliance2()),
			"ExpelGiver alliance should be the leader of league");
		Preconditions.checkArgument(expelGiver.getPlayerAlliance2().isLeader(expelGiver),
			"ExpelGiver should be the leader of alliance");
		PlayerAlliance alliance = expelGiver.getPlayerAlliance2();
		League league = alliance.getLeague();
		league.onEvent(new LeagueLeftEvent(league, expelledPlayer.getPlayerAlliance2(), LeaveReson.EXPEL));
	}

	/**
	 * Disband league after minimum of members has been reached
	 */
	public static void disband(League league) {
		Preconditions.checkState(league.onlineMembers() <= 1, "Can't disband league with more than one online member");
		leagues.remove(league.getTeamId());
		league.onEvent(new LeagueDisbandEvent(league));
	}

	static class AllianceDisbandListener extends PlayerAllianceDisbandCallback {

		@Override
		public void onBeforeAllianceDisband(PlayerAlliance alliance) {
		}

		@Override
		public void onAfterAllianceDisband(PlayerAlliance alliance) {
			try {
				for (League league : leagues.values()) {
					if (league.hasMember(alliance.getTeamId())) {
						league.onEvent(new LeagueLeftEvent(league, alliance));
					}
				}
			}
			catch (Throwable t) {
				log.error("Error during alliance disband listen", t);
			}
		}

	}

}