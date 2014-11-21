/**
 * 
 */
package src.edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nikhillo
 * This class implements Wikipedia markup processing.
 * Wikipedia markup details are presented here: http://en.wikipedia.org/wiki/Help:Wiki_markup
 * It is expected that all methods marked "todo" will be implemented by students.
 * All methods are static as the class is not expected to maintain any state.
 */
public class WikipediaParser {
	/* TODO */
	/**
	 * Method to parse section titles or headings.
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Sections
	 * @param titleStr: The string to be parsed
	 * @return The parsed string with the markup removed
	 */
	
	public static String parseSectionTitle(String titleStr) 
	{
		if (titleStr!=null) 
		{
			String title_pattern = "^(={1,}\\s{0,})|(\\s{0,}={1,})$";
			Pattern r = Pattern.compile(title_pattern);
			Matcher m = r.matcher(titleStr);
			if (m.find()) 
				titleStr = m.replaceAll("");
			
			return titleStr;
		}
		else 
			return null;
	}
	
	/* TODO */
	/**
	 * Method to parse list items (ordered, unordered and definition lists).
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Lists
	 * @param itemText: The string to be parsed
	 * @return The parsed string with markup removed
	 */
	public static String parseListItem(String itemText) {
		if (itemText!=null)
		{	
			String olist_pattern = "\\*{1,}[\\s]{0,}";
			Pattern r = Pattern.compile(olist_pattern);
			Matcher m = r.matcher(itemText);
			if (m.find()) 
			{
				itemText = m.replaceAll("");		
				return itemText;
			}
		
			String ulist_pattern = "\\#{1,}[\\s]{0,}";
			r = Pattern.compile(ulist_pattern);
			m = r.matcher(itemText);
			if (m.find()) 
			{
				itemText = m.replaceAll("");
				return itemText;
			}
		
			String dlist_pattern = "\\;{1,}[\\s]{0,}";
			r = Pattern.compile(dlist_pattern);
			m = r.matcher(itemText);
			if (m.find()) 
			{
				itemText = m.replaceAll("");
				return itemText;
			}
		
			String tlist_pattern = "\\:{1,}[\\s]{0,}";
			r = Pattern.compile(tlist_pattern);
			m = r.matcher(itemText);
			if (m.find())
			{	
				itemText = m.replaceAll("");			
				return itemText;
			}
		}
		return itemText;
	}
	
	/* TODO */
	/**
	 * Method to parse text formatting: bold and italics.
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Text_formatting first point
	 * @param text: The text to be parsed
	 * @return The parsed text with the markup removed
	 */
	public static String parseTextFormatting(String text) {
		if (text!=null) {
		String formatting_pattern = "'{2,}";
		Pattern r = Pattern.compile(formatting_pattern);
		Matcher m = r.matcher(text);
		if (m.find()) {
				text = m.replaceAll("");
			}
		}
		//System.out.println(text);
		return text;
	}
	
	/* TODO */
	/**
	 * Method to parse *any* HTML style tags like: <xyz ...> </xyz>
	 * For most cases, simply removing the tags should work.
	 * @param text: The text to be parsed
	 * @return The parsed text with the markup removed.
	 */
	public static String parseTagFormatting(String text) 
	{
		//System.out.println("Text : " + text);
		if (text!=null)
		{
			String start_tag_pattern1 = "<[^>]{1,}>\\s{0,}|\\s<[^>]{1,}/>|\\s{0,}</[^>]{1,}>";
			Pattern r = Pattern.compile(start_tag_pattern1);
			Matcher m = r.matcher(text);
			if (m.find())			
			    text = m.replaceAll("");
			    
			String start_tag_pattern2 = "(.*?)&lt;/&gt;|&lt;(.*?)&gt;";
			r = Pattern.compile(start_tag_pattern2);
			m = r.matcher(text);
			if (m.find())
			    text = m.replaceAll(" ");		
		}
		//System.out.println("Tag Txt : " + text);
		return text;
	}
	
