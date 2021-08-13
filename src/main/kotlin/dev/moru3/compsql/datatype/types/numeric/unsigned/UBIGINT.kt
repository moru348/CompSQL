package dev.moru3.compsql.datatype.types.numeric.unsigned

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.DataHub.addCustomType
import java.math.BigDecimal
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

/**
 * UBigInt(Unsigned BigInt)とは 0 から 18,446,744,073,709,551,615 までの整数を格納できるSQLの型です。
 * これ以上大きな型が必要な場合はDECIMAL、NUMERICを使用します。
 * Unsigned: UBIGINT, Non-Unsigned: BIGINT
 * 注意: numeric系のプロパティは"最大数"ではなく"最大桁数"なのでお間違えなく。
 *
 * 18446744073709551615 = 1844京6744兆737億955万1615
 */
open class UBIGINT(val property: Byte): DataType<BigDecimal, BigDecimal> {

    override val typeName: String = "BIGINT"
    override val from: Class<BigDecimal> = BigDecimal::class.java
    override val type: Class<BigDecimal> = BigDecimal::class.java
    override val sqlType: Int = Types.BIGINT
    override val allowPrimaryKey: Boolean = true
    override val allowNotNull: Boolean = true
    override val allowUnique: Boolean = true
    override val isUnsigned: Boolean = true
    override val allowZeroFill: Boolean = true
    override val allowAutoIncrement: Boolean = true
    override val allowDefault: Boolean = true
    override val defaultProperty: String = "$property"
    override val priority: Int = 10
    override val action: (PreparedStatement, Int, BigDecimal) -> Unit = { ps, i, a -> ps.setBigDecimal(i, a) }
    override val convert: (value: BigDecimal) -> BigDecimal = { it }

    override fun set(ps: PreparedStatement, index: Int, any: Any?) {
        check(any is BigDecimal) { "The type of \"any\" is different from \"type\"." }
        check(any >= BigDecimal(0) && any <= max) { "Unsigned BigInt can only be stored within the range of 0 to 18446744073709551615." }
        action.invoke(ps, index, any)
    }

    override fun get(resultSet: ResultSet, id: String): BigDecimal? = resultSet.getBigDecimal(id)

    init { addCustomType(this) }

    companion object { private val max = BigDecimal("18446744073709551615") }
}