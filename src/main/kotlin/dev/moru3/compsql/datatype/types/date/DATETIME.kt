package dev.moru3.compsql.datatype.types.date

import dev.moru3.compsql.datatype.BaseDataType
import dev.moru3.compsql.datatype.types.date.property.DateDefaultProperty
import java.sql.Date
import java.sql.ResultSet
import java.sql.Types

class DATETIME(property: DateDefaultProperty?): DateTimeBase<Date>(property) {
    override val from: Class<Date> = Date::class.javaObjectType
    override fun get(resultSet: ResultSet, id: String): Date? = resultSet.getDate(id)
}

abstract class DateTimeBase<F>(val property: DateDefaultProperty?): BaseDataType<F,Date> {

    final override val typeName: String = "DATETIME"
    final override val type: Class<Date> = Date::class.javaObjectType
    final override val sqlType: Int = Types.DATE
    final override val allowPrimaryKey: Boolean = false
    final override val allowNotNull: Boolean = true
    final override val allowUnique: Boolean = false
    final override val isUnsigned: Boolean = false
    final override val allowZeroFill: Boolean = false
    final override val allowAutoIncrement: Boolean = false
    override val allowDefault: Boolean = true
    override val defaultProperty: String? = property?.toString()
    override val priority: Int = 10
}