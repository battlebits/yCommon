package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import br.com.battlebits.ycommon.common.exception.HandlePacketException;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class CPacketTranslationsLoad extends CommonPacket {

	private Language language;
	private String json;

	public CPacketTranslationsLoad() {
	}

	public CPacketTranslationsLoad(Language language, String json) {
		this.language = language;
		this.json = json;
	}

	public Language getLanguage() {
		return language;
	}

	public String getJson() {
		return json;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.language = Language.valueOf(in.readUTF());
		this.json = in.readUTF();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(language.name());
		out.writeUTF(json);
	}

	@Override
	public void handle(CommonHandler handler) throws HandlePacketException {
		handler.handleTranslationsLoad(this);
	}

}
