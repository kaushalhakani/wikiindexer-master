/**
 * 
 */
package src.edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import src.edu.buffalo.cse.ir.wikiindexer.FileUtil;

/**
 * @author nikhillo
 * This class is used to introspect a given index
 * The expectation is the class should be able to read the index
 * and all associated dictionaries.
 */
public class IndexReader {
	private INDEXFIELD field;
	private Properties idxprops;
	private Map<Integer,String> docDic;
	private Map<String, List<Postings>> readIndexMap;
	private Map<Integer,List<Postings>> linkIndexmap;
	boolean flag=false;
	File file;
	/**
	 * Constructor to create an instance 
	 * @param props: The properties file
	 * @param field: The index field whose index is to be read
	 */
	public IndexReader(Properties props, INDEXFIELD field) 
	{
		this.idxprops=props;
		this.field=field;
		File docFile=new File(idxprops.getProperty("root.dir") + idxprops.getProperty("document_index"));
		if(field == INDEXFIELD.TERM)
			file = new File(idxprops.getProperty("root.dir") + idxprops.getProperty("term_index"));
		else if(field == INDEXFIELD.AUTHOR)
			file = new File(idxprops.getProperty("root.dir") + idxprops.getProperty("author_index"));
		else if(field == INDEXFIELD.LINK)
			file = new File(idxprops.getProperty("root.dir") + idxprops.getProperty("link_index"));
		else if(field == INDEXFIELD.CATEGORY)
			file = new File(idxprops.getProperty("root.dir") + idxprops.getProperty("category_index"));
			
		try
		{
			if(field==INDEXFIELD.LINK)
			{
				FileInputStream fin=new FileInputStream(file);
				ObjectInputStream oin=new ObjectInputStream(fin);
				linkIndexmap=(Map<Integer,List<Postings>>)oin.readObject();
				FileInputStream finDoc=new FileInputStream(docFile);
				ObjectInputStream oinDict=new ObjectInputStream(finDoc);
				docDic=(Map<Integer,String>)oinDict.readObject();	
			}
			
			else
			{
				FileInputStream fin=new FileInputStream(file);
				ObjectInputStream oin=new ObjectInputStream(fin);
				readIndexMap=(Map<String,List<Postings>>)oin.readObject();
				FileInputStream finDoc=new FileInputStream(docFile);
				ObjectInputStream oinDict=new ObjectInputStream(finDoc);
				docDic=(Map<Integer,String>)oinDict.readObject();
			}
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		flag=true;
			
	}
	
	/**
	 * Method to get the total number of terms in the key dictionary
	 * @return The total number of terms as above
	 */
	public int getTotalKeyTerms() {
		//TODO: Implement this method
		if(flag){
			if(field==INDEXFIELD.LINK)
				return linkIndexmap.size();
			else
				return readIndexMap.size();
		}
		else 
			return 0;
		
	}
	
	/**
	 * Method to get the total number of terms in the value dictionary
	 * @return The total number of terms as above
	 */
	public int getTotalValueTerms() {
		if(flag)
			return docDic.size();
		else
			return 0;
	}
	
	/**
	 * Method to retrieve the postings list for a given dictionary term
	 * @param key: The dictionary term to be queried
	 * @return The postings list with the value term as the key and the
	 * number of occurrences as value. An ordering is not expected on the map
	 */
	public Map<String, Integer> getPostings(String key) {
		
		try{
		if(flag)
		{
			Map<String,Integer> posting=new HashMap<String, Integer>();
			List<Postings> tempList=new ArrayList<Postings>();
			int numOfOccur;
			String str;
			Postings temp;
			for(Entry<String,List<Postings>> etr: this.readIndexMap.entrySet())
				{
					if(etr.getKey().equals(key))
					{
						tempList=etr.getValue();
						for(int x=0;x<tempList.size();x++)
						{
							temp=tempList.get(x);
							str= docDic.get(temp.docID);
							numOfOccur=temp.noc;
							posting.put(str, numOfOccur);
						}
					
					}
					break;
				}
			return posting;
		
		}
		}
		catch(Exception e){
			System.out.println("Error in getpostings");
		}
			return null;
	}
		
	
	/**
	 * Method to get the top k key terms from the given index
	 * The top here refers to the largest size of postings.
	 * @param k: The number of postings list requested
	 * @return An ordered collection of dictionary terms that satisfy the requirement
	 * If k is more than the total size of the index, return the full index and don't 
	 * pad the collection. Return null in case of an error or invalid inputs
	 */
	public Collection<String> getTopK(int k) {
		try{
		if(flag)
		{	
			Map<String, List<Postings>> tempMap=this.readIndexMap;
			ArrayList<String> topK=new ArrayList<String>();
			int max=0;
			String keyterm=new String();
			if(k>=tempMap.size())
				k=tempMap.size();
			for(int i=0;i<k;i++){
				for(Entry<String,List<Postings>> etr: tempMap.entrySet()){
					if(etr.getValue().size()>max)
						{
							max=etr.getValue().size();
							keyterm=etr.getKey();
						}
					topK.add(keyterm);
					tempMap.remove(keyterm);
				}
			}	
			return topK;	
		}
		}
		catch(Exception e)
		{
			System.out.println("Error in topK");
		}
		return null;
		
		
		
	}
	
	/**
	 * Method to execute a boolean AND query on the index
	 * @param terms The terms to be queried on
	 * @return An ordered map containing the results of the query
	 * The key is the value field of the dictionary and the value
	 * is the sum of occurrences across the different postings.
	 * The value with the highest cumulative count should be the
	 * first entry in the map.
	 */
	public Map<String, Integer> query(String... terms) {
		//TODO: Implement this method (FOR A BONUS)
		return null;
	}
}
