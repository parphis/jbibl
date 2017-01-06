/*
 * Copyright (c) 2009, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

import static java.nio.file.FileVisitResult.*;

/**
 * A {@code FileVisitor} that finds
 * all files that match the
 * specified pattern.
 */
public class Finder extends SimpleFileVisitor<Path> {

    private final PathMatcher matcher;
    private int numMatches = 0;
    private String pattern;
    private String rootDir;

    Finder(String pattern) {
        this.matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
    }

    void setPattern(String p) {
        this.pattern = p;
    }
    
    String getPattern() {
        return this.pattern;
    }

    void setRootDir(String r) {
        this.rootDir = r;
    }
    
    String getRootDir() {
        return this.rootDir;
    }
    
    // Compares the glob pattern against the file or directory name.
    void find(Path file) {
    	String guessedTags = "";
    	String contentType = "";
        Path name = file.getFileName();
        
        if (name != null && matcher.matches(name)) {
        	try {
        		contentType = Files.probeContentType(name);
        		Log.info("Content type: "+contentType);
        		contentType = contentType.replaceAll("[^A-Za-z0-9 ]", "");
        		guessedTags = ContentGuesserFactory.buildContentGuesser(jBibl.FileType.valueOf(contentType), file).guessTags();
        		//Log.info("Guessed tags: "+guessedTags);
        	}
        	catch(IOException ioe) {
        		Log.error("Determining file type: "+ioe.getMessage());
        	}
        	catch(java.lang.IllegalArgumentException iae) {
        		Log.warning(contentType+" not implemented yet!");
        	}
        	
            FileEntry tmp;
            tmp = new FileEntry((long)numMatches, file.toAbsolutePath().toString(), file.getFileName().toString(), "title", "author", guessedTags, file.toFile().length());
            jBiblXML.fileEntryList.add(tmp);
            if((numMatches%10)==0) {
            	System.out.print("Found "+numMatches+"\r");
            }
            if(jBiblXML.s.logLevel==3) {
            	Log.info("FileEntry object created with full path: "+jBiblXML.fileEntryList.get(numMatches).fullPath);
            }
            numMatches++;
        }
    }

    // Prints the total number of matches to standard out.
    void done() {
    	System.out.println("Found "+numMatches+" files");
    }

    // Invoke the pattern matching method on each file.
    @Override
    public FileVisitResult visitFile(Path file,
            BasicFileAttributes attrs) {
        find(file);
        return CONTINUE;
    }

    // Invoke the pattern matching method on each directory.
    @Override
    public FileVisitResult preVisitDirectory(Path dir,
            BasicFileAttributes attrs) {
        find(dir);
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file,
            IOException exc) {
        System.err.println(exc);
        return CONTINUE;
    }
}