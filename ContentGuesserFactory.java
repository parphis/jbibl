import java.nio.file.Path;

/**
 * Th factory class to create the file type - specific object. 
 * @author istvan_vig
 *
 */
public class ContentGuesserFactory {
	public static ContentGuesser buildContentGuesser(jBibl.FileType f, Path inFile) {
		ContentGuesser cg = null;
		
		switch(f) {
		case textplain:
			cg = new TextContentGuesser();
			cg.setPath(inFile);
			break;
		default:
			Log.warning(f.toString()+" not implemented yet!");
		}
		return cg;
	}
}
