package br.com.battlebits.ycommon.bungee.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.managers.ServerManager;
import br.com.battlebits.ycommon.bungee.servers.BattleServer;
import br.com.battlebits.ycommon.bungee.servers.HungerGamesServer;
import br.com.battlebits.ycommon.bungee.servers.HungerGamesServer.HungerGamesState;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MessageListener implements Listener {

	private ServerManager manager;

	public MessageListener(ServerManager manager) {
		this.manager = manager;
	}

	@EventHandler
	public void onPluginMessageBungeeCordChannel(PluginMessageEvent event) {
		if (!event.getTag().equals("BungeeCord"))
			return;
		if (!(event.getSender() instanceof Server))
			return;
		if (!(event.getReceiver() instanceof ProxiedPlayer))
			return;
		Server serverSender = (Server) event.getSender();
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer) event.getReceiver();
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(proxiedPlayer.getUniqueId());
		ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
		String subChannel = in.readUTF();
		switch (subChannel) {
		case "ServerInfo": {
			event.setCancelled(true);
			String serverName = in.readUTF();
			BattleServer server = manager.getServer(serverName);
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("ServerInfo");
			out.writeUTF(serverName);
			int online = 0;
			int max = 0;
			int tempo = 0;
			HungerGamesState state = HungerGamesState.NONE;
			if (server != null) {
				online = server.getOnlinePlayers();
				max = server.getMaxPlayers();
				if (server instanceof HungerGamesServer) {
					state = ((HungerGamesServer) server).getState();
					tempo = ((HungerGamesServer) server).getTempo();
				}
			}
			out.writeInt(online);
			out.writeInt(max);
			out.writeInt(tempo);
			out.writeUTF(state.toString());
			serverSender.sendData("BungeeCord", out.toByteArray());
			break;
		}
		case "NetworkCount": {
			event.setCancelled(true);
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("NetworkCount");
			out.writeInt(BungeeMain.getPlugin().getProxy().getOnlineCount());
			serverSender.sendData("BungeeCord", out.toByteArray());
			break;
		}
		case "DoubleKitHGCount": {
			event.setCancelled(true);
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("DoubleKitHGCount");
			out.writeInt(manager.getDoubleKitHGBalancer().getTotalNumber());
			serverSender.sendData("BungeeCord", out.toByteArray());
			break;
		}
		case "HGCount": {
			event.setCancelled(true);
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("HGCount");
			out.writeInt(manager.getHgBalancer().getTotalNumber());
			serverSender.sendData("BungeeCord", out.toByteArray());
			break;
		}
		case "CustomHGCount": {
			event.setCancelled(true);
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("CustomHGCount");
			out.writeInt(manager.getCustomHgBalancer().getTotalNumber());
			serverSender.sendData("BungeeCord", out.toByteArray());
			break;
		}
		case "FPCount": {
			event.setCancelled(true);
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("FPCount");
			out.writeInt(0);
			serverSender.sendData("BungeeCord", out.toByteArray());
			break;
		}
		case "SimulatorCount": {
			event.setCancelled(true);
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("SimulatorCount");
			out.writeInt(manager.getPeladoBalancer().getTotalNumber());
			serverSender.sendData("BungeeCord", out.toByteArray());
			break;
		}
		case "FullironCount": {
			event.setCancelled(true);
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("FullironCount");
			out.writeInt(manager.getFullIronBalancer().getTotalNumber());
			serverSender.sendData("BungeeCord", out.toByteArray());
			break;
		}
		case "Hungergames": {
			event.setCancelled(true);
			BattleServer server = manager.getHgBalancer().next();
			if (server != null && server.getServerInfo() != null) {
				if (!server.isFull() || (server.isFull() && player.hasGroupPermission(Group.ULTIMATE))) {
					proxiedPlayer.connect(server.getServerInfo());
					break;
				}
			}
			proxiedPlayer.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(proxiedPlayer.getUniqueId()).getLanguage(), "server-not-available")));
			break;
		}
		case "CustomHungergames": {
			event.setCancelled(true);
			BattleServer server = manager.getCustomHgBalancer().next();
			if (server != null && server.getServerInfo() != null) {
				if (!server.isFull() || (server.isFull() && player.hasGroupPermission(Group.ULTIMATE))) {
					proxiedPlayer.connect(server.getServerInfo());
					break;
				}
			}
			proxiedPlayer.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(proxiedPlayer.getUniqueId()).getLanguage(), "server-not-available")));
			break;
		}
		case "DoubleKitHungergames": {
			event.setCancelled(true);
			BattleServer server = manager.getDoubleKitHGBalancer().next();
			if (server != null && server.getServerInfo() != null) {
				if (!server.isFull() || (server.isFull() && player.hasGroupPermission(Group.ULTIMATE))) {
					proxiedPlayer.connect(server.getServerInfo());
					break;
				}
			}
			proxiedPlayer.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(proxiedPlayer.getUniqueId()).getLanguage(), "server-not-available")));
			break;
		}
		case "Fairplayhg": {
			event.setCancelled(true);
			proxiedPlayer.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(proxiedPlayer.getUniqueId()).getLanguage(), "server-not-available")));
			break;
		}

		case "PVPFulliron": {
			event.setCancelled(true);
			BattleServer server = manager.getFullIronBalancer().next();
			if (server != null && server.getServerInfo() != null) {
				if (!server.isFull() || (server.isFull() && player.hasGroupPermission(Group.ULTIMATE))) {
					proxiedPlayer.connect(server.getServerInfo());
					break;
				}
			}
			proxiedPlayer.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(proxiedPlayer.getUniqueId()).getLanguage(), "server-not-available")));
			break;
		}

		case "PVPSimulator": {
			event.setCancelled(true);
			BattleServer server = manager.getPeladoBalancer().next();
			if (server != null && server.getServerInfo() != null) {
				if (!server.isFull() || (server.isFull() && player.hasGroupPermission(Group.ULTIMATE))) {
					proxiedPlayer.connect(server.getServerInfo());
					break;
				}
			}
			proxiedPlayer.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(proxiedPlayer.getUniqueId()).getLanguage(), "server-not-available")));
			break;
		}
		
		case "Lobby": {
			event.setCancelled(true);
			BattleServer server = manager.getLobbyBalancer().next();
			if (server != null && server.getServerInfo() != null) {
				if (!server.isFull() || (server.isFull() && player.hasGroupPermission(Group.ULTIMATE))) {
					proxiedPlayer.connect(server.getServerInfo());
					break;
				}
			}
			proxiedPlayer.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(proxiedPlayer.getUniqueId()).getLanguage(), "server-not-available")));
			break;
		}
		default:
			break;
		}
	}

	@EventHandler
	public void onPluginMessageBattlebitsChannel(PluginMessageEvent event) {
		if (!event.getTag().equals(BattlebitsAPI.getBungeeCordChannel()))
			return;
		if (!(event.getSender() instanceof Server))
			return;
		if (!(event.getReceiver() instanceof ProxiedPlayer))
			return;
		// ProxiedPlayer proxiedPlayer = (ProxiedPlayer) event.getReceiver();
		// ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
		// String subchannel = in.readUTF();
	}

}
