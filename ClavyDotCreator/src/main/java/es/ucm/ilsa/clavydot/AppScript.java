package es.ucm.ilsa.clavydot;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import fdi.ucm.server.modelComplete.collection.CompleteCollection;

/**
 * Hello world!
 *
 */
public class AppScript 
{
    public static void main( String[] args )
    {
    	String message="Exception .clavy-> Params Null ";
		try {

			String folderData;
			if (args.length>0)
				folderData=args[0];
			else
				folderData = "base";
			 
			System.out.println(folderData);
			
			 String Folder = System.nanoTime()+"";
				
			 new File(Folder).mkdirs();
			 
			 System.out.println(new File(Folder).getAbsolutePath());
			
			
			File directoryPath = new File(folderData);
			File filesList[] = directoryPath.listFiles();
			for(File file : filesList) {
				if (file.getName().endsWith(".clavy"))
				{
					System.out.println("Procesando...."+file.getName());
					 FileInputStream fis = new FileInputStream(file);
					 ObjectInputStream ois = new ObjectInputStream(fis);
					 CompleteCollection object = (CompleteCollection) ois.readObject();
					 
					 String FolderCol = Folder+File.separator+object.getName()+"_BOnly";
					 new File(FolderCol).mkdirs();
					 
					 
					 
					 DotSaveCollection SPFalse=new DotSaveCollection();
					 SPFalse.processCollecccion(object,FolderCol,false);
						
						
						String FolderCol2 = Folder+File.separator+object.getName()+"_ALL";
						 new File(FolderCol2).mkdirs();
						 
						DotSaveCollection SPTrue=new DotSaveCollection();
						SPTrue.processCollecccion(object,FolderCol2,true);
					 
					 try {
						 ois.close();
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					 try {
						 fis.close();
					} catch (Exception e) {
						// TODO: handle exception
					}
					 
					 
				}
			}
			
			
			

		 


			 
			
			 
	    }catch (Exception e) {
			e.printStackTrace();
			System.err.println(message);
			throw new RuntimeException(message);
		}
    }
}
