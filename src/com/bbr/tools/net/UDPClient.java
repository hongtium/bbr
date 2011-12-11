package com.bbr.tools.net;

import java.net.*;

public class UDPClient {
    static UDPSocketManager socks = new UDPSocketManager();
    public static long counter = 0;
    public static int Timeout = 0;
    public static long TotalTime = 0;
    public static int TimeLimit = 0;
    public static int TimeCount = 0;

    public static void memIntcpy(byte[] buf, int offset, int val, int len) {
        if (len > 4)
            return;
        try {
            for (int x1 = 0; x1 < len; x1++) {
                buf[offset + x1] = (byte) (val % 256);
                // System.out.println(buf[offset+x1]);
                val = val >> 8;
            }
            // System.out.println();
        } catch (Exception E) {
        }
    }

    public static void memLongcpy(byte[] buf, int offset, long val, int len) {
        if (len > 8)
            return;
        try {
            for (int x1 = 0; x1 < len; x1++) {
                buf[offset + x1] = (byte) (val % 256);
                // System.out.println(buf[offset+x1]);
                val = val >> 8;
            }
            // System.out.println();
        } catch (Exception E) {
        }
    }

    public static int intMemcpy(byte[] buf, int offset, int len) {
        int ret = 0;

        if (len > 4)
            return 0;
        try {
            for (int x1 = len - 1; x1 >= 0; x1--) {

                ret *= 256;
                ret += (buf[offset + x1] >= 0 ? buf[offset + x1]
                        : (256 + buf[offset + x1]));
            }
        } catch (Exception E) {
        }
        return ret;
    }

    public static int longMemcpy(byte[] buf, int offset, int len) {
        int ret = 0;

        if (len > 8)
            return 0;
        try {
            for (int x1 = len - 1; x1 >= 0; x1--) {

                ret *= 256;
                ret += (buf[offset + x1] >= 0 ? buf[offset + x1]
                        : (256 + buf[offset + x1]));
            }
        } catch (Exception E) {
        }
        return ret;
    }

    public static void memcpy(byte[] buf, int offset, byte[] inbuf, int off2, int len) {
        try {
            for (int x1 = 0; x1 < len; x1++) {
                buf[x1 + offset] = inbuf[x1 + off2];
            }
        } catch (Exception E) {
        }
    }

    public static int UDPIO(String ServerIP, short method, short function,
                            byte[] L2PkgIn, byte[] L2PkgOut) {
        return UDPIO(ServerIP, (short) 10000, method, function, L2PkgIn,
                L2PkgOut);
    }

    public static int UDPIO(String ServerIP, short port, short method,
                            short function, byte[] L2PkgIn, byte[] L2PkgOut) {
        return UDPIO(ServerIP, port, method, function, (short) 1200,
                (short) (function == 2 ? 1 : 2), L2PkgIn, L2PkgOut);
    }

    public static int UDPIO(String ServerIP, short port, short method,
                            short function, short timeout, short retry, byte[] L2PkgIn,
                            byte[] L2PkgOut) {
        short seq;
        long t0, t1;
        boolean isOK = false;
        counter++;
        TimeCount++;
        if (TimeCount > TimeLimit)
            TimeCount = 0;

        byte[] outBuffer = new byte[10 + L2PkgIn.length];
        byte[] inBuffer = null;

        seq = (short) (((t0 = System.currentTimeMillis()) + counter) % 31231);

        memIntcpy(outBuffer, 0, (int) method, 2);
        memIntcpy(outBuffer, 2, (int) function, 2);
        memIntcpy(outBuffer, 4, seq, 2);
        memIntcpy(outBuffer, 6, L2PkgIn.length + 10, 2);

        memcpy(outBuffer, 10, L2PkgIn, 0, L2PkgIn.length);

        DatagramSocket ds = socks.getSock();
        DatagramPacket dpin = socks.getInPackage();

        if (ds == null || dpin == null) {
            System.out
                    .println("socks.getSock() || socks.getInPackage() == null");
            return -1;
        }

        DatagramPacket dp = new DatagramPacket(outBuffer, outBuffer.length,
                socks.getAddr(ServerIP), port);

        int never = 0;
        // int retry = (function==2?1:2);

        for (int x1 = 0; x1 < retry; x1++) {
            try {
                if (x1 == 1)
                    System.out.println("UDPClient->UDPIO(" + seq + ") To:"
                            + ServerIP + " FUNC=" + function + " try again!");
                never++;
                if (never >= 3)
                    break;
                if (timeout != 0)
                    ds.setSoTimeout(timeout);

                ds.send(dp);
                if (timeout != 0)
                    ds.receive(dpin);
                else {
                    isOK = true;
                    break;
                }
            } catch (Exception E) {
                // System.out.println("UDPClient->UDPIO:"+E.toString()+"
                // To:"+ServerIP+" FUNC="+function+"
                // TM="+(System.currentTimeMillis() - t0));
                System.out
                        .println("UDPClient->UDPIO(" + seq
                                + "): Receive timed out:" + ServerIP + " FUNC="
                                + function + " TM="
                                + (System.currentTimeMillis() - t0));
                Timeout++;
                TimeLimit += 3;
                continue;
            }

            if (dpin.getLength() < 10) {
                x1--;
                continue;
            }
            inBuffer = dpin.getData();

            if ((int) intMemcpy(inBuffer, 4, 2) == seq) {
                isOK = true;
                break;
            } else
                x1--;
        }
        t1 = System.currentTimeMillis();

        TotalTime += (t1 - t0);

        if (isOK) {
            memcpy(L2PkgOut, 0, dpin.getData(), 10, dpin.getLength());
            socks.putSock(ds);
            socks.putInPackage(dpin);
            if (TimeLimit > 0)
                TimeLimit /= 2;
            return dpin.getLength() - 10;
        } else {
            System.out.println("Timeout");
        }

        socks.putSock(ds);
        socks.putInPackage(dpin);

        return -1;
    }

