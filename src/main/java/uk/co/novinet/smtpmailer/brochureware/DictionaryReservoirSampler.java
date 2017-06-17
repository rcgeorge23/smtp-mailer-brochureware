package uk.co.novinet.smtpmailer.brochureware;

import static java.lang.String.format;
import static org.apache.commons.lang3.text.WordUtils.capitalize;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

@Service
public class DictionaryReservoirSampler {

	private static final String DICTIONARY_FILENAME = "/dictionary_shuffled.txt";
	private static final String EMAIL_ADDRESS_FORMAT = "%s@%s.com";
	private static final int MINIMUM_WORDS_PER_SENTENCE = 3;
	private static final int MAXIMUM_WORDS_PER_SENTENCE = 12;

	private List<String> words;
	
	@PostConstruct
	public void init() {
		try {
			words = IOUtils.readLines(dictionaryFileStream(), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String randomEmailAddress() {
		return format(EMAIL_ADDRESS_FORMAT, chooseWord(), chooseWord());
	}

	public String randomSentences(int numberOfSentences) {
		StringBuffer stringBuffer = new StringBuffer();

		for (int i = 0; i < numberOfSentences; i++) {
			stringBuffer.append(randomSentence());
			if (i < numberOfSentences - 1) {
				stringBuffer.append(" ");
			}
		}

		return stringBuffer.toString();
	}

	public String randomSentence() {
		int numberOfWords = ThreadLocalRandom.current().nextInt(MINIMUM_WORDS_PER_SENTENCE, MAXIMUM_WORDS_PER_SENTENCE);
		
		StringBuffer stringBuffer = new StringBuffer();

		for (int i = 0; i < numberOfWords; i++) {
			String word = chooseWord();
			
			if (i == 0) {
				word = capitalize(word);
			}
			
			stringBuffer.append(word);
			
			if (i < numberOfWords - 1) {
				stringBuffer.append(" ");
			} else {
				stringBuffer.append(".");
			}
		}

		return stringBuffer.toString();
	}

	public String chooseWord() {
		return words.get(ThreadLocalRandom.current().nextInt(0, words.size() - 1));
	}

	protected InputStream dictionaryFileStream() {
		return this.getClass().getResourceAsStream(DICTIONARY_FILENAME);
	}
}
