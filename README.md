# jbibl

Document catalogizer

I have a plenty of e-books (PDFs, docs, txts etc). Each time I need a book or article from my library I must scroll over this huge number of files. This is why this application had been developed.

Usage

1. Copy the files start.bat, *.class, main.css, *.jar, jBibl.mf, jbibl.xslt to a directory which is readable and writable by your current user.

2. Edit the start.bat file:
  java -jar "THE_PATH_YOU_COPIED_THE\jBibl.jar" UPDATE "THE_PATH_WHERE_YOUR_DOCUMENTS_RESIDE" [COMMA_SEP_LIST_OF_FILE_TYPES e.g. *.txt,*.pdf] [LOG_LEVEL 1,2 or 3] [OPEN_THE_DOCUMENT_LIST_WHEN_DONE yes or no]

3. Execute the batch file.

4. You should have a new XML file created within your application folder with name like jBibl_PATH_TO_YOUR_DOC_DIR.XML.

5. Open this XML in your favorite browser. The included XSLT aims to display the list of the documents in a friendly table.

The program uses the Finder class form the Oracle to enumerate the files and folders.

This application is under development which means that some features were not implemented yet.
