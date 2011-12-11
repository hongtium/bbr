package com.bbr.tools;

import com.bbr.tools.net.UDPClient;

/**
 * 中心型lserv UDP 客户端
 * User: chenjx
 * Date: 11-3-31
 */
public class LogServ {

    public static int log(String mod, String str, String serverIP) {
        return log(mod, str, serverIP, (short)10000 );
    }

    /**
     * 发送日志到lserv
     * @param mod 模块名（对应于lserv日志目录下的一级子目录名）
     * @param str 日志内容，自己定义格式
     * @param serverIP  lserv对应的IP
     * @param serverPort  lserv对应的端口，默认10000
     * @return  0: 失败; >0 成功，UDP回包的长度
     */
    public static int log(String mod, String str, String serverIP, short serverPort) {
        byte[] MODB = mod.getBytes();
        byte[] STRB = str.getBytes();
        if (MODB.length > 32)
            return 0;
        byte[] outBuff = new byte[32 + 4 + 2 + STRB.length + 1];
        //byte[] cuuid;
        byte[] inBuff = new byte[100];
        //long time = System.currentTimeMillis();
        try {
            UDPClient.memcpy(outBuff, 0, MODB, 0, MODB.length);
            UDPClient.memLongcpy(outBuff, 32, 0, 4);
            UDPClient.memLongcpy(outBuff, 36, STRB.length, 2);
            UDPClient.memcpy(outBuff, 38, STRB, 0, STRB.length);
            outBuff[STRB.length + 38] = 0;

            int rlen = UDPClient
                    .UDPIO(serverIP, serverPort, (short) 0, (short) 1,
                            (short) 0, (short) 1, outBuff, inBuff);

            return rlen;
        } catch (Exception E) {
            //E.printStackTrace();
            System.err.println("LogServ.log() to " + serverIP + " ex:" + E.getMessage());
            return 0;
        }
    }
}
