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
    
    public class NotConnectedException extends Exception {
        public NotConnectedException() {
            super("Select a file before performing this operation");
        }
    }
    
    public class NotEnoughAttributesException extends Exception {
        public NotEnoughAttributesException() {
            super("Not all attributes were informed");
        }
    }
    
    private List<String> tabela;
    Path path;
    int numberOfAttributes;
    String header;
    
    //New records are always inserted at the end. Returns line number as PK
    //Data needs to be added with exactly the number of attributes separated by ";"
    public int insertRecord(String data) throws NotConnectedException, NotEnoughAttributesException {
        if (tabela == null)
            throw new NotConnectedException();
        
        String[] fields = data.split(";");
        if (this.numberOfAttributes == 0 || fields.length != this.numberOfAttributes)
            throw new NotEnoughAttributesException();
        
        this.tabela.add(data);
        this.save();
        return tabela.size() - 1;
    }
    
    //Returns the record according to the index/PK supplied
    public String getRecord(int id) throws NotConnectedException {
        if (tabela == null)
            throw new NotConnectedException();
        
        if (0 <= id && id < tabela.size())
            return tabela.get(id);
        else
            return null;
    }
    
    //Updates record according to index/PK supplied
    //Data needs to be added with exactly the number of attributes separated by ";"
    public void updateRecord(String newValue, int id) throws NotConnectedException, NotEnoughAttributesException {
        if (tabela == null)
            throw new NotConnectedException();
        
        String[] fields = newValue.split(";");
        if (this.numberOfAttributes == 0 || fields.length != this.numberOfAttributes)
            throw new NotEnoughAttributesException();
        
        if (0 <= id && id < tabela.size()) {
            tabela.set(id, newValue);
            this.save();
        }
    }
    
    //Set line to blank. Prevents other records from changing index/PK.
    public void deleteRecord(int id) throws NotConnectedException {
        if (tabela == null)
            throw new NotConnectedException();
        
        if (0 <= id && id < tabela.size()) {
            tabela.set(id, "");
            this.save();
        }
    }
    
    //Checks if database exists, loads data into memory.
    //If it doesn't exists returns empty database to write data.
    public void connect(String fileName) {
        File file = new File(fileName);
        this.path = Paths.get(fileName);
        if (file.exists()) {
            try {
                this.tabela = Files.readAllLines(this.path);
                this.header = tabela.get(0);
                tabela.remove(0);
                
                String[] attributes = this.header.split(";");
                this.numberOfAttributes = attributes.length;
            } catch (IOException ex) {
                Logger.getLogger(CrudManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            this.tabela = new ArrayList<String>();
        }
    }
    
    //Prevents any new operations from being made
    public void disconnect()  {
        this.tabela = null;
        this.path = null;
        this.numberOfAttributes = 0;
        this.header = null;
    }
    
    //Persist changes to disk
    private void save() {
        try {
            this.tabela.add(0, this.header);
            Files.write(this.path, this.tabela, Charset.defaultCharset().forName("UTF-8"));
            this.tabela.remove(0);
        } catch (IOException ex) {
            Logger.getLogger(CrudManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Return all data
    public List<String> getAllRecords() throws NotConnectedException {
        if (tabela == null)
            throw new NotConnectedException();
        
        return this.tabela;
    }
    
    //Create header to know how many attributes the entity stores
    public void createEntityHeader(int numberOfAttributes) {
        this.numberOfAttributes = numberOfAttributes;
        String headerLine = "";
        for(int i = 0; i < numberOfAttributes; i++)
            headerLine += "Atribute" + i + ";";
        headerLine = headerLine.substring(0, headerLine.length() - 1);
        this.header = headerLine;
    }
}
