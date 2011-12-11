package com.bbr.tools;

/**
 * 利用 Memcached 进行频度控制，如发帖控制
 * @author Norton Chen(chenjx)
 * @since 2011-4-20
 */


public class FreqControl {
	public static SpyMemcached FC_SESSION = SpyMemcached.getInstance("memcache.server");
	public static String KEY_PREFIX = "FrEq:";

    /**
     * 构造函数，设置频率控制参数
     * @param _key  频率控制节点uniq key，用于标注频率控制的节点
     * @param _countLimit   在实践窗口内，允许几次
     * @param _timeFrame    时间窗口的大小，单位：秒
     */
	public FreqControl(String _key, int _countLimit, int _timeFrame) {
		this.key = KEY_PREFIX + _key;
		this.countLimit = _countLimit;
		this.timeFrame = _timeFrame;
	}

	public int inc() {
		return inc(1);
	}
	/**
	 * @return
	 * 0 : Server Error
	 * >0 : The new frequence counter
	 */
	public int inc(int delta) {
		int ret = (int) FC_SESSION.incr(key, delta);
		if(ret < 0) { //not exists
			FC_SESSION.set(key, timeFrame, (long)delta);
			ret = delta;
        }
		return ret;
	}

	public int getCount() {
		int ret = (int)FC_SESSION.getIncrLong(key);
		if(ret < 0) ret = 0;
		return ret;
	}

	public boolean isValid() {
		return getCount() < countLimit;
	}

	public String toString() {
		return this.key + " ; " + countLimit + " ; " + timeFrame;
	}

	protected String key;		//Key, 通常是UserID
	protected int countLimit; //窗口内次数限制
	protected int timeFrame;  //时间窗口长度(秒)

	public static void main(String[] v) {
		if(v == null || v.length < 2) {
			System.out.println("Usage: cn.atsport.tools.FreqControl {cmd} {args...}");
			System.out.println("\tinit key");
			System.out.println("\tinc key");
			System.out.println("\tget key");
			System.out.println("\tisvalid key");
			return;
		}
		String cmd = v[0];
		String key = v[1];
		FreqControl fc = new FreqControl(key, 3, 10); //10秒钟只允许4次
		if("inc".equals(cmd)) {
			System.out.println("inc=" + fc.inc());
		} else if("get".equals(cmd)) {
			System.out.println("getCount=" + fc.getCount());
		} else if("isvalid".equals(cmd)) {
			System.out.println("isvalid=" + fc.isValid());
		}
	}

}
