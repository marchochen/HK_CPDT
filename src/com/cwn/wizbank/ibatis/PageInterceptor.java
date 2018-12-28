package com.cwn.wizbank.ibatis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cwn.wizbank.ibatis.dialect.Dialect;
import com.cwn.wizbank.ibatis.dialect.MysqlDialect;
import com.cwn.wizbank.ibatis.dialect.OracleDialect;
import com.cwn.wizbank.ibatis.dialect.SqlServerDialect;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.Page;

@Intercepts({ @Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class }) })
public class PageInterceptor implements Interceptor {
	private static final Logger logger = LoggerFactory.getLogger(PageInterceptor.class);

	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();

	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		BoundSql boundSql = statementHandler.getBoundSql();
		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);

		Object o = boundSql.getParameterObject();
		if (o instanceof Page<?>) {

			Page<?> page = (Page<?>) o;

			Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
			Dialect.Type dbType = null;
			try {
				dbType = Dialect.Type.valueOf(configuration.getDatabaseId());
			} catch (Exception e) {
				throw new RuntimeException(" dialect error ");
			}

			Dialect dialect = null;
			switch (dbType) {
			case MYSQL:
				dialect = new MysqlDialect();
				break;
			case SQLSERVER:
				dialect = new SqlServerDialect();
				break;
			case ORACLE:
				dialect = new OracleDialect();
				break;
			case DB2:
				throw new RuntimeException(" no db2 dialect support ");
			default:
				dialect = new MysqlDialect();
			}

			int skip = (page.getPageNo() - 1) * page.getPageSize();

			// 通過這3行代碼將metaParameters的值给countBS對象。
			// 而且是在使用了<foreach>一類的特殊標簽時會產生的問題。<foreach>循環的時候，會定義入参为__frch_item_0,__frch_item_1等等
			BoundSql countBS = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
			MetaObject countBsObject = SystemMetaObject.forObject(countBS);
			MetaObject boundSqlObject = SystemMetaObject.forObject(boundSql);
			countBsObject.setValue("metaParameters", boundSqlObject.getValue("metaParameters"));

			setTotalRecord(page, (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement"), (Connection) invocation.getArgs()[0], dbType);

			String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
			metaStatementHandler.setValue("delegate.boundSql.sql", dialect.getLimitString(originalSql, skip, page.getPageSize(), page.getSortname(), page.getSortorder()));
			metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
			metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
			logger.debug(boundSql.getSql());
		}
		return invocation.proceed();
	}

	private void setTotalRecord(Page<?> page, MappedStatement mappedStatement, Connection connection, Dialect.Type dbType) {
		// 获取对应的BoundSql，这个BoundSql其实跟我们利用StatementHandler获取到的BoundSql是同一个对象。
		// delegate里面的boundSql也是通过mappedStatement.getBoundSql(paramObj)方法获取到的。
		BoundSql boundSql = mappedStatement.getBoundSql(page);
		// 获取到我们自己写在Mapper映射语句中对应的Sql语句
		String sql = boundSql.getSql();
		// 通过查询Sql语句获取到对应的计算总记录数的sql语句
		String countSql = this.getCountSql(sql, dbType);
		// 通过BoundSql获取对应的参数映射
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		// 利用Configuration、查询记录数的Sql语句countSql、参数映射关系parameterMappings和参数对象page建立查询记录数对应的BoundSql对象。
		BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, parameterMappings, page);
		// 通过mappedStatement、参数对象page和BoundSql对象countBoundSql建立一个用于设定参数的ParameterHandler对象
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, page, countBoundSql);
		// 通过connection建立一个countSql对应的PreparedStatement对象。
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(countSql);
			// 通过parameterHandler给PreparedStatement对象设置参数
			parameterHandler.setParameters(pstmt);
			// 之后就是执行获取总记录数的Sql语句和获取结果了。
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int totalRecord = rs.getInt(1);
				// 给当前的参数page对象设置总记录数
				page.setTotalRecord(totalRecord);
			}
		} catch (SQLException e) {
			CommonLog.error(e.getMessage(),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				CommonLog.error(e.getMessage(),e);
			}
		}
	}

	/**
	 * 根据原Sql语句获取对应的查询总记录数的Sql语句
	 * 
	 * @param sql
	 * @return
	 */
	private String getCountSql(String sql, Dialect.Type dbType) {
		sql = sql.replaceAll("\\s+", " ").replaceAll("ORDER BY", "order by").trim(); //过滤掉重复空格以及两端的空格
		//sql = sql.toLowerCase();
		// 替换掉order by
		if (!dbType.equals(Dialect.Type.ORACLE)) {
			String orderStr = "";
			String orderBy = "order by";
			if (sql.indexOf(orderBy) > -1) {
				orderStr = sql.substring(sql.indexOf(orderBy), sql.length());
				sql = sql.replace(orderStr, "");
			}
		}
		// int index = sql.indexOf("from");
		return "select count(1) from( " + sql + ") a";
	}

	@Override
	public Object plugin(Object o) {
		if (Executor.class.isAssignableFrom(o.getClass())) {
			return Plugin.wrap(new PageExecutor((Executor) o), this);
		}
		return Plugin.wrap(o, this);
	}

	/**
	 * 设置注册拦截器时设定的属性
	 */
	public void setProperties(Properties properties) {

	}

}