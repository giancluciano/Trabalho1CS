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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vinicius
 */
public class CrudManager {
    
    String databaseFile;
    
    public class NotConnectedException extends Exception {
        public NotConnectedException() {
            super("Select a file before performing this operation");
        }
    }
    
    private ArrayList<Object> tabelaGenerica;
    
    //New records are always inserted at the end. Returns line number as PK
    //Data needs to be added with exactly the number of attributes separated by ";"
    public int insertRecord(Object obj) throws NotConnectedException {
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
    public void updateRecord(Object newObj, int id) throws NotConnectedException {
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
        this.databaseFile = fileName;
        File arquivo = new File(this.databaseFile);
        if(!arquivo.exists()) {
            arquivo.createNewFile();
            tabelaGenerica = new ArrayList<>();
        }
        else{
            FileInputStream fis=new FileInputStream(arquivo);
            ObjectInputStream ois=new ObjectInputStream(fis);
            tabelaGenerica = (ArrayList<Object>)ois.readObject();
            ois.close();
            fis.close();
        }
    }
    
    //Prevents any new operations from being made
    public void disconnect()  {
        this.tabelaGenerica = null;
    }
    
    //Persist changes to disk
    private void save() {
        try {
            FileOutputStream fop=new FileOutputStream(this.databaseFile);
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
}
