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
import com.light.gameserver.ai2.NpcAI2;
import com.light.gameserver.ai2.manager.WalkManager;
import com.light.gameserver.model.gameobjects.Npc;

/**
 * @author ATracer
 */
public class ActivateEventHandler {

	public static void onActivate(NpcAI2 npcAI) {
		if (npcAI.isInState(AIState.IDLE)) {
			npcAI.getOwner().updateKnownlist();
			npcAI.think();
		}
	}

	public static void onDeactivate(NpcAI2 npcAI) {
		if (npcAI.isInState(AIState.WALKING)) {
			WalkManager.stopWalking(npcAI);
		}
		npcAI.think();
		Npc npc = npcAI.getOwner();
		npc.updateKnownlist();
		npc.getAggroList().clear();
		npc.getEffectController().removeAllEffects();
	}
}