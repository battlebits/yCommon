package br.com.battlebits.yutils.common.account;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.battlebits.yutils.common.account.battlecraft.BattlecraftStatus;
import br.com.battlebits.yutils.common.account.game.GameStatus;
import br.com.battlebits.yutils.common.account.hungergames.HGStatus;
import br.com.battlebits.yutils.common.banmanager.history.BanHistory;
import br.com.battlebits.yutils.common.clans.Clan;
import br.com.battlebits.yutils.common.enums.Liga;
import br.com.battlebits.yutils.common.enums.ServerType;
import br.com.battlebits.yutils.common.friends.Friend;
import br.com.battlebits.yutils.common.friends.block.Blocked;
import br.com.battlebits.yutils.common.friends.request.Request;
import br.com.battlebits.yutils.common.party.Party;
import br.com.battlebits.yutils.common.payment.constructors.Expire;
import br.com.battlebits.yutils.common.permissions.enums.Group;
import br.com.battlebits.yutils.common.tagmanager.enums.Tag;
import br.com.battlebits.yutils.common.translate.languages.Language;
import br.com.battlebits.yutils.common.translate.nationalities.Nationalities;

public class BattlePlayer {

	private String userName;
	private UUID uuid;
	private String fakeName;

	private List<String> nameHistory;

	private int fichas;
	private int money;
	private int xp;
	private Liga liga;
	private Tag tag;
	private String ipAddress;

	private long onlineTime;
	private long lastLoggedIn;
	private long firstTimePlaying;
	private String lastIpAddress;

	private boolean ignoreAll;

	private Map<ServerType, Group> groups;
	private Map<Group, Expire> ranks;
	private Map<UUID, Friend> friends;
	private Map<UUID, Request> friendRequests;
	private Map<UUID, Blocked> blockedPlayers;

	private String name;
	private Clan actualClan;
	private Party actualParty;
	private String skype;
	private boolean friendOnly;
	private String twitter;
	private String youtubeChannel;
	private Nationalities nationality;
	private Language language;

	private HGStatus hungerGamesStatus;
	private BattlecraftStatus battlecraftStatus;
	private GameStatus gameStatus;
	private BanHistory banHistory;
}
