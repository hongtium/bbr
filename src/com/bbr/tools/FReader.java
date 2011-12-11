package com.bbr.tools;

/**
 * 对文件读取的简单封装。从文件读成字符串
 * @author chenjx
 * @since 2011-7-7
 */
import java.io.*;

public class FReader {

	public static String readFile(String fname) throws IOException {
		File cacheFile = new File(fname);
		BufferedReader in = new BufferedReader(new FileReader(cacheFile));
		String ret = readerToString(in);
		in.close();
		return ret;
	}

	public static String readerToString(Reader is) throws IOException {
		StringBuffer sb = new StringBuffer( );
		char[] b = new char[8192];
		int n;
		// Read a block. If it gets any chars, append them.
		while ((n = is.read(b)) > 0) {
			sb.append(b, 0, n);
		}

		// Only construct the String object once, here.
		return sb.toString( );
	}

}