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
package com.light.gameserver.ai2.handler;

import com.light.gameserver.ai2.AIState;
import com.light.gameserver.ai2.AbstractAI;
import com.light.gameserver.ai2.NpcAI2;
import com.light.gameserver.ai2.event.AIEventType;
import com.light.gameserver.ai2.manager.EmoteManager;
import com.light.gameserver.model.gameobjects.Creature;
import com.light.gameserver.model.gameobjects.VisibleObject;
import com.light.gameserver.utils.MathUtil;

/**
 * @author ATracer
 */
public class FollowEventHandler {

	/**
	 * @param npcAI
	 * @param creature
	 */
	public static void follow(NpcAI2 npcAI, Creature creature) {
		if (npcAI.setStateIfNot(AIState.FOLLOWING)) {
			npcAI.getOwner().setTarget(creature);
			EmoteManager.emoteStartFollowing(npcAI.getOwner());
		}
	}

	/**
	 * @param npcAI
	 * @param creature
	 */
	public static void creatureMoved(NpcAI2 npcAI, Creature creature) {
		if (npcAI.isInState(AIState.FOLLOWING)) {
			if (npcAI.getOwner().isTargeting(creature.getObjectId()) && !creature.getLifeStats().isAlreadyDead()) {
				checkFollowTarget(npcAI, creature);
			}
		}
	}

	/**
	 * @param creature
	 */
	public static void checkFollowTarget(NpcAI2 npcAI, Creature creature) {
		if (!isInRange(npcAI, creature)) {
			npcAI.onGeneralEvent(AIEventType.TARGET_TOOFAR);
		}
	}

	public static boolean isInRange(AbstractAI ai, VisibleObject object) {
		if (object == null) {
			return false;
		}
		return MathUtil.isIn3dRange(ai.getOwner(), object, 2);
	}

	/**
	 * @param npcAI
	 * @param creature
	 */
	public static void stopFollow(NpcAI2 npcAI, Creature creature) {
		if (npcAI.setStateIfNot(AIState.IDLE)) {
			npcAI.getOwner().setTarget(null);
			npcAI.getOwner().getMoveController().abortMove();
			npcAI.getOwner().getController().scheduleRespawn();
			npcAI.getOwner().getController().onDelete();
		}
	}
}