	/* TODO */
	/**
	 * Method to parse wikipedia templates. These are *any* {{xyz}} tags
	 * For most cases, simply removing the tags should work.
	 * @param text: The text to be parsed
	 * @return The parsed text with the markup removed
	 */
	public static String parseTemplates(String text) 
	{	
		text = text.replaceAll("\\{\\{.*\\}\\}", "");
		while(text.contains("{{"))
		{
			String[] temp1 = text.split("\\{\\{",2);
			String[] temp2 = temp1[1].split("\\}\\}",2);
			if(temp2.length >1)
				text = temp1[0] + temp2[1];
			else
				text = temp1[0];
		}
		return text;
	}
	
	
	/* TODO */
	/**
	 * Method to parse links and URLs.
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Links_and_URLs
	 * @param text: The text to be parsed
	 * @return An array containing two elements as follows - 
	 *  The 0th element is the parsed text as visible to the user on the page
	 *  The 1st element is the link url
	 */
	public static String[] parseLinks(String text)
	{
		String[] final_token = {"",""};
		
		if(!(text != null && text != ""))
		{}
		else
		{
			//System.out.println("Text to parseLink: " + text);
			String token, token1;
			String[] token2; 
		    
			if(text.contains("http://www.wikipedia.org"))
			{
				if(text == "[http://www.wikipedia.org]")
					return final_token;
				else
				{
					int length = "[http://www.wikipedia.org".length();
					int start = text.indexOf("[http://www.wikipedia.org", 0);
					int end = text.indexOf(']', start);
					token = text.substring(start, end);
					token1 = token.substring(start+length, end);
					final_token[0] = token1.trim();
					final_token[1] = "";
					if(end < text.length()-1)
						text = text.substring(start+length+1) + text.substring(end+2, text.length());
					else
					{
						text = text.substring(start+length+1) + text.substring(end+1, text.length());
						text = text.replaceAll("\\]", "");
					}
				}
			}
			String pattern1 = "\\[\\[";
			String pattern2 = "\\]\\]";
			
			Pattern r1 = Pattern.compile(pattern1);
		    Matcher m1 = r1.matcher(text);
		    
		    Pattern r2 = Pattern.compile(pattern2);
		    Matcher m2 = r2.matcher(text);
		    
			if(m1.find() && m2.find())
			{	
				text = text.replaceAll("<nowiki />", "");
				String old_link = text.substring(m1.start(), m2.start()+2);
				String link = old_link.replaceAll("\\[\\[", "");
				link = link.replaceAll("\\]\\]", "");
				link = link.replaceAll("File:wiki.png", "");
				link = link.trim();
				
				if(link.isEmpty())
					return final_token;
			
				else if(!(link.contains("|")) && !(link.contains(":")) || (link.endsWith("|") && !(link.contains(":"))))
				{
					link = link.replaceAll("\\|", "");
					if(link.contains("("))
						final_token[0] = link.substring(0,link.indexOf('(')).trim();
					else if(link.contains(","))
						final_token[0] = link.substring(0,link.indexOf(',')).trim();
					else
						final_token[0] = link.trim();
					final_token[1] = link.trim();
					int i = text.indexOf(old_link);
					text = text.substring(0, i) + final_token[0] + text.substring(i+old_link.length());
					final_token[0] = text;
					final_token[1] = final_token[1].replaceAll(" ", "_");
					final_token[1] = final_token[1].substring(0, 1).toUpperCase() + final_token[1].substring(1);
					link = "";
				}
				else if(link.contains(":") && !(link.contains("|")))
				{	
					token2 = link.split(":");
					if(token2.length == 2)
					{
						if(token2[0].contains("Category"))
							final_token[0] = token2[1].trim();
						else
							final_token[0] = token2[0].trim() + ":" + token2[1].trim();
						final_token[1] = "";
						int i = text.indexOf(old_link);
						text = text.substring(0, i) + final_token[0] + text.substring(i+old_link.length());
						link = "";
 					}
					else
					{
						final_token[0] = token2[1].trim() + ":" + token2[2].trim();
						final_token[1] = "";
						int i = text.indexOf(old_link);
						text = text.substring(0, i) + final_token[0] + text.substring(i+old_link.length());
						link = "";
					}
				}
				
				if(link.contains("|"))
				{
					token2 = link.split("\\|");
					if(token2.length == 1)
					{
						if(token2[0].contains("#"))
							final_token[0] = token2[0].trim();
						else
						{
							if(token2[0].contains("("))
								token2[0] = token2[0].substring(0, token2[0].indexOf("(")-1) + token2[0].substring(token2[0].indexOf(")")+1);
							int index = token2[0].indexOf(":")+1;
							String temp = token2[0].substring(index);
							final_token[0] = temp.trim();
						}
						final_token[1] = "";
						int i = text.indexOf(old_link);
						text = text.substring(0, i) + final_token[0] + text.substring(i+old_link.length());
						link = "";
					}
					else if(token2.length == 2)
					{
						final_token[0] = token2[1].trim();
						if(token2[0].contains(":"))
							final_token[1] = "";
						else
						{
							final_token[1] = token2[0].trim();
							final_token[1] = final_token[1].replaceAll(" ", "_");
							final_token[1] = final_token[1].substring(0, 1).toUpperCase() + final_token[1].substring(1);
						}
						int i = text.indexOf(old_link);
						final_token[0] = text.substring(0, i) + final_token[0] + text.substring(i+old_link.length());
						link = "";
					}
					else
					{
						final_token[0] = token2[token2.length-1];
						final_token[1] = "";
						//int i = text.indexOf(old_link);
						//text = text.substring(0, i) + final_token[0] + text.substring(i+old_link.length());
						link = "";
 					}
				}
			}
		}
		return final_token;
	}

