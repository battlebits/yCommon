package br.com.battlebits.ycommon.bukkit.board;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

public class BattleBoard {

	// TEAMS:
	// - CREATE - OK
	// - JOIN - OK
	// - EDIT - OK
	// - GET - NONE
	// - LEAVE - OK
	// - REMOVE - OK
	// OBJECTIVES
	// - CREATE - OK
	// - SET - OK
	// - CUSTOM SCORE - OK
	// - REMOVE - SOON

	public Team getTeamFromPlayer(Player player, String teamID) {
		if (teamID.length() > 16) {
			teamID = teamID.substring(0, 16);
		}
		return player.getScoreboard().getTeam(teamID);
	}

	public boolean teamExistsForPlayer(Player player, String teamID) {
		return getTeamFromPlayer(player, teamID) != null;
	}

	public Team getPlayerCurrentTeamForPlayer(Player player, Player get) {
		return player.getScoreboard().getPlayerTeam(get);
	}

	public boolean playerHasCurrentTeamForPlayer(Player player, Player has) {
		return getPlayerCurrentTeamForPlayer(player, has) != null;
	}

	public Team createTeamToPlayer(Player player, String teamID, String teamPrefix, String teamSuffix) {
		Team team = getTeamFromPlayer(player, teamID);
		if (team == null) {
			if (teamID.length() > 16) {
				teamID = teamID.substring(0, 16);
			}
			if (teamPrefix.length() > 16) {
				teamPrefix = teamPrefix.substring(0, 16);
			}
			if (teamSuffix.length() > 16) {
				teamSuffix = teamSuffix.substring(0, 16);
			}
			team = player.getScoreboard().registerNewTeam(teamID);
		}
		team.setPrefix(teamPrefix);
		team.setSuffix(teamSuffix);
		player = null;
		teamID = null;
		teamPrefix = null;
		teamSuffix = null;
		return team;
	}

	public void createTeamForPlayers(Collection<? extends Player> players, String teamID, String teamPrefix, String teamSuffix) {
		for (Player player : players) {
			createTeamToPlayer(player, teamID, teamPrefix, teamSuffix);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void createTeamForOnlinePlayers(String teamID, String teamPrefix, String teamSuffix) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			createTeamToPlayer(player, teamID, teamPrefix, teamSuffix);
		}
	}

	public Team createTeamIfNotExistsToPlayer(Player player, String teamID, String teamPrefix, String teamSuffix) {
		Team team = getTeamFromPlayer(player, teamID);
		if (team == null) {
			team = createTeamToPlayer(player, teamID, teamPrefix, teamSuffix);
		}
		player = null;
		teamSuffix = null;
		teamPrefix = null;
		teamID = null;
		return team;
	}

	public void createTeamIfNotExistsForPlayers(Collection<? extends Player> players, String teamID, String teamPrefix, String teamSuffix) {
		for (Player player : players) {
			createTeamIfNotExistsToPlayer(player, teamID, teamPrefix, teamSuffix);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void createTeamIfNotExistsForOnlinePlayers(String teamID, String teamPrefix, String teamSuffix) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			createTeamIfNotExistsToPlayer(player, teamID, teamPrefix, teamSuffix);
		}
	}

	public void joinTeam(Team team, String join) {
		if (team != null) {
			if (join.length() > 16) {
				join = join.substring(0, 16);
			}
			if (!team.getEntries().contains(join)) {
				team.addEntry(join);
			}
			team = null;
		}
		join = null;
	}

	public void joinTeam(Team team, Player join) {
		joinTeam(team, join.getName());
	}

	public void joinTeamForPlayer(Player player, String teamID, String join) {
		joinTeam(getTeamFromPlayer(player, teamID), join);
		teamID = null;
		player = null;
	}

	public void joinTeamForPlayer(Player player, String teamID, Player join) {
		Team team = getTeamFromPlayer(player, teamID);
		if (team != null) {
			if (!team.getEntries().contains(join.getName())) {
				leaveCurrentTeamForPlayer(player, join);
				team.addEntry(join.getName());
			}
			team = null;
		}
		join = null;
		player = null;
	}

