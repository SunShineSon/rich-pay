package com.common.util;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

public class UUid {
	private static Logger log = Logger.getLogger(UUid.class);
	private final static String SN = "1";// 服务器number
	private static long seq = 0;
	private static long lastseq = System.currentTimeMillis();
	private static final AtomicInteger NEXT_COUNTER = new AtomicInteger(new SecureRandom().nextInt());
	private static Random rand = new Random();

	public static void main(String[] args) {
		Random random2 = new Random();
		for (int i = 0; i < 10; i++) {
			System.out.println(random2.nextInt(10));
		}
	}

	public static String getSn() {
		return SN;
	}

	public static String genNextIdLocal() {
		long now = System.currentTimeMillis();
		String idString;
		if ((now - lastseq) <= seq) {
			seq++;
		} else {
			seq = 0;
			lastseq = now;
		}
		idString = String.valueOf((lastseq + seq));
		return idString;
	}

	public static long getNextLongId() {
		return Long.valueOf("1" + UUid.genNextIdLocal());
	}

	public static long getNextLongId2() {
		return Long.valueOf(rand.nextInt(10) + UUid.genNextIdLocal() + rand.nextInt(10));
	}

}
