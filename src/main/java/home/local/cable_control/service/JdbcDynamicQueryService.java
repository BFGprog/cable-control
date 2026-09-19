package home.local.cable_control.service;

import home.local.cable_control.model.SqlQueryParam;
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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class JdbcDynamicQueryService {

    private final DataSource dataSource;


    public QueryResult getAlias(String sql, List<SqlQueryParam> params, Map<String, Object> values) {
        if (!sql.trim().toLowerCase().startsWith("select")) {
            throw new RuntimeException("Разрешены только SELECT запросы");
        }

        StringBuilder preparedSql = new StringBuilder();
        List<Object> parameterValues = new ArrayList<>();
        int lastPosition = 0;

        Map<String, SqlQueryParam> paramMap = params.stream()
                .collect(Collectors.toMap(
                        SqlQueryParam::getCode,
                        param -> param
                ));

        Pattern pattern = Pattern.compile("(?<![a-zA-Z0-9_]):([a-zA-Z0-9_]+)");
        Matcher matcher = pattern.matcher(sql);

        while (matcher.find()) {
            String code = matcher.group(1);
            SqlQueryParam param = paramMap.get(code);

            if (param == null) {
                throw new RuntimeException("В отчета отсутствует параметр: " + code);
            }
            if (!values.containsKey(code)) {
                throw new RuntimeException("Не передан параметр: " + code);
            }

            preparedSql.append(sql, lastPosition, matcher.start());
            preparedSql.append("?");
            parameterValues.add(values.get(code));
            lastPosition = matcher.end();
        }

        preparedSql.append(sql, lastPosition, sql.length());
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(preparedSql.toString())) {

            for (int i = 0; i < parameterValues.size(); i++) {
                stmt.setObject(i + 1, parameterValues.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();
                List<String> columns = new ArrayList<>();
                List<List<Object>> rows = new ArrayList<>();

                for (int i = 1; i <= columnCount; i++) {
                    columns.add(meta.getColumnLabel(i));
                }

                while (rs.next()) {
                    List<Object> row = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getObject(i));
                    }
                    rows.add(row);
                }

                return new QueryResult(columns, rows);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка выполнения SQL", e);
        }
    }

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
