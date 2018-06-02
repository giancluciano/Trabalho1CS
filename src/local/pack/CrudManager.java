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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vinicius
 */
public class CrudManager {
    private List<String> tabela;
    Path path;
    
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
    
    
    
    
    //Checks if database exists, loads data into memory.
    //If it doesn't exists returns empty database to write data.
    public void connect(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            this.path = Paths.get(fileName);
            try {
                this.tabela = Files.readAllLines(this.path);
            } catch (IOException ex) {
                Logger.getLogger(CrudManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            this.tabela = new ArrayList<String>();
        }
    }
    
    public void disconnect() {
        
    }
    
    public void save() throws IOException {
        Files.write(path, tabela, Charset.defaultCharset().forName("UTF-8"), StandardOpenOption.WRITE);
    }
    
    public List<String> getAllRecords() {
        return this.tabela;
    }
}
