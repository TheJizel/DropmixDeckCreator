package dropmixdeckcreator

import dropmixdeckcreator.TryCatchTagLib
import grails.testing.web.taglib.TagLibUnitTest
import spock.lang.Specification

class TryCatchTagLibSpec extends Specification implements TagLibUnitTest<TryCatchTagLib> {

    def setup() {
    }

    def cleanup() {
    }

    void "test try - exception"() {
        given:
        Exception exception = new Exception()
        Closure body = { throw exception }
        assert request['exception'] == null

        when:
        tagLib.try([:], body)

        then:
        request['exception'] == exception
    }

    void "test try - no exception"() {
        given:
        Exception exception = new Exception()
        request['exception'] = exception
        String body = 'body'

        when:
        String result = applyTemplate("<g:try>$body</g:try>")

        then:
        result == body
        request['exception'] == null
    }

    void "test catch - no exception"() {
        given:
        request['exception'] = null
        String body = 'body'

        when:
        String result = applyTemplate("<g:catch>$body</g:catch>")

        then:
        request['exception'] == null
        !result
    }

    void "test catch - exception"() {
        given:
        Exception exception = new Exception("This is my exception")
        request['exception'] = exception
        String body = '${exception}'

        when:
        String result = applyTemplate("<g:catch>$body</g:catch>")

        then:
        request['exception'] == null
        result == "$exception"
    }
}
