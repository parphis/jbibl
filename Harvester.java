/**
 * Purpose: try to gather (harvest) files from the file system 
 *          based on a given root directory and create a searchable list of 
 *          the found files. The files are documents and/or executables it depends 
 *          on the extension list defined in a configuration.
 *          It should be able to guess the document content/title/author etc. 
 *          in different type of files.
 *          This is the part of the jBibl app. It will be executed by a scheduled 
 *          task or by hand by the user.
 *
 * @since 08th of Arpil 2015.
 * @author Istvan Vig
 * @version 0.0.0.1
 */
import java.io.*;
import java.nio.file.*;

public class Harvester
{
	private String fileTypes;
	private String rootDir;
	
	public void setFileTypes(String s) {
		fileTypes = s;
	}
	
	public void setRootDir(String s) {
		rootDir = s;
	}
	
	public Harvester() {
		;
	}
	
    public static void main(String[] args)
       throws IOException {
    	if(args.length<2) {
    		System.out.println("Usage: java jBibl [root dir] [file types]");
    		return;
    	}
    	
        // *.txt,*.pdf,*.csv
        Finder f = new Finder("{" + args[1] + "}");
        Path rootDir = Paths.get(args[0]);
        Files.walkFileTree(rootDir, f);
        f.done();
    }
    
    public void harvest() {
    	if(fileTypes=="") {
    		Log.error("Harvester::Empty file types parameter! Could not run, exiting...");
    		return;
    	}
    	
    	if(rootDir=="") {
    		Log.error("Harvester::Empty root directory parameter! Could not run, exiting...");
    		return;
    	}
    	
    	try {
			Finder f = new Finder("{"+fileTypes+"}");
	        Path root = Paths.get(rootDir);
	        Files.walkFileTree(root, f);
	        f.done();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
