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
    private List<String> tabela;
    
    //New records are always inserted at the end. Returns line number
    public int insertRecord(String line) throws IOException {
        this.tabela.add(line);
        return tabela.size() - 1;
    }
    
    public String getRecord(int id) throws IOException{
        return tabela.get(id);
    }
    
    public void updateRecord(String newValue, int id) {
        tabela.add(id, newValue);
        
    }
    
    public void deleteRecord(int id) {
        tabela.remove(id);
    }
    
    
    
    public void connect(String fileName) throws IOException {
        List<String> content = Files.readAllLines(file);
        this.tabela = content;
        System.out.println(content);
    }
    
    public void disconnect() {
        
    }
    
    public void save() throws IOException {
        Files.write(file, tabela, Charset.defaultCharset().forName("UTF-8"), StandardOpenOption.WRITE);
    }
    
    public List<String> getAllRecords() {
        return tabela;
    }
}
