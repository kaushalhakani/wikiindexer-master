
package src.edu.buffalo.cse.ir.wikiindexer.parsers;
import java.io.IOException;
import java.text.ParseException;

import org.xml.sax.Attributes;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import src.edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument;
import src.edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaParser;

import java.util.Collection;
import java.util.Date;
import java.util.Properties;

//import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocumentDummy;

/**
 * @author nikhillo
 *
 */
public class Parser1 extends DefaultHandler { 
	/* */
	//private final Properties props;
	private static int currentPageId;
	private static String currentPageAuthor;
	private static String currentPageTitle;
	private static String currentPageDate;
	private static String tmpValue = null;
	private static StringBuilder currentWikiText;
	static String sectionText = new String();
	private static String s;

	/**
	 * 
	 * @param idxConfig
	 * @param parser
	 */
	/*public Parser(Properties idxProps) {
		props = idxProps;
	}*/


	/**
	 * 
	 * @param filename
	 * @param docs
	 */
	public static void main(String args []){
		//public void parse(String filename, Collection<WikipediaDocument> docs){

		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			DefaultHandler handler = new DefaultHandler() {

				boolean isId = false;
				boolean isPublishDate = false;
				boolean isAuthor = false;
				boolean isTitle = false;
				boolean isRevision=false;
				boolean isText=false;
				int counter=0;
				//StringBuilder s=new StringBuilder();
				String s;

				WikipediaDocument wikiDoc = null;

				public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {


					if (qName.equalsIgnoreCase("id")) {
						isId = true;
					}
					if(qName.equalsIgnoreCase("publishDate")){
						isPublishDate = true;
					}
					if(qName.equalsIgnoreCase("Author")){
						isAuthor = true;
					}
					if(qName.equalsIgnoreCase("Title")){
						isTitle = true;
					}
					if(qName.equalsIgnoreCase("revision")){
						isRevision = true;
					}
					if(qName.equalsIgnoreCase("text")){
						isText = true;
						currentWikiText = new StringBuilder("");
					}
				}

				public void characters(char ch[], int start, int length) throws SAXException {

					tmpValue = new String(ch, start, length);


					if (!isRevision){
						if (isId) {
							//System.out.println("Id : " + tmpValue);
							isId = false;
						}
					}

					if(isPublishDate){
						//System.out.println("Publish Date :" + tmpValue);
						isPublishDate = false;
					}
					if(isAuthor){
						//System.out.println("Author :" + tmpValue);
						isAuthor = false;
					}
					if(isTitle){
						//System.out.println("Title :" + tmpValue);
						isTitle = false;
					}
					if(isText){
						currentWikiText = currentWikiText.append(ch, start, length);
						//s=s.append(currentWikiText);
					}
				}

				public void endElement(String uri, String localName,
						String qName) throws SAXException {

					//System.out.println("End Element :" + qName);

					if (!isRevision) {
						if (qName.equalsIgnoreCase("id")) {
							currentPageId = Integer.parseInt(tmpValue);			    
						}
					}
					if(qName.equalsIgnoreCase("publishDate")){
						currentPageDate= tmpValue;
					}

					if(qName.equalsIgnoreCase("revision")){
						isRevision = false;
					}

					if(qName.equalsIgnoreCase("author")){
						currentPageAuthor = tmpValue;
					}

					if(qName.equalsIgnoreCase("title")){
						currentPageTitle=tmpValue;
					}

					if(qName.equalsIgnoreCase("page"))	
					{   
						WikipediaDocument doc=new WikipediaDocument();
						try {
							doc=new WikipediaDocument(currentPageId,currentPageDate,currentPageAuthor,currentPageTitle);
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						try {
                            counter++;
							for (int i=0; i<currentWikiText.length(); i++){
								if ((currentWikiText.charAt(i)=='\n') && (currentWikiText.charAt(i+1)=='=') && (currentWikiText.charAt(i+2)=='=') && (i<currentWikiText.length())){
									int j=i+1;
									while ((j < currentWikiText.length()) && !((currentWikiText.charAt(j)=='\n') && (currentWikiText.charAt(j+1)=='=') && (currentWikiText.charAt(j+2)=='=')) )
									{
										j++;
										
									}
									//System.out.println("rvvy");
									s= currentWikiText.substring(i, j);
									//System.out.println(s);
									//s=s.append(sectionText);
									
									s=WikipediaParser.parseListItem(s);
									//System.out.println(s);
									s=WikipediaParser.parseTextFormatting(s);
									//System.out.println(s);
									s=WikipediaParser.parseTagFormatting(s);
									//System.out.println("vgrbny");
									//System.out.println(s);
									s=WikipediaParser.parseTemplates(s);
									String secTitle;
									secTitle= WikipediaParser.parseSectionTitle(s);
									String s[]=secTitle.split("\\@\\#\\$\\%\\^");

									
									doc.addSection(s[0], s[1]);

									
								}

								
							}				
							/* Now to extract section title and section text out of the extracted text */
							//WikipediaParser parser = new WikipediaParser()
							
							
							}
						
						catch (Exception e) {
							e.printStackTrace();
						}
					}			
				}
			};
			saxParser.parse( "C:\\Users\\Kaushal\\Documents\\Education\\SemesterI\\wikiindexer-master\\files\\five_entries.xml", handler);
//C:\\Users\\pranav\\Downloads\\information retrieval\\IR lectures\\0-project\\wikiindexer-master\\wikiindexer-master\\src\\five_entires.xml
			//C:\\Users\\pranav\\Downloads\\information retrieval\\IR lectures\\0-project\\wikiindexer-master\\wikiindexer-master\\WikiDump_1600.xml\\WikiDump_1600.xml
		}

		catch (Exception e) {
			e.printStackTrace();
		}


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

/*private synchronized void add(WikipediaDocument doc, Collection<WikipediaDocument> documents) {
	documents.add(doc);	
	}*/
