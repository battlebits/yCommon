package br.com.battlebits.ycommon.bungee;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandLoader;
import br.com.battlebits.ycommon.bungee.event.UpdateScheduler;
import br.com.battlebits.ycommon.bungee.listeners.LoginListener;
import br.com.battlebits.ycommon.bungee.listeners.PlayerListener;
import br.com.battlebits.ycommon.bungee.listeners.QuitListener;
import br.com.battlebits.ycommon.bungee.managers.AccountManager;
import br.com.battlebits.ycommon.bungee.managers.BanManager;
import br.com.battlebits.ycommon.bungee.networking.CommonServer;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.connection.backend.MySQLBackend;
import br.com.battlebits.ycommon.common.enums.BattleInstance;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeeMain extends Plugin {

	private static BungeeMain plugin;
	private MySQLBackend mysql;
	// MYSQL DATA
	private String hostname = "localhost";
	private int port = 3306;
	private String database = "ycommon";
	private String username = "ycommon";
	private String password = "qhu6fvCu5X8ZH7TY";

	private Configuration config;

	private CommonServer commonServer;
	private AccountManager accountManager;
	private BanManager banManager;
	private BungeeCommandFramework commandFramework;
	private BungeeCommandLoader commandLoader;

	{
		plugin = this;
	}

	@Override
	public void onLoad() {
		BattlebitsAPI.setBattleInstance(BattleInstance.BUNGEECORD);
	}

	@Override
	public void onEnable() {
		// loadConfiguration();
		banManager = new BanManager();
		accountManager = new AccountManager();
		try {
			getProxy().getScheduler().runAsync(this, commonServer = new CommonServer());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		mysql = new MySQLBackend(hostname, port, database, username, password);
		try {
			mysql.startConnection();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		loadTranslations();
		getProxy().getScheduler().schedule(this, new UpdateScheduler(), 0, 50, TimeUnit.MILLISECONDS);
		getProxy().registerChannel(BattlebitsAPI.getBungeeChannel());
		loadListeners();
		try {
			commandFramework = new BungeeCommandFramework(plugin);
			commandLoader = new BungeeCommandLoader(commandFramework);
			commandLoader.loadCommandsFromPackage("br.com.battlebits.ycommon.bungee.commands.register");
		} catch (Exception e) {
			BattlebitsAPI.getLogger().warning("Erro ao carregar o commandFramework!");
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		try {
			commonServer.stopServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			mysql.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		getProxy().getScheduler().cancel(plugin);
		config = null;
		commonServer = null;
		plugin = null;
		mysql = null;
		hostname = null;
		port = 0;
		database = null;
		username = null;
		password = null;
		banManager = null;
		accountManager = null;
	}

	private void loadListeners() {
		getProxy().getPluginManager().registerListener(this, new PlayerListener());
		getProxy().getPluginManager().registerListener(this, new LoginListener());
		getProxy().getPluginManager().registerListener(this, new QuitListener());
	}

	@SuppressWarnings("unused")
	private void loadConfiguration() {
		hostname = config.getString("database.hostname");
		port = config.getInt("database.port");
		database = config.getString("database.database");
		username = config.getString("database.username");
		password = config.getString("database.password");
	}

	private void loadTranslations() {
		try {
			BattlebitsAPI.debug("TRANSLATIONS > LOADING");
			PreparedStatement stmt = null;
			ResultSet result = null;
			for (Language lang : Language.values()) {
				try {
					stmt = getConnection().getConnection().prepareStatement("SELECT * FROM `translations` WHERE `language`='" + lang + "';");
					result = stmt.executeQuery();
					if (result.next()) {
						Translate.loadTranslations(lang, result.getString("json"));
						BattlebitsAPI.debug(lang.toString() + " > LOADED");

					}
					result.close();
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
					BattlebitsAPI.debug(lang.toString() + " > FAILED");
				}
			}
			result = null;
			stmt = null;
			BattlebitsAPI.debug("TRANSLATIONS > CLOSE");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public MySQLBackend getConnection() {
		return mysql;
	}

	public AccountManager getAccountManager() {
		return accountManager;
	}
	
	public BanManager getBanManager() {
		return banManager;
	}

	public BungeeCommandLoader getCommandLoader() {
		return commandLoader;
	}

	public static BungeeMain getPlugin() {
		return plugin;
	}

}
