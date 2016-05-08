package br.com.battlebits.ycommon.bungee.networking;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.networking.client.CommonClient;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
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
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeAccount;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeLanguage;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeLiga;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeTag;
import br.com.battlebits.ycommon.common.networking.packets.CPacketCommandRun;
import br.com.battlebits.ycommon.common.networking.packets.CPacketCreateParty;
import br.com.battlebits.ycommon.common.networking.packets.CPacketDisbandParty;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveBlockedPlayer;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveFriend;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveFriendRequest;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveGroup;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveRank;
import br.com.battlebits.ycommon.common.networking.packets.CPacketServerNameLoad;
import br.com.battlebits.ycommon.common.networking.packets.CPacketServerNameRequest;
import br.com.battlebits.ycommon.common.networking.packets.CPacketTranslationsLoad;
import br.com.battlebits.ycommon.common.networking.packets.CPacketTranslationsRequest;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateClan;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateGameStatus;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateProfile;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import net.md_5.bungee.api.config.ServerInfo;

public class BungeePacketHandler extends CommonHandler {

	private CommonClient sender;

	public BungeePacketHandler(CommonClient sender) {
		this.sender = sender;
	}

	@Override
	public void handleAccountConfiguration(CPacketAccountConfiguration packet) throws Exception {
		BattlebitsAPI.getLogger().warning("Recebendo AccountConfiguration!");
		BattlebitsAPI.getLogger().info(packet.getConfiguration().toString());
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(packet.getUuid());
		player.setConfiguration(packet.getConfiguration());
		player = null;
	}

	@Override
	public void handleAccountRequest(CPacketAccountRequest packet) throws Exception {
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(packet.getUuid());
		if (player == null)
			player = BungeeMain.getPlugin().getAccountManager().loadBattlePlayer(packet.getUuid());
		sender.sendPacket(new CPacketAccountLoad(player));
		player = null;
	}

	@Override
	public void handleAccountLoad(CPacketAccountLoad packet) {
		// PROVAVEL QUE NUNCA VAI RECEBER.
	}

	@Override
	public void handleTranslationsRequest(CPacketTranslationsRequest packet) throws Exception {
		Language lang = packet.getLanguage();
		String json = Translate.getMapTranslation(lang);
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
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(packet.getPlayerUUID());
		if (player != null) {
			BattlebitsAPI.debug("ADD BLOCK>" + packet.getPlayerUUID() + "/" + packet.getBlocked().getPlayerUUID());
			player.getBlockedPlayers().put(packet.getBlocked().getPlayerUUID(), packet.getBlocked());
			player = null;
		} else {
			BattlebitsAPI.getLogger().warning("Ocorreu um erro ao tentar adicionar o block do jogador " + packet.getPlayerUUID() + "!");
		}
	}

	@Override
	public void handleUnblockPlayer(CPacketRemoveBlockedPlayer packet) {
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(packet.getPlayer());
		if (player != null) {
			BattlebitsAPI.debug("REMOVE BLOCK>" + packet.getPlayer() + "/" + packet.getUnblocked());
			player.getBlockedPlayers().remove(packet.getUnblocked());
			player = null;
		} else {
			BattlebitsAPI.getLogger().warning("Ocorreu um erro ao tentar remover o block do jogador " + packet.getPlayer() + "!");
		}
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
	public void handleUpdateProfile(CPacketUpdateProfile packet) {

	}

	@Override
	public void handleUpdateGameStatus(CPacketUpdateGameStatus packet) {
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(packet.getUuid());
		player.getGameStatus().getMinigameStatus().put(packet.getGameType(), packet.getJson());
		player = null;
	}

	@Override
	public void handleUpdateClan(CPacketUpdateClan packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleChangeLiga(CPacketChangeLiga packet) {
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(packet.getUuid());
		player.setLiga(packet.getLiga());
		player = null;
	}

	@Override
	public void handleChangeAccount(CPacketChangeAccount packet) throws Exception {
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(packet.getUuid());
		player.setXp(packet.getXp());
		player.setFichas(packet.getFichas());
		player.setMoney(packet.getMoney());
		player = null;
	}

	@Override
	public void handleChangeLanguage(CPacketChangeLanguage packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleChangeTag(CPacketChangeTag packet) throws Exception {
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(packet.getUniqueId());
		player.setTag(packet.getTag());
		player = null;
	}

	@Override
	public void handleServerRequest(CPacketServerNameRequest packet) throws Exception {
		String serverHostName = "none";
		for (ServerInfo info : BungeeMain.getPlugin().getProxy().getServers().values()) {
			String string = info.getAddress().getHostString() + ":" + info.getAddress().getPort();
			if (string.equals(packet.getServerListening())) {
				serverHostName = info.getName();
				break;
			}
		}
		sender.sendPacket(new CPacketServerNameLoad(serverHostName));
	}

	@Override
	public void handleServerLoad(CPacketServerNameLoad packet) throws Exception {
		// PROVAVEL QUE NUNCA VAI ACONTECER
	}

	@Override
	public void handleCommandRun(CPacketCommandRun packet) throws Exception {
		BungeeMain.getPlugin().getProxy().getPluginManager().dispatchCommand(BungeeMain.getPlugin().getProxy().getConsole(), packet.getCommand());
	}

}
