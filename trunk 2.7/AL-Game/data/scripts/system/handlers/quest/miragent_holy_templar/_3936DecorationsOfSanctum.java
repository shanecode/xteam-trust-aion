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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique. If not, see <http://www.gnu.org/licenses/>.
 */
package quest.miragent_holy_templar;

import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.questEngine.handlers.QuestHandler;
import com.light.gameserver.questEngine.model.QuestDialog;
import com.light.gameserver.questEngine.model.QuestEnv;
import com.light.gameserver.questEngine.model.QuestState;
import com.light.gameserver.questEngine.model.QuestStatus;

/**
 * @author Nanou
 * @modified Gigi
 */
public class _3936DecorationsOfSanctum extends QuestHandler {

	private final static int questId = 3936;

	public _3936DecorationsOfSanctum() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203710).addOnQuestStart(questId);// Dairos
		qe.registerQuestNpc(203710).addOnTalkEvent(questId);// Dairos
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();

		// Start to Dairos
		if (qs == null || (qs.getStatus() == QuestStatus.NONE || qs.getStatus() == QuestStatus.COMPLETE)) {
			if (targetId == 203710) {
				if (dialog == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 4762);
				else
					return sendQuestStartDialog(env);
			}
		}

		if (qs == null)
			return false;
			
		int var = qs.getQuestVarById(0);

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				// 1 - Report the result to Dairos.
				case 203710:
					switch (dialog) {
						case START_DIALOG:
							if (var == 0)
								return sendQuestDialog(env, 1011);
							else if (var == 1)
								return sendQuestDialog(env, 1352);
						case STEP_TO_1:
							return defaultCloseDialog(env, 0, 1); // 1
						case CHECK_COLLECTED_ITEMS:
							long itemCount1 = player.getInventory().getItemCountByItemId(182206091);
							long itemCount2 = player.getInventory().getItemCountByItemId(182206092);
							long itemCount3 = player.getInventory().getItemCountByItemId(182206093);
							long itemCount4 = player.getInventory().getItemCountByItemId(182206094);
							if (itemCount1 >= 10 && itemCount2 >= 10 && itemCount3 >= 10 && itemCount4 >= 10) {
								removeQuestItem(env, 182206091, 10);
								removeQuestItem(env, 182206092, 10);
								removeQuestItem(env, 182206093, 10);
								removeQuestItem(env, 182206094, 10);
								changeQuestStep(env, 1, 1, true);
								return sendQuestDialog(env, 5);
							}
							else
								return sendQuestDialog(env, 10001);
					}
					break;
				// No match
				default:
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203710)
				return sendQuestEndDialog(env);
		}
		return false;
	}
}
