package src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;


@RuleClass(className = RULENAMES.CAPITALIZATION)
public class Capitalization implements TokenizerRule {
	
	public static int isUpper(String s)
	{
		int j=0;
		for (int i=0; i<s.length(); i++)
		{			
			if (Character.isUpperCase(s.charAt(i)))
			{
				j++;
			}
		}
		// return 0 if all characters are small case,1 if only first character is upper-case, 
		//length of string if all character are upper-case
		return j;		
		
	}
	
	public void apply(TokenStream stream) throws TokenizerException 
	{
		// TODO Auto-generated method stub
		
		if(stream != null)
		{
			while(stream.hasNext())
			{
				String cap_term = stream.next();
				String next_term = null;
				String prev_term = null;
				
				if(stream.hasNext())
				{
					next_term = stream.next();
					stream.previous();
				}
				
				stream.previous();
				
				if(stream.hasPrevious())
				{
					prev_term = stream.previous();
					stream.next();
				}
				
				
				int x = cap_term.charAt(0);
				
				if(x < 65 || x > 90)
				{
					//System.out.println("No Captials");
					//System.out.println("Out Captial Term : " + cap_term);
					//System.out.println("Out Prev Term : " + prev_term);
					//System.out.println("Out NextTerm : " + next_term);
					if(stream.hasNext())
					{
						stream.next();
						continue;
					}
					else
						break;
				}
				else
				{
					//System.out.println("Captial Term : " + cap_term);
					//System.out.println("Prev Term : " + prev_term);
					//System.out.println("NextTerm : " + next_term);
					
					int y = cap_term.charAt(1);
					if(y >= 65 && y <= 90)
					{
						//System.out.println("All Capital");
						if(stream.hasNext())
						{
							stream.next();
							continue;
						}
					}
					
					else
					{
						/*while(stream.hasNext())
						{
							int ctr = 1;
							String temp = stream.next();
							if()
						}*/
						if(next_term != null)
						{
							y = next_term.charAt(0); 
							if(y >= 65 && y <= 90)
							{
								//System.out.println("San Framn");
								stream.next();
								stream.next();
								continue;
							}
						}
							
						if(prev_term != null)
						{		
							if(prev_term.endsWith(".") || prev_term.endsWith("?") || prev_term.endsWith("!"))
							{
								//System.out.println("Not New Line Character.");
								stream.set(cap_term.toLowerCase());
								continue;
							}
							
							if(next_term != null)
							{
								stream.next();
								continue;
							}
							else
								break;
						}	
						else 
						{
							//System.out.println("Start of Line.");
							stream.set(cap_term.toLowerCase());
							stream.next();
							//stream.print_stream();
							continue;
						}
					}
				}
			}
		}
	}
}		