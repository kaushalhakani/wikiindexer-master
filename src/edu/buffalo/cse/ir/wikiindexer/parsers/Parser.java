/**
 * 
 */
package src.edu.buffalo.cse.ir.wikiindexer.parsers;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Properties;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import src.edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument;
import src.edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaParser;
//import edu.buffalo.cse.ir.wikiindexer.saxparser.SaxParser;

/**
 * @author nikhillo
 *
 */
public class Parser{
	
	/* */
	private final Properties props;
	
	/**
	 * 
	 * @param idxConfig
	 * @param parser
	 */
	public Parser(Properties idxProps) {
		props = idxProps;
	}
	
	/* TODO: Implement this method */
	/**
	 * 
	 * @param filename the file to be parsed
	 * @param docs to hold the individual wikipedia pages
	 */
	public void parse(String filename, Collection<WikipediaDocument> docs) 
	{	
		if (filename == null || filename.equals(""))
			return;	
		else 
		{
			File file = new File(filename);
			if(!(file.exists()))
				return;
		}
		XMLReader p;
			
		class SaxParser extends DefaultHandler
		{
			int idFromXml;
			String timestampFromXml;
			String authorFromXml;
			String ttl;
			String text;
			WikipediaDocument doc;
			Collection<WikipediaDocument> realDocs;
			StringBuffer buffer;
			boolean rightidflag;		
					
			public SaxParser (Collection<WikipediaDocument> realDocs)
			{
				this.realDocs = realDocs;
				buffer = new StringBuffer();
			}
						
			public void startDocument()
			{
				System.out.print("\nStarted Parsing XML....");
			}
					
			public void endDocument() 
			{
				System.out.print("\nFinished Parsing XML....");
			}
			
			public void startElement(String nameSpaceURI, String localName, String qName, Attributes atts) 
			{
				buffer.delete(0, buffer.length());
				if ( localName.equals("page") )
					rightidflag = true;
				if ( localName.equals("revision") )
					rightidflag = false;
			}
				
			public void endElement(String nameSpaceURI, String localName, String qName) 
			{		
				if (localName.equals("page"))
				{
					try 
					{
						doc = new WikipediaDocument(idFromXml, timestampFromXml, authorFromXml, ttl);
						text = WikipediaParser.parseTagFormatting(text);
						text = WikipediaParser.parseTemplates(text);
						text = WikipediaParser.parseTextFormatting(text);
						WikipediaParser.parseSection(doc, text);
						add(doc,realDocs);
					} 
					catch (ParseException e) 
					{
						e.printStackTrace();
					}
				}
				else if (localName.equals("text")) 	
					text = buffer.toString();
				else if (localName.equals("id") && rightidflag)
					idFromXml = Integer.valueOf(buffer.toString());
				else if (localName.equals("timestamp"))
					timestampFromXml = buffer.toString();
				else if (localName.equals("ip") || localName.equals("username"))
					authorFromXml = buffer.toString();
				else if (localName.equals("title"))
					ttl = buffer.toString();
				}
				
				public void characters(char[] ch, int start, int length) {
					for(int i = start; i<(start+length); i++){
						buffer.append(ch[i]);
					}
				}
			
		}
		
		
		try 
		{
			p = XMLReaderFactory.createXMLReader();
			p.setContentHandler(new SaxParser(docs));
			
			try 
			{
				p.parse(filename);
			} 
			catch (IOException e) 
			{
				System.err.println("invalid filename/ XML BOM error");
			}
		} catch (SAXException e) 
		{
			e.printStackTrace();
		}
	}
	/**
	 * Method to add the given document to the collection.
	 * PLEASE USE THIS METHOD TO POPULATE THE COLLECTION AS YOU PARSE DOCUMENTS
	 * For better performance, add the document to the collection only after
	 * you have completely populated it, i.e., parsing is complete for that document.
	 * @param doc: The WikipediaDocument to be added
	 * @param documents: The collection of WikipediaDocuments to be added to
	 */
	private synchronized void add(WikipediaDocument doc, Collection<WikipediaDocument> documents) {
		documents.add(doc);
	}
}
