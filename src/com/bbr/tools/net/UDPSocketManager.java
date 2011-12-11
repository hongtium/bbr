package com.bbr.tools.net;

import java.net.*;
import java.util.*;

/**
 * UDP socket 封装类
 *
 * @author : chenjx
 * @since: 2011-3-31
 */
public class UDPSocketManager {
	//Vector Socks;
	//Vector Packages;
	Hashtable<String,InetAddress> Addrs;
	long counter;
	final static int UDPMAX = 54000;

	public UDPSocketManager() {
		//Socks = new Vector(1500);
		//Packages = new Vector(1500);
		Addrs = new Hashtable<String,InetAddress>(10);
		counter = 0;
	}

	public InetAddress getAddr(String IP) {
		InetAddress ad = null;
		ad = (InetAddress) Addrs.get(IP);
		if (ad == null) {
			try {
				ad = InetAddress.getByName(IP);
			} catch (Exception E) {
				E.printStackTrace();
				return null;
			}
			Addrs.put(IP, ad);
		}
		return ad;
	}

	int sockseq = 10000;

	public DatagramSocket getSock() {
        DatagramSocket ds = null;
        sockseq++;

		if (sockseq > 30000)
			sockseq = 10000;
//		if(!Socks.isEmpty()) ds = (DatagramSocket) Socks.remove(0);

		DatagramSocket r;
		if (ds == null) {
			try {
				r = new DatagramSocket(/*sockseq*/);
				r.setSoTimeout(4100);
				return r;
			} catch (Exception E) {
				try {
					r = new DatagramSocket(/*sockseq - 4000*/);
					r.setSoTimeout(4100);
					return r;
				} catch (Exception Ex) {
					Ex.printStackTrace();
				}
				E.printStackTrace();
			}
		}
		return ds;
	}

	public DatagramPacket getInPackage() {
		counter++;
		try {
			byte[] inBuff = new byte[UDPMAX];
			return new DatagramPacket(inBuff, UDPMAX);
		} catch (Exception E) {
			E.printStackTrace();
			return null;
		}
		//		return dpin;
	}

	public void putSock(DatagramSocket ds) {
		ds.close();
		//		Socks.add(ds);
	}

	public void putInPackage(DatagramPacket dp) {
		//		dp.close();
		//		dp.setData(dp.getData(),0,65536);
		//		Packages.add(dp);
	}
}
