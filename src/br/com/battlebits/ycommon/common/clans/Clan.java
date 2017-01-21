package br.com.battlebits.ycommon.common.clans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.clans.ranking.ClanRank;
import br.com.battlebits.ycommon.common.enums.BattleInstance;
import br.com.battlebits.ycommon.common.permissions.enums.Group;

public class Clan {
	private String clanName;
	private String abbreviation;
	private ClanRank rank;
	private int xp = 0;
	private UUID owner;
	private List<UUID> administrators;
	private HashMap<UUID, String> participants;
	private List<UUID> invites;
	private List<UUID> vips;
	private transient long cacheExpire;
	private transient long confirmDisband = Long.MAX_VALUE;

	public Clan(String clanName, String abbreviation, BattlePlayer owner) {
		this.owner = owner.getUuid();
		this.clanName = clanName;
		this.abbreviation = abbreviation;
		this.rank = ClanRank.INITIAL;
		participants = new HashMap<>();
		administrators = new ArrayList<>();
		invites = new ArrayList<>();
		vips = new ArrayList<>();
		participants.put(owner.getUuid(), owner.getUserName());
		administrators.add(owner.getUuid());
		owner.setClan(clanName);
	}

	public String getClanName() {
		return clanName;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public ClanRank getClanRank() {
		return rank;
	}

	public int getXp() {
		return xp;
	}

	public UUID getOwner() {
		return owner;
	}

	public String getOwnerName() {
		return participants.get(owner);
	}

	public Set<UUID> getParticipants() {
		return participants.keySet();
	}

	public List<UUID> getAdministrators() {
		return administrators;
	}

	public boolean isCacheExpired() {
		return System.currentTimeMillis() > cacheExpire;
	}

	public void updateCache() {
		this.cacheExpire = System.currentTimeMillis() + (60 * 5 * 1000);
	}

	public String getPlayerName(UUID uuid) {
		return participants.get(uuid);
	}

	public void addXp(int xp) {
		if (xp < 0)
			xp = 0;
		this.xp += xp;
		updateStatus();
	}

	public void changeAbbreviation(String str) {
		abbreviation = str;
		updateStatus();
	}

	public boolean isOwner(BattlePlayer player) {
		return isOwner(player.getUuid());
	}

	public boolean isOwner(UUID uuid) {
		return owner.equals(uuid);
	}

	public boolean isAdministrator(BattlePlayer player) {
		return isAdministrator(player.getUuid());
	}

	public boolean isAdministrator(UUID uuid) {
		return administrators.contains(uuid);
	}

	public boolean isParticipant(BattlePlayer player) {
		return isParticipant(player.getUuid());
	}

	public boolean isParticipant(UUID uuid) {
		return participants.containsKey(uuid);
	}

	public boolean isInvited(BattlePlayer player) {
		return isInvited(player.getUuid());
	}

	public boolean isInvited(UUID uuid) {
		return invites.contains(uuid);
	}

	public boolean confirm() {
		if (System.currentTimeMillis() < confirmDisband + 60000)
			return true;
		confirmDisband = System.currentTimeMillis();
		return false;
	}

	public boolean promote(UUID uuid) {
		if (!participants.containsKey(uuid))
			return false;
		if (administrators.contains(uuid))
			return false;
		administrators.add(uuid);
		updateStatus();
		return true;
	}

	public boolean demote(UUID uuid) {
		if (!participants.containsKey(uuid))
			return false;
		if (!administrators.contains(uuid))
			return false;
		administrators.remove(uuid);
		updateStatus();
		return true;
	}

	public boolean updatePlayer(BattlePlayer player) {
		participants.put(player.getUuid(), player.getUserName());
		if (player.hasGroupPermission(Group.LIGHT)) {
			if (!vips.contains(player.getUuid()))
				vips.add(player.getUuid());
			return false;
		} else {
			if (vips.contains(player.getUuid()))
				vips.remove(player.getUuid());
		}
		if (getSlots() >= getParticipants().size())
			return false;
		if (isOwner(player))
			return false;
		UUID uuid = player.getUuid();
		if (isAdministrator(player)) {
			ArrayList<UUID> random = new ArrayList<>();
			for (UUID unique : getParticipants()) {
				if (!isAdministrator(unique)) {
					random.add(unique);
				}
			}
			if (random.size() > 0) {
				int i = random.size() - 1 > 0 ? new Random().nextInt(random.size() - 1) : 0;
				uuid = random.get(i);
			}
			if (uuid == player.getUuid())
				demote(uuid);
		}
		removeParticipant(player.getUuid());
		updateStatus();
		return true;
	}

	public void addParticipant(BattlePlayer player) {
		invites.remove(player.getUuid());
		participants.put(player.getUuid(), player.getUserName());
		player.setClan(clanName);
		updateStatus();
		if (player.hasGroupPermission(Group.LIGHT)) {
			if (!vips.contains(player.getUuid()))
				vips.add(player.getUuid());
		}
	}

	public boolean removeParticipant(UUID uuid) {
		if (owner == uuid)
			return false;
		if (administrators.contains(uuid))
			return false;
		participants.remove(uuid);
		vips.remove(uuid);
		updateStatus();
		return true;
	}

	public void invite(BattlePlayer player) {
		if (invites.contains(player.getUuid()))
			return;
		invites.add(player.getUuid());
	}

	public void removeInvite(UUID uuid) {
		if (!invites.contains(uuid))
			return;
		invites.remove(uuid);
	}

	public int getSlots() {
		return 5 + (2 * rank.ordinal()) + vips.size();
	}

	public void updateStatus() {
		if (BattlebitsAPI.getBattleInstance() != BattleInstance.BUNGEECORD)
			return;
		BungeeMain.getPlugin().getClanManager().updateClan(this);
	}

}
