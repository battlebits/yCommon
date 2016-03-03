package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class CPacketTranslationsRequest extends CommonPacket {
	private Language language;

	public CPacketTranslationsRequest() {
	}

	public CPacketTranslationsRequest(Language language) {
		this.language = language;
	}
	
	public Language getLanguage() {
		return language;
	}

	@Override
	public void read(DataInputStream in) throws Exception {
		this.language = Language.valueOf(in.readUTF());
	}

	@Override
	public void write(DataOutputStream out) throws Exception {
		out.writeUTF(language.name());
	}

	@Override
	public void handle(CommonHandler handler) throws Exception {
		handler.handleTranslationsRequest(this);
	}

}
