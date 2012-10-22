package quest.eltnen;

import com.light.gameserver.model.gameobjects.Item;
import com.light.gameserver.model.gameobjects.Npc;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.questEngine.handlers.HandlerResult;
import com.light.gameserver.questEngine.handlers.QuestHandler;
import com.light.gameserver.questEngine.model.QuestDialog;
import com.light.gameserver.questEngine.model.QuestEnv;
import com.light.gameserver.questEngine.model.QuestState;
import com.light.gameserver.questEngine.model.QuestStatus;
import com.light.gameserver.services.QuestService;
import com.light.gameserver.world.zone.ZoneName;

/**
 * @author Ritsu
 *
 */
public class _1373WaterTherapy extends QuestHandler
{

	private final static int   questId   = 1373;

	public _1373WaterTherapy()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.registerQuestNpc(203949).addOnQuestStart(questId); //Aerope
		qe.registerQuestNpc(203949).addOnTalkEvent(questId);
		qe.registerQuestItem(182201372, questId);
		qe.registerOnQuestTimerEnd(questId);
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) 
		{
			if (player.isInsideZone(ZoneName.LF2_ITEMUSEAREA_Q1373)) 
			{
				removeQuestItem(env, 182201372, 1);
				useQuestItem(env, item, 0, 2, false, 182201373, 1, 0);
				QuestService.questTimerStart(env, 180);
				return HandlerResult.SUCCESS;
			}
		}
		return HandlerResult.FAILED;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		QuestDialog dialog = env.getDialog();
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE)
		{
			if(targetId == 203949) //Aerope
			{
				if (dialog == QuestDialog.START_DIALOG)
					return sendQuestDialog(env, 1011);
				else if (dialog == QuestDialog.ACCEPT_QUEST)
				{
					if (!giveQuestItem(env, 182201372, 1))
						return true;
					return sendQuestStartDialog(env);
				}
				else
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 203949:
				{
					if (qs.getQuestVarById(0) == 2)
					{
						if (dialog == QuestDialog.START_DIALOG)
							return sendQuestDialog(env, 2375);
						else if (dialog == QuestDialog.CHECK_COLLECTED_ITEMS)
						{							
							if (player.getInventory().getItemCountByItemId(182201373) == 1)
							{
								QuestService.questTimerEnd(env);
								return checkQuestItems(env, 2, 3, true, 5, 2716);
							}
						}
						else
							return sendQuestEndDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 203949)
				return sendQuestEndDialog(env);
		}
		return false;
	}
	
	@Override
	public boolean onQuestTimerEndEvent(QuestEnv env) 
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START)
		{
			removeQuestItem(env, 182201373, 1);
			QuestService.abandonQuest(player, questId);
			player.getController().updateNearbyQuests();
			return true;
		}
		return false;
	}
}