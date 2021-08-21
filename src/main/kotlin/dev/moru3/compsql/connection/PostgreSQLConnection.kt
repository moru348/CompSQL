package dev.moru3.compsql.connection

import dev.moru3.compsql.*
import dev.moru3.compsql.table.Table
import java.sql.Connection
import java.sql.DriverManager

/**
 * 新しくPostgreSQLのコネクションを開きます。すでに開いているコネクションがある場合はそのコネクションをcloseします。
 */
class PostgreSQLConnection(private var url: String, private val username: String, private val password: String, private val properties: Map<String, Any>, override val timeout: Int = 5, action: PostgreSQLConnection.()->Unit = {}): SQL() {
    init { url.also{ properties.keys.mapIndexed { index, key -> url+="${if(index==0) '?' else '&'}${key}=${properties[key]}" }.forEach(it::plus) } }

    constructor(host: String, database: String, username: String, password: String, properties: Map<String, Any>, timeout: Int = 5, action: PostgreSQLConnection.()->Unit = {}): this("jdbc:postgresql://${host}/${database}", username, password, properties, timeout, action)

    override var connection: Connection = DriverManager.getConnection(url, username, password)
        private set

    override val safeConnection: Connection get() = reconnect(false)

    override fun reconnect(force: Boolean): Connection {
        if(!this.isClosed) if(force) connection.close() else return connection
        connection = DriverManager.getConnection(url, username, password)
        return connection
    }

    override fun add(instance: Any, force: Boolean) {
        TODO("Not yet implemented")
    }

    override fun put(instance: Any, force: Boolean) {
        TODO("Not yet implemented")
    }

    override fun putOrUpdate(instance: Any) {
        TODO("Not yet implemented")
    }

    override fun select(table: String, vararg columns: String, action: Select.() -> Unit): Select {
        TODO("Not yet implemented")
    }

    override fun select(table: String, vararg columns: String): Select {
        TODO("Not yet implemented")
    }

    override fun upsert(name: String): Upsert {
        TODO("Not yet implemented")
    }

    override fun upsert(name: String, action: Upsert.() -> Unit): Upsert {
        TODO("Not yet implemented")
    }

    override fun insert(name: String, force: Boolean): Insert {
        TODO("Not yet implemented")
    }

    override fun insert(name: String, force: Boolean, action: Insert.() -> Unit): Insert {
        TODO("Not yet implemented")
    }

    override fun table(name: String, force: Boolean): Table {
        TODO("Not yet implemented")
    }

    override fun table(name: String, force: Boolean, action: Table.() -> Unit): Table {
        TODO("Not yet implemented")
    }

    override fun <T> get(type: Class<T>, limit: Int): List<T> {
        TODO("Not yet implemented")
    }

    override fun <T> get(type: Class<T>, where: Where, limit: Int): List<T> {
        TODO("Not yet implemented")
    }

    init { this.apply(action);DataHub.setConnection(this) }
}