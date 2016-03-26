package br.com.battlebits.ycommon.bungee.networking;

import java.io.DataOutputStream;
import java.util.UUID;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.AccountConfiguration;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAccountConfiguration;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAccountLoad;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAccountRequest;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAddBlockedPlayer;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAddFriend;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAddFriendRequest;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAddGroup;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAddRank;
import br.com.battlebits.ycommon.common.networking.packets.CPacketBanPlayer;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeFichas;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeLanguage;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeLiga;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeMoney;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeXp;
import br.com.battlebits.ycommon.common.networking.packets.CPacketCreateParty;
import br.com.battlebits.ycommon.common.networking.packets.CPacketDisbandParty;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveBlockedPlayer;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveFriend;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveFriendRequest;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveGroup;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveRank;
import br.com.battlebits.ycommon.common.networking.packets.CPacketTranslationsLoad;
import br.com.battlebits.ycommon.common.networking.packets.CPacketTranslationsRequest;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUnbanPlayer;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateClan;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateGameStatus;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateProfile;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import net.md_5.bungee.BungeeCord;

public class BungeePacketHandler extends CommonHandler {

	private PacketSender sender;

	public BungeePacketHandler(PacketSender sender) {
		this.sender = sender;
	}

	public static void handleTranslationsLoad(Language lang, DataOutputStream output) throws Exception {
		output.writeUTF("Translations");
		String json = BungeeMain.getGson().toJson(Translate.getMapTranslation(lang));
		output.writeUTF(json);
		output.flush();
	}

	@Override
	public void handleAccountConfiguration(CPacketAccountConfiguration packet) throws Exception {
		BattlebitsAPI.getLogger().warning("Recebendo AccountConfiguration!");
		BattlebitsAPI.getLogger().info(packet.getConfiguration());
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(packet.getUuid());
		player.setConfiguration(BungeeMain.getGson().fromJson(packet.getConfiguration(), AccountConfiguration.class));
		player = null;
	}

	@Override
	public void handleAccountRequest(CPacketAccountRequest packet) throws Exception {
		UUID uuid = packet.getUuid();
		String json = BungeeMain.getGson().toJson(BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid));
		sender.sendPacket(new CPacketAccountLoad(uuid, json));
		uuid = null;
		json = null;
	}

	@Override
	public void handleAccountLoad(CPacketAccountLoad packet) {
		// PROVAVEL QUE NUNCA VAI RECEBER.
	}

	@Override
	public void handleTranslationsRequest(CPacketTranslationsRequest packet) throws Exception {
		Language lang = packet.getLanguage();
		String json = BungeeMain.getGson().toJson(Translate.getMapTranslation(lang));
		sender.sendPacket(new CPacketTranslationsLoad(lang, json));
		lang = null;
		json = null;
	}

	@Override
	public void handleTranslationsLoad(CPacketTranslationsLoad packet) {

	}

	@Override
	public void handleCreateParty(CPacketCreateParty packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDisbandParty(CPacketDisbandParty packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleAddFriend(CPacketAddFriend packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleRemoveFriend(CPacketRemoveFriend packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleAddFriendRequest(CPacketAddFriendRequest packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleRemoveFriendRequest(CPacketRemoveFriendRequest packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleBlockPlayer(CPacketAddBlockedPlayer packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleUnblockPlayer(CPacketRemoveBlockedPlayer packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleAddGroup(CPacketAddGroup packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleRemoveGroup(CPacketRemoveGroup packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleAddRank(CPacketAddRank packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleRemoveRank(CPacketRemoveRank packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleBanPlayer(CPacketBanPlayer packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleUnbanPlayer(CPacketUnbanPlayer packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleUpdateProfile(CPacketUpdateProfile packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleUpdateGameStatus(CPacketUpdateGameStatus packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleUpdateClan(CPacketUpdateClan packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleChangeXp(CPacketChangeXp packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleChangeMoney(CPacketChangeMoney packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleChangeFichas(CPacketChangeFichas packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleChangeLiga(CPacketChangeLiga packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleChangeLanguage(CPacketChangeLanguage packet) {
		// TODO Auto-generated method stub

	}

}
