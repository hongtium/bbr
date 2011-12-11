package com.bbr.tools;

import java.util.TimeZone;

/**
 * 频度控制，但周期固定是当天
 * @see com.bbr.tools.FreqControl
 *
 * @author Norton Chen(chenjx)
 * @since 2011-4-20
 */
public class FreqControlDaily extends com.bbr.tools.FreqControl {
	public static String KEY_PREFIX = "fQ:";
	public static long ONEDAY = 24L * 3600L * 1000L;

	public FreqControlDaily(String _key, int _countLimit ) {
		super(_key, _countLimit, 3600);
		this.key = KEY_PREFIX + _key;
		this.countLimit = _countLimit;
		this.timeFrame = todayRemain();
		System.out.println("FreqControlDaily.timeFrame=" + timeFrame);
	}

	public static int todayRemain() {
		long ts = System.currentTimeMillis();
		ts += TimeZone.getDefault().getRawOffset(); //加上偏移量
		long todayGone = ts % ONEDAY;
		long todayRemain = ONEDAY - todayGone;
		//System.out.println(ONEDAY + ":" + ts + ":" + todayGone + ":" + todayRemain);
		return (int) (todayRemain / 1000L) ;
	}
	/**
	 * 错误写法！由于TimeZone=GMT+8的问题，将按早上八点为“自然天”的开始
	 * 不过有的场景似乎挺合适,所以保留
	 */
	public static int todayRemainX() {
		long ts = System.currentTimeMillis();
		long todayGone = ts % ONEDAY;
		long todayRemain = ONEDAY - todayGone;
		System.out.println(ONEDAY + ":" + ts + ":" + todayGone + ":" + todayRemain);
		return (int) (todayRemain / 1000L) ;
	}

	public static void main(String[] v) {
		/*String[] tz = TimeZone.getAvailableIDs();
		for(int i=0; i<tz.length; i++) {
			System.out.println(tz[i]);
		}*/

		if(v == null || v.length < 2) {
			System.out.println("Usage: java com.chinaren.tools.FreqControlDaily {cmd} {args...}");
			System.out.println("\tinit key");
			System.out.println("\tinc key");
			System.out.println("\tget key");
			System.out.println("\tisvalid key");
			return;
		}
		String cmd = v[0];
		String key = v[1];
		FreqControlDaily fc = new FreqControlDaily(key, 4);
		if("inc".equals(cmd)) {
			System.out.println("inc=" + fc.inc());
		} else if("get".equals(cmd)) {
			System.out.println("getCount=" + fc.getCount());
		} else if("isvalid".equals(cmd)) {
			System.out.println("isvalid=" + fc.isValid());
		}
	}

}

