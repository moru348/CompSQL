package dev.moru3.compsql

import dev.moru3.compsql.table.Table
import java.io.Closeable

interface SQL: Closeable, Connection {

    val timeout: Int


    /**
     * テーブルを作成します。また、自動的にSQLに変更内容が同期されます。
     */
    fun table(table: Table, force: Boolean = false)

    /**
     * テーブルを作成します。また、自動的にSQLに変更内容が同期されます。
     */
    fun table(name: String, force: Boolean = false, action: Table.()->Unit = {})

    /**
     * データをinsertします。
     */
    fun insert(name: String, force: Boolean, action: Insert.() -> Unit)

    /**
     * テーブルを作成します。また、自動的にSQLに変更内容が同期されます。
     */
    fun upsert(name: String, action: Upsert.()->Unit = {})

    /**
     * データをinsertします。
     */
    fun upsert(upsert: Upsert)

    /**
     * データをinsertします。
     */
    fun insert(insert: Insert,  force: Boolean)

    /**
     * Databaseにデータをプットします。
     */
    fun put(instance: Any, force: Boolean)

    /**
     * 重複しないように
     */
    fun putOrUpdate(instance: Any)

    /**
     * テーブルを追加します。
     */
    fun add(instance: Any, force: Boolean)

    /**
     * Whereを元にデータベースからデータを取得します。
     */
    fun get(instance: Any, where: Where, limit: Int = 1)

    /**
     * データベースからデータを取得します。
     */
    fun get(instance: Any, limit: Int = 1)

    /**
     * 接続が既に閉じているかを返します。
     */
    val isClosed: Boolean
}