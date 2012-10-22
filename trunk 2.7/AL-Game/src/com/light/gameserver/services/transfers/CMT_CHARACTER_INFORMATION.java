package com.light.gameserver.services.transfers;

import java.util.List;

import javolution.util.FastList;

import org.slf4j.Logger;

import com.aionemu.commons.database.dao.DAOManager;
import com.light.gameserver.configs.main.PlayerTransferConfig;
import com.light.gameserver.dao.InventoryDAO;
import com.light.gameserver.dao.PlayerBindPointDAO;
import com.light.gameserver.dao.PlayerNpcFactionsDAO;
import com.light.gameserver.dao.PlayerTitleListDAO;
import com.light.gameserver.dataholders.DataManager;
import com.light.gameserver.model.Gender;
import com.light.gameserver.model.PlayerClass;
import com.light.gameserver.model.Race;
import com.light.gameserver.model.account.Account;
import com.light.gameserver.model.gameobjects.Item;
import com.light.gameserver.model.gameobjects.PersistentState;
import com.light.gameserver.model.gameobjects.player.AbyssRank;
import com.light.gameserver.model.gameobjects.player.BindPointPosition;
import com.light.gameserver.model.gameobjects.player.MacroList;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.model.gameobjects.player.PlayerAppearance;
import com.light.gameserver.model.gameobjects.player.PlayerCommonData;
import com.light.gameserver.model.gameobjects.player.PlayerSettings;
import com.light.gameserver.model.gameobjects.player.QuestStateList;
import com.light.gameserver.model.gameobjects.player.RecipeList;
import com.light.gameserver.model.gameobjects.player.emotion.EmotionList;
import com.light.gameserver.model.gameobjects.player.motion.Motion;
import com.light.gameserver.model.gameobjects.player.motion.MotionList;
import com.light.gameserver.model.gameobjects.player.npcFaction.ENpcFactionQuestState;
import com.light.gameserver.model.gameobjects.player.npcFaction.NpcFaction;
import com.light.gameserver.model.gameobjects.player.npcFaction.NpcFactions;
import com.light.gameserver.model.gameobjects.player.title.Title;
import com.light.gameserver.model.gameobjects.player.title.TitleList;
import com.light.gameserver.model.skill.PlayerSkillList;
import com.light.gameserver.model.templates.item.ItemTemplate;
import com.light.gameserver.network.aion.AionClientPacket;
import com.light.gameserver.network.aion.AionConnection.State;
import com.light.gameserver.questEngine.model.QuestState;
import com.light.gameserver.questEngine.model.QuestStatus;
import com.light.gameserver.services.AccountService;
import com.light.gameserver.services.item.ItemSocketService;
import com.light.gameserver.services.player.PlayerService;
import com.light.gameserver.skillengine.model.SkillTemplate;
import com.light.gameserver.utils.idfactory.IDFactory;

/**
 * @author KID
 */
public class CMT_CHARACTER_INFORMATION extends AionClientPacket {

