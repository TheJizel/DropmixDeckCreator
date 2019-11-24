package dropmixdeckcreator

import dropmixdeckcreator.Card
import dropmixdeckcreator.SetTemplate
import dropmixdeckcreator.SetTemplateUtil
import spock.lang.Specification

class SetTemplateUtilSpec extends Specification {
    def setup() {
    }

    def cleanup() {
    }

    void "test computePossibleSetTemplates"() {
        given:
        List<Card> cards = [Mock(Card)]
        int decks = 4
        Map<List<SetTemplate>, List<Card>> possibilities = [:]
        SetTemplate newSetTemplate = GroovyMock(SetTemplate)
        List<List<SetTemplate>> expected = [[newSetTemplate]]

        and:
        SetTemplateUtil.metaClass.static.computeNewPossibilities = { Map<List<SetTemplate>, List<Card>> possibilitiesToUpdate,
                                                                     List<Card> availableCards ->
            assert possibilitiesToUpdate == possibilities
            assert availableCards == cards
            possibilitiesToUpdate.put([newSetTemplate], cards)
        }
        SetTemplateUtil.metaClass.static.computePossibleSetTemplates = { int numberOfDecks,
                                                                         Map<List<SetTemplate>, List<Card>> currentPossibilities ->
            assert numberOfDecks == decks
            assert currentPossibilities.size() == 1
            assert currentPossibilities[([newSetTemplate])] == cards
            expected
        }

        when:
        List<List<SetTemplate>> actual = SetTemplateUtil.computePossibleSetTemplates(cards, decks)

        then:
        actual == expected

        cleanup:
        GroovySystem.metaClassRegistry.removeMetaClass(SetTemplateUtil)
    }

    void "test computePossibleSetTemplates (currentPossibilities is null)"() {
        expect:
        SetTemplateUtil.computePossibleSetTemplates(4, null) == null
    }

    void "test computePossibleSetTemplates (currentPossibilities is empty)"() {
        expect:
        SetTemplateUtil.computePossibleSetTemplates(4, [:]) == []
    }

    void "test computePossibleSetTemplates (non-recursive scenario)"() {
        given:
        int numberOfDecks = 2
        List<SetTemplate> finishedPossibility = [SetTemplate.BOARD_FLEXIBLE_WITH_TWO_PLUS_THREE_FX, SetTemplate.COLOR_BALANCE_WITH_TWO_PLUS_THREE_FX]
        List<SetTemplate> possibilityToExpand = [SetTemplate.BOARD_FLEXIBLE_WITH_TWO_PLUS_THREE_FX]
        Card finishCard = Mock(Card)
        List<Card> finishedList = [finishCard]
        Card expandCard = Mock(Card)
        List<Card> listToExpand = [expandCard]
        Map<List<SetTemplate>, List<Card>> currentPossibilities = [(finishedPossibility): finishedList,
                                                                   (possibilityToExpand): listToExpand]
        int count = 0

        and:
        SetTemplateUtil.metaClass.static.computeNewPossibilities = { Map<List<SetTemplate>, List<Card>> possibilitiesToUpdate,
                                                                     List<Card> availableCards,
                                                                     List<SetTemplate> existingSetTemplates ->
            assert possibilitiesToUpdate == [:]
            assert availableCards == listToExpand
            assert existingSetTemplates == possibilityToExpand
            count++
        }

        when:
        List<List<SetTemplate>> result = SetTemplateUtil.computePossibleSetTemplates(numberOfDecks, currentPossibilities)

        then:
        count == 1
        result == [finishedPossibility]

        cleanup:
        GroovySystem.metaClassRegistry.removeMetaClass(SetTemplateUtil)
    }

    void "test computePossibleSetTemplates (recursive scenario)"() {
        given:
        int numberOfDecks = 2
        List<SetTemplate> possibilityToExpand = [SetTemplate.BOARD_FLEXIBLE_WITH_TWO_PLUS_TWO_FX]
        SetTemplate setTemplateToBeAdded = SetTemplate.COLOR_BALANCE_WITH_TWO_PLUS_TWO_FX
        List<SetTemplate> expandedPossibility = possibilityToExpand + setTemplateToBeAdded
        Card expandCard = Mock(Card)
        List<Card> listToExpand = [expandCard]
        Map<List<SetTemplate>, List<Card>> currentPossibilities = [(possibilityToExpand): listToExpand]
        int count = 0

        and:
        SetTemplateUtil.metaClass.static.computeNewPossibilities = { Map<List<SetTemplate>, List<Card>> possibilitiesToUpdate,
                                                                     List<Card> availableCards,
                                                                     List<SetTemplate> existingSetTemplates ->
            assert possibilitiesToUpdate == [:]
            assert availableCards == listToExpand
            assert existingSetTemplates == possibilityToExpand
            possibilitiesToUpdate.put(expandedPossibility, listToExpand)
            count++
        }

        when:
        List<List<SetTemplate>> result = SetTemplateUtil.computePossibleSetTemplates(numberOfDecks, currentPossibilities)

        then:
        count == 1
        result == [expandedPossibility]

        cleanup:
        GroovySystem.metaClassRegistry.removeMetaClass(SetTemplateUtil)
    }

    void "test computeNewPossibilities (availableCards is null)"() {
        given:
        Map<List<SetTemplate>, List<Card>> possibilitiesToUpdate = [:]

        when:
        SetTemplateUtil.computeNewPossibilities(possibilitiesToUpdate, null)

        then:
        possibilitiesToUpdate.isEmpty()
    }

    void "test computeNewPossibilities (possibilitiesToUpdate is null)"() {
        given:
        Map<List<SetTemplate>, List<Card>> possibilitiesToUpdate = null

        when:
        SetTemplateUtil.computeNewPossibilities(possibilitiesToUpdate, [])

        then:
        possibilitiesToUpdate == null
    }

    void "test computeNewPossibilities (exception)"() {
        given:
        Map<List<SetTemplate>, List<Card>> possibilitiesToUpdate = [:]
        Card cardMock = Mock(Card)
        List<Card> availableCards = [cardMock]
        SetTemplate setTemplateMock = GroovyMock(SetTemplate)

        and:
        SetTemplate.metaClass.static.values = {
            [setTemplateMock]
        }

        when:
        SetTemplateUtil.computeNewPossibilities(possibilitiesToUpdate, availableCards)

        then:
        1 * setTemplateMock.removeNecessaryCardsFromList(availableCards) >> {
            throw new Exception()
        }
        possibilitiesToUpdate.isEmpty()

        cleanup:
        GroovySystem.metaClassRegistry.removeMetaClass(SetTemplate)
    }

    void "test computeNewPossibilities"() {
        given:
        Map<List<SetTemplate>, List<Card>> possibilitiesToUpdate = [:]
        Card cardMock = Mock(Card)
        List<Card> availableCards = [cardMock]
        SetTemplate setTemplateMock = GroovyMock(SetTemplate)

        and:
        SetTemplate.metaClass.static.values = {
            [setTemplateMock]
        }

        when:
        SetTemplateUtil.computeNewPossibilities(possibilitiesToUpdate, availableCards)

        then:
        1 * setTemplateMock.removeNecessaryCardsFromList(availableCards) >> { }
        possibilitiesToUpdate.size() == 1
        possibilitiesToUpdate[([setTemplateMock])] == availableCards

        cleanup:
        GroovySystem.metaClassRegistry.removeMetaClass(SetTemplate)
    }
}
