package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.UUID;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.AccountConfiguration;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketAccountConfiguration extends CommonPacket {

	private UUID uuid;
	private AccountConfiguration configuration;

	public CPacketAccountConfiguration() {
	}

	public CPacketAccountConfiguration(UUID uuid, AccountConfiguration accountConfiguration) {
		this.uuid = uuid;
		this.configuration = accountConfiguration;
	}

	public UUID getUuid() {
		return uuid;
	}

	public AccountConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	public void read(DataInputStream in) throws Exception {
		this.uuid = UUID.fromString(in.readUTF());
		this.configuration = BattlebitsAPI.getGson().fromJson(in.readUTF(), AccountConfiguration.class);
	}

	@Override
	public void write(DataOutputStream out) throws Exception {
		out.writeUTF(uuid.toString());
		out.writeUTF(BattlebitsAPI.getGson().toJson(configuration));
	}

	@Override
	public void handle(CommonHandler handler) throws Exception {
		handler.handleAccountConfiguration(this);
	}

}
