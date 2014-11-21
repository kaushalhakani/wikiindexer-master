/**
 * 
 */
package src.edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;


/**
 * @author nikhillo
 * This class is used to write an index to the disk
 * 
 */
class Postings implements Serializable,Comparable<Postings>
{
	int noc;
	int docID;
	
	public Postings(int numOccurances,int docID){
		this.noc=numOccurances;
		this.docID=docID;
	}
	public int compareTo(Postings temp){
		return (noc<temp.noc ? -1 : (noc==temp.noc ? 0 : 1));
	}
}

public class IndexWriter implements Writeable 
{
	static int author_ctr = 0;
	static int link_ctr = 0;
	static int term_ctr = 0;
	static int category_ctr = 0;
	HashMap<String,List<Postings>> invertedIndex;
	INDEXFIELD keyfield,valueField;
	boolean isForward;
	HashMap<Integer, List<Postings>> forwardIndex;
	Properties props;
	LocalDictionary local_dict;
	
	File file;
	FileWriter fw = null;
	BufferedWriter bw = null;
	FileReader fr = null;
	BufferedReader br = null;
	
	/**
	 * Constructor that assumes the underlying index is inverted
	 * Every index (inverted or forward), has a key field and the value field
	 * The key field is the field on which the postings are aggregated
	 * The value field is the field whose postings we are accumulating
	 * For term index for example:
	 * 	Key: Term (or term id) - referenced by TERM INDEXFIELD
	 * 	Value: Document (or document id) - referenced by LINK INDEXFIELD
	 * @param props: The Properties file
	 * @param keyField: The index field that is the key for this index
	 * @param valueField: The index field that is the value for this index
	 */
	public IndexWriter(Properties props, INDEXFIELD keyField, INDEXFIELD valueField) {
		this(props, keyField, valueField, false);
	}
	
