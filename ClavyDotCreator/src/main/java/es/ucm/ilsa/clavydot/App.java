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
public class App 
{
    public static void main( String[] args )
    {
    	String message="Exception .clavy-> Params Null ";
		try {

			String fileName;
			if (args.length>0)
				fileName=args[0];
			else
				fileName = "test.clavy";
			 
			System.out.println(fileName);
			
			
			boolean onlyBrowsing=false;
			try {
				if (args.length>0)
					onlyBrowsing=Boolean.parseBoolean(args[1]);
			} catch (Exception e) {
				System.out.println("Parsing whit flag false by default");
			}


			 File file = new File(fileName);
			 FileInputStream fis = new FileInputStream(file);
			 ObjectInputStream ois = new ObjectInputStream(fis);
			 CompleteCollection object = (CompleteCollection) ois.readObject();
			 
			 
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
			 
			 String Folder = System.nanoTime()+"";
		
			 new File(Folder).mkdirs();
			 
			 System.out.println(new File(Folder).getAbsolutePath());
		 
		
			 
			DotSaveCollection SP=new DotSaveCollection();
			SP.processCollecccion(object,Folder,onlyBrowsing);
			 
	    }catch (Exception e) {
			e.printStackTrace();
			System.err.println(message);
			throw new RuntimeException(message);
		}
    }
}
