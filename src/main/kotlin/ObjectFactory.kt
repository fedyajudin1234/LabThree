class ObjectFactory(objectBuilder: ObjectBuilder) {
    private val userTypeList = ArrayList<ObjectBuilder>()

    init {
        objectRecorder(objectBuilder)
    }

    fun objectRecorder(objectBuilder: ObjectBuilder): List<*> {
        val list: List<ObjectBuilder> = ArrayList()
        userTypeList.add(objectBuilder)
        return list
    }

    fun getBuilderByName(name: String): ObjectBuilder? {
        val userTypeIterator: Iterator<ObjectBuilder> = userTypeList.iterator()
        while (userTypeIterator.hasNext()) {
            val objectBuilder = userTypeIterator.next()
            if (objectBuilder.typeName() == name) {
                return objectBuilder
            }
        }
        return null
    }
}