	/**
	 * Overloaded constructor that allows specifying the index type as
	 * inverted or forward
	 * Every index (inverted or forward), has a key field and the value field
	 * The key field is the field on which the postings are aggregated
	 * The value field is the field whose postings we are accumulating
	 * For term index for example:
	 * 	Key: Term (or term id) - referenced by TERM INDEXFIELD
	 * 	Value: Document (or document id) - referenced by LINK INDEXFIELD
	 * @param props: The Properties file
	 * @param keyField: The index field that is the key for this index
	 * @param valueField: The index field that is the value for this index
	 * @param isForward: true if the index is a forward index, false if inverted
	 */
	public IndexWriter(Properties props, INDEXFIELD keyField, INDEXFIELD valueField, boolean isForward) 
	{
		this.props=props;
		this.keyfield=keyField;
		this.valueField=valueField;
		this.isForward=isForward;
		if(isForward)
			forwardIndex=new HashMap<Integer, List<Postings>>();
		else
		{
			invertedIndex=new HashMap<String,List<Postings>>();
			local_dict=new LocalDictionary(this.props, keyField);
		}
		
		if(keyfield == INDEXFIELD.TERM)
			file = new File(props.getProperty("root.dir") + props.getProperty("term_index"));
		else if(keyfield == INDEXFIELD.AUTHOR)
			file = new File(props.getProperty("root.dir") + props.getProperty("author_index"));
		else if(keyfield == INDEXFIELD.LINK)
			file = new File(props.getProperty("root.dir") + props.getProperty("link_index"));
		else if(keyfield == INDEXFIELD.CATEGORY)
			file = new File(props.getProperty("root.dir") + props.getProperty("category_index"));
			
		try 
		{
			file.createNewFile();
		}
		catch (IOException e) 
		{
			System.out.println(keyfield + " Index file not created!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to add a given key - value mapping to the index
	 * @param keyId: The id for the key field, pre-converted
	 * @param valueId: The id for the value field, pre-converted
	 * @param numOccurances: Number of times the value field is referenced
	 *  by the key field. Ignore if a forward index
	 * @throws IndexerException: If any exception occurs while indexing
	 */
	public void addToIndex(int keyId, int valueId, int numOccurances) throws IndexerException 
	{
		System.out.println("Enters III");
		Postings temp=new Postings(numOccurances, valueId);
		if(isForward)
		{
			if(forwardIndex.containsKey(keyId))
			{
				if(forwardIndex.get(keyId).contains(valueId))
				{
					forwardIndex.get(keyId).remove(valueId);
					forwardIndex.get(keyId).add(temp);
				}
				else
					forwardIndex.get(keyId).add(temp);
			}
			else
			{
				List<Postings> list = new ArrayList<Postings>();
				list.add(temp);
				forwardIndex.put(keyId, list);
			}
			if(forwardIndex.size() > 2)
			writeToDisk();
		}
	}
			
	/**
	 * Method to add a given key - value mapping to the index
	 * @param keyId: The id for the key field, pre-converted
	 * @param value: The value for the value field
	 * @param numOccurances: Number of times the value field is referenced
	 *  by the key field. Ignore if a forward index
	 * @throws IndexerException: If any exception occurs while indexing
	 */
	public void addToIndex(int keyId, String value, int numOccurances) throws IndexerException {
		//TODO: Implement this method
		System.out.println("Not Applicable");
	}
	
	/**
	 * Method to add a given key - value mapping to the index
	 * @param key: The key for the key field
	 * @param valueId: The id for the value field, pre-converted
	 * @param numOccurances: Number of times the value field is referenced
	 *  by the key field. Ignore if a forward index
	 * @throws IndexerException: If any exception occurs while indexing
	 */
	public void addToIndex(String key, int valueId, int numOccurances) throws IndexerException 
	{
		//System.out.println("Enters SII");
		Postings temp=new Postings(numOccurances, valueId);
		if(!isForward)
		{
			if(invertedIndex.containsKey(key))
			{
				if(invertedIndex.get(key).contains(valueId))
				{
					invertedIndex.get(key).remove(valueId);
					invertedIndex.get(key).add(temp);
				}
				else
					invertedIndex.get(key).add(temp);
			}
			else
			{
				List<Postings> list = new ArrayList<Postings>();
				list.add(temp);
				invertedIndex.put(key, list);
			}
			if(invertedIndex.size() > 2)
			{
				writeToDisk();
			}
		}
	}
	
	/**
	 * Method to add a given key - value mapping to the index
	 * @param key: The key for the key field
	 * @param value: The value for the value field
	 * @param numOccurances: Number of times the value field is referenced
	 *  by the key field. Ignore if a forward index
	 * @throws IndexerException: If any exception occurs while indexing
	 */
	public void addToIndex(String key, String value, int numOccurances) throws IndexerException {
		System.out.println("Not Applicable");
	}

	

	/**
	 * Method to make the writer self aware of the current partition it is handling
	 * Applicable only for distributed indexes.
	 * @param pnum: The partition number
	 */
	public void setPartitionNumber(int pnum) {
		//TODO: Optionally implement this method
	}
	
	private Map<String, List<Postings>> getSortedMap(Map<String, List<Postings>> inputMap) {
		return new TreeMap<String, List<Postings>>(inputMap);
		
	}
	
	private Map<Integer, List<Postings>> getforwardSortedMap(Map<Integer, List<Postings>> inputMap) {
		return new TreeMap<Integer, List<Postings>>(inputMap);
		
	}
	/* (non-Javadoc)
	 * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#writeToDisk()
	 */
	public void writeToDisk() throws IndexerException
	{	
		//System.out.println("Enters Index Writer!");
		open_writer();
		String towrite = "";
		try
		{
			if(isForward)
			{
				Map<Integer,List<Postings>> sortedForwardMap=getforwardSortedMap(this.forwardIndex); 
				Iterator<Entry<Integer, List<Postings>>> itr = sortedForwardMap.entrySet().iterator();
				while(itr.hasNext())
				{
					Entry<Integer, List<Postings>> temp = itr.next();
					towrite = "(" + temp.getKey() + ")";
					List<Postings> postings = temp.getValue();
					Iterator<Postings> post_itr = postings.iterator();
					while(post_itr.hasNext())
					{
						Postings post_tmp = post_itr.next();
						towrite = towrite + "," + post_tmp.docID + "-" + post_tmp.noc;
					}
					towrite = towrite + "\n";
 				}
				bw.write(towrite);
				bw.flush();
				close_writer();
			}
			else
			{
				Map<String,List<Postings>> sortedInvertedMap=getSortedMap(this.invertedIndex); 
				Iterator<Entry<String, List<Postings>>> itr = sortedInvertedMap.entrySet().iterator();
				while(itr.hasNext())
				{
					Entry<String, List<Postings>> temp = itr.next();
					towrite = "(" + temp.getKey() + ")";
					List<Postings> postings = temp.getValue();
					Iterator<Postings> post_itr = postings.iterator();
					while(post_itr.hasNext())
					{
						Postings post_tmp = post_itr.next();
						towrite = towrite + "," + post_tmp.docID + "-" + post_tmp.noc;
					}
					towrite = towrite + "\n";
 				}
				bw.write(towrite);
				bw.flush();
				close_writer();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		close_writer();
	}

	/* (non-Javadoc)
	 * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#cleanUp()
	 */
	public void cleanUp() 
	{
		try 
		{
			writeToDisk();
			close_writer();
		} catch (IndexerException e) 
		{
			e.printStackTrace();
		}
		close_writer();
		
		if(isForward)
			forwardIndex.clear();
		else
			invertedIndex.clear();
	}
	
	public void open_reader()
	{
		try 
		{
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			br.mark(0);
		} catch (IOException e) {
			System.out.println("File Writer not Working!");
			e.printStackTrace();
		}
	}
	
	public void close_reader()
	{
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void open_writer()
	{
		try 
		{
			fw = new FileWriter(file,true);
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			System.out.println("File Writer not Working!");
			e.printStackTrace();
		}
	}
	
	public void close_writer()
	{
		try 
		{
			bw.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
