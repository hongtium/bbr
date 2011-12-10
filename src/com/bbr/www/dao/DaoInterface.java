package com.bbr.www.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: chenjx
 * Date: 11-9-29
 */
public interface DaoInterface {
    public<DTO> DTO getFirst(String fieldName, String fieldVal, boolean reload);
    public<DTO> List<DTO> getList(String fieldName, String fieldVal, int start, int end, String sortField, boolean reload);
    public int countList(String fieldName, String fieldVal, boolean reload);
    public void clearCache(String fieldName, String fieldVal);
    public void clearCache(String fieldName, String fieldVal, int start, int end);

    public double printHitRatio();
    public String getClobColumn(ResultSet rs, int columnIndex) throws SQLException;
    public void setClobColumn(PreparedStatement stmt, int parameterIndex, String value) throws SQLException;
}
