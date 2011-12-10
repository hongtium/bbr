package com.bbr.www.dao;

import com.bbr.www.factory.SpyMemcachedFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * DAO高度统一抽象类！
 * This is a customizable template within FireStorm/DAO.
 * @author  chenjx
 * @since 2011-6-4
 */
public abstract class AbstractDao implements DaoInterface {
    protected SimpleJdbcTemplate jdbcTemplate;
    protected DataSource dataSource;
    public  boolean DEBUG = false;

    /** 字段列表 */
    protected String FIELDS = null;
    /** 缓存key的前缀 */
    protected String cachePre = "NODEFINED";
    /** 缓存的模块名 */
    protected String cacheModule = "common";
    /** 缓存工厂实例 */
    protected SpyMemcachedFactory cache = null;
    /** 缓存时间 */
    protected int TIMEOUT = 60;
    /** 缓存统计 */
    protected int CacheCount = 0, TotalCount = 0;

    /**
     * 统一所有DAO的某字段查询函数。根据Key取得相应的对象。并带有缓存效果的。
     * @param fieldName 字段名
     * @param fieldVal  字段值
     * @param reload  是否刷新缓存
     * @return   第一个符合  “SELECT * FROM x WHERE fieldName = fieldVal“ 的条件查询结果
     */
    @Override
    public<DTO> DTO getFirst(String fieldName, String fieldVal, boolean reload) {
        DTO ret = null;
        TotalCount++;
        fieldName = fieldName.toLowerCase(); //字段名固定小写
        try {
            if(cache == null || "NODEFINED".equals(cachePre)) {
                if(DEBUG) System.out.println("WARNING: cache is not set of " + this.getClass().getName());
                return (DTO)__getFirst(fieldName, fieldVal);
            }
            String ck = cachePre + fieldName + fieldVal;
            if(reload) clearCache(fieldName, fieldVal);
            else ret = (cache != null) ? (DTO)cache.get(ck) : null ;
            if(ret != null) { //cache bingo!
                CacheCount++;
                return ret;
            }
            ret = (DTO)__getFirst(fieldName, fieldVal);
            if(ret != null && cache != null) cache.set(ck, TIMEOUT, ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 统一所有DAO的某字段查询列表函数。根据Key取得相应的对象列表。并带有缓存效果的。
     * @param fieldName 字段名
     * @param fieldVal  字段值
     * @param start     列表起点
     * @param end       列表终点
     * @param sortField  排序(ORDER BY)字段名
     * @param reload  是否刷新缓存
     * @return   查询结果列表 “SELECT * FROM x WHERE fieldName = fieldVal“ 的条件查询结果
     */
    @Override
    public<DTO> List<DTO> getList(String fieldName, String fieldVal, int start, int end, String sortField, boolean reload) {
        List<DTO> ret = null;
        TotalCount++;
        fieldName = fieldName.toLowerCase(); //字段名固定小写
        try {
            if(cache == null || "NODEFINED".equals(cachePre)) {
                return __getList(fieldName, fieldVal, start, end, sortField);
            }
            String ck = cachePre + fieldName + fieldVal + "-" + start + "-" + end;
            if(reload) clearCache(fieldName, fieldVal, start, end);
            else ret = (cache != null) ? (List<DTO>)cache.get(ck) : null;
            if(ret != null) { //cache bingo!
                CacheCount++;
                return ret;
            }
            ret = __getList(fieldName, fieldVal, start, end, sortField);
            if(ret != null && cache != null) cache.set(ck, TIMEOUT/6, ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 统一函数： 统计单字段条件的总数
     * @param fieldName
     * @param fieldVal
     * @return  统计总数
     */
    @Override
    public int countList(String fieldName, String fieldVal, boolean reload) {
        int ret = -1;
        TotalCount++;
        fieldName = fieldName.toLowerCase(); //字段名固定小写
        try {
            if(cache == null || "NODEFINED".equals(cachePre)) {
                if(DEBUG) System.out.println("WARNING: cache is not set of " + this.getClass().getName());
                return __getCount(fieldName, fieldVal);
            }
            String ck = cachePre + fieldName + fieldVal + "-CNT";
            if(reload) clearCache(fieldName, fieldVal);
            else ret = (cache != null) ? (Integer)cache.get(ck) : -1;
            if(ret >= 0) { //cache bingo!
                CacheCount++;
                return ret;
            }
            ret = __getCount(fieldName, fieldVal);
            if(ret >= 0 && cache != null) cache.set(ck, TIMEOUT, new Integer(ret));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /** 清缓存 */
    public void clearCache(String fieldName, String fieldVal) {
        fieldName = fieldName.toLowerCase(); //字段名固定小写
        String ck = cachePre + fieldName + fieldVal;
        cache.remove(ck);
        ck = cachePre + fieldName + fieldVal + "-CNT";
        cache.remove(ck);
    }
    /** 清列表缓存 */
    public void clearCache(String fieldName, String fieldVal, int start, int end) {
        if(cache == null) return;
        fieldName = fieldName.toLowerCase(); //字段名固定小写
        String ck = cachePre + fieldName + fieldVal + "-" + start + "-" + end;
        cache.remove(ck);
        ck = cachePre + fieldName + fieldVal + "-CNT";
        cache.remove(ck);
    }

    /** 必须要重载的纯虚函数，每个DAO各自实现。从DB中根据PrimaryKey取得相应的对象  */
    protected abstract<DTO> DTO __getFirst(String fieldName, String fieldVal)
            throws DaoException;
    protected abstract<DTO> List<DTO> __getList(String fieldName, String fieldVal, int start, int end, String sortField)
            throws DaoException;
    protected abstract String getTableName();

    protected int __getCount(String fieldName, String fieldVal) {
        String sql = "SELECT count(*) FROM " + getTableName() + " WHERE " + fieldName + "=?";
        int ret = jdbcTemplate.queryForInt(sql, fieldVal);
        return ret;
    }

    public double printHitRatio() {
        double r = 0.00;
        if(TotalCount == 0) r = 0.00;
        r = (double)CacheCount / (double)TotalCount * 100.0000;
        System.out.println(this.getClass().getName() + "::CacheHitRatio is " + r);
        return r;
    }

    public byte[] getBlobColumn(ResultSet rs, int columnIndex)
            throws SQLException {
        try {
            Blob blob = rs.getBlob(columnIndex);
            if (blob == null) {
                return null;
            }

            InputStream is = blob.getBinaryStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            if (is == null) {
                return null;
            } else {
                byte buffer[] = new byte[64];
                int c = is.read(buffer);
                while (c > 0) {
                    bos.write(buffer, 0, c);
                    c = is.read(buffer);
                }
                return bos.toByteArray();
            }
        } catch (IOException e) {
            throw new SQLException("Failed to read BLOB column due to IOException: " + e.getMessage());
        }
    }

    public void setBlobColumn(PreparedStatement stmt, int parameterIndex, byte[] value)
            throws SQLException {
        if (value == null) {
            stmt.setNull(parameterIndex, Types.BLOB);
        } else {
            stmt.setBinaryStream(parameterIndex, new ByteArrayInputStream(value), value.length);
        }
    }

    public String getClobColumn(ResultSet rs, int columnIndex)
            throws SQLException {
        try {
            Clob clob = rs.getClob(columnIndex);
            if (clob == null) {
                return null;
            }

            StringBuffer ret = new StringBuffer();
            InputStream is = clob.getAsciiStream();

            if (is == null) {
                return null;
            } else {
                byte buffer[] = new byte[64];
                int c = is.read(buffer);
                while (c > 0) {
                    ret.append(new String(buffer, 0, c));
                    c = is.read(buffer);
                }
                return ret.toString();
            }
        } catch (IOException e) {
            throw new SQLException("Failed to read CLOB column due to IOException: " + e.getMessage());
        }
    }

    public void setClobColumn(PreparedStatement stmt, int parameterIndex, String value)
            throws SQLException {
        if (value == null) {
            stmt.setNull(parameterIndex, Types.CLOB);
        } else {
            stmt.setAsciiStream(parameterIndex, new ByteArrayInputStream(value.getBytes()), value.length());
        }
    }

    public List<?> searchBySQL(String sql, Object...args){
        return null;
    }
}
