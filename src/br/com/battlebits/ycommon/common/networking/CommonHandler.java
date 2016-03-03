package br.com.battlebits.ycommon.common.networking;

import br.com.battlebits.ycommon.common.networking.packets.CPacketAccountLoad;
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
import br.com.battlebits.ycommon.common.networking.packets.CPacketUnbanPlayer;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateClan;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateGameStatus;
import br.com.battlebits.ycommon.common.networking.packets.CPacketUpdateProfile;

public abstract class CommonHandler {

	public abstract void handleAccountLoad(CPacketAccountLoad packet);

	public abstract void handleTranslationsLoad(CPacketTranslationsLoad packet);

	public abstract void handleCreateParty(CPacketCreateParty packet);

	public abstract void handleDisbandParty(CPacketDisbandParty packet);

	public abstract void handleAddFriend(CPacketAddFriend packet);

	public abstract void handleRemoveFriend(CPacketRemoveFriend packet);

	public abstract void handleAddFriendRequest(CPacketAddFriendRequest packet);

	public abstract void handleRemoveFriendRequest(CPacketRemoveFriendRequest packet);

	public abstract void handleBlockPlayer(CPacketAddBlockedPlayer packet);

	public abstract void handleUnblockPlayer(CPacketRemoveBlockedPlayer packet);

	public abstract void handleAddGroup(CPacketAddGroup packet);

	public abstract void handleRemoveGroup(CPacketRemoveGroup packet);

	public abstract void handleAddRank(CPacketAddRank packet);

	public abstract void handleRemoveRank(CPacketRemoveRank packet);

	public abstract void handleBanPlayer(CPacketBanPlayer packet);

	public abstract void handleUnbanPlayer(CPacketUnbanPlayer packet);

	public abstract void handleUpdateProfile(CPacketUpdateProfile packet);

	public abstract void handleUpdateGameStatus(CPacketUpdateGameStatus packet);

	public abstract void handleUpdateClan(CPacketUpdateClan packet);

	public abstract void handleChangeXp(CPacketChangeXp packet);

	public abstract void handleChangeMoney(CPacketChangeMoney packet);

	public abstract void handleChangeFichas(CPacketChangeFichas packet);

	public abstract void handleChangeLiga(CPacketChangeLiga packet);

	public abstract void handleChangeLanguage(CPacketChangeLanguage packet);

}
