package br.com.battlebits.ycommon.common.networking;

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

public abstract class CommonHandler {

	public abstract void handleAccountRequest(CPacketAccountRequest packet) throws Exception;

	public abstract void handleAccountLoad(CPacketAccountLoad packet) throws Exception;

	public abstract void handleTranslationsRequest(CPacketTranslationsRequest packet) throws Exception;
	
	public abstract void handleTranslationsLoad(CPacketTranslationsLoad packet) throws Exception;

	public abstract void handleCreateParty(CPacketCreateParty packet) throws Exception;

	public abstract void handleDisbandParty(CPacketDisbandParty packet) throws Exception;

	public abstract void handleAddFriend(CPacketAddFriend packet) throws Exception;

	public abstract void handleRemoveFriend(CPacketRemoveFriend packet) throws Exception;

	public abstract void handleAddFriendRequest(CPacketAddFriendRequest packet) throws Exception;

	public abstract void handleRemoveFriendRequest(CPacketRemoveFriendRequest packet) throws Exception;

	public abstract void handleBlockPlayer(CPacketAddBlockedPlayer packet) throws Exception;

	public abstract void handleUnblockPlayer(CPacketRemoveBlockedPlayer packet) throws Exception;

	public abstract void handleAddGroup(CPacketAddGroup packet) throws Exception;

	public abstract void handleRemoveGroup(CPacketRemoveGroup packet) throws Exception;

	public abstract void handleAddRank(CPacketAddRank packet) throws Exception;

	public abstract void handleRemoveRank(CPacketRemoveRank packet) throws Exception;

	public abstract void handleBanPlayer(CPacketBanPlayer packet) throws Exception;

	public abstract void handleUnbanPlayer(CPacketUnbanPlayer packet) throws Exception;

	public abstract void handleUpdateProfile(CPacketUpdateProfile packet) throws Exception;

	public abstract void handleUpdateGameStatus(CPacketUpdateGameStatus packet) throws Exception;

	public abstract void handleUpdateClan(CPacketUpdateClan packet) throws Exception;

	public abstract void handleChangeXp(CPacketChangeXp packet) throws Exception;

	public abstract void handleChangeMoney(CPacketChangeMoney packet) throws Exception;

	public abstract void handleChangeFichas(CPacketChangeFichas packet) throws Exception;

	public abstract void handleChangeLiga(CPacketChangeLiga packet) throws Exception;

	public abstract void handleChangeLanguage(CPacketChangeLanguage packet) throws Exception;

}
