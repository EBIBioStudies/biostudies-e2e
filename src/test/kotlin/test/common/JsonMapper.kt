package test.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class JsonMapper {
    companion object{
         fun createMapper(): ObjectMapper {
            val module = SimpleModule().apply {
                addDeserializer(LoginUser::class.java, LoginDeserialization())
            }
            return jacksonObjectMapper().apply {
                registerModule(module)
            }
        }

    }
}