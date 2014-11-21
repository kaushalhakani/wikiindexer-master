package src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;


import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.APOSTROPHE)
public class Apostrophe implements TokenizerRule {

	private static String apos1="([a-zA-Z]{1,})\\'[s]{1}"; 
	private static String apos2="[a-zA-Z]{1,}[s]{1}\\'"; 
	private static String apos3="[a-zA-Z]{1,}\\'[d]{1}"; 
	private static String apos4="[a-zA-Z]{1,}[n]{1}\\'[t]{1}";
	private static String apos5="[a-zA-Z]{1,}\\'[v]{1}[e]{1}";
	private static String apos6="[a-zA-Z]{1,}\\'[l]{2}";
	private static String apos7="[a-zA-Z]{1,}\\'[r]{1}[e]{1}";
	private static String apos8="[a-zA-Z]{1,}\\'[aA][mM]";
	private static String apos9="^(\\'[nN]\\')";
	private static String apos10="[iI]\\'[mM]";
	private static String apos11="shan't";
	private static String apos12="won't";
	private static String apos13="let's";
	private static String apos14="[a-zA-Z]{1,}\\'[e]{1}[m]{1}";
	private static String apos="'s";
	
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
	
	public void apply(TokenStream stream) throws TokenizerException {
		// TODO Auto-generated method stub
		
		if (stream != null) {
			String token;
			int flag = 0;
			
			while (stream.hasNext()) { 
				token = stream.next();
				
				if (token != null) {
					
			     if (token.contains("'")) {
			    	 
			    	if (token.matches(apos14)) {
						token = token.replaceAll("'em"," them");
					}
			    	if (token.matches(apos13)) {
						token = token.replaceAll(apos13,"let us");
					}
			    	if (token.matches(apos11)) {
						token = token.replaceAll(apos11,"shall not");
					}
					if (token.matches(apos12)) {
						token = token.replaceAll(apos12,"will not");
					}
					if (token.matches(apos1)) {
						token = token.replaceAll(apos, "");
					}
					else
					if (token.matches(apos2)) {
						stream.mergeWithNext();
						token = token.replaceAll("s'","");
						flag = 1;
					}
					
					if (token.matches(apos3)) {
						token = token.replaceAll("'"," woul");
					}
					
					if (token.matches(apos4)) {
						token = token.replaceAll("n't"," not");
					}
					if (token.matches(apos5)) {
						token = token.replaceAll("'ve"," have");
					}
					
					if (token.matches(apos6)) {
						token = token.replaceAll("'ll"," will");
					}
					
					if (token.matches(apos7)) {
						token = token.replaceAll("'re"," are");
					}
					if (token.matches(apos8)) {
						if (isUpper(token)>1 )
						token = token.replaceAll("'AM","DAM");	
						else
							token = token.replaceAll("'am","dam");	
					}
					if (token.matches(apos9)) {
						if (Character.isLowerCase(apos9.charAt(1)))
							token = token.replaceAll(apos9,"and");
						else	
							token = token.replaceAll(apos9,"AND");
						
					}
					
					if (token.matches(apos10)) {
						token = token.replaceAll(apos10,"I am");
					}
					if (token.contains("'")) {
						token = token.replaceAll("'","");
						flag = 1;
					}
					if (token.startsWith("'") || token.endsWith("'"))
					{
						token.replaceFirst("'","");
					}
					stream.previous();
					if(flag == 0)
					{
						String[] token1 = token.split(" ");
						stream.set(token1);
					}
					else
						stream.set(token);
			     }	
						
				}
			}
				
		}
		stream.reset();
	}

}