    public static int UDPIOtest(String ServerIP, short port, short method,
                                short function, byte[] L2PkgIn, byte[] L2PkgOut) {
        short seq;
        long t0, t1;
        boolean isOK = false;
        counter++;
        TimeCount++;
        if (TimeCount > TimeLimit)
            TimeCount = 0;

        byte[] outBuffer = new byte[10 + L2PkgIn.length];
        byte[] inBuffer = null;

        seq = (short) (((t0 = System.currentTimeMillis()) + counter) % 31231);

        memIntcpy(outBuffer, 0, (int) method, 2);
        memIntcpy(outBuffer, 2, (int) function, 2);
        memIntcpy(outBuffer, 4, seq, 2);
        memIntcpy(outBuffer, 6, L2PkgIn.length + 10, 2);

        memcpy(outBuffer, 10, L2PkgIn, 0, L2PkgIn.length);

        DatagramSocket ds = socks.getSock();
        DatagramPacket dpin = socks.getInPackage();

        if (ds == null || dpin == null) {
            System.out
                    .println("socks.getSock() || socks.getInPackage() == null");
            return -1;
        }

        DatagramPacket dp = new DatagramPacket(outBuffer, outBuffer.length,
                socks.getAddr(ServerIP), port);

        int never = 0;
        int retry = (function == 2 ? 1 : 2);

        for (int x1 = 0; x1 < retry; x1++) {
            try {
                if (x1 == 1)
                    System.out.println("UDPClient->UDPIO(" + seq + ") To:"
                            + ServerIP + " FUNC=" + function + " try again!");
                never++;
                if (never >= 3)
                    break;
                // {orsonzou
                // if(function == 4)
                ds.setSoTimeout(4200);

                // }orsonzou
                ds.send(dp);
                // ds.receive(dpin);
            } catch (Exception E) {
                // System.out.println("UDPClient->UDPIO:"+E.toString()+"
                // To:"+ServerIP+" FUNC="+function+"
                // TM="+(System.currentTimeMillis() - t0));
                System.out
                        .println("UDPClient->UDPIO(" + seq
                                + "): Receive timed out:" + ServerIP + " FUNC="
                                + function + " TM="
                                + (System.currentTimeMillis() - t0));
                Timeout++;
                TimeLimit += 3;
                continue;
            }

            if (dpin.getLength() < 10) {
                x1--;
                continue;
            }
            inBuffer = dpin.getData();

            if ((int) intMemcpy(inBuffer, 4, 2) == seq) {
                isOK = true;
                break;
            } else
                x1--;
        }
        t1 = System.currentTimeMillis();

        TotalTime += (t1 - t0);

        if (isOK) {
            memcpy(L2PkgOut, 0, dpin.getData(), 10, dpin.getLength());
            socks.putSock(ds);
            socks.putInPackage(dpin);
            if (TimeLimit > 0)
                TimeLimit /= 2;
            return dpin.getLength() - 10;
        } else {
            // System.out.println("Timeout");
        }

        socks.putSock(ds);
        socks.putInPackage(dpin);

        return -1;
    }
}
