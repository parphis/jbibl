import java.nio.file.Path;

/**
 * Abstract class to guess the title, the author and the tags for a specific file type.
 * @author istvan_vig
 * @since 2015.06.17.
 *
 */
public abstract class ContentGuesser {
	protected Path p;
	private jBibl.FileType fileType = null;
	
	public ContentGuesser(jBibl.FileType t) {
		this.fileType = t;
	}
	
	public void setPath(Path in_p) {
		this.p = in_p;
	}
	
	protected abstract String guessTitle();
	protected abstract String guessAuthor();
	protected abstract String guessTags();
}
