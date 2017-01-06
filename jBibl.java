/**
 * This is the base class for the jBibl application.
 * Purpose: try to gather (harvest) files from the file system 
 *          based on a given root directory and create a searchable list of 
 *          the found files. The files are documents and/or executables.
 *          It can be used to maintain a library of documents and softwares
 *          in order to find them later. The items in the library should be 
 *          accept tags and the harvesting part of the software should be 
 *          able to guess the document content/title/author etc. in order to 
 *          reduce the manual entering.
 *
 * Requests:
 * 			- Be able to read zip files.
 * 			- Auto detect of title, author and tags.
 *
 * @since 08th of April 2015.
 *          
 */
import java.io.*;

public class jBibl {
	public enum FileType {
		textplain
	}
	
    public static void main(String[] args)
       throws IOException, ClassNotFoundException {
    	jBiblXML xml = new jBiblXML(args);
    	
    	Harvester h = new Harvester();
    	h.setFileTypes(jBiblXML.s.harvestFiletype);
    	h.setRootDir(jBiblXML.s.rootDir);
    	h.harvest();
    	
    	xml.flush();
	}
}