	public static void parseSection(WikipediaDocument doc, String text)
	{	
		if(text != null && text.isEmpty() == false)
		{
		//Add Category
			while(text.contains("[[Category:"))
			{
				String category = "[[Category:";
				int length = category.length();
				int start = text.indexOf("[[Category:");
				int end = text.indexOf("]]", start);
				doc.addCategory(text.substring(start+length, end));
				text = text.substring(0,start) + text.substring(end+2);
			}
		}
		
		//Add Link
		if(text != null && text.isEmpty() == false)
		{
			while(text.contains("[http:"))
			{
				int start = text.indexOf("[http:");
				int end = text.indexOf("]", start);
				doc.addLink(text.substring(start+1, end));
				text = text.substring(0,start) + text.substring(end+1);
			}
		}
		
		
		if(text != null && text.isEmpty() == false)
		{
			while(text.contains("[["))
			{		
				String[] temp = text.split("\\]\\]",2);
				String[] new_temp = parseLinks(temp[0]+"]]");
				temp[0] = new_temp[0];
				if(!(temp[1].isEmpty()))
					doc.addLink(new_temp[1]);
				text = temp[0] + temp[1];
			}
		}
		
		
		// Remove List Item Tags
		if(text != null && text.isEmpty() == false)
			text = parseListItem(text);
	
		
		//Sections....
		if(text != null && text.isEmpty() == false)
		{
			text = text.trim();
			text = "==Default==\n" + text;
			text = text.replaceAll("(=){2,}", "==");
			String[] temp = text.split("==");
			
			for(int i = 1; i < temp.length ; i=i+2)
			{
				if(i < temp.length -1 )
					doc.addSection(temp[i].trim(), temp[i+1].trim());
				else
					doc.addSection(temp[i].trim(),"");
			}
		}
		
		/*Iterator<String> itr = doc.getLinks().iterator();
		System.out.println("Parsed : ");
		
		while(itr.hasNext())
		{
			//Section sect = itr.next();
			System.out.println("WIKI PARSER :\n" + itr.next());
			//System.out.println("Text :\n" + sect.getText());
		}
		System.out.println("\n");*/
	}
}
