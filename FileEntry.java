/**
 * Represents one file entry.
 * @author istvan_vig
 *
 */
public class FileEntry {
	public Long id;
	public String fullPath;
	public String fileName;
	public String title;
	public String author;
	public String tags;
	public Long fileSize;
	
	public FileEntry() {;}
	
	public FileEntry(Long id_, String full_path, String file_name, String title_, String author_, String tags_, Long file_size) {
		id = id_;
		fullPath = full_path;
		fileName = file_name;
		title = title_;
		author = author_;
		tags = tags_;
		fileSize = file_size;
    }
}
