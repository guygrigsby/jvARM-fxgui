package com.guygrigsby.jvarm.fxgui;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtilities {

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	static Charset getSystemCharset() {
		String os = System.getProperty("os.name");
		Charset encoding;
		if (os.contains("windows")) {
			encoding = StandardCharsets.ISO_8859_1;
		} else if (os.contains("mac")) {
			encoding = StandardCharsets.UTF_8;
		} else if(os.contains("linux")) {
			encoding = StandardCharsets.UTF_8;
		} else {
			encoding = StandardCharsets.UTF_8;
		}
		return encoding;
	}

}
