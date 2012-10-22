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
package com.light.gameserver.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.light.gameserver.ai2.poll.AIQuestion;
import com.light.gameserver.dataholders.DataManager;
import com.light.gameserver.dataholders.NpcShoutData;
import com.light.gameserver.model.gameobjects.AionObject;
import com.light.gameserver.model.gameobjects.Creature;
import com.light.gameserver.model.gameobjects.Item;
import com.light.gameserver.model.gameobjects.Npc;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.model.templates.npcshout.NpcShout;
import com.light.gameserver.model.templates.npcshout.ShoutEventType;
import com.light.gameserver.utils.ThreadPoolManager;
import com.light.gameserver.world.World;

/**
 * This class is handling NPC shouts
 * 
 * @author Rolandas
 */
public class NpcShoutsService {

	private static final Logger log = LoggerFactory.getLogger(NpcShoutsService.class);

	NpcShoutData shoutsCache = DataManager.NPC_SHOUT_DATA;

	private NpcShoutsService() {
		for (Npc npc : World.getInstance().getNpcs()) {
			final int npcId = npc.getNpcId();
			final int worldId = npc.getSpawn().getWorldId();
			final int objectId = npc.getObjectId();

			if (!shoutsCache.hasAnyShout(worldId, npcId, ShoutEventType.IDLE))
				continue;

			List<NpcShout> shouts = shoutsCache.getNpcShouts(worldId, npcId, ShoutEventType.IDLE, null, 0);
			final List<NpcShout> finalShouts = new ArrayList<NpcShout>();
			for (NpcShout s : shouts) {
				// shouts with pattern name should be handled from AI
				if (s.getPattern() == null)
					finalShouts.add(s);
			}
			if (finalShouts.size() == 0)
				continue;
			
			int defaultPollDelay = Rnd.get(180, 360) * 1000;
			for (NpcShout shout : finalShouts) {
				if (shout.getPollDelay() != 0 && shout.getPollDelay() < defaultPollDelay)
					defaultPollDelay = shout.getPollDelay();
			}

			ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					AionObject npcObj = World.getInstance().findVisibleObject(objectId);
					if (npcObj != null && npcObj instanceof Npc) {
						Npc npc2 = (Npc) npcObj;
						// check if AI overrides
						if (!npc2.getAi2().poll(AIQuestion.CAN_SHOUT))
							return;
						int randomShout = Rnd.get(finalShouts.size());
						NpcShout shout = finalShouts.get(randomShout);
						Iterator<Player> iter = npc2.getKnownList().getKnownPlayers().values().iterator();
						while (iter.hasNext()) {
							Player kObj = iter.next();
							if (kObj.getLifeStats().isAlreadyDead())
								return;
							shout(npc2, kObj, shout, 0);
						}
					}
				}
			}, 0, defaultPollDelay);
		}
	}

	public void shout(Npc owner, Creature target, List<NpcShout> shouts, int delaySeconds, boolean isSequence) {
		if (owner == null || shouts == null)
			return;
		if (shouts.size() > 1) {
			if (isSequence) {
				int nextDelay = 5;
				for (NpcShout shout : shouts) {
					if (delaySeconds == -1) {
						shout(owner, target, shout, nextDelay);
						nextDelay += 5;
					}
					else {
						shout(owner, target, shout, delaySeconds);
						delaySeconds = -1;
					}
				}
			}
			else {
				int randomShout = Rnd.get(shouts.size());
				shout(owner, target, shouts.get(randomShout), delaySeconds);
			}
		}
		else if (shouts.size() == 1)
			shout(owner, target, shouts.get(0), delaySeconds);
	}

	public void shout(Npc owner, Creature target, NpcShout shout, int delaySeconds) {
		if (owner == null || shout == null)
			return;

		Object param = shout.getParam();

		if (target instanceof Player) {
			Player player = (Player) target;
			if ("username".equals(param))
				param = player.getName();
			else if ("userclass".equals(param))
				param = (240000 + player.getCommonData().getPlayerClass().getClassId()) * 2 + 1;
			else if ("usernation".equals(param)) {
				log.warn("Shout with param 'usernation' is not supported");
				return;
			}
			else if ("usergender".equals(param))
				param = (902012 + player.getCommonData().getGender().getGenderId()) * 2 + 1;
			else if ("mainslotitem".equals(param)) {
				Item weapon = player.getEquipment().getMainHandWeapon();
				if (weapon == null)
					return;
				param = weapon.getItemTemplate().getNameId();
			}
			else if ("quest".equals(shout.getPattern())) {
				delaySeconds = 0;
			}
		}

		if ("target".equals(param) && target != null) {
			param = target.getObjectTemplate().getName();
		}

		owner.shout(shout, target, param, delaySeconds);
	}

	public static final NpcShoutsService getInstance() {
		return SingletonHolder.instance;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final NpcShoutsService instance = new NpcShoutsService();
	}

}
