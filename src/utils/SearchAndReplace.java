package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.lang3.StringUtils;

public class SearchAndReplace {
		
	private final File[] allFiles;
	private final String search;
	private final String replace;
	
	public SearchAndReplace(File[] allFiles, String search, String replace) throws Exception {
		
		if(search.equals(replace)) {
			throw new Exception("Search and replace are equals");
		}
		
		this.allFiles = allFiles;
		this.search = search;
		this.replace = replace;
	}
	
	
	public String analyze() throws IOException {
		String output = "Number of occurrences: \n";
		String content;
		int count;
		
		for(File file : allFiles) {
			
			content = readFile(file);
			
			count = StringUtils.countMatches(content, search);
						
			if(count > 0) {
				output += ("[" + count + "] " + file.getAbsolutePath() + "\n");
			}
		}

		return output;
	}
	
	
	public String execute() throws Exception {		
		String output = "Changes: \n";
		
		for(File file : allFiles) {
			String content = readFile(file);
			
			int count = 0;
			while(content.contains(search)) {
				content = content.replace(search, replace);
				count ++;
			}
			if(count > 0) {
				writeFile(file, content);
				output += ("[" + count + "] " + file.getAbsolutePath() + "\n");
			}
		}
		return output;
	}
	
	
	private String readFile(File file) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(file.toURI()));
		// return new String(encoded, Charset.defaultCharset());	
		return new String(encoded, StandardCharsets.UTF_8);		
	}
	
	
	private void writeFile(File file, String content) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(content);
		bw.close();
	}
}

