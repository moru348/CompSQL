package dev.moru3.compsql.datatype.types.text

import dev.moru3.compsql.datatype.BaseDataType
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

/**
 * LONGTEXT とは 0KBから65535KB までの文字を格納できます。これ以上のサイズが必要な場合はLONGTEXTを使用してください。
 * 文字数ではないためUTF8を使用する場合は一文字に3KB使用します。
 * CharやVarcharとは違い、PrimaryKeyとしては利用できません。
 */
class TEXT(property: Int): TextBase<String>(property) {
    override val from: Class<String> = String::class.javaObjectType
    override fun get(resultSet: ResultSet, id: String): String? = resultSet.getNString(id)
}

abstract class TextBase<F>(val property: Int): BaseDataType<F, String> {

    final override val typeName: String = "TEXT"
    final override val type: Class<String> = String::class.javaObjectType
    final override val sqlType: Int = Types.VARCHAR
    final override val allowPrimaryKey: Boolean = false
    final override val allowNotNull: Boolean = true
    final override val allowUnique: Boolean = true
    final override val isUnsigned: Boolean = false
    final override val allowZeroFill: Boolean = false
    final override val allowAutoIncrement: Boolean = false
    override val allowDefault: Boolean = true
    override val defaultProperty: String = "$property"
    override val priority: Int = 12

    override fun set(ps: PreparedStatement, index: Int, any: Any?) { super.set(ps, index, any.toString()) }
}