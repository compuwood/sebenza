package mad.apps.sabenza.data.model.me

import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

@JsonAdapter(Role.Adapter::class)
enum class Role constructor(val value : String?) {

    EMPLOYER("employer"),
    EMPLOYEE("employee"),
    NONE(null);

    override fun toString(): String {
        return value.toString()
    }

    class Adapter : TypeAdapter<Role>() {
        @Throws(IOException::class)
        override fun write(out: JsonWriter, value: Role) {
            out.value(value.value)
        }

        @Throws(IOException::class)
        override fun read(reader: JsonReader): Role {
            val value = reader.nextString()
            return Role.fromValue(value.toString())!!
        }

    }

    companion object {
        fun fromValue(text: String) : Role? {
            for (b in Role.values()) {
                if (b.value.toString() == text) {
                    return b
                }
            }
            return null
        }
    }

}