import java.util.*
import kotlin.collections.ArrayList


class Hash(var size: Int) : IHash, Cloneable {
    var currValue = 0
    var totalValue = 0
    var maxValue = 0
    inner class Entry constructor(val key: Any?, var value: Any?) {
        var next: Entry? = null

        override fun toString(): String {
            var tmp: Entry? = this
            val sb = StringBuilder()
            while (tmp != null) {
                sb.append(tmp.key.toString() + " -> " + tmp.value + "; ")
                tmp = tmp.next
            }
            return sb.toString()
        }
    }

    private val array: Array<Entry?>

    init {
        array = arrayOfNulls(size)
    }

    override fun put(key: Any?, value: Any?) {
        val hash = key.hashCode() % size
        var e = array[hash]
        if (e == null) {
            array[hash] = Entry(key, value)
        } else {
            while (e!!.next != null) {
                if (e.key === key) {
                    e.value = value
                    return
                }
                e = e.next
            }
            if (e.key === key) {
                e.value = value
                return
            }
            e.next = Entry(key, value)
        }
    }

    override fun get(key: Any): Any? {
        val hash = key.hashCode() % size
        var e = array[hash] ?: return null
        while (e != null) {
            if (e.key == key) {
                return e.value
            }
            e = e.next!!
        }
        return null
    }

    fun remove(key: Any): Entry? {
        val hash = key.hashCode() % size
        var e = array[hash] ?: return null
        if (e.key == key) {
            array[hash] = e.next
            e.next = null
            return e
        }
        var prev = e
        e = e.next!!
        while (e != null) {
            if (e.key == key) {
                prev = e
                e = e.next!!
                prev.next = e.next
                e.next = null
                return e
            }
        }
        return null
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (i in 0 until size) {
            if (array[i] != null) {
                sb.append(
                    """	$i ${array[i]}
"""
                )
            } else {
                sb.append("$i null\n")
            }
        }
        return sb.toString()
    }

    override fun forEach(`as`: ActionStarter) {
        var e = array[0]
        for (i in 0 until size) {
            e = array[i]
            `as`.toDo(e)
        }
    }

    fun sizeRecorder() {
        var middleValue = 0
        val arrayList = ArrayList<Int>()
        for (i in 0 until size) {
            var e = array[i]
            currValue = 0
            while(e != null){
                currValue++
                e = e.next
            }
            arrayList.add(currValue)
            middleValue += currValue
        }
        val min = Collections.min(arrayList)
        val max = Collections.max(arrayList)
        middleValue = middleValue / size
        totalValue = max - min - middleValue
        maxValue = max
        println("--------------------------------------------------------------------------------")
        println("Размер: $size")
        println("Минимальное значение: $min")
        println("Максимальное значение: $max")
        println("Среднее значение: $middleValue")
        println("Значение, которое мы берём для увеличения хэш-таблицы((макс - мин) - ср.знач): $totalValue")
        println("Для просмотра, по окончании итераций хэш-таблицы, нажмите Hash")
        println("--------------------------------------------------------------------------------")
    }

    @Throws(CloneNotSupportedException::class)
    public override fun clone(): Hash {
        return super.clone() as Hash
    }

    fun insert(hash: Hash): Hash {
        for (i in 0 until size) {
            var e = array[i]
            while (e != null) {
                hash.put(e.key, e.value)
                e = e.next
            }
        }
        return hash
    }

    fun resizeHash(hash: Hash): Hash {
        var hash = hash
        hash.sizeRecorder()
        if(hash.maxValue > hash.totalValue) {
            val hash1 = hash.clone()
            while (hash.maxValue > hash.totalValue) {
                size = size * 2
                hash = Hash(size)
                hash = hash1.insert(hash)
                    /*for (i in 0 until number) {
                    hash = hash1.insert(hash)
                }*/
                hash.sizeRecorder()
            }
        }
        return hash
    }
}