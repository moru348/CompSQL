package dev.moru3.compsql.connection

import dev.moru3.compsql.DataHub
import dev.moru3.compsql.Database
import dev.moru3.compsql.table.Table
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

class SQLiteConnection(private val url: String, override val timeout: Int = 10, action: SQLiteConnection.()->Unit = {}): Database() {

    constructor(file: File, timeout: Int = 10): this("jdbc:sqlite:${file.absolutePath}", timeout)

    override var connection: Connection = DriverManager.getConnection(url)
        private set

    override val safeConnection: Connection get() = reconnect(false)

    override fun table(table: Table, force: Boolean) {
        TODO("Not yet implemented")
    }

    override fun table(name: String, force: Boolean, action: Table.() -> Unit) {
        TODO("Not yet implemented")
    }

    override fun reconnect(force: Boolean): Connection {
        if(!this.isClosed) if(force) connection.close() else return connection
        connection = DriverManager.getConnection(url)
        return connection
    }

    init { this.apply(action);DataHub.setConnection(this) }
}