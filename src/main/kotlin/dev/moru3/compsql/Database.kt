package dev.moru3.compsql

import dev.moru3.compsql.syntax.*
import dev.moru3.compsql.syntax.table.Table
import java.io.Closeable

interface Database: Closeable, Connection {

    val url: String

    /**
     * テーブルを作成します。
     */
    fun table(table_name: String, action: Table.()->Unit): Table

    /**
     * テーブルを作成します。
     */
    fun table(table_name: String): Table

    /**
     * データをinsertします。
     */
    fun insert(table_name: String, action: Insert.() -> Unit): Insert

    /**
     * データをinsertします。
     * @param table_name 挿入するテーブルの名前。
     */
    fun insert(table_name: String): Insert

    /**
     * PK、UIをキーに、データが存在する場合update、存在しない場合insertします。
     */
    fun upsert(table_name: String, action: Upsert.()->Unit): Upsert

    /**
     * PK、UIをキーに、データが存在する場合update、存在しない場合insertします。
     */
    fun upsert(table_name: String): Upsert

    /**
     * SQLのSelect文を作成する際に使用する関数です。
     * select文はSQLのrowを取得する際に使用されます。
     * @param table_name selectに使用するテーブル名。
     * @param columns selectに使用するカラム名。
     */
    fun select(table_name: String, vararg columns: String): Select

    /**
     * SQLのSelect文を作成する際に使用する関数です。
     * select文はSQLのrowを取得する際に使用されます。
     * @param table_name selectに使用するテーブル名。
     * @param columns selectに使用するカラム名。
     * @param action Kotlin向けの引数。高階関数を使う際に使用します。
     */
    fun select(table_name: String, vararg columns: String, action: Select.()->Unit): Select

    /**
     * SQLのSelect文を作成する際に使用する関数です。
     * select文はSQLのrowを取得する際に使用されます。
     * @param table_name selectに使用するテーブル名。
    */
    fun delete(table_name: String): Delete

    /**
     * SQLのdelete文を作成する際に使用する関数です。
     * delete文はテーブル内のrowの削除に使用されます。
     * 注意: 必ずwhere文を使用してください。しなかった場合はすべてのrowが削除されます。
     * @param table_name 対象のテーブル名
     * @param action Kotlin向けの引数。高階関数を使う際に使用します。
     */
    fun delete(table_name: String, action: Delete.()->Unit): Delete

    /**
     * :スマートクエリ。
     * データベースにインスタンス内に定義された情報をinsertします。
     * @TableName や @Column アノテーションをクラス内に定義することでテーブル名やカラム名を先に定義できます。
     * @param instance データベースにinsertする情報を含んだインスタンス。
     */
    fun put(instance: Any): Insert

    /**
     * :スマートクエリ。
     * データベースにインスタンス内に定義された情報をinsertします。
     * put(..)とは違い、PrimaryKeyが重複する場合はinsertせず、情報をアップデートします。
     * @TableName や @Column アノテーションをクラス内に定義することでテーブル名やカラム名を先に定義できます。
     * @param instance データベースにinsertする情報を含んだインスタンス。
     */
    fun putOrUpdate(instance: Any): Upsert

    /**
     * データベースにインスタンス内に定義された情報のテーブルを作成する。
     * @TableName や @Column アノテーションをクラス内に定義することでテーブル名やカラム名を先に定義できます。
     * @param instance データベースにinsertする情報を含んだインスタンス。
     */
    @Deprecated("add(cls: Class<?>)を使用することをおすすめします。")
    fun add(instance: Any): Table

    /**
     * データベースにインスタンス内に定義された情報のテーブルを作成します。
     * @TableName や @Column アノテーションをクラス内に定義することでテーブル名やカラム名を先に定義できます。
     * @param cls データベースにinsertする情報を含んだクラス。
     */
    fun add(cls: Class<*>): Table

    /**
     * :スマートクエリ。
     * データベースにインスタンス内に定義された情報のテーブルを作成します。
     * @TableName や @Column アノテーションをクラス内に定義することでテーブル名やカラム名を先に定義できます。
     */
    fun remove(instance: Any): Delete

    /**
     * :スマートクエリ。
     * Select文でデータを取得し、自動的にtype型のインスタンスを作成してリストで返します。
     * @param type 変換する型
     * @return 取得した値の一覧
     */
    fun <T> get(type: Class<T>): GetQuery<T>

    /**
     * :スマートクエリ。
     * Select文でデータを取得し、自動的にtype型のインスタンスを作成してリストで返します。
     * @param type 変換する型
     * @param where where文
     * @return 取得した値の一覧
     */
    fun <T> get(type: Class<T>, where: FirstWhere): GetQuery<T>

    /**
     * :スマートクエリ。
     * Select文でデータを取得し、自動的にtype型のインスタンスを作成してリストで返します。
     * @param type 変換する型
     * @param action Kotlinの高階関数のための値。
     * @return 取得した値の一覧
     */
    fun <T> get(type: Class<T>, action: Select.() -> Unit): GetQuery<T>

    /**
     * select文を使用する際に使用するwhere文を作成します。
     * 例: where("name").equal("moru")
     * @param key where文の最初のカラムの名前
     */
    fun where(key: String): KeyedWhere

    /**
     * 接続が既に閉じているかを返します。
     */
    val isClosed: Boolean

    interface GetQuery<T> {
        /**
         * クエリを送信します。
         * 一致するデータのインスタンスを格納したリストを返します。
         * 一致するデータがない場合はからのリストを返します。
         */
        fun send(): List<T>
    }
}