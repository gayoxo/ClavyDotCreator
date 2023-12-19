package es.ucm.ilsa.clavydot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.*;

public class DotSaveCollection {
	
	
	 class At_Val implements Comparable<At_Val>{
		
		private CompleteElementType cet;
		
		private String Value;


		public At_Val(CompleteElementType cet, String value) {
			super();
			this.cet = cet;
			Value = value;
		}

		@Override
		public int compareTo(At_Val o) {
			if (cet.getClavilenoid().equals(o.cet.getClavilenoid())&&Value.equals(o.Value)) 
				return 0;
			if (cet==o.cet)
				if (Value.compareToIgnoreCase(o.Value)<=0)
					return 1;
				else 
					return -1;
			else
				if (cet.getClavilenoid()<o.cet.getClavilenoid())
					return 1;
				else
					return -1;
					
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof At_Val)
				if (cet.getClavilenoid().equals(((At_Val)o).cet.getClavilenoid())
						&&Value.equals(((At_Val)o).Value)) 
					return true;
			return super.equals(o);
		}
		
		public CompleteElementType getCet() {
			return cet;
		}
		
		public String getValue() {
			return Value;
		}
		
		@Override
		public String toString() {
			return "(Class:"+cet.getClavilenoid()+" Value:"+Value+" )";
		}

		
		
	}

	private CompleteCollection Collections;
	private String FolderFinal;
	private boolean onlyBrowsing;


	public void processCollecccion(CompleteCollection object, String folder,boolean onlyBrowsingin) throws IOException {
		
		Collections=object;
		FolderFinal=folder;
		onlyBrowsing=onlyBrowsingin;
		
		List<CompleteGrammar> GrammarValidas=new LinkedList<CompleteGrammar>();
		

		
		for (CompleteGrammar GG : object.getMetamodelGrammar()) {
			if (!ignore(GG.getViews()))
				GrammarValidas.add(GG);
			else
				System.out.println("ignorada Gramatica.>"+GG.getNombre());
		}
		
		HashMap<CompleteElementType, Long> CE2IdCOI=new HashMap<CompleteElementType, Long>();
		HashMap<Long, CompleteElementType> ID2CE=new HashMap<Long, CompleteElementType>();
		
		for (CompleteGrammar completeGrammar : GrammarValidas) 
			fillHashes(completeGrammar.getSons(),CE2IdCOI,ID2CE);
		
		
		String ruta = FolderFinal+File.separator+Collections.getName()+".dat";
        File archivo = new File(ruta);
        BufferedWriter dotLongFile;
        
        if(archivo.exists())
        	archivo.delete();
        
            dotLongFile = new BufferedWriter(new FileWriter(archivo));
            System.out.println("fichero dot creado");
            
            
            String ruta2 = FolderFinal+File.separator+Collections.getName()+".dot_table.csv";
	        File archivo2 = new File(ruta2);
	        BufferedWriter dotLeyendFile;
	        
	        if(archivo2.exists())
	        	archivo2.delete();
	        
	            dotLeyendFile = new BufferedWriter(new FileWriter(archivo2));
	            System.out.println("fichero dot table creado");
	            
	            
	            
	            String ruta3 = FolderFinal+File.separator+Collections.getName()+".dot_text.dat";
		        File archivo3 = new File(ruta3);
		        BufferedWriter dotValueFile;
		        
		        if(archivo3.exists())
		        	archivo3.delete();
		        
		            dotValueFile = new BufferedWriter(new FileWriter(archivo3));
		            System.out.println("fichero dot creado");
	            
	            String ruta4 = FolderFinal+File.separator+Collections.getName()+".dot_table_stru.csv";
		        File archivo4 = new File(ruta4);
		        BufferedWriter dotLeyendFileExtend;
		        
		        if(archivo4.exists())
		        	archivo4.delete();
		        
		        dotLeyendFileExtend = new BufferedWriter(new FileWriter(archivo4));
		            System.out.println("fichero dot strucreado");
	            
	            
		            for (CompleteElementType Stru_nomber : CE2IdCOI.keySet()) {
		            	if (Stru_nomber.getClassOfIterator()==null||Stru_nomber.getClassOfIterator()==Stru_nomber)
		            		dotLeyendFileExtend.write(Stru_nomber.getClavilenoid()+";\""+pathElem(Stru_nomber)+"\"\n");
					}
		            
		            dotLeyendFileExtend.close();
		
		            
		Long IndiceClave=1l;
		HashMap<At_Val, Long> listaT = new HashMap<DotSaveCollection.At_Val, Long>();            
		            
		for (CompleteDocuments cc : object.getEstructuras()) {
			for (CompleteElement ce : cc.getDescription()) {
				
				if (ce instanceof CompleteTextElement)
				{
					String Value = ((CompleteTextElement) ce).getValue().trim().toLowerCase();
					if (Value.length() > 10)
						Value = Value.substring(0, 10).toLowerCase();
					

					
					Value = Value.replaceAll(";", "");
					
					At_Val at_val = new At_Val(ce.getHastype(), Value);
					
					List<At_Val> ListaTempo=new LinkedList<DotSaveCollection.At_Val>(listaT.keySet());
					
					if (ListaTempo.contains(at_val))
						at_val=ListaTempo.get(ListaTempo.indexOf(at_val));
	        		else
	        			{
	        			listaT.put(at_val,IndiceClave);
	        			IndiceClave++;
	        			}
					
					
					dotLongFile.write(listaT.get(at_val)+" ");
					dotValueFile.write(at_val+" ");
					
				}
		

				

				
			}
			
			dotLongFile.write("\n");
			dotValueFile.write("\n");
		}
		
		
		List<At_Val> ListaTempo=new LinkedList<DotSaveCollection.At_Val>(listaT.keySet());
		java.util.Collections.sort(ListaTempo);
		
		for (At_Val at_Val : ListaTempo) {
			
			dotLeyendFile.write(listaT.get(at_Val)+";"+at_Val);
			dotLeyendFile.write("\n");
		}

		
		
		dotValueFile.close();
		 dotLongFile.close();
	     dotLeyendFile.close();
		
	}
	
	
	private String pathElem(CompleteElementType ss) {
		if (ss.getFather()==null)
			return ss.getCollectionFather().getNombre()+"/"+ss.getName();
		else
			return pathElem(ss.getFather())+"/"+ss.getName();
	}
	
	private void fillHashes(List<CompleteElementType> sons,
			HashMap<CompleteElementType, Long> cE2IdCOI,
			HashMap<Long, CompleteElementType> iD2CE) {
		for (CompleteElementType ss : sons) {
			
				if (ss instanceof CompleteTextElementType)
					if (!ignoreS(ss.getShows()))
					{
						
						if (ss.getClassOfIterator()==null||ss.getClassOfIterator()==ss) {
						
							if (onlyBrowsing&&ss.isBrowseable())
								{
								cE2IdCOI.put(ss, ss.getClavilenoid());
								iD2CE.put(ss.getClavilenoid(), ss);
								}
							else if (!onlyBrowsing)
								{
								cE2IdCOI.put(ss, ss.getClavilenoid());
								iD2CE.put(ss.getClavilenoid(), ss);
								}
							else
								System.out.println("ignorada structure.>"+ss.getName());
						
						}else
						{
							cE2IdCOI.put(ss, ss.getClassOfIterator().getClavilenoid());
							iD2CE.put(ss.getClavilenoid(), ss);
						}
						
					}
					else
					System.out.println("ignorada structure.>"+ss.getName());
			
			
			fillHashes(ss.getSons(), cE2IdCOI, iD2CE);
			
		}
		
	}


	private boolean ignoreS(ArrayList<CompleteOperationalValueType> operational) {
		for (CompleteOperationalValueType operationalValueTypeGS : operational) {
			if (operationalValueTypeGS.getView().toLowerCase().equals("fcalgs")
					&&
					operationalValueTypeGS.getName().toLowerCase().equals("ignore"))
						try {
							return Boolean.parseBoolean(operationalValueTypeGS.getDefault().toLowerCase());
						} catch (Exception e) {
							return false;
						}
		}
		return false;
	}


	private boolean ignore(List<CompleteOperationalValueType> operational) {
		for (CompleteOperationalValueType operationalValueTypeGS : operational) {
			if (operationalValueTypeGS.getView().toLowerCase().equals("fcalgs")
					&&
					operationalValueTypeGS.getName().toLowerCase().equals("ignore"))
						try {
							return Boolean.parseBoolean(operationalValueTypeGS.getDefault().toLowerCase());
						} catch (Exception e) {
							return false;
						}
		}
		return false;
	}

}
