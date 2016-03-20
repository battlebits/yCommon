package br.com.battlebits.ycommon.bungee.event;

import br.com.battlebits.ycommon.bungee.event.UpdateEvent.UpdateType;
import net.md_5.bungee.BungeeCord;

public class UpdateScheduler implements Runnable {

	private long lastSecond = Long.MIN_VALUE;
	private long lastMinute = Long.MIN_VALUE;

	@Override
	public void run() {
		BungeeCord.getInstance().getPluginManager().callEvent(new UpdateEvent(UpdateType.TICK));
		if (lastSecond + 1000 <= System.currentTimeMillis()) {
			BungeeCord.getInstance().getPluginManager().callEvent(new UpdateEvent(UpdateType.SECOND));
			lastSecond = System.currentTimeMillis();
		}
		if (lastMinute + 60000 <= System.currentTimeMillis()) {
			BungeeCord.getInstance().getPluginManager().callEvent(new UpdateEvent(UpdateType.MINUTE));
			lastMinute = System.currentTimeMillis();
		}
	}

}