	protected CMT_CHARACTER_INFORMATION(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() { }

	@Override
	protected void runImpl() { }

	public Player readInfo(String name, int targetAccount, String accountName, List<Integer> rsList, Logger textLog) {
		long st = System.currentTimeMillis();
		PlayerCommonData playerCommonData = new PlayerCommonData(IDFactory.getInstance().nextId());
		playerCommonData.setName(name);
		playerCommonData.setExp(readQ());
		playerCommonData.setPlayerClass(PlayerClass.getPlayerClassById((byte) readD()));
		playerCommonData.setRace(readD() == 0 ? Race.ELYOS : Race.ASMODIANS);
		playerCommonData.setGender(readD() == 0 ? Gender.MALE : Gender.FEMALE);
		playerCommonData.setTitleId(readD());
		playerCommonData.setDp(readD());
		playerCommonData.setQuestExpands(readD());
		playerCommonData.setNpcExpands(readD());
		playerCommonData.setAdvencedStigmaSlotSize(readD());
		playerCommonData.setWarehouseSize(readD());
		
		PlayerAppearance playerAppearance = new PlayerAppearance();
		playerAppearance.setSkinRGB(readD());
		playerAppearance.setHairRGB(readD());
		playerAppearance.setEyeRGB(readD());
		playerAppearance.setLipRGB(readD());
		playerAppearance.setFace(readC());
		playerAppearance.setHair(readC());
		playerAppearance.setDeco(readC());
		playerAppearance.setTattoo(readC());
		playerAppearance.setFaceContour(readC());
		playerAppearance.setExpression(readC());
		playerAppearance.setJawLine(readC());
		playerAppearance.setForehead(readC());
		playerAppearance.setEyeHeight(readC());
		playerAppearance.setEyeSpace(readC());
		playerAppearance.setEyeWidth(readC());
		playerAppearance.setEyeSize(readC());
		playerAppearance.setEyeShape(readC());
		playerAppearance.setEyeAngle(readC());
		playerAppearance.setBrowHeight(readC());
		playerAppearance.setBrowAngle(readC());
		playerAppearance.setBrowShape(readC());
		playerAppearance.setNose(readC());
		playerAppearance.setNoseBridge(readC());
		playerAppearance.setNoseWidth(readC());
		playerAppearance.setNoseTip(readC());
		playerAppearance.setCheek(readC());
		playerAppearance.setLipHeight(readC());
		playerAppearance.setMouthSize(readC());
		playerAppearance.setLipSize(readC());
		playerAppearance.setSmile(readC());
		playerAppearance.setLipShape(readC());
		playerAppearance.setJawHeigh(readC());
		playerAppearance.setChinJut(readC());
		playerAppearance.setEarShape(readC());
		playerAppearance.setHeadSize(readC());
		playerAppearance.setNeck(readC());
		playerAppearance.setNeckLength(readC());
		playerAppearance.setShoulderSize(readC());
		playerAppearance.setTorso(readC());
		playerAppearance.setChest(readC());
		playerAppearance.setWaist(readC());
		playerAppearance.setHips(readC());
		playerAppearance.setArmThickness(readC());
		playerAppearance.setHandSize(readC());
		playerAppearance.setLegThicnkess(readC());
		playerAppearance.setFootSize(readC());
		playerAppearance.setFacialRate(readC());
		playerAppearance.setArmLength(readC());
		playerAppearance.setLegLength(readC());
		playerAppearance.setShoulders(readC());
		playerAppearance.setFaceShape(readC());
		playerAppearance.setVoice(readC());
		playerAppearance.setHeight(readF());
		
		Account account = AccountService.loadAccount(targetAccount);
		account.setName(accountName);
		Player player = PlayerService.newPlayer(playerCommonData, playerAppearance, account);
		player.getPosition().setXYZH(readF(), readF(), readF(), readSC());
		player.getPosition().setMapId(readD());
		
		if (!PlayerService.storeNewPlayer(player, accountName, targetAccount)) {
			textLog.info("failed to store new player to "+accountName);
			IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
			return null;
		}
				
		int cnt = readD();
		FastList<String> itemOut = FastList.newInstance();
		for(int a = 0; a < cnt; a++) { //inventory
			int objIdOld = readD();
			int itemId = readD();
			long itemCnt = readQ();
			int itemColor = readD();
			String itemCreator = readS();
			int itemExpireTime = readD();
			int itemActivationCnt = readD();
			boolean itemEquipped = readD() == 1;
			boolean itemSoulBound = readD() == 1;
			int equipSlot = readD();
			int location = readD();
			int enchant = readD();
			int skinId = readD();
			int fusionId = readD();
			int optSocket = readD();
			int optFusion = readD();
			int charge = readD();
			FastList<int[]> manastones = FastList.newInstance(), fusions = FastList.newInstance();
			int len = readD();
			for(int b = 0; b < len; b++) {
				manastones.add(new int[] { readD(), readD() });
			}
			len = readD();
			for(int b = 0; b < len; b++) {
				fusions.add(new int[] { readD(), readD() });
			}
			
			int godstone = 0;
			if(readC() == 1)
				godstone = readD();
			if(PlayerTransferConfig.ALLOW_INV) {
				ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(itemId);
				if(template == null) {
					textLog.info("(cube"+targetAccount+")item with id "+itemId+" was not found in dp");
					continue;
				}
				
				if(template.isStigma() && !PlayerTransferConfig.ALLOW_STIGMA) {
					continue;
				}

				int newId = IDFactory.getInstance().nextId();
				Item item = new Item(newId, itemId, itemCnt, itemColor, itemCreator, itemExpireTime, itemActivationCnt, itemEquipped, itemSoulBound, equipSlot, location, enchant, skinId, fusionId, optSocket, optFusion, charge);
				if(manastones.size() > 0) {
					for(int[] stone : manastones) {
						ItemSocketService.addManaStone(item, stone[0], stone[1]);
					}
				}
				if(fusions.size() > 0) {
					for(int[] stone : fusions) {
						ItemSocketService.addFusionStone(item, stone[0], stone[1]);
					}
				}
				if(godstone != 0) {
					item.addGodStone(godstone);
				}
				
				String itemTxt = "(cube)#itemId="+itemId+"; objectIdChange["+objIdOld+"->"+newId+"] "+item.getItemCount()+";"+item.getItemColor()+";"+item.getItemCreator()+";"+item.getExpireTime()+";"+item.getActivationCount()+";"+item.getEnchantLevel()+";"+item.getItemSkinTemplate().getTemplateId()+";"+item.getFusionedItemTemplate()+";"+item.getOptionalSocket()+";"+item.getOptionalFusionSocket()+";"+item.getChargePoints();
				itemOut.add(itemTxt);
				item.setPersistentState(PersistentState.NEW);
				player.getInventory().add(item);
			}
		}
		
		cnt = readD();
		for(int a = 0; a < cnt; a++) { //warehouse
			int objIdOld = readD();
			int itemId = readD();
			long itemCnt = readQ();
			int itemColor = readD();
			String itemCreator = readS();
			int itemExpireTime = readD();
			int itemActivationCnt = readD();
			boolean itemEquipped = readD() == 1;
			boolean itemSoulBound = readD() == 1;
			int equipSlot = readD();
			int location = readD();
			int enchant = readD();
			int skinId = readD();
			int fusionId = readD();
			int optSocket = readD();
			int optFusion = readD();
			int charge = readD();
			FastList<int[]> manastones = FastList.newInstance(), fusions = FastList.newInstance();
			int len = readD();
			for(int b = 0; b < len; b++) {
				manastones.add(new int[] { readD(), readD() });
			}
			len = readD();
			for(int b = 0; b < len; b++) {
				fusions.add(new int[] { readD(), readD() });
			}
			
			int godstone = 0;
			if(readC() == 1)
				godstone = readD();
			
			if(PlayerTransferConfig.ALLOW_WAREHOUSE) {
				ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(itemId);
				if(template == null) {
					textLog.info("(warehouse"+targetAccount+")item with id "+itemId+" was not found in dp");
					continue;
				}
				
				if(template.isStigma() && !PlayerTransferConfig.ALLOW_STIGMA) {
					continue;
				}

				int newId = IDFactory.getInstance().nextId();
				Item item = new Item(newId, itemId, itemCnt, itemColor, itemCreator, itemExpireTime, itemActivationCnt, itemEquipped, itemSoulBound, equipSlot, location, enchant, skinId, fusionId, optSocket, optFusion, charge);
				if(manastones.size() > 0) {
					for(int[] stone : manastones) {
						ItemSocketService.addManaStone(item, stone[0], stone[1]);
					}
				}
				if(fusions.size() > 0) {
					for(int[] stone : fusions) {
						ItemSocketService.addFusionStone(item, stone[0], stone[1]);
					}
				}
				if(godstone != 0) {
					item.addGodStone(godstone);
				}
				
				String itemTxt = "(warehouse)#itemId="+itemId+"; objectIdChange["+objIdOld+"->"+newId+"] "+item.getItemCount()+";"+item.getItemColor()+";"+item.getItemCreator()+";"+item.getExpireTime()+";"+item.getActivationCount()+";"+item.getEnchantLevel()+";"+item.getItemSkinTemplate().getTemplateId()+";"+item.getFusionedItemTemplate()+";"+item.getOptionalSocket()+";"+item.getOptionalFusionSocket()+";"+item.getChargePoints();
				itemOut.add(itemTxt);
				item.setPersistentState(PersistentState.NEW);
				player.getWarehouse().add(item);
			}
		}
		DAOManager.getDAO(InventoryDAO.class).store(player);
		
		for(String s : itemOut)
			textLog.info(s);
		
		FastList.recycle(itemOut);
		cnt = readD();
		textLog.info("EmotionList:"+cnt);
		player.setEmotions(new EmotionList(player));
		for(int a = 0; a < cnt; a++) { //emotes
			if(PlayerTransferConfig.ALLOW_EMOTIONS)
				player.getEmotions().add(readD(), readD(), true);
			else {
				readQ();
			}
		}

		cnt = readD();
		textLog.info("MotionList:"+cnt);
		player.setMotions(new MotionList(player));
		for(int i = 0; i < cnt; i++) { //motions
			if(PlayerTransferConfig.ALLOW_MOTIONS)
				player.getMotions().add(new Motion(readD(), readD(), readC() == 1), true);
			else
				readB(9);
		}

		cnt = readD();
		textLog.info("MacroList:"+cnt);
		player.setMacroList(new MacroList());
		for(int a = 0; a < cnt; a++) { //macros
			if(PlayerTransferConfig.ALLOW_MACRO)
				PlayerService.addMacro(player, readD(), readS());
			else {
				readD(); readS();
			}
		}
		
		cnt = readD();
		textLog.info("NpcFactions:"+cnt);
		player.setNpcFactions(new NpcFactions(player));
		for(int a = 0; a < cnt; a++) { //npc factions
			if(PlayerTransferConfig.ALLOW_NPCFACTIONS)
				player.getNpcFactions().addNpcFaction(new NpcFaction(readD(), readD(), readD() == 1, ENpcFactionQuestState.valueOf(readS()), readD()));
			else {
				readB(12); readS(); readD();
			}
		}
		if(cnt > 0 && PlayerTransferConfig.ALLOW_NPCFACTIONS)
			DAOManager.getDAO(PlayerNpcFactionsDAO.class).storeNpcFactions(player);

		cnt = readD();
		textLog.info("Pets:"+cnt);
		for(int i = 0; i < cnt; i++) { //pets
			if(PlayerTransferConfig.ALLOW_PETS) {
				int petId = readD();
				int decorationId = readD();
				long bday = readQ();
				if(bday == 0)
					bday = System.currentTimeMillis();
				player.getPetList().addPet(player, petId, decorationId, bday, readS());
			}
			else {
				readB(16); readS();
			}
		}

		cnt = readD();
		textLog.info("RecipeList:"+cnt);
		player.setRecipeList(new RecipeList());
		for(int a = 0; a < cnt; a++) { //recipes
			if(PlayerTransferConfig.ALLOW_RECIPES)
				player.getRecipeList().addRecipe(player.getObjectId(), readD());
			else
				readD();
		}
		
		cnt = readD();
		textLog.info("PlayerSkillList:"+cnt);
		player.setSkillList(new PlayerSkillList());
		boolean rsCheck = rsList.size() > 0;
		for(int a = 0; a < cnt; a++) { //skills
			int skillId = readD();
			int skillLvl = readD();
			
			if(rsCheck && rsList.contains(skillId))
				continue;
			
			SkillTemplate temp = DataManager.SKILL_DATA.getSkillTemplate(skillId);
			if(!PlayerTransferConfig.ALLOW_SKILLS) {
				if(temp.isPassive())
					player.getSkillList().addSkill(player, skillId, skillLvl);
			}
			else
				player.getSkillList().addSkill(player, skillId, skillLvl);
		}

		cnt = readD();
		textLog.info("TitleList:"+cnt);
		player.setTitleList(new TitleList());
		for(int a = 0; a < cnt; a++) { //titles
			if(PlayerTransferConfig.ALLOW_TITLES)
				player.getTitleList().addEntry(readD(), readD());
			else {
				readB(8);
			}
		}
		if(cnt > 0 && PlayerTransferConfig.ALLOW_TITLES)
			for(Title t : player.getTitleList().getTitles()) {
				DAOManager.getDAO(PlayerTitleListDAO.class).storeTitles(player, t);
			}
		
		String[] pos = null;
		switch(player.getRace()) {
			case ELYOS:
				pos = PlayerTransferConfig.BIND_ELYOS.split(" ");
				break;
			case ASMODIANS:
				pos = PlayerTransferConfig.BIND_ASMO.split(" ");
				break;
		}
		
		player.setBindPoint(new BindPointPosition(Integer.parseInt(pos[0]), Float.parseFloat(pos[1]), Float.parseFloat(pos[2]), Float.parseFloat(pos[3]), Byte.parseByte(pos[4])));
		DAOManager.getDAO(PlayerBindPointDAO.class).store(player);
		
		int uilen = readD(), shortlen = readD();
		byte[] ui = readB(uilen), sc = readB(shortlen);
		player.setPlayerSettings(new PlayerSettings(uilen > 0 ? ui : null, shortlen > 0 ? sc : null, readD(), readD()));
		player.setAbyssRank(new AbyssRank(0,0,0,1,0,0,0,0,1,0,0,0));
		
		cnt = readD();
		textLog.info("QuestStateList:"+cnt);
		player.setQuestStateList(new QuestStateList());
		for(int a = 0; a < cnt; a++) { //quests
			int questId = readD();
			if(PlayerTransferConfig.ALLOW_QUESTS)
				player.getQuestStateList().addQuest(questId, new QuestState(questId, QuestStatus.valueOf(readS()), readD(), readD(), null, readD(), null)); //TODO null timestamp
			else {
				readS(); readB(12);
			}
		}
		
		PlayerService.storePlayer(player);
		textLog.info("finished in "+(System.currentTimeMillis()-st)+" ms");
		return player;
	}
}
