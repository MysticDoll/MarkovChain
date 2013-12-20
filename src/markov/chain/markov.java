package markov.chain;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.java.sen.SenFactory;
import net.java.sen.StringTagger;
import net.java.sen.dictionary.Token;

public class markov {

	private ArrayList<String> MarkovTable = new ArrayList<String>();
	private ArrayList<String> SubjectTable = new ArrayList<String>();

	public markov() {

	}

	public void createTable(String source) {
		StringTagger tagger = SenFactory.getStringTagger(null);
		String sourceText = source;
		final Pattern urlPattern = Pattern.compile(
				"(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+",
				Pattern.CASE_INSENSITIVE);

		final Matcher matcher = urlPattern.matcher(sourceText);

		while (matcher.find()) {
			sourceText = sourceText.replaceAll(matcher.group(), "");
		}
		sourceText = sourceText.replaceAll(":", "");
		sourceText = sourceText.replaceAll("RT", "");
		sourceText = sourceText.replaceAll("QT", "");
		sourceText = sourceText.replaceAll("#", "");

		List<Token> tokens = new ArrayList<Token>();
		try {
			tagger.analyze(sourceText, tokens);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			System.out.println("Error");
		}

		for (Token token : tokens) {
			MarkovTable.add(token.getSurface());
			String pos = String.valueOf(token.getMorpheme().getPartOfSpeech()
					.charAt(0));
			if (pos.equals("名")) {
				SubjectTable.add(token.getSurface());
			}// else if(pos.equals("未")){
			// SubjectTable.add(token.getSurface());
			// }
		}
		for (int i = 0; i < MarkovTable.size(); i++) {
			String rep = MarkovTable.get(i);
			if (String.valueOf(rep.charAt(0)).equals("@")) {
				MarkovTable.remove(i);
				MarkovTable.remove(i);
			}

			// System.out.println(MarkovTable.get(i));

		}

		MarkovTable.add("[END]");
	}

	public String chain() {
		String topWord = null;
		String Statement = null;
		String word;
		Random rnd = new Random();
		int indexnum = rnd.nextInt(SubjectTable.size() - 1);
		topWord = SubjectTable.get(indexnum); // 開始後のランダム取り出し
		word = wordselect(topWord); // Exception
		Statement = topWord + word;
		String tempword = word;
		int i = 1;
		while (i == 1) {
			tempword = wordselect(tempword);
			if (tempword == null) {
				i++;
			} else if (tempword == "[END]") {
				i++;
			} else {
				Statement = Statement + tempword;
			}
		}
		return (Statement);

	}

	public String wordselect(String source) {
		String nextWord;
		ArrayList<String> wordList = new ArrayList<String>();
		for (int i = 0; i < MarkovTable.size(); i++) {
			if (MarkovTable.get(i).equals(source)) {
				try {
					wordList.add(MarkovTable.get(i + 1));
				} catch (IndexOutOfBoundsException e) {
					wordList.add("[END]");
				}
			}
		}
		Random rnd = new Random();
		nextWord = wordList.get(rnd.nextInt(wordList.size()));
		/*
		 * Random rnd2 = new Random(); if(rnd2.nextInt(20) == 0){ nextWord =
		 * null;
		 * 
		 * }
		 */
		return (nextWord);

	}

}
