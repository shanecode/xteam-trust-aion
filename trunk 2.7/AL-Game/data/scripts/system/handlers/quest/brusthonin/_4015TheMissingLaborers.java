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
package quest.brusthonin;

import com.light.gameserver.model.gameobjects.Npc;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.questEngine.handlers.QuestHandler;
import com.light.gameserver.questEngine.model.QuestDialog;
import com.light.gameserver.questEngine.model.QuestEnv;
import com.light.gameserver.questEngine.model.QuestState;
import com.light.gameserver.questEngine.model.QuestStatus;

/**
 * @author Mr. Poke remod by Nephis and quest helper team
 */
public class _4015TheMissingLaborers extends QuestHandler {

	private final static int questId = 4015;

	public _4015TheMissingLaborers() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(205130).addOnQuestStart(questId);
		qe.registerQuestNpc(205130).addOnTalkEvent(questId);
		qe.registerQuestNpc(730107).addOnTalkEvent(questId);
		qe.registerQuestNpc(205130).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			if (targetId == 205130) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 730107: {
					if (qs.getQuestVarById(0) == 0 && env.getDialog() == QuestDialog.USE_OBJECT) {
						return useQuestObject(env, 0, 1, false, false); // 1
					}
				}
				case 205130: {
					if (qs.getQuestVarById(0) == 1) {
						if (env.getDialog() == QuestDialog.START_DIALOG)
							return sendQuestDialog(env, 2375);
						else if (env.getDialogId() == 1009) {
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestEndDialog(env);
						}
						else
							return sendQuestEndDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 205130)
				return sendQuestEndDialog(env);
		}
		return false;
	}
}