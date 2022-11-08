package test.common

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.databind.ser.std.StdSerializer



class LoginDeserialization : StdDeserializer<LoginUser>(LoginUser::class.java) {
    override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext): LoginUser {
        val mapper = jsonParser.codec as ObjectMapper
        val node: JsonNode = mapper.readTree(jsonParser)
        val userName = node.getNode<TextNode>("login").textValue()
        val password = node.getNode<TextNode>("password").textValue()

        return LoginUser(userName, password)
    }

}

inline fun <reified T : JsonNode> JsonNode.getNode(property: String): T =
    when (val node = get(property)) {
        is NullNode, null -> throw IllegalStateException("Expecting to find property with '$property' in node '$this'")
        else -> ensureType(node, property)
    }

inline fun <reified T : JsonNode?> JsonNode.ensureType(node: JsonNode, property: String): T {
    require(node is T) {
        val type = node::class.java.simpleName
        val expectedType = T::class.java.simpleName
        "Expecting node: '$this', property: '$property' to be of type '$expectedType' but '$type' was found instead"
    }

    return node
}