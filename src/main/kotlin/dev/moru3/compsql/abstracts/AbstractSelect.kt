package dev.moru3.compsql.abstracts

import dev.moru3.compsql.Connection
import dev.moru3.compsql.ResultSet
import dev.moru3.compsql.mysql.query.select.MySQLSelectWhere
import dev.moru3.compsql.syntax.OrderType
import dev.moru3.compsql.syntax.Select
import dev.moru3.compsql.syntax.SelectKeyedWhere
import dev.moru3.compsql.syntax.SelectWhere
import dev.moru3.compsql.syntax.table.Table
import java.sql.PreparedStatement

abstract class AbstractSelect(val table: Table, vararg columns: String): Select {
    protected val columns = columns.toList()

    override val connection: Connection = table.connection

    override fun build(): PreparedStatement {
        val result = buildAsRaw()
        val preparedStatement = connection.safeConnection.prepareStatement(result.first)
        val keys = result.second
        keys.forEachIndexed { index, pair -> pair.second.set(preparedStatement, index+1, pair.first) }
        return preparedStatement
    }

    override fun where(key: String): SelectKeyedWhere = MySQLSelectWhere().also { this.where = it }.key(key)

    override fun orderBy(table: String, orderType: OrderType): SelectWhere = MySQLSelectWhere().orderBy(table, orderType).also { this.where = it }

    override fun orderBy(vararg values: Pair<String, OrderType>): SelectWhere = MySQLSelectWhere().orderBy(*values).also { this.where = it }

    override fun send(): ResultSet = ResultSet(connection.sendQuery(build()))
}