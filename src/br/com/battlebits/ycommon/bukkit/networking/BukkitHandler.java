package br.com.battlebits.ycommon.bukkit.networking;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.clans.Clan;
import br.com.battlebits.ycommon.common.exception.HandlePacketException;
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
import br.com.battlebits.ycommon.common.networking.packets.CPacketClanAbbreviationChange;
import br.com.battlebits.ycommon.common.networking.packets.CPacketClanLoad;
import br.com.battlebits.ycommon.common.networking.packets.CPacketCommandRun;
import br.com.battlebits.ycommon.common.networking.packets.CPacketCreateParty;
import br.com.battlebits.ycommon.common.networking.packets.CPacketDisbandParty;
import br.com.battlebits.ycommon.common.networking.packets.CPacketKeepAlive;
import br.com.battlebits.ycommon.common.networking.packets.CPacketPlayerJoinClan;
import br.com.battlebits.ycommon.common.networking.packets.CPacketPlayerLeaveClan;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveBlockedPlayer;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveFriend;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveFriendRequest;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveGroup;
import br.com.battlebits.ycommon.common.networking.packets.CPacketRemoveRank;
import br.com.battlebits.ycommon.common.networking.packets.CPacketServerInfo;
import br.com.battlebits.ycommon.common.networking.packets.CPacketServerNameLoad;
import br.com.battlebits.ycommon.common.networking.packets.CPacketServerRecall;
import br.com.battlebits.ycommon.common.networking.packets.CPacketServerStart;
import br.com.battlebits.ycommon.common.networking.packets.CPacketServerStop;
import br.com.battlebits.ycommon.common.networking.packets.CPacketTranslationsLoad;
import br.com.battlebits.ycommon.common.networking.packets.CPacketTranslationsRequest;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateClan;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateGameStatus;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateProfile;
import br.com.battlebits.ycommon.common.tag.Tag;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class BukkitHandler extends CommonHandler {

	public static Object LOCK = new Object();

	@Override
	public void handleAccountRequest(CPacketAccountRequest packet) {
		// PROVAVEL QUE NUNCA VAI TER
	}

	@Override
	public void handleAccountLoad(CPacketAccountLoad packet) {
		BukkitPlayer battlePlayer = packet.getBukkitPlayer();
		battlePlayer.connect(BukkitMain.getServerHostName());
		battlePlayer.injectBukkitClass();
		battlePlayer.loadTags();
		if (battlePlayer.getTag() == Tag.STAFF)
			battlePlayer.setTag(battlePlayer.getDefaultTag());
		BattlebitsAPI.getAccountCommon().loadBattlePlayer(packet.getBattlePlayer().getUuid(), battlePlayer);
		BattlebitsAPI.debug("NEW BATTLEPLAYER>" + battlePlayer.getUserName() + "(" + battlePlayer.getUuid() + ")");
		battlePlayer = null;
		synchronized (LOCK) {
			LOCK.notifyAll();
		}
	}

	@Override
	public void handleTranslationsRequest(CPacketTranslationsRequest packet) {
		// PROVAVEL QUE NUNCA VAI TER
	}

	@Override
	public void handleTranslationsLoad(CPacketTranslationsLoad packet) {
		Language lang = packet.getLanguage();
		String json = packet.getJson();
		Translate.loadTranslations(BattlebitsAPI.TRANSLATION_ID, lang, json);
		BattlebitsAPI.debug("NEW TRANSLATION>" + lang);
		lang = null;
		json = null;
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
	public void handleChangeLiga(CPacketChangeLiga packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleChangeLanguage(CPacketChangeLanguage packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleAccountConfiguration(CPacketAccountConfiguration packet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleChangeTag(CPacketChangeTag packet) {
		// PROVAVEL QUE NUNCA VAI TER

	}

	@Override
	public void handleChangeAccount(CPacketChangeAccount packet) {

	}

	@Override
	public void handleServerStart(CPacketServerStart packet) {
		// PROVAVEL QUE NUNCA VAI TER
	}

	@Override
	public void handleServerRecall(CPacketServerRecall packet) {
		// PROVAVEL QUE NUNCA VAI TER
	}

	@Override
	public void handleServerStop(CPacketServerStop packet) {
		// PROVAVEL QUE NUNCA VAI TER
	}

	@Override
	public void handleServerLoad(CPacketServerNameLoad packet) {
		BukkitMain.setServerName(packet.getServerHostName());
		BattlebitsAPI.getLogger().info("SERVER HOST NAME > " + packet.getServerHostName());
		BattlebitsAPI.getLogger().info("SERVER TYPE > " + BukkitMain.getServerType());
	}

	@Override
	public void handleServerInfo(CPacketServerInfo packet) {
		// PROVAVEL QUE NUNCA VAI TER
	}

	@Override
	public void handleCommandRun(CPacketCommandRun packet) {
		// PROVAVEL QUE NUNCA VAI TER
	}

	@Override
	public void handlerKeepAlive(CPacketKeepAlive packet) {
		// NAO PRECISA
	}

	@Override
	public void handleClanLoad(CPacketClanLoad packet) throws HandlePacketException {
		BattlebitsAPI.getClanCommon().loadClan(packet.getClan());
	}

	@Override
	public void handlerPlayerJoinClan(CPacketPlayerJoinClan packet) throws HandlePacketException {
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(packet.getUuid());
		if (player == null)
			return;
		Clan clan = BattlebitsAPI.getClanCommon().getClan(packet.getClanName());
		if (clan == null)
			return;
		clan.addParticipant(player);
	}

	@Override
	public void handlerPlayerLeaveClan(CPacketPlayerLeaveClan packet) throws HandlePacketException {
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(packet.getUuid());
		if (player == null)
			return;
		if (player.getClan() != null) {
			player.getClan().demote(player.getUuid());
			player.getClan().removeParticipant(player.getUuid());
			if (player.getClan().isOwner(player)) {
				BattlebitsAPI.getClanCommon().unloadClan(player.getClanName());
			}
		}
		player.setClan("");
	}

	@Override
	public void handlerClanAbbreviationChange(CPacketClanAbbreviationChange packet) throws HandlePacketException {
		Clan clan = BattlebitsAPI.getClanCommon().getClan(packet.getClanName());
		if (clan == null)
			return;
		clan.changeAbbreviation(packet.getAbbreviation());
	}

}
