package zk.demo.init;

import org.apache.commons.io.IOUtils;
import org.zkoss.zk.ui.http.SimpleWebApp;

import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Stream;

public class CacheBreakingWebApp extends SimpleWebApp {
	private static String customCssHash = null;

	@Override
	public String getBuild() {
		return super.getBuild() + "_" + getCustomCssHash();
	}

	private String getCustomCssHash() {
		if (customCssHash == null) {
			int combinedCssHash = Stream.of(this.getConfiguration().getThemeURIs())
					.mapToInt(this::toContentHash)
					.sum();
			customCssHash = Integer.toHexString(combinedCssHash);
		}
		return customCssHash;
	}

	private int toContentHash(String themeUri) {
		try {
			InputStream inputStream = this.getResourceAsStream(themeUri);
			return Arrays.hashCode(IOUtils.toByteArray(inputStream));
		} catch (Exception e) {
			throw new RuntimeException("error loading resource: " + themeUri);
		}
	}
}
