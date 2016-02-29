package br.com.battlebits.ycommon.bungee;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.battlebits.ycommon.bungee.listeners.LoginListener;
import br.com.battlebits.ycommon.bungee.listeners.QuitListener;
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
	private static Gson gson = new Gson();
	// MYSQL DATA
	private String hostname = "localhost";
	private int port = 3306;
	private String database = "ycommon";
	private String username = "root";
	private String password = "";

	private Configuration config;

	private CommonServer commonServer;
	private BanManager banManager;

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
		getProxy().registerChannel(BattlebitsAPI.getBungeeChannel());
		loadListeners();
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
	}

	private void loadListeners() {
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
			PreparedStatement stmt = getConnection().getConnection().prepareStatement("SELECT * FROM `translations`;");
			ResultSet result = stmt.executeQuery();
			BattlebitsAPI.debug("TRANSLATIONS > EXCUTED");
			if (result.next()) {
				for (Language lang : Language.values()) {
					String json = result.getString(lang.toString());
					if (json != null) {
						HashMap<String, String> translation = BungeeMain.getGson().fromJson(json, new TypeToken<HashMap<String, String>>() {
						}.getType());
						Translate.loadTranslations(lang, translation);
						BattlebitsAPI.debug(lang.toString() + " > LOADED");
					} else {
						BattlebitsAPI.debug(lang.toString() + " > FAILED");
					}
				}
			}
			result.close();
			stmt.close();
			result = null;
			stmt = null;
			BattlebitsAPI.debug("TRANSLATIONS > CLOSE");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public MySQLBackend getConnection() {
		return mysql;
	}

	public static Gson getGson() {
		return gson;
	}

	public BanManager getBanManager() {
		return banManager;
	}

	public static BungeeMain getPlugin() {
		return plugin;
	}

}
