/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package local.pack;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 *
 * @author vinicius
 */
public class CrudManager {
    private Path file = Paths.get("file.txt");
    
    //New records are always inserted at the end. Returns line number
    public int insertRecord(List<String> lines) throws IOException {
        Files.write(file, lines, Charset.defaultCharset().forName("UTF-8"), StandardOpenOption.APPEND);
        return 0;
    }
    
    public List<String> getRecord(int id) throws IOException{
        List<String> content = Files.readAllLines(file);
        System.out.println(content);
        return content;
    }
    
    public void updateRecord(String newValue, int id) {
        
    }
    
    public void deleteRecord(int id) {
    }
    
    
    
    public void connect(String fileName) {
    
    }
    
    public void disconnect() {
        
    }
    
    private void save() {
    }
    
    public String[] getAllRecords() {
        return new String[0];
    }
}
