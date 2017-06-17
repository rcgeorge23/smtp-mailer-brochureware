package uk.co.novinet.smtpmailer.brochureware

import spock.lang.Specification

class DictionaryReservoirSamplerSpecification extends Specification {
	
	static String[] DICTIONARY = ["this", "is", "a", "simple", "dictionary", "with", "different", "length", "words"]
	
	DictionaryReservoirSampler testObj = Spy(DictionaryReservoirSampler)
	
	def setup() {
		testObj.dictionaryFileStream() >> {
			String result = ""
			
			DICTIONARY.each { String word ->
				result = result + (word + "\n")
			}
			
			return new ByteArrayInputStream(result.trim().getBytes())
		}
		testObj.init()
	}
	
	def "chooseWord returns random word from list"() {
		when:
		String choice = testObj.chooseWord()
		
		then:
		DICTIONARY.contains(choice)
	}
	
	def "randomSentence returns a string of between 3 and 12 words. The first letter is capitalised and the last character is a full stop"() {
		when:
		String sentence = testObj.randomSentence()
		int numberOfWords = sentence.split(" ").size()
		
		then:
		numberOfWords <= 12
		numberOfWords >= 3
		sentence.charAt(0).isUpperCase()
		sentence.substring(1).toLowerCase() == sentence.substring(1) //check the rest of the sentence is lowercase
		sentence.substring(sentence.length() - 1) == "."
	}
}
