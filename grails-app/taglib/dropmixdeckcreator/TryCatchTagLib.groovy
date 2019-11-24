package dropmixdeckcreator

class TryCatchTagLib {
    def Try = { attrs, body ->
        try {
            out << body()
            request.exception = null
        } catch (Throwable e) {
            request.exception = e
        }
    }

    def Catch = { attrs, body ->
        if (request.exception) {
            out << body(exception: request.exception)
            request.exception = null
        }
    }
}
