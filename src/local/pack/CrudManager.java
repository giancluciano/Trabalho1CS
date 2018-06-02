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
    public int insertRecord(String data) {
        this.tabela.add(data);
        this.save();
        return tabela.size() - 1;
    }
    
    //Record the record according to the index/PK supplied
    public String getRecord(int id) {
        if (0 <= id && id < tabela.size())
            return tabela.get(id);
        else
            return null;
    }
    
    //Updates record according to index/PK supplied
    public void updateRecord(String newValue, int id) {
        if (0 <= id && id < tabela.size()) {
            tabela.set(id, newValue);
            this.save();
        }
    }
    
    //Set line to blank. Prevents other records from changing index/PK.
    public void deleteRecord(int id) {
        if (0 <= id && id < tabela.size()) {
            tabela.set(id, "");
            this.save();
        }
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
    
    //Persist changes to disk
    public void save() {
        try {
            Files.write(this.path, this.tabela, Charset.defaultCharset().forName("UTF-8"));
        } catch (IOException ex) {
            Logger.getLogger(CrudManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Return all data
    public List<String> getAllRecords() {
        return this.tabela;
    }
}
