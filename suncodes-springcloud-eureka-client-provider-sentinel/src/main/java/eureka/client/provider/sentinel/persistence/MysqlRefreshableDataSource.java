package eureka.client.provider.sentinel.persistence;

import com.alibaba.csp.sentinel.datasource.AutoRefreshDataSource;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import eureka.client.provider.sentinel.pojo.po.ResourceRoleQps;
import eureka.client.provider.sentinel.util.DataSourceUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class MysqlRefreshableDataSource extends AutoRefreshDataSource<List<ResourceRoleQps>, List<FlowRule>> {

    private static final long DEFAULT_REFRESH_MS = 3000;

    public MysqlRefreshableDataSource(Converter<List<ResourceRoleQps>, List<FlowRule>> configParser) {
        super(configParser, DEFAULT_REFRESH_MS);
        firstLoad();
    }

    private void firstLoad() {
        try {
            List<FlowRule> newValue = loadConfig();
            getProperty().updateValue(newValue);
        } catch (Throwable e) {
            System.out.println(e);
            RecordLog.info("loadConfig exception", e);
        }
    }

    @Override
    public List<ResourceRoleQps> readSource() throws Exception {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSourceUtils.getDataSource());
        return jdbcTemplate.query("select id, app_id, api, limit_qps, create_at from resource_role_qps"
                , new RowMapper<ResourceRoleQps>() {
                    @Override
                    public ResourceRoleQps mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new ResourceRoleQps(resultSet.getLong("id"),
                                resultSet.getString("app_id"),
                                resultSet.getString("api"),
                                resultSet.getLong("limit_qps"),
                                resultSet.getLong("create_at"));
                    }
                });
    }

    @Override
    public void close() throws Exception {
        super.close();
    }
}