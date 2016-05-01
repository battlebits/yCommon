package br.com.battlebits.ycommon.bukkit.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class PluginUpdater {

	private boolean running;
	private Plugin plugin;
	private String pluginName;
	private String versaoAtual;
	private String versaoUpdate;
	private String downloadURL = "http://127.0.0.1/hkjaosdja3sd/update/";
	private boolean needUpdate = true;

	public PluginUpdater(Plugin plugin) {
		this.plugin = plugin;
		this.pluginName = plugin.getName();
		versaoAtual = plugin.getDescription().getVersion();
	}

	public boolean run() {
		if (!needUpdate)
			return false;
		if (running)
			return false;
		running = true;
		try {
			URL url = new URL(downloadURL + pluginName + "/version.txt");
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				versaoUpdate = inputLine;
			}
			if (versaoAtual.equals(versaoUpdate)) {
				System.out.println("============================");
				System.out.println("Plugin " + pluginName + " ja esta atualizado");
				System.out.println("============================");
				running = false;
				return false;
			}
			return downloadUpdate();
		} catch (Exception e) {
			System.out.println("============================");
			System.out.println("Erro ao procurar atualização de " + pluginName);
			System.out.println(downloadURL);
			System.out.println("============================");
			e.printStackTrace();
			running = false;
			return false;
		}
	}

	private boolean downloadUpdate() {
		try {
			File to = new File(plugin.getServer().getUpdateFolderFile(), pluginName + ".jar");
			File tmp = new File(to.getPath() + ".au");
			if (!tmp.exists()) {
				plugin.getServer().getUpdateFolderFile().mkdirs();
				tmp.createNewFile();
			}
			URL url = new URL(downloadURL + pluginName + "/" + pluginName + ".jar");
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(tmp);
			byte[] buffer = new byte[4096];
			int fetched;
			while ((fetched = is.read(buffer)) != -1)
				os.write(buffer, 0, fetched);
			is.close();
			os.flush();
			os.close();
			if (to.exists())
				to.delete();
			tmp.renameTo(to);
			System.out.println("============================");
			System.out.println("Atualizacao de " + pluginName + " baixada com sucesso! Servidor reiniciando");
			System.out.println("============================");
			needUpdate = false;
			running = false;
			Bukkit.getPluginManager().disablePlugin(plugin);
			plugin.getServer().shutdown();
			return true;
		} catch (Exception e) {
			System.out.println("============================");
			System.out.println("Erro ao tentar baixar update de " + pluginName);
			System.out.println("============================");
			e.printStackTrace();
			running = false;
			return false;
		}
	}

}