	public void joinTeamForPlayers(Collection<? extends Player> players, String teamID, String join) {
		for (Player player : players) {
			joinTeamForPlayer(player, teamID, join);
		}
		players = null;
	}

	public void joinTeamForPlayers(Collection<? extends Player> players, String teamID, Player join) {
		for (Player player : players) {
			joinTeamForPlayer(player, teamID, join);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void joinTeamForOnlinePlayers(String join, String teamID) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			joinTeamForPlayer(player, teamID, join);
		}
	}

	@SuppressWarnings("deprecation")
	public void joinTeamForOnlinePlayers(Player join, String teamID) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			joinTeamForPlayer(player, teamID, join);
		}
	}

	public void leaveCurrentTeamForPlayer(Player player, Player leave) {
		Team team = getPlayerCurrentTeamForPlayer(player, leave);
		if (team != null) {
			leaveTeam(team, leave);
		}
	}

	public void leaveCurrentTeamForPlayers(Collection<? extends Player> players, Player leave) {
		for (Player player : players) {
			leaveCurrentTeamForPlayer(player, leave);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void leaveCurrentTeamForOnlinePlayers(Player leave) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			leaveCurrentTeamForPlayer(player, leave);
		}
	}

	public void leaveTeam(Team team, String leave) {
		if (team != null) {
			if (leave.length() > 16) {
				leave = leave.substring(0, 16);
			}
			if (team.getEntries().contains(leave)) {
				team.removeEntry(leave);
				unregisterTeamIfEmpty(team);
			}
			team = null;
		}
	}

	public void leaveTeam(Team team, Player leave) {
		leaveTeam(team, leave.getName());
	}

	public void leaveTeamToPlayer(Player player, String teamID, String leave) {
		leaveTeam(getTeamFromPlayer(player, teamID), leave);
		teamID = null;
	}

	public void leaveTeamToPlayer(Player player, String teamID, Player leave) {
		leaveTeamToPlayer(player, teamID, leave.getName());
	}

	public void leaveTeamForPlayers(Collection<? extends Player> players, String teamID, String leave) {
		for (Player player : players) {
			leaveTeamToPlayer(player, teamID, leave);
		}
		players = null;
	}

	public void leaveTeamForPlayers(Collection<? extends Player> players, String teamID, Player leave) {
		leaveTeamForPlayers(players, teamID, leave.getName());
	}

	@SuppressWarnings("deprecation")
	public void leaveTeamForOnlinePlayers(String teamID, String leave) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			leaveTeamToPlayer(player, teamID, leave);
		}
	}

	public void leaveTeamForOnlinePlayers(String teamID, Player leave) {
		leaveTeamForOnlinePlayers(teamID, leave.getName());
	}

	public void unregisterTeam(Team team) {
		if (team != null) {
			team.unregister();
			team = null;
		}
	}

	public void unregisterTeamToPlayer(Player player, String teamID) {
		unregisterTeam(getTeamFromPlayer(player, teamID));
		player = null;
		teamID = null;
	}

	public void unregisterTeamForPlayers(Collection<? extends Player> players, String teamID) {
		for (Player player : players) {
			unregisterTeamToPlayer(player, teamID);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void unregisterTeamForOnlinePlayers(String teamID) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			unregisterTeamToPlayer(player, teamID);
		}
	}

	public void unregisterTeamIfEmpty(Team team) {
		if (team != null) {
			if (team.getSize() == 0) {
				unregisterTeam(team);
			}
			team = null;
		}
	}

	public void unregisterTeamIfEmptyToPlayer(Player player, String teamID) {
		unregisterTeamIfEmpty(getTeamFromPlayer(player, teamID));
		teamID = null;
		player = null;
	}

	public void unregisterTeamForEmptyForPlayers(Collection<? extends Player> players, String teamID) {
		for (Player player : players) {
			unregisterTeamIfEmptyToPlayer(player, teamID);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void unregisterTeamIfEmptyForOnlinePlayers(String teamID) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			unregisterTeamIfEmptyToPlayer(player, teamID);
		}
	}

	public void setTeamPrefix(Team team, String prefix) {
		if (team != null) {
			if (prefix.length() > 16) {
				prefix = prefix.substring(0, 16);
			}
			team.setPrefix(prefix);
			team = null;
		}
		prefix = null;
	}

	public void setTeamPrefixToPlayer(Player player, String teamID, String prefix) {
		setTeamPrefix(getTeamFromPlayer(player, teamID), prefix);
		player = null;
		teamID = null;
	}

	public void setTeamPrefixForPlayers(Collection<? extends Player> players, String teamID, String prefix) {
		for (Player player : players) {
			setTeamPrefixToPlayer(player, teamID, prefix);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void setTeamPrefixForOnlinePlayers(String teamID, String prefix) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			setTeamPrefixToPlayer(player, teamID, prefix);
		}
	}

	public void setTeamSuffix(Team team, String suffix) {
		if (team != null) {
			if (suffix.length() > 16) {
				suffix = suffix.substring(0, 16);
			}
			team.setSuffix(suffix);
			team = null;
		}
		suffix = null;
	}

	public void setTeamSuffixToPlayer(Player player, String teamID, String suffix) {
		setTeamSuffix(getTeamFromPlayer(player, teamID), suffix);
		player = null;
		teamID = null;
	}

	public void setTeamSuffixForPlayers(Collection<? extends Player> players, String teamID, String suffix) {
		for (Player player : players) {
			setTeamSuffixToPlayer(player, teamID, suffix);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void setTeamSuffixForOnlinePlayers(String teamID, String suffix) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			setTeamSuffixToPlayer(player, teamID, suffix);
		}
	}

	public void setTeamPrefixAndSuffix(Team team, String prefix, String suffix) {
		if (team != null) {
			if (prefix.length() > 16) {
				prefix = prefix.substring(0, 16);
			}
			if (suffix.length() > 16) {
				suffix = suffix.substring(0, 16);
			}
			team.setPrefix(prefix);
			team.setSuffix(suffix);
			team = null;
		}
		prefix = null;
		suffix = null;
	}

	public void setTeamPrefixAndSuffixToPlayer(Player player, String teamID, String prefix, String suffix) {
		setTeamPrefixAndSuffix(getTeamFromPlayer(player, teamID), prefix, suffix);
		player = null;
		teamID = null;
	}

	public void setTeamPrefixAndSuffixForPlayers(Collection<? extends Player> players, String teamID, String prefix, String suffix) {
		for (Player player : players) {
			setTeamPrefixAndSuffixToPlayer(player, teamID, prefix, suffix);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void setTeamPrefixAndSuffixForOnlinePlayers(String teamID, String prefix, String suffix) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			setTeamPrefixAndSuffixToPlayer(player, teamID, prefix, suffix);
		}
	}

	public void setTeamDisplayName(Team team, String name) {
		if (team != null) {
			if (name.length() > 16) {
				name = name.substring(0, 16);
			}
			team.setDisplayName(name);
			team = null;
		}
		name = null;
	}

	public void setTeamDisplayNameToPlayer(Player player, String teamID, String name) {
		setTeamDisplayName(getTeamFromPlayer(player, teamID), name);
		player = null;
	}

	public void setTeamDisplayNameForPlayers(Collection<? extends Player> players, String teamID, String name) {
		for (Player player : players) {
			setTeamDisplayNameToPlayer(player, teamID, name);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void setTeamDisplayNameForOnlinesPlayers(String teamID, String name) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			setTeamDisplayNameToPlayer(player, teamID, name);
		}
	}

	public Objective getObjectiveFromPlayer(Player player, String objectiveID) {
		if (objectiveID.length() > 16) {
			objectiveID = objectiveID.substring(0, 16);
		}
		return player.getScoreboard().getObjective(objectiveID);
	}

	public Objective getObjectiveFromPlayer(Player player, DisplaySlot displaySlot) {
		return player.getScoreboard().getObjective(displaySlot);
	}

	public Objective createObjectiveToPlayer(Player player, String objectiveID, String displayName, DisplaySlot displaySlot) {
		Objective objective = getObjectiveFromPlayer(player, objectiveID);
		if (objective != null) {
			if (objectiveID.length() > 16) {
				objectiveID = objectiveID.substring(0, 16);
			}
			objective = player.getScoreboard().registerNewObjective(objectiveID, "battleboard");
		}
		if (displayName.length() > 16) {
			displayName = displayName.substring(0, 16);
		}
		objective.setDisplayName(displayName);
		objective.setDisplaySlot(displaySlot);
		displaySlot = null;
		displayName = null;
		objective = null;
		player = null;
		return objective;
	}

	public void createObjectiveForPlayers(Collection<? extends Player> players, String objectiveID, String displayName, DisplaySlot displaySlot) {
		for (Player player : players) {
			createObjectiveToPlayer(player, objectiveID, displayName, displaySlot);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void createObjectiveForOnlinePlayers(String objectiveID, String displayName, DisplaySlot displaySlot) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			createObjectiveToPlayer(player, objectiveID, displayName, displaySlot);
		}
	}

	public Objective createObjectiveIfNotExistsToPlayer(Player player, String objectiveID, String displayName, DisplaySlot displaySlot) {
		Objective objective = getObjectiveFromPlayer(player, objectiveID);
		if (objective == null) {
			createObjectiveToPlayer(player, objectiveID, displayName, displaySlot);
		}
		return objective;
	}

	public void createObjectiveIfNotExistsToPlayer(Collection<? extends Player> players, String objectiveID, String displayName,
			DisplaySlot displaySlot) {
		for (Player player : players) {
			createObjectiveIfNotExistsToPlayer(player, objectiveID, displayName, displaySlot);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void createObjectiveIfNotExistsForOnlinePlayers(String objectiveID, String displayName, DisplaySlot displaySlot) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			createObjectiveIfNotExistsToPlayer(player, objectiveID, displayName, displaySlot);
		}
	}

	public void setObjectiveDisplayName(Objective objective, String name) {
		if (objective != null) {
			if (name.length() > 16) {
				name = name.substring(0, 16);
			}
			objective.setDisplayName(name);
			objective = null;
		}
		name = null;
	}

	public void setObjectiveDisplayNameToPlayer(Player player, String objectiveID, String name) {
		setObjectiveDisplayName(getObjectiveFromPlayer(player, objectiveID), name);
		player = null;
		objectiveID = null;
	}

	public void setObjectiveDisplayNameForPlayers(Collection<? extends Player> players, String objectiveID, String name) {
		for (Player player : players) {
			setObjectiveDisplayNameToPlayer(player, objectiveID, name);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void setObjectiveDisplayNameForOnlinePlayers(String objectiveID, String name) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			setObjectiveDisplayNameToPlayer(player, objectiveID, name);
		}
	}

	public void setObjectiveDisplaySlot(Objective objective, DisplaySlot displaySlot) {
		if (objective != null) {
			objective.setDisplaySlot(displaySlot);
			objective = null;
		}
	}

	public void setObjectiveDisplaySlotToPlayer(Player player, String objectiveID, DisplaySlot displaySlot) {
		setObjectiveDisplaySlot(getObjectiveFromPlayer(player, objectiveID), displaySlot);
		player = null;
		objectiveID = null;
	}

	public void setObjectiveDisplaySlotForPlayers(Collection<? extends Player> players, String objectiveID, DisplaySlot displaySlot) {
		for (Player player : players) {
			setObjectiveDisplaySlotToPlayer(player, objectiveID, displaySlot);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void setObjectiveDisplaySlotForOnlinePlayers(String objectiveID, DisplaySlot displaySlot) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			setObjectiveDisplaySlotToPlayer(player, objectiveID, displaySlot);
		}
	}

	public void setScoreOnObjective(Objective objective, String scoreName, int scoreValue) {
		if (scoreName.length() > 16) {
			scoreName = scoreName.substring(0, 16);
		}
		if (objective != null) {
			objective.getScore(scoreName).setScore(scoreValue);
			objective = null;
		}
		scoreName = null;
	}

	public void setScoreOnObjectiveToPlayer(Player player, String objectiveID, String scoreName, int scoreValue) {
		setScoreOnObjective(getObjectiveFromPlayer(player, objectiveID), scoreName, scoreValue);
		player = null;
		objectiveID = null;
	}

	public void setScoreOnObjectiveToPlayer(Player player, DisplaySlot objectiveSlot, String scoreName, int scoreValue) {
		setScoreOnObjective(getObjectiveFromPlayer(player, objectiveSlot), scoreName, scoreValue);
		player = null;
	}

	public void setScoreOnObjectiveForPlayers(Collection<? extends Player> players, String objectiveID, String scoreName, int scoreValue) {
		for (Player player : players) {
			setScoreOnObjective(getObjectiveFromPlayer(player, objectiveID), scoreName, scoreValue);
		}
		players = null;
	}

	public void setScoreOnObjectiveForPlayers(Collection<? extends Player> players, DisplaySlot objectiveSlot, String scoreName, int scoreValue) {
		for (Player player : players) {
			setScoreOnObjective(getObjectiveFromPlayer(player, objectiveSlot), scoreName, scoreValue);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void setScoreOnObjectiveForOnlinePlayers(String objectiveID, String scoreName, int scoreValue) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			setScoreOnObjective(getObjectiveFromPlayer(player, objectiveID), scoreName, scoreValue);
		}
	}

	@SuppressWarnings("deprecation")
	public void setScoreOnObjectiveForOnlinePlayers(DisplaySlot objectiveSlot, String scoreName, int scoreValue) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			setScoreOnObjective(getObjectiveFromPlayer(player, objectiveSlot), scoreName, scoreValue);
		}
	}

	public void addScoreOnObjectiveToPlayer(Player player, Objective objective, String scoreID, int score, String name, String prefix,
			String suffix) {
		if (objective != null) {
			Team team = createTeamIfNotExistsToPlayer(player, objective.getName() + scoreID, prefix, suffix);
			if (team != null) {
				if (name.length() > 16) {
					name = name.substring(0, 16);
				}
				setScoreOnObjective(objective, name, score);
				joinTeam(team, name);
				team = null;
				name = null;
			}
		}
		scoreID = null;
		prefix = null;
		suffix = null;
		player = null;
	}

	public void addScoreOnObjectiveToPlayer(Player player, String objectiveID, String scoreID, int score, String name, String prefix, String suffix) {
		addScoreOnObjectiveToPlayer(player, getObjectiveFromPlayer(player, objectiveID), scoreID, score, name, prefix, suffix);
		objectiveID = null;
	}

	public void addScoreOnObjectiveToPlayer(Player player, DisplaySlot objectiveSlot, String scoreID, int score, String name, String prefix,
			String suffix) {
		addScoreOnObjectiveToPlayer(player, getObjectiveFromPlayer(player, objectiveSlot), scoreID, score, name, prefix, suffix);
	}

	public void addScoreOnObjectiveForPlayers(Collection<? extends Player> players, String objectiveID, String scoreID, int score, String name,
			String prefix, String suffix) {
		for (Player player : players) {
			addScoreOnObjectiveToPlayer(player, objectiveID, scoreID, score, name, prefix, suffix);
		}
		players = null;
	}

	public void addScoreOnObjectiveForPlayers(Collection<? extends Player> players, DisplaySlot objectiveSlot, String scoreID, int score, String name,
			String prefix, String suffix) {
		for (Player player : players) {
			addScoreOnObjectiveToPlayer(player, objectiveSlot, scoreID, score, name, prefix, suffix);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void addScoreOnObjectiveForOnlinePlayers(String objectiveID, String scoreID, int score, String name, String prefix, String suffix) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			addScoreOnObjectiveToPlayer(player, getObjectiveFromPlayer(player, objectiveID), scoreID, score, name, prefix, suffix);
		}
	}

	@SuppressWarnings("deprecation")
	public void addScoreOnObjectiveForOnlinePlayers(DisplaySlot objectiveSlot, String scoreID, int score, String name, String prefix, String suffix) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			addScoreOnObjectiveToPlayer(player, objectiveSlot, scoreID, score, name, prefix, suffix);
		}
	}

	public void updateScoreNameOnObjectiveToPlayer(Player player, Objective objective, String scoreID, String name) {
		if (objective != null) {
			Team team = getTeamFromPlayer(player, objective.getName());
			if (team != null) {
				setTeamPrefix(team, name);
				team = null;
			}
		}
		scoreID = null;
		name = null;
		player = null;
	}

	public void updateScoreNameOnObjectiveToPlayer(Player player, String objectiveID, String scoreID, String name) {
		updateScoreNameOnObjectiveToPlayer(player, getObjectiveFromPlayer(player, objectiveID), scoreID, name);
		objectiveID = null;
	}

	public void updateScoreNameOnObjectiveToPlayer(Player player, DisplaySlot objectiveSlot, String scoreID, String name) {
		updateScoreNameOnObjectiveToPlayer(player, getObjectiveFromPlayer(player, objectiveSlot), scoreID, name);
	}

	public void updateScoreNameOnObjectiveForPlayers(Collection<? extends Player> players, String objectiveID, String scoreID, String name) {
		for (Player player : players) {
			updateScoreNameOnObjectiveToPlayer(player, objectiveID, scoreID, name);
		}
		players = null;
	}

	public void updateScoreNameOnObjectiveForPlayers(Collection<? extends Player> players, DisplaySlot objectiveSlot, String scoreID, String name) {
		for (Player player : players) {
			updateScoreNameOnObjectiveToPlayer(player, objectiveSlot, scoreID, name);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void updateScoreNameOnObjectiveForOnlinePlayers(String objectiveID, String scoreID, String name) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			updateScoreNameOnObjectiveToPlayer(player, objectiveID, scoreID, name);
		}
	}

	@SuppressWarnings("deprecation")
	public void updateScoreNameOnObjectiveForOnlinePlayers(DisplaySlot objectiveSlot, String scoreID, String name) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			updateScoreNameOnObjectiveToPlayer(player, getObjectiveFromPlayer(player, objectiveSlot), scoreID, name);
		}
	}

	public void updateScoreValueOnObjectiveToPlayer(Player player, Objective objective, String scoreID, String value) {
		if (objective != null) {
			Team team = getTeamFromPlayer(player, objective.getName());
			if (team != null) {
				setTeamSuffix(team, value);
				team = null;
			}
		}
		scoreID = null;
		value = null;
		player = null;
	}

	public void updateScoreValueOnObjectiveToPlayer(Player player, String objectiveID, String scoreID, String value) {
		updateScoreValueOnObjectiveToPlayer(player, getObjectiveFromPlayer(player, objectiveID), scoreID, value);
		objectiveID = null;
	}

	public void updateScoreValueOnObjectiveToPlayer(Player player, DisplaySlot objectiveSlot, String scoreID, String value) {
		updateScoreValueOnObjectiveToPlayer(player, getObjectiveFromPlayer(player, objectiveSlot), scoreID, value);
	}

	public void updateScoreValueOnObjectiveForPlayers(Collection<? extends Player> players, String objectiveID, String scoreID, String value) {
		for (Player player : players) {
			updateScoreValueOnObjectiveToPlayer(player, objectiveID, scoreID, value);
		}
		players = null;
	}

	public void updateScoreValueOnObjectiveForPlayers(Collection<? extends Player> players, DisplaySlot objectiveSlot, String scoreID, String value) {
		for (Player player : players) {
			updateScoreValueOnObjectiveToPlayer(player, objectiveSlot, scoreID, value);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void updateScoreValueOnObjectiveForOnlinePlayers(String objectiveID, String scoreID, String value) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			updateScoreValueOnObjectiveToPlayer(player, objectiveID, scoreID, value);
		}
	}

	@SuppressWarnings("deprecation")
	public void updateScoreValueOnObjectiveForOnlinePlayers(DisplaySlot objectiveSlot, String scoreID, String value) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			updateScoreValueOnObjectiveToPlayer(player, objectiveSlot, scoreID, value);
		}
	}

	public void updateScoreNameAndValueOnObjectiveToPlayer(Player player, Objective objective, String scoreID, String name, String value) {
		if (objective != null) {
			Team team = getTeamFromPlayer(player, objective.getName());
			if (team != null) {
				setTeamPrefix(team, name);
				setTeamSuffix(team, value);
				team = null;
			}
		}
		scoreID = null;
		value = null;
		player = null;
	}

	public void updateScoreNameAndValueOnObjectiveToPlayer(Player player, String objectiveID, String scoreID, String name, String value) {
		updateScoreNameAndValueOnObjectiveToPlayer(player, getObjectiveFromPlayer(player, objectiveID), scoreID, name, value);
		objectiveID = null;
	}

	public void updateScoreNameAndValueOnObjectiveToPlayer(Player player, DisplaySlot objectiveSlot, String scoreID, String name, String value) {
		updateScoreNameAndValueOnObjectiveToPlayer(player, getObjectiveFromPlayer(player, objectiveSlot), scoreID, name, value);
	}

	public void updateScoreNameAndValueOnObjectiveForPlayers(Collection<? extends Player> players, String objectiveID, String scoreID, String name,
			String value) {
		for (Player player : players) {
			updateScoreNameAndValueOnObjectiveToPlayer(player, objectiveID, scoreID, name, value);
		}
		players = null;
	}

	public void updateScoreNameAndValueOnObjectiveForPlayers(Collection<? extends Player> players, DisplaySlot objectiveSlot, String scoreID,
			String name, String value) {
		for (Player player : players) {
			updateScoreNameAndValueOnObjectiveToPlayer(player, objectiveSlot, scoreID, name, value);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void updateScoreNameAndValueOnObjectiveForOnlinePlayers(String objectiveID, String scoreID, String name, String value) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			updateScoreNameAndValueOnObjectiveToPlayer(player, objectiveID, scoreID, name, value);
		}
	}

	@SuppressWarnings("deprecation")
	public void updateScoreNameAndValueOnObjectiveForOnlinePlayers(DisplaySlot objectiveSlot, String scoreID, String name, String value) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			updateScoreNameAndValueOnObjectiveToPlayer(player, objectiveSlot, scoreID, name, value);
		}
	}

	public void updateScoreOnObjectiveToPlayer(Player player, Objective objective, String scoreID, String score) {
		if (objective != null) {
			Team team = getTeamFromPlayer(player, objective.getName());
			if (team != null) {
				String name = score;
				String value = "";
				if (score.length() > 16) {
					name = score.substring(0, 16);
					value = score.substring(16, score.length());
				}
				setTeamPrefix(team, name);
				setTeamSuffix(team, value);
				name = null;
				value = null;
				team = null;
			}
		}
		scoreID = null;
		score = null;
		player = null;
	}

	public void updateScoreOnObjectiveToPlayer(Player player, String objectiveID, String scoreID, String score) {
		updateScoreOnObjectiveToPlayer(player, getObjectiveFromPlayer(player, objectiveID), scoreID, score);
		objectiveID = null;
	}

	public void updateScoreOnObjectiveToPlayer(Player player, DisplaySlot objectiveSlot, String scoreID, String score) {
		updateScoreOnObjectiveToPlayer(player, getObjectiveFromPlayer(player, objectiveSlot), scoreID, score);
	}

	public void updateScoreOnObjectiveForPlayers(Collection<? extends Player> players, String objectiveID, String scoreID, String score) {
		for (Player player : players) {
			updateScoreOnObjectiveToPlayer(player, objectiveID, scoreID, score);
		}
		players = null;
	}

	public void updateScoreOnObjectiveForPlayers(Collection<? extends Player> players, DisplaySlot objectiveSlot, String scoreID, String score) {
		for (Player player : players) {
			updateScoreOnObjectiveToPlayer(player, objectiveSlot, scoreID, score);
		}
		players = null;
	}

	@SuppressWarnings("deprecation")
	public void updateScoreOnObjectiveForOnlinePlayers(String objectiveID, String scoreID, String score) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			updateScoreOnObjectiveToPlayer(player, objectiveID, scoreID, score);
		}
	}

	@SuppressWarnings("deprecation")
	public void updateScoreOnObjectiveForOnlinePlayers(DisplaySlot objectiveSlot, String scoreID, String score) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			updateScoreOnObjectiveToPlayer(player, objectiveSlot, scoreID, score);
		}
	}

}
