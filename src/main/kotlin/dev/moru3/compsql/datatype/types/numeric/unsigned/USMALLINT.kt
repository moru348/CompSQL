package dev.moru3.compsql.datatype.types.numeric.unsigned

import dev.moru3.compsql.datatype.BaseDataType
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

/**
 * SmallInt 0 から 65535 までの整数を格納できるSQLの型です。
 * これ以上大きな型が必要な場合は INTEGER, UINTEGER を使用します。
 * Unsigned: USMALLINT, Non-Unsigned: SMALLINT
 * 注意: numeric系のプロパティは"最大数"ではなく"最大桁数"なのでお間違えなく。
 */
open class USMALLINT(property: Byte): USmallIntBase<Int>(property) {
    override val from: Class<Int> = Int::class.javaObjectType
    override fun get(resultSet: ResultSet, id: String): Int? = resultSet.getInt(id)
}

abstract class USmallIntBase<F>(val property: Byte): BaseDataType<F, Int> {

    final override val typeName: String = "SMALLINT"
    final override val type: Class<Int> = Int::class.javaObjectType
    final override val sqlType: Int = Types.SMALLINT
    final override val allowPrimaryKey: Boolean = true
    final override val allowNotNull: Boolean = true
    final override val allowUnique: Boolean = true
    final override val isUnsigned: Boolean = true
    final override val allowZeroFill: Boolean = true
    final override val allowAutoIncrement: Boolean = true
    override val allowDefault: Boolean = true
    override val defaultProperty: String = "$property"
    override val priority: Int = 10

    override fun set(ps: PreparedStatement, index: Int, any: Any?) {
        check(any is Number?) { "The type of \"${if(any!=null) any::class.javaObjectType.simpleName else "null"}\" is different from \"Number\"." }
        super.set(ps, index, any?.toInt())
    }
}