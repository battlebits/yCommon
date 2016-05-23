package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.exception.HandlePacketException;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketChangeAccount extends CommonPacket {

	private UUID uuid;
	private int xp;
	private int money;
	private int fichas;

	public CPacketChangeAccount() {
	}

	public UUID getUuid() {
		return uuid;
	}

	public int getXp() {
		return xp;
	}

	public int getMoney() {
		return money;
	}

	public int getFichas() {
		return fichas;
	}

	public CPacketChangeAccount(BattlePlayer player) {
		this(player.getUuid(), player.getXp(), player.getMoney(), player.getFichas());
	}

	public CPacketChangeAccount(UUID uuid, int xp, int money, int fichas) {
		this.uuid = uuid;
		this.xp = xp;
		this.money = money;
		this.fichas = fichas;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		uuid = UUID.fromString(in.readUTF());
		xp = in.readInt();
		money = in.readInt();
		fichas = in.readInt();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(uuid.toString());
		out.writeInt(xp);
		out.writeInt(money);
		out.writeInt(fichas);
	}

	@Override
	public void handle(CommonHandler handler) throws HandlePacketException {
		handler.handleChangeAccount(this);
	}

}
