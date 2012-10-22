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

import com.light.gameserver.ai2.AI2Logger;
import com.light.gameserver.ai2.AIState;
import com.light.gameserver.ai2.NpcAI2;
import com.light.gameserver.ai2.event.AIEventType;
import com.light.gameserver.ai2.manager.AttackManager;
import com.light.gameserver.ai2.manager.FollowManager;
import com.light.gameserver.ai2.manager.WalkManager;
import com.light.gameserver.model.gameobjects.Creature;
import com.light.gameserver.model.gameobjects.VisibleObject;

/**
 * @author ATracer
 */
public class TargetEventHandler {

	/**
	 * @param npcAI
	 */
	public static void onTargetReached(NpcAI2 npcAI) {
		if (npcAI.isLogging()) {
			AI2Logger.info(npcAI, "onTargetReached");
		}

		AIState currentState = npcAI.getState();
		switch (currentState) {
			case FIGHT:
				npcAI.getOwner().getMoveController().abortMove();
				AttackManager.scheduleNextAttack(npcAI);
				break;
			case RETURNING:
				npcAI.getOwner().getMoveController().abortMove();
				npcAI.onGeneralEvent(AIEventType.BACK_HOME);
				break;
			case WALKING:
				WalkManager.targetReached(npcAI);
				break;
			case FOLLOWING:
				npcAI.getOwner().getMoveController().abortMove();
				break;
			case FEAR://TODO remove this state
				npcAI.getOwner().getMoveController().abortMove();
				break;
		}
	}

	/**
	 * @param npcAI
	 */
	public static void onTargetTooFar(NpcAI2 npcAI) {
		if (npcAI.isLogging()) {
			AI2Logger.info(npcAI, "onTargetTooFar");
		}
		switch (npcAI.getState()) {
			case FIGHT:
				AttackManager.targetTooFar(npcAI);
				break;
			case FOLLOWING:
				FollowManager.targetTooFar(npcAI);
				break;
			case FEAR:
				break;
			default:
				if (npcAI.isLogging()) {
					AI2Logger.info(npcAI, "default onTargetTooFar");
				}
		}
	}

	/**
	 * @param npcAI
	 */
	public static void onTargetGiveup(NpcAI2 npcAI) {
		if (npcAI.isLogging()) {
			AI2Logger.info(npcAI, "onTargetGiveup");
		}
		VisibleObject target = npcAI.getOwner().getTarget();
		if (target != null) {
			npcAI.getOwner().getAggroList().stopHating(target);
		}
		if (npcAI.isMoveSupported()) {
			npcAI.getOwner().getMoveController().abortMove();
		}
		npcAI.think();
	}

	/**
	 * @param npcAI
	 */
	public static void onTargetChange(NpcAI2 npcAI, Creature creature) {
		if (npcAI.isLogging()) {
			AI2Logger.info(npcAI, "onTargetChange");
		}
		if (npcAI.isInState(AIState.FIGHT)) {
			npcAI.getOwner().setTarget(creature);
			AttackManager.scheduleNextAttack(npcAI);
		}
	}
}