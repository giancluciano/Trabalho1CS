/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package local.pack;

import java.io.File;
import java.util.List;
import local.pack.CrudManager.NotConnectedException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vinicius
 */
public class CrudManagerTest {
    
    @Before
    public void setUp() {
        File arquivo = new File("testDatabase.ser");
        if(arquivo.exists())
            arquivo.delete();
    }

    CrudManager crudManager = new CrudManager();
    Pessoa pessoa = new Pessoa("11122233344", "José", 32);

    @Test(expected = NotConnectedException.class)
    public void testInsertRecordWhenNotConnectedRaiseException() throws NotConnectedException {
        crudManager.insertRecord(pessoa);
    }
    
    @Test(expected = NotConnectedException.class)
    public void testGetRecordWhenNotConnectedRaiseException() throws NotConnectedException {
        crudManager.getRecord(10);
    }
    
    @Test(expected = NotConnectedException.class)
    public void testUpdateRecordWhenNotConnectedRaiseException() throws NotConnectedException {
        crudManager.updateRecord(pessoa, 10);
    }
    
    @Test(expected = NotConnectedException.class)
    public void testDeleteRecordWhenNotConnectedRaiseException() throws NotConnectedException {
        crudManager.deleteRecord(10);
    }
    
    @Test(expected = NotConnectedException.class)
    public void testGetAllRecordsWhenNotConnectedRaiseException() throws NotConnectedException {
        crudManager.getAllRecords();
    }
    
    @Test
    public void testInsertRecordReturnsId() throws NotConnectedException {
        crudManager.connect("testDatabase.ser");
        int pk = crudManager.insertRecord(pessoa);
        assertEquals(0, pk);
    }
    
    @Test
    public void testGetRecordReturnsObject() throws NotConnectedException {
        //Need to insert data to test getRecord
        crudManager.connect("testDatabase.ser");
        int pk = crudManager.insertRecord(pessoa);
        
        Pessoa alguem = (Pessoa)crudManager.getRecord(0);
        assertEquals(alguem.getNome(), "José");
    }
    
    @Test
    public void testGetRecordReturnsNull() throws NotConnectedException {
        crudManager.connect("testDatabase.ser");
        
        Object alguem = crudManager.getRecord(0);
        assertEquals(alguem, null);
    }
    
    @Test
    public void testUpdateRecordUpdatesData() throws NotConnectedException {
        //Need to insert data to check if update works
        crudManager.connect("testDatabase.ser");
        int pk = crudManager.insertRecord(pessoa);
        
        //Change data on the database
        pessoa.setIdade(69);
        crudManager.updateRecord(pessoa, pk);
        
        Pessoa alguem = (Pessoa)crudManager.getRecord(0);
        assertEquals(alguem.getIdade(), 69);
    }
    
    @Test
    public void testDeleteRecordReturnsNull() throws NotConnectedException {
        //Need to insert data to check if update works
        crudManager.connect("testDatabase.ser");
        int pk = crudManager.insertRecord(pessoa);
        
        //Check if data was successfully recorded
        Object alguem = crudManager.getRecord(0);
        assertNotEquals(alguem, null);
        
        //Check if data was really deleted
        crudManager.deleteRecord(pk);
        alguem = crudManager.getRecord(0);
        assertEquals(alguem, null);
    }
    
    @Test
    public void testGetAllRecordsReturnsOneObject() throws NotConnectedException {
        //Need to insert data to check if update works
        crudManager.connect("testDatabase.ser");
        int pk = crudManager.insertRecord(pessoa);
        
        List<Object> pessoas = crudManager.getAllRecords();
        assertEquals(pessoas.size(), 1);
    }
}
