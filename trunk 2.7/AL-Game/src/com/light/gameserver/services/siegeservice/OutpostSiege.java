package com.light.gameserver.services.siegeservice;

import java.util.Collections;
import java.util.Map;

import com.light.gameserver.model.Race;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.model.siege.OutpostLocation;
import com.light.gameserver.model.siege.SiegeRace;
import com.light.gameserver.network.aion.AionServerPacket;
import com.light.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.light.gameserver.services.SiegeService;
import com.light.gameserver.services.player.PlayerService;
import com.light.gameserver.utils.PacketSendUtility;
import com.light.gameserver.world.World;
import com.light.gameserver.world.knownlist.Visitor;

public class OutpostSiege extends Siege<OutpostLocation> {

	/**
	 * TODO: This should be removed
	 */
	@Deprecated
	public static final String SIEGE_BOSS_AI_NAME = "siege_raceprotector";

	public OutpostSiege(OutpostLocation siegeLocation) {
		super(siegeLocation);
	}

	@Override
	protected void onSiegeStart() {
		SiegeService.getInstance().deSpawnNpcs(getSiegeLocationId());
		SiegeService.getInstance().deSpawnProtectors(getSiegeLocationId());

		getSiegeLocation().setVulnerable(true);

		SiegeService.getInstance().spawnProtectors(getSiegeLocationId(), getSiegeLocation().getRace());
		initSiegeBoss(Collections.singleton(SIEGE_BOSS_AI_NAME));

		// TODO: Refactor me
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(getSiegeLocationId() == 2111 ? 1400317 : 1400318));
			}
		});

		broadcastUpdate(getSiegeLocation());
	}

	@Override
	protected void onSiegeFinish() {
		getSiegeLocation().setVulnerable(false);
		unregisterSiegeBossListeners();

		// TODO: Refactor messages
		if (isBossKilled()) {

			SiegeRaceCounter winnerCounter = getSiegeCounter().getWinnerRaceCounter();
			Map<Integer, Long> topPlayerDamages = winnerCounter.getPlayerDamageCounter();
			if (!topPlayerDamages.isEmpty()) {

				// prepare top player
				Integer topPlayer = topPlayerDamages.keySet().iterator().next();
				final String topPlayerName = PlayerService.getPlayerName(topPlayer);
				// Prepare message for sending to all players
				int messageId = getSiegeLocationId() == 2111 ? 1400324 : 1400323;
				Race race = winnerCounter.getSiegeRace() == SiegeRace.ELYOS ? Race.ELYOS : Race.ASMODIANS;
				final AionServerPacket asp = new SM_SYSTEM_MESSAGE(messageId, race, topPlayerName);

				// send packet for all players
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, asp);
					}
				});
			}
		}
		else {
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(Player player) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(getSiegeLocationId() == 2111 ? 1400319 : 1400320));
				}
			});
		}

		broadcastUpdate(getSiegeLocation());
	}

	/**
	 * Returns 2 hours (7200 seconds)
	 * 
	 * @return 7200 seconds
	 */
	@Override
	public int getDurationInSeconds() {
		return 60 * 60 * 2;
	}

	@Override
	public boolean isEndless() {
		return false;
	}
}