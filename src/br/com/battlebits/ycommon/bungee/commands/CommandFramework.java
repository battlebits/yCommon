package br.com.battlebits.ycommon.bungee.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class CommandFramework {

	private final Map<String, Entry<Method, Object>> commandMap = new HashMap<String, Entry<Method, Object>>();
	private Map<String, net.md_5.bungee.api.plugin.Command> map;
	private final Plugin plugin;

	public CommandFramework(Plugin plugin) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		this.plugin = plugin;
		map = getCommandMap();
	}

	@SuppressWarnings("unchecked")
	private Map<String, net.md_5.bungee.api.plugin.Command> getCommandMap() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Class<?> pluginManager = plugin.getProxy().getPluginManager().getClass();
		Field field = pluginManager.getField("commandMap");
		field.setAccessible(true);
		Object obj = field.get(plugin.getProxy().getPluginManager());
		return (HashMap<String, net.md_5.bungee.api.plugin.Command>) obj;
	}

	public boolean handleCommand(CommandSender sender, String label, String[] args) {
		for (int i = args.length; i >= 0; i--) {
			StringBuilder buffer = new StringBuilder();
			buffer.append(label.toLowerCase());
			for (int x = 0; x < i; x++) {
				buffer.append(".").append(args[x].toLowerCase());
			}
			String cmdLabel = buffer.toString();
			if (commandMap.containsKey(cmdLabel)) {
				Entry<Method, Object> entry = commandMap.get(cmdLabel);
				Command command = entry.getKey().getAnnotation(Command.class);
				if (!sender.hasPermission(command.permission())) {
					sender.sendMessage(TextComponent.fromLegacyText(command.noPerm()));
					return true;
				}
				try {
					entry.getKey().invoke(entry.getValue(), new CommandArgs(sender, label, args, cmdLabel.split("\\.").length - 1));
				} catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
					e.printStackTrace();
				}
				return true;
			}
		}
		defaultCommand(new CommandArgs(sender, label, args, 0));
		return true;
	}

	public void registerCommands(Object obj) {
		for (Method m : obj.getClass().getMethods()) {
			if (m.getAnnotation(Command.class) != null) {
				Command command = m.getAnnotation(Command.class);
				if (m.getParameterTypes().length > 1 || m.getParameterTypes()[0] != CommandArgs.class) {
					System.out.println("Unable to register command " + m.getName() + ". Unexpected method arguments");
					continue;
				}
				registerCommand(command, command.name(), m, obj);
				for (String alias : command.aliases()) {
					registerCommand(command, alias, m, obj);
				}
			}
		}
	}

	/**
	 * Registers all the commands under the plugin's help
	 */

	private void registerCommand(Command command, String label, Method m, Object obj) {
		Entry<Method, Object> entry = new AbstractMap.SimpleEntry<Method, Object>(m, obj);
		commandMap.put(label.toLowerCase(), entry);
		String cmdLabel = label.replace(".", ",").split(",")[0].toLowerCase();
		if (map.get(cmdLabel) == null) {
			net.md_5.bungee.api.plugin.Command cmd = new BungeeCommand(cmdLabel);
			plugin.getProxy().getPluginManager().registerCommand(plugin, cmd);
		}
	}

	private void defaultCommand(CommandArgs args) {
		args.getSender().sendMessage(TextComponent.fromLegacyText(args.getLabel() + " is not handled! Oh noes!"));
	}

	class BungeeCommand extends net.md_5.bungee.api.plugin.Command {

		protected BungeeCommand(String label) {
			super(label);
		}

		@Override
		public void execute(CommandSender sender, String[] args) {
			handleCommand(sender, getName(), args);
		}

	}

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Command {

		public String name();

		public String permission() default "";

		public String noPerm() default "You do not have permission to perform that action";

		public String[] aliases() default {};

		public String description() default "";

		public String usage() default "";
	}

	public class CommandArgs {

		private final CommandSender sender;
		private final String label;
		private final String[] args;

		protected CommandArgs(CommandSender sender, String label, String[] args, int subCommand) {
			String[] modArgs = new String[args.length - subCommand];
			System.arraycopy(args, 0 + subCommand, modArgs, 0, args.length - subCommand);

			StringBuilder buffer = new StringBuilder();
			buffer.append(label);
			for (int x = 0; x < subCommand; x++) {
				buffer.append(".").append(args[x]);
			}
			String cmdLabel = buffer.toString();
			this.sender = sender;
			this.label = cmdLabel;
			this.args = modArgs;
		}

		public CommandSender getSender() {
			return sender;
		}

		public String getLabel() {
			return label;
		}

		public String[] getArgs() {
			return args;
		}

		public boolean isPlayer() {
			return sender instanceof ProxiedPlayer;
		}

		public ProxiedPlayer getPlayer() {
			if (sender instanceof ProxiedPlayer) {
				return (ProxiedPlayer) sender;
			} else {
				return null;
			}
		}
	}
}