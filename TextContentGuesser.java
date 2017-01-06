import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Guess the tags of a given text file. It collects the words from the file and counts 
 * their occurence and returns the most 5 rare words.
 * The title can not be guessed yet.
 * The author can not be guessed yet.
 * @author istvan_vig
 *
 */
public class TextContentGuesser extends ContentGuesser {
	private BufferedReader reader;
	
	private void createFileBuffer() {
		Log.info("start guessing tags of "+super.p.toString());
		try {
			reader = Files.newBufferedReader(super.p, StandardCharsets.ISO_8859_1);
			reader.lines();
		}
		catch(java.io.UncheckedIOException mie) {
			try {
				FileInputStream input = new FileInputStream(new File(super.p.toString()));
		        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
		        decoder.onMalformedInput(CodingErrorAction.IGNORE);
		        InputStreamReader inputReader = new InputStreamReader(input, decoder);
		        this.reader = new BufferedReader(inputReader);
			}
			catch(FileNotFoundException fnf) {
				Log.error("TextContentGuesser::createFileBuffer - "+fnf.getMessage());
			}
		}
		catch(IOException ioe) {
			Log.error("TextContentGuesser::createFileBuffer - "+ioe.getMessage());
		}
	}
	
	TextContentGuesser() {
		super(jBibl.FileType.textplain);
	}
	
	@Override
	protected String guessTitle() {
		return "Title can not be guessed yet from a text file";
	}
	
	@Override
	protected String guessAuthor() {
		return "Author can not be guessed yet from a text file";
	}
	
	@Override
	protected String guessTags() {
		int sizeCounter;
		int maxReadChar = 8000;
		int tagsCounter = 0;
		int maxReturnTags = 10;
		Map<String, Long> mapByWords = new HashMap<String, Long>();
		String w = "";
		String tags = "";
		int c;
		
		this.createFileBuffer();
		
		try {
			sizeCounter = 0;
			
			while( ((c=reader.read())!=-1) || (sizeCounter<=maxReadChar) ) {
				if(c>=65) {
					w += (char)c;
				}
				else {
					if(w.length()>4) {
						if (mapByWords.containsKey(w)) {
							mapByWords.replace(w, mapByWords.get(w)+1L);
						}
						else {
							mapByWords.put(w, 1L);
							//Log.info("Added word: "+w);
						}
					}
					w = "";
				}
				sizeCounter++;
			}
			
			while(tagsCounter<=maxReturnTags) {
				if(mapByWords.size()>0) {
					Long foundMax = Collections.max(mapByWords.values());
					
					for(Iterator<Map.Entry<String, Long>> it = mapByWords.entrySet().iterator(); (it.hasNext()) && (tagsCounter<=maxReturnTags); ) {
				      Map.Entry<String, Long> entry = it.next();
				      if(entry.getValue()==foundMax) {
				    	  tags += entry.getKey() + ",";
				    	  tagsCounter++;
				    	  it.remove();
				      }
				    }
				}
				tagsCounter++;
			}
		}
		catch(IOException ioe) {
			Log.error("TextContentGuesser::guessTags - "+ioe.getMessage());
		}
		return tags;
	}
}
