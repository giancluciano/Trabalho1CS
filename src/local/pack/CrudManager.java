/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package local.pack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    private ArrayList<Object> tabelaGenerica;
    Path path;
    int numberOfAttributes;
    String header;
    
    //New records are always inserted at the end. Returns line number as PK
    //Data needs to be added with exactly the number of attributes separated by ";"
    
    public int insertRecord(Object obj) throws NotConnectedException, NotEnoughAttributesException {
        if (tabelaGenerica == null)
            throw new NotConnectedException();
        
        tabelaGenerica.add(obj);
        this.save();
        return tabelaGenerica.size() - 1;
    }
    
    
    //Returns the record according to the index/PK supplied
    public Object getRecord(int id) throws NotConnectedException {
        if (tabelaGenerica == null)
            throw new NotConnectedException();
        
        if (0 <= id && id < tabelaGenerica.size())
            return tabelaGenerica.get(id);
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
    
    //Updates record according to index/PK supplied
    //Data needs to be added with exactly the number of attributes separated by ";"
    public void updateRecord(Object newObj, int id) throws NotConnectedException, NotEnoughAttributesException {
        if (tabelaGenerica == null)
            throw new NotConnectedException();
        
        if (0 <= id && id < tabelaGenerica.size()) {
            tabelaGenerica.set(id, newObj);
            this.save();
        }
    }
    
    //Set line to blank. Prevents other records from changing index/PK.
    public void deleteRecord(int id) throws NotConnectedException {
        if (tabelaGenerica == null)
            throw new NotConnectedException();
        
        if (0 <= id && id < tabelaGenerica.size()) {
            tabelaGenerica.set(id, null);
            this.save();
        }
    }
    //Checks if database exists, loads data into memory.
    //If it doesn't exists returns empty database to write data.
    public void connect(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        File arquivo = new File("object.ser");
        if(!arquivo.exists()) {
            arquivo.createNewFile();
            tabelaGenerica = new ArrayList<>();
        }else{
            FileInputStream fis=new FileInputStream(arquivo);
            ObjectInputStream ois=new ObjectInputStream(fis);
            tabelaGenerica = (ArrayList<Object>)ois.readObject();
            ois.close();
            fis.close();
        }
        
//        
//        File file = new File(fileName);
//        this.path = Paths.get(fileName);
//        if (file.exists()) {
//            try {
//                this.tabela = Files.readAllLines(this.path);
//                this.header = tabela.get(0);
//                tabela.remove(0);
//                
//                String[] attributes = this.header.split(";");
//                this.numberOfAttributes = attributes.length;
//            } catch (IOException ex) {
//                Logger.getLogger(CrudManager.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        else {
//            this.tabela = new ArrayList<String>();
//        }
    }
    
    //Prevents any new operations from being made
    public void disconnect()  {
        this.tabelaGenerica = null;
        
    }
 
    
    //Persist changes to disk
    private void save() {
        try {
            FileOutputStream fop=new FileOutputStream("object.ser");
            ObjectOutputStream oos=new ObjectOutputStream(fop);
            oos.writeObject(tabelaGenerica);
            oos.close();
            fop.close();
        } catch (IOException ex) {
            Logger.getLogger(CrudManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Return all data
    public List<Object> getAllRecords() throws NotConnectedException {
        if (tabelaGenerica == null)
            throw new NotConnectedException();
        
        return this.tabelaGenerica;
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
