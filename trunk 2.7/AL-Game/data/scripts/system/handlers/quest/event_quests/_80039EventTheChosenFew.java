/* This file is part of aion-lightning <aion-lightning.com>.
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
package quest.event_quests;

import java.util.List;

import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.model.items.storage.Storage;
import com.light.gameserver.model.templates.quest.QuestItems;
import com.light.gameserver.model.templates.rewards.BonusType;
import com.light.gameserver.questEngine.handlers.HandlerResult;
import com.light.gameserver.questEngine.handlers.QuestHandler;
import com.light.gameserver.questEngine.model.QuestDialog;
import com.light.gameserver.questEngine.model.QuestEnv;
import com.light.gameserver.questEngine.model.QuestState;
import com.light.gameserver.questEngine.model.QuestStatus;
import com.light.gameserver.services.QuestService;

/**
 * @author Rolandas
 */

public class _80039EventTheChosenFew extends QuestHandler {

	private final static int questId = 80039;

	public _80039EventTheChosenFew() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799780).addOnQuestStart(questId);
		qe.registerQuestNpc(799780).addOnTalkEvent(questId);
		qe.registerOnLevelUp(questId);
		qe.registerOnBonusApply(questId, BonusType.LUNAR);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.NONE)
			return false;

		if (qs.getStatus() == QuestStatus.START || qs.getStatus() == QuestStatus.COMPLETE
			&& QuestService.collectItemCheck(env, false)) {
			if (targetId == 799780) {
				if (env.getDialog() == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else if (env.getDialog() == QuestDialog.ACCEPT_QUEST)
					return sendQuestDialog(env, 1003);
				else if (env.getDialog() == QuestDialog.CHECK_COLLECTED_ITEMS)
					return checkQuestItems(env, 0, 0, true, 5, 2716);
				else if (env.getDialog() == QuestDialog.SELECT_NO_REWARD)
					return sendQuestRewardDialog(env, 799780, 5);
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (env.getDialog() == QuestDialog.USE_OBJECT)
				return sendQuestDialog(env, 5);
			return sendQuestEndDialog(env);
		}
		return sendQuestRewardDialog(env, 799780, 0);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		if (env.getQuestId() != questId)
			return false;
		Player player = env.getPlayer();
		Storage storage = player.getInventory();
		if (storage.getItemCountByItemId(164002018) > 0) // if has more restart quest
			QuestService.startEventQuest(new QuestEnv(null, player, questId, 0), QuestStatus.START);
		return false;
	}

	@Override
	public HandlerResult onBonusApplyEvent(QuestEnv env, BonusType bonusType, List<QuestItems> rewardItems) {
		if (bonusType != BonusType.LUNAR || env.getQuestId() != questId)
			return HandlerResult.UNKNOWN;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && (qs.getStatus() == QuestStatus.START || qs.getStatus() == QuestStatus.COMPLETE)) {
			if (qs.getQuestVarById(0) == 0)
				return HandlerResult.SUCCESS;
		}
		return HandlerResult.FAILED;
	}

}