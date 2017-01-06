/**
 * Read in and save out the Settings and the FileEntry objects
 * into the jBibl.xml file.
 * @author istvan_vig
 *
 */
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.Attribute;

public class jBiblXML {
	private static int idx;
	private boolean openAfterUpdate;
	private String jbiblXmlFilename;
	private File jbiblXmlFile;
	public static Settings s;
	public static List<FileEntry> fileEntryList;
	
	private void createDefault(boolean useFixtures) {
		if (useFixtures) {
			for(int i=0; i<10; i++) {
				FileEntry f = new FileEntry((long) i, "d:\\"+i+".pdf", i+".pdf", i+" texts", "unknown author", i+", pdf, unknown, áíûõüöúóé", (long) 128+i);
				fileEntryList.add(f);
			}
		}
	}
	
	private boolean backupjBiblXML() {
		try {
			Files.move(Paths.get(jbiblXmlFilename), Paths.get(jbiblXmlFilename+"~"), StandardCopyOption.REPLACE_EXISTING);
			return true;
		}
		catch(IOException eio) {
			Log.error(eio.getMessage());
			//eio.printStackTrace();
			return false;
		}
	}
	
	public static FileEntry getNextFileEntry() {
    	if(fileEntryList.size()==0) return null;
    	return fileEntryList.get((idx>fileEntryList.size()-1) ? fileEntryList.size()-1 : idx++);
    }
	
	public static FileEntry getPrevFileEntry() {
    	if(fileEntryList.size()==0) return null;
    	return fileEntryList.get((idx<0) ? 0 : idx--);
    }
	
	public static FileEntry getNthFileEntry(int nth) {
    	if(fileEntryList.size()==0) return null;
    	if (nth>fileEntryList.size()-1) {
    		nth = fileEntryList.size()-1;
    	}
    	if (nth<0) {
    		nth = 0;
    	}
    	return fileEntryList.get(nth);
    }
	
	public jBiblXML(String[] args) {
		idx = 0;
		
		jbiblXmlFilename = "jBibl_";
		
		fileEntryList = new ArrayList<FileEntry>();
		
		openAfterUpdate = false;
		
		if(args.length>=1) {
			if(args[0].equals("UPDATE")) {
				s = new Settings("", "*.txt", 3, 1, "");
		
				if(args.length>=1) {
					s.rootDir = args[1];
					String tmp = "";
					tmp = s.rootDir.replace(":\\", "_");
					tmp = tmp.replace("\\", "_");
					tmp = tmp.replace(" ", "_");
					tmp = tmp.replace("/", "_");
					jbiblXmlFilename += tmp + ".xml";
				}
				if(args.length>=2) {
					s.harvestFiletype = args[2];
				}
				if(args.length>=3) {
					s.logLevel = Integer.parseInt(args[3]);
				}
				if(args.length>=4) {
					if(args[4].equals("yes")) {
						openAfterUpdate = true;
					}
				}
				
				if(jBiblXML.s.logLevel==3) {
					Log.info("jBiblXML::Start it using the following settings - root dir: " + s.rootDir + ", file types: " + s.harvestFiletype + ", log level: " + s.logLevel.toString());
				}
			}
			if(args[0].equals("FIND")) {
				;
			}
		}
	}
	
	public void read() {
		try {
			jbiblXmlFile = new File(jbiblXmlFilename);
			// First, create a new XMLInputFactory
		  XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		  // Setup a new eventReader
		  InputStream in = new FileInputStream(jbiblXmlFilename);
		  XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
		  // read the XML document
		  FileEntry f = null;
		
		  while (eventReader.hasNext()) {
			  XMLEvent event = eventReader.nextEvent();
			  
			  if (event.isStartElement()) {
				  StartElement startElement = event.asStartElement();
				  
				  if(startElement.getName().getLocalPart()=="file") {
					  f = new FileEntry();
				  }
				  
				  if(startElement.getName().getLocalPart()=="file") {
					  @SuppressWarnings("unchecked")
					Iterator<Attribute> attributes = startElement.getAttributes();
		            while (attributes.hasNext()) {
		              Attribute attribute = attributes.next();
		              if (attribute.getName().getLocalPart()=="full_path") {
		                f.fullPath = attribute.getValue();
		              }
		            }
				  }
				  
				  if(startElement.getName().getLocalPart()=="file_name") {
					  event = eventReader.nextEvent();
					  f.fileName = event.asCharacters().getData();
				  }
				  
				  if(startElement.getName().getLocalPart()=="title") {
					  event = eventReader.nextEvent();
					  f.title = event.asCharacters().getData();
				  }
				  
				  if(startElement.getName().getLocalPart()=="author") {
					  event = eventReader.nextEvent();
					  f.author = event.asCharacters().getData();
				  }
				  
				  if(startElement.getName().getLocalPart()=="tags") {
					  event = eventReader.nextEvent();
					  f.tags = event.asCharacters().getData();
				  }
				  
				  if(startElement.getName().getLocalPart()=="file_size") {
					  event = eventReader.nextEvent();
					  f.fileSize = Long.parseLong(event.asCharacters().getData());
				  }
				  
				  if(startElement.getName().getLocalPart()=="id") {
					  @SuppressWarnings("unchecked")
					Iterator<Attribute> attributes = startElement.getAttributes();
		            while (attributes.hasNext()) {
		              Attribute attribute = attributes.next();
		              if (attribute.getName().getLocalPart()=="value") {
		                f.id = Long.parseLong(attribute.getValue());
		              }
		            }
				  }
			  }
			  if (event.isEndElement()) {
				  if (event.asEndElement().asEndElement().getName().getLocalPart()=="file") {
					  if(f!=null) {
						  fileEntryList.add(f);
					  }
				  }
			  }
		  }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Given an arraylist - fileEntryList - this function writes out the data to 
	 * the jbibl.xml file. First it truncates the content and then writes.
	 */
	public void flush() {
		jbiblXmlFile = new File(jbiblXmlFilename);
		
		if (jbiblXmlFile.exists()) {
			if (backupjBiblXML()==false) {
				Log.error("Could not create backup of the current jBibl.xml file!");
				return;
			}
			jbiblXmlFile.delete();
			jbiblXmlFile = new File(jbiblXmlFilename);
		}
		
		FileOutputStream fos;
		XMLOutputFactory factory = XMLOutputFactory.newInstance();

		try {
			fos = new FileOutputStream(jbiblXmlFilename, false);
			XMLStreamWriter writer = factory.createXMLStreamWriter(fos, "UTF-8");
		    
		    writer.writeStartDocument("UTF-8", "1.0");
		    writer.writeProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"jbibl.xslt\"");
		    writer.writeStartElement("jbibl");
		    
		    writer.writeStartElement("settings");
		    writer.writeStartElement("setting");
		    //writer.writeAttribute("profile_name", jBiblXML.s.profileName);
		    writer.writeStartElement("harvest_filetype");
		    writer.writeCharacters(jBiblXML.s.harvestFiletype);
		    writer.writeEndElement();
		    writer.writeStartElement("root_dir");
		    writer.writeCharacters(jBiblXML.s.rootDir);
		    writer.writeEndElement();
		    writer.writeStartElement("log_level");
		    writer.writeCharacters(jBiblXML.s.logLevel.toString());
		    writer.writeEndElement();
		    writer.writeEndElement();
		    writer.writeEndElement();
		     
		    writer.writeStartElement("files");
		    for (FileEntry tmp : fileEntryList) {
			    writer.writeStartElement("file");
			    writer.writeAttribute("full_path", tmp.fullPath);
			    writer.writeStartElement("file_name");
			    writer.writeCharacters(tmp.fileName);
			    writer.writeEndElement();
			    writer.writeStartElement("title");
			    writer.writeCharacters(tmp.title);
			    writer.writeEndElement();
			    writer.writeStartElement("author");
			    writer.writeCharacters(tmp.author);
			    writer.writeEndElement();
			    writer.writeStartElement("tags");
			    writer.writeCharacters(tmp.tags);
			    writer.writeEndElement();
			    writer.writeStartElement("file_size");
			    if(tmp.fileSize!=null) {
			    	writer.writeCharacters(tmp.fileSize.toString());
			    }
			    else {
			    	writer.writeCharacters("0");
			    }
			    writer.writeEndElement();
			    writer.writeStartElement("id");
			    if(tmp.id!=null) {
			    	writer.writeAttribute("value", tmp.id.toString());
			    }
			    else {
			    	writer.writeCharacters("0");
			    }
			    writer.writeEndElement();
			    writer.writeEndElement();
		    }
		    writer.writeEndElement();
		     
		    writer.writeEndElement();
		    writer.writeEndDocument();

		    writer.flush();
		    writer.close();
		    fos.close();

		 } catch (XMLStreamException e) {
		     e.printStackTrace(); 
		 } catch(FileNotFoundException fe) {
			 Log.error("File not found "+jbiblXmlFilename+" "+fe.getMessage());
		 } catch(IOException ioe) {
			 Log.error(ioe.getMessage());
		 }
		
		if(openAfterUpdate) {
			try {
				File f = new File(jbiblXmlFilename);
				Desktop.getDesktop().browse(f.toURI());
			}
			catch(IOException ioe) {
				Log.error("Could not open jBibl.xml file: "+ioe.getMessage());
			}
		}
	}
}
