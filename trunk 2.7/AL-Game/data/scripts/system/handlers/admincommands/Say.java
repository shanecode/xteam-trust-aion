package admincommands;

import com.light.gameserver.model.ChatType;
import com.light.gameserver.model.gameobjects.Npc;
import com.light.gameserver.model.gameobjects.VisibleObject;
import com.light.gameserver.model.gameobjects.player.Player;
import com.light.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.light.gameserver.utils.PacketSendUtility;
import com.light.gameserver.utils.chathandlers.ChatCommand;

/**
 * @author Divinity
 */
public class Say extends ChatCommand {

	public Say() {
		super("say");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params.length < 1) {
			onFail(admin, null);
			return;
		}

		VisibleObject target = admin.getTarget();

		if (target == null) {
			PacketSendUtility.sendMessage(admin, "You must select a target !");
			return;
		}

		StringBuilder sbMessage = new StringBuilder();

		for (String p : params)
			sbMessage.append(p + " ");

		String sMessage = sbMessage.toString().trim();

		if (target instanceof Player) {
			PacketSendUtility.broadcastPacket(((Player) target),
				new SM_MESSAGE(((Player) target), sMessage, ChatType.NORMAL), true);
		}
		else if (target instanceof Npc) {
			// admin is not right, but works
			PacketSendUtility.broadcastPacket(admin, new SM_MESSAGE(((Npc) target).getObjectId(), ((Npc) target).getName(),
				sMessage, ChatType.NORMAL), true);
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Syntax: //say <Text>");
	}
}
