package home.local.cable_control.service;

import home.local.cable_control.model.dto.QueryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JdbcDynamicQueryService {

    private final DataSource dataSource;

    public QueryResult getAlias(String sql) {

        if (!sql.trim().toLowerCase().startsWith("select")) {
            throw new RuntimeException("Разрешены только SELECT запросы");
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            List<String> columns = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                columns.add(meta.getColumnLabel(i));
            }

            List<List<Object>> rows = new ArrayList<>();

            while (rs.next()) {
                List<Object> row = new ArrayList<>();

                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }

                rows.add(row);
            }

            return new QueryResult(columns, rows);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка выполнения SQL", e);
        }
    }
}
