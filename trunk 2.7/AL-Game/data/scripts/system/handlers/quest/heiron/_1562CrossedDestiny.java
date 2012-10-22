/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.heiron;

import com.light.gameserver.model.gameobjects.Npc;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.questEngine.handlers.QuestHandler;
import com.light.gameserver.questEngine.model.QuestDialog;
import com.light.gameserver.questEngine.model.QuestEnv;
import com.light.gameserver.questEngine.model.QuestState;
import com.light.gameserver.questEngine.model.QuestStatus;
import com.light.gameserver.services.QuestService;

/**
 * Find Litonos (204616) (bring him the Berone's Necklace (182201780)). Talk with Litonos. Take Litonos to Berone
 * (204589). Talk with Berone.
 * 
 * @author Balthazar
 * @reworked vlog
 */

public class _1562CrossedDestiny extends QuestHandler {

	private final static int questId = 1562;

	public _1562CrossedDestiny() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204589).addOnQuestStart(questId);
		qe.registerQuestNpc(204589).addOnTalkEvent(questId);
		qe.registerQuestNpc(204616).addOnTalkEvent(questId);
		qe.registerQuestNpc(204616).addOnLostTargetEvent(questId);
		qe.registerQuestNpc(204616).addOnReachTargetEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204589) { // Berone
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 4762);
				if (env.getDialogId() == 1007)
					return sendQuestDialog(env, 4);
				if (env.getDialogId() == 1002)
					return sendQuestDialog(env, 1003);
				if (env.getDialogId() == 1003)
					return sendQuestDialog(env, 1004);
				if (env.getDialogId() == 1008)
					if (QuestService.startQuest(env))
						return defaultCloseDialog(env, 0, 1, false, false, 182201780, 1, 0, 0); // 1
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 204616: { // Litonos
					switch (env.getDialog()) {
						case START_DIALOG:
							if (qs.getQuestVarById(0) == 1 && player.getInventory().getItemCountByItemId(182201780) == 1)
								return sendQuestDialog(env, 1352);
							else
								return sendQuestDialog(env, 1438);
						case FINISH_DIALOG:
							return defaultCloseDialog(env, 0, 0);
						case STEP_TO_2:
							if (qs.getQuestVarById(0) == 1) {
								defaultStartFollowEvent(env, 204589, 0, 0);
								return defaultCloseDialog(env, 1, 2, false, false, 0, 0, 182201780, 1); // 2
							}
						case USE_OBJECT:
							if (qs.getQuestVarById(0) == 1) {
								return defaultStartFollowEvent(env, 204589, 1, 2);
							}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204589) { // Berone
				if (env.getDialogId() == 1009)
					return sendQuestDialog(env, 10002);
				else
					return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onNpcReachTargetEvent(QuestEnv env) {
		return defaultFollowEndEvent(env, 2, 2, true); // reward
	}

	@Override
	public boolean onNpcLostTargetEvent(QuestEnv env) {
		return defaultFollowEndEvent(env, 2, 1, false); // 1
	}
}