package io.summernova.admin.core.mapper;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.sql.Connection;

import static org.apache.ibatis.io.Resources.getResourceAsReader;

/**
 * @author gongshuiwen
 */
public final class ScriptRunnerUtil {

    private ScriptRunnerUtil() {
    }

    public static void runScript(SqlSession sqlSession, String filename) {
        Connection connection = sqlSession.getConnection();
        ScriptRunner runner = new ScriptRunner(connection);
        runner.setAutoCommit(true);
        runner.setStopOnError(true);
        try {
            runner.runScript(getResourceAsReader(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
