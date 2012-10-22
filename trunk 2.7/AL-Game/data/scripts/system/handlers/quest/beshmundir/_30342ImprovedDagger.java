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
package quest.beshmundir;

import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.questEngine.handlers.QuestHandler;
import com.light.gameserver.questEngine.model.QuestDialog;
import com.light.gameserver.questEngine.model.QuestEnv;
import com.light.gameserver.questEngine.model.QuestState;
import com.light.gameserver.questEngine.model.QuestStatus;
import com.light.gameserver.services.QuestService;

/**
 * @author vlog
 */
public class _30342ImprovedDagger extends QuestHandler {

	private final static int questId = 30342;

	public _30342ImprovedDagger() {
		super(questId);
	}

	@Override
	public void register() {
		int[] debilkarims = { 286904, 281419, 215795 };
		qe.registerQuestNpc(799336).addOnQuestStart(questId);
		qe.registerQuestNpc(799336).addOnTalkEvent(questId);
		qe.registerGetingItem(182209740, questId);
		for (int debilkarim : debilkarims) {
			qe.registerQuestNpc(debilkarim).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 799336) { // Tataka
				if (player.getInventory().getItemCountByItemId(100200844) >= 1) { // Noble Siel's Supreme Dagger
					if (dialog == QuestDialog.START_DIALOG) {
						return sendQuestDialog(env, 4762);
					}
					else {
						return sendQuestStartDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799336) { // Tataka
				if (dialog == QuestDialog.USE_OBJECT) {
					if (player.getInventory().getItemCountByItemId(182209740) > 0) {
						return sendQuestDialog(env, 10002);
					}
				}
				else {
					removeQuestItem(env, 182209740, 1);
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 286904:
				case 281419:
				case 215795: {
					if (QuestService.collectItemCheck(env, true)) {
						return giveQuestItem(env, 182209740, 1);
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean onGetItemEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			changeQuestStep(env, 0, 0, true); // reward
			return true;
		}
		return false;
	}
}
