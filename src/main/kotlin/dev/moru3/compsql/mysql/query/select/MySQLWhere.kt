package dev.moru3.compsql.mysql.query.select

import dev.moru3.compsql.*
import dev.moru3.compsql.datatype.BaseDataType
import dev.moru3.compsql.syntax.*

open class MySQLWhere: FirstWhere {

    var limit: Int? = null

    val list = mutableListOf<Pair<Any?, BaseDataType<*,*>>>()

    var string = ""

    override fun add(string: String): FilteredWhere {
        this.string+=string
        return MySQLFilteredWhere(this)
    }

    override fun limit(limit: Int): FirstWhere {
        this.limit = limit
        return this
    }

    override fun orderBy(key: String, orderType: OrderType): FirstWhere {
        string+=" ORDER BY $key $orderType"
        return this
    }

    override fun key(key: String): KeyedWhere {
        string += " $key"
        return MySQLKeyedWhere(this)
    }

    override fun buildAsRaw(): Pair<String, List<Pair<Any?, BaseDataType<*,*>>>> {
        if(limit!=null) { string+=" LIMIT $limit" }
        return (if(string.isEmpty()) "" else " WHERE $string") to list
    }
}

class MySQLKeyedWhere(private val data: MySQLWhere): KeyedWhere {
    fun end(string: String, value: Any): FilteredWhere {
        data.string += " $string ?"
        data.list.add((value to checkNotNull(TypeHub[value::class.javaObjectType].getOrNull(0))))
        return MySQLFilteredWhere(data)
    }

    fun end(string: String): FilteredWhere {
        data.string += " $string"
        return MySQLFilteredWhere(data)
    }

    override fun equal(value: Any): FilteredWhere = end("<=>", value)

    override fun notEquals(value: Any): FilteredWhere = end("<>", value)

    override fun greater(value: Any): FilteredWhere = end(">", value)

    override fun less(value: Any): FilteredWhere = end("<", value)

    override fun greaterOrEquals(value: Any): FilteredWhere = end(">=", value)

    override fun lessOrEquals(value: Any): FilteredWhere = end("<=", value)
    override fun isNull(): FilteredWhere = end("IS NULL")

    override fun isNotNull(): FilteredWhere = end("IS NOT NULL")

    override fun isTrue(): FilteredWhere = end("IS TRUE")

    override fun isFalse(): FilteredWhere = end("IS FALSE")

    override fun isUnknown(): FilteredWhere = end("IS UNKNOWN")

    override fun between(from: Any, to: Any): FilteredWhere {
        data.string += " BETWEEN ? AND ?"
        data.list.add((from to checkNotNull(TypeHub[from::class.javaObjectType].getOrNull(0))))
        data.list.add((to to checkNotNull(TypeHub[to::class.javaObjectType].getOrNull(0))))
        return MySQLFilteredWhere(data)
    }

    override fun notBetween(from: Any, to: Any): FilteredWhere {
        data.string += " NOT"
        return between(from, to)
    }

    override fun isIn(vararg values: Any): FilteredWhere {
        data.string += " IN (${MutableList(values.size) { "?" }.joinToString(", ")})"
        values.forEach { data.list.add((it to checkNotNull(TypeHub[it::class.javaObjectType].getOrNull(0)))) }
        return MySQLFilteredWhere(data)
    }

    override fun isNotIn(vararg values: Any): FilteredWhere {
        data.string += " NOT"
        return isIn(values)
    }

    override fun like(regex: String): FilteredWhere {
        return end("LIKE", regex)
    }

    override fun notLike(regex: String): FilteredWhere {
        data.string += " NOT"
        return like(regex)
    }
}

class MySQLFilteredWhere(private val data: MySQLWhere): FilteredWhere {
    override fun limit(limit: Int): Where {
        data.limit = limit
        return this
    }

    override fun orderBy(key: String, orderType: OrderType): Where {
        data.string+=" ORDER BY $key $orderType"
        return this
    }

    override fun and(key: String): KeyedWhere {
        data.string += " and $key"
        return MySQLKeyedWhere(data)
    }

    override fun or(key: String): KeyedWhere {
        data.string += " or $key"
        return MySQLKeyedWhere(data)
    }

    override fun add(string: String): FilteredWhere {
        data.string+=string
        return this
    }

    override fun buildAsRaw(): Pair<String, List<Pair<Any?, BaseDataType<*,*>>>> = data.buildAsRaw()
}