package src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

//import java.util.Date;

import java.text.SimpleDateFormat;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;



import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import src.edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.DATES)
public class Dates implements TokenizerRule 
{	
	private static final String MONTH[]={"January","February","March","April","May","June","July","August",
        "September","October","November","December","jan","feb",
        "mar","apr","may","jun","jul","aug","sep","oct","nov","dec"};
	private static final String Day[] = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday","monday","tuesday","wednesday","thursday","friday","saturday","sunday"};
private static final String YEAR3="^[0-9][0-9]$";
private static final String YEAR2="(^[0-9])[0-9][0-9]([0-9])$";
private static final String YEAR1="^(\\d{0,3}[0-9]$)";
private static final String DATE="^((0?[1-9])|[12][0-9]|3[01])$";
private static final String TIME2="(0?[1-9])|1[0-2]:{1}([0-5][0-9])[^\\s]([aApP][mM])$";
private static final String TIME="(0?[1-9])|1[0-2]\\:(([0-5][0-9]))[^aApP][^mM]";

private static final String TIMESTAMP_PATTERN = "[0-9]{4}-(0?[1-9]|1[012])-((0?[1-9])|[12][0-9]|3[01])T[01][0-9]|2[0-3]:[0-5][0-9]:[0-5][0-9]Z";
	

	public void apply(TokenStream stream) throws TokenizerException 
	{
		// TODO Auto-generated method stub
		//String token="fgg hrtgh 12:30pm ththtr fnfrj May 23 efme 12:03am fnmefn 11:40 am April 29, 2010";
		//	int index=0;
			StringBuilder q=new StringBuilder();
			
			
			if (stream != null) {
				String token;
				char t;

				while (stream.hasNext()) 
				{ 	
					token = stream.next();
					String old_token = token;
					for(int i = 0; i<Day.length; i++)
						token = token.replaceAll(Day[i], "");
					
					int length=token.length();
					String token1="";
					String token2="";
					String token3="";
					int pos=0;
				int pos_1=0;
				int pos_2=0;
				int pos_3=0;
				int d=0;
				String final_tok="";
				char c;
				String s="";
				int count=0;
				
				int counter_year=0;
				int flag=0;
				for (int i=0; i<length;i++)
				{
					int track=0;
					while (i<length && token.charAt(i)!=' ')
					{
						pos=i;
						c=token.charAt(i);
						s=Character.toString(c);
						token1=token1.concat(s);
			
						i++;
					}
					
						
						String timestamp_pattern = TIMESTAMP_PATTERN;
						Pattern r = Pattern.compile(timestamp_pattern);
						Matcher m = r.matcher(token1);
						
						if (m.find()) 
						  {		
							d=1;
					     
							 	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
								Date time;
								final_tok=token1;
								if (final_tok.contains(",")||final_tok.contains(".")||final_tok.contains("?")||final_tok.contains("!"))
								{
								if (final_tok.contains(","))
								{
									track=1;
									final_tok=final_tok.replace(",","");
								}
								if (final_tok.contains("."))
								{
									track=2;
									final_tok=final_tok.replace(".","");
								}
								if (final_tok.contains("?"))
								{
									track=3;
									final_tok=final_tok.replace("?","");
								}
								if (final_tok.contains("!"))
								{
									track=4;
									final_tok=final_tok.replace("!","");
								}
								}
								
								
								try   {
								
									   time = sdf1.parse(final_tok);
									   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
									   final_tok = dateFormat.format(time);
									   track=0;
									   
									   
									   if (track==1)
										   final_tok=final_tok+",";
									   else if (track==2)
										   final_tok=final_tok+".";
									   else if (track==3)
										   final_tok=final_tok+"?";
									   else if (track==4)
										   final_tok=final_tok+"!";
									   q.append(final_tok+" ");
								
									   token1="";
									   token2="";
									   token3="";
									   
									   continue;
									   } 
								       catch(Exception ex) 
								       {
									   // handle exception
									   }														
					     }		
						
						else{
		
						String date_pattern = DATE;
						Pattern rdate = Pattern.compile(date_pattern);
						Matcher mdate = rdate.matcher(token1);
						
					if (mdate.find())
					{
						d=1;


						pos_1=i-token1.length();
				
						i++;
						while (i<length && token.charAt(i)!=' ')
						{
							c=token.charAt(i);
							
							s=Character.toString(c);
							token2=token2.concat(s);
							i++;
						}
			
						for(int j=0;j<12;j++)
						{
				
						if (token2.equalsIgnoreCase(MONTH[j]) || token2.equalsIgnoreCase(MONTH[j+12]))
						  {
							d=1;
							pos_2=i-token.length();
							flag=1;
							token2=token2.substring(0,3);
			
							break;
						  }
						}
						if (flag==1)
						{
							i++;

						while(i<length && token.charAt(i)!=' ')
						  {
							c=token.charAt(i);
							s=Character.toString(c);
							token3=token3.concat(s);			
							track=0;
							if (token3.contains(",")||token3.contains(".")||token3.contains("?")||token3.contains("!"))
							{
							if (token3.contains(","))
							{
								track=1;
							token3=token3.replace(",","");
							}
							if (token3.contains("."))
							{
								track=2;
							token3=token3.replace(".","");
							}
							if (token3.contains("?"))
							{
								track=3;
							token3=token3.replace("?","");
							}
							if (token3.contains("!"))
							{
								track=4;
							token3=token3.replace("!","");
							}
							}
							i++;					
						  }
			
						String year2 = YEAR2;
						Pattern ryear2 = Pattern.compile(year2);
						Matcher myear2 = ryear2.matcher(token3);
						if (myear2.find())
						{
							d=1;
							counter_year=1;
							pos_3=i-token3.length();
							track=0;
							if (token3.contains(",")||token3.contains(".")||token3.contains("?")||token3.contains("!"))
							{
							if (token3.contains(","))
							{
								track=1;
							token3=token3.replace(",","");
							}
							if (token3.contains("."))
							{
								track=2;
							token3=token3.replace(".","");
							}
							if (token3.contains("?"))
							{
								track=3;
							token3=token3.replace("?","");
							}
							if (token3.contains("!"))
							{
								track=4;
							token3=token3.replace("!","");
							}
							}		
						}
						
						String year3 = YEAR3;
						Pattern ryear3 = Pattern.compile(year3);
						Matcher myear3 = ryear3.matcher(token3);
						if (myear3.find())
							{
								counter_year=1;
								pos_3=i-token3.length();
								token3="19"+token3;
								track=0;
								if (token3.contains(",")||token3.contains(".")||token3.contains("?")||token3.contains("!"))
								{
								if (token3.contains(","))
								{
									track=1;
								token3=token3.replace(",","");
								}
								if (token3.contains("."))
								{
									track=2;
								token3=token3.replace(".","");
								}
								if (token3.contains("?"))
								{
									track=3;
								token3=token3.replace("?","");
								}
								if (token3.contains("!"))
								{
									track=4;
								token3=token3.replace("!","");
								}
								}
								
							}
						if (counter_year==1)
						{
							final_tok=token1+token2+token3;
					
							SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyyyy");
							Date date;

							try{
							   date = sdf.parse(final_tok);
							   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
							   final_tok = dateFormat.format(date);
							   counter_year=0;
							   if (track==1)
								   final_tok=final_tok+",";
							   else if (track==2)
								   final_tok=final_tok+".";
							   else if (track==3)
								   final_tok=final_tok+"?";
							   else if (track==4)
								   final_tok=final_tok+"!";
							   
							   q.append(final_tok+" ");	
							   token1="";
							   token2="";
							   token3="";
							   						  
							   continue;
							   }
							catch(Exception ex){
							   // handle exception
							   }
							
						 }
							
					   }
					}
						}
					flag=0;	
					count=0;	
					for(int j=0;j<12;j++)
					{				
						if (token1.equalsIgnoreCase(MONTH[j]) || token1.equalsIgnoreCase(MONTH[j+12]))
						{							
							pos_1=i-token1.length();
							flag=1;
							token1=token1.substring(0,3);
							break;
						}
					}	
					if (flag==1)
					{
						i++;
						while (i<length && token.charAt(i)!=' ')
						{
							pos=i;
							c=token.charAt(i);
							s=Character.toString(c);
							token2=token2.concat(s);
							i++;
						}
						
						String year0 = "(0?[1-9])|[12][0-9]|3[01]\\,{0,}";
						Pattern ryear0 = Pattern.compile(year0);
						Matcher myear0 = ryear0.matcher(token2);
						if (myear0.find())
						{
							i++;
							
							String year9 = "^0?[1-9][^0-9]$";
							
							Pattern ryear9 = Pattern.compile(year9);
							Matcher myear9 = ryear9.matcher(token2);
							if (myear9.find())
							{
								token2="0"+token2;
								
							}
						
							pos_2=i-token2.length();
							int l=i;
							int h=0;
							while(i<length && token.charAt(i)!=' ')
							  {
								
								c=token.charAt(i);
								s=Character.toString(c);
								token3=token3.concat(s);
								i++;					
							  }
							if (token3.contains(",")||token3.contains(".")||token3.contains("?")||token3.contains("!"))
							{
							if (token3.contains(","))
							{
								track=1;
							token3=token3.replace(",","");
							}
							if (token3.contains("."))
							{
								track=2;
							token3=token3.replace(".","");
							}
							if (token3.contains("?"))
							{
								track=3;
							token3=token3.replace("?","");
							}
							if (token3.contains("!"))
							{
								track=4;
							token3=token3.replace("!","");
							}
							}
							
							String year4 = YEAR2;
							Pattern ryear4 = Pattern.compile(year4);
							Matcher myear4 = ryear4.matcher(token3);
							
							if (myear4.find())
							{
								h=i;
								
								counter_year=1;
								pos_3=i-token3.length();
													
							}
							
							String year5 = YEAR3;
							Pattern ryear5 = Pattern.compile(year5);
							Matcher myear5 = ryear5.matcher(token3);
							if (myear5.find())
								{
								    h=i;
							
									counter_year=1;
									pos_3=i-token3.length();
									token3="19"+token3;			
								}
							
							String year1 = "[^0-9]{1,}";
							Pattern ryear1 = Pattern.compile(year1);
							Matcher myear1 = ryear1.matcher(token3);
							if (h==0 || myear1.find())
								{
								i=l-1;
								    counter_year=1;
									token2=token2+"1900";	
								
								}
						
							if (counter_year==1)
							{
							
								if (h!=0)
								final_tok=token1+token2+token3;
								else
								final_tok=token1+token2;
							
								SimpleDateFormat sdf;
								
								String comma = "\\,{1,}";
								Pattern rcomma = Pattern.compile(comma);
								Matcher mcomma = rcomma.matcher(token2);
								
								if (mcomma.find())
								{
			
								sdf = new SimpleDateFormat("MMMdd,yyyy");
			
								}
								else
								{
									sdf = new SimpleDateFormat("MMMddyyyy");	
								}
								Date date;

								try{
								   date = sdf.parse(final_tok);
								   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
								   final_tok = dateFormat.format(date);
								   if (track==1)
									   final_tok=final_tok+",";
								   else if (track==2)
									   final_tok=final_tok+".";
								   else if (track==3)
									   final_tok=final_tok+"?";
								   else if (track==4)
									   final_tok=final_tok+"!";
								   counter_year=0;
				
								   q.append(final_tok+" ");	
								   token1="";
								   token2="";
								   token3="";
								   							   
								   continue;
								   }
								catch(Exception ex){
								   // handle exception
								   }
							 }
						}
					}
						
					String year10 = "([^0-9aApPbB][^mMdDcC])$";
					Pattern ryear10 = Pattern.compile(year10);
					Matcher myear10 = ryear10.matcher(token1);
					if (myear10.find())
					{
						final_tok=token1;
						q.append(final_tok+" ");
						
						   token1="";
						   token2="";
						   token3="";
						   
						   continue;
					}	
						
						String year7 = YEAR1;
						Pattern ryear7 = Pattern.compile(year7);
						Matcher myear7 = ryear7.matcher(token1);
						int l=i;
						if (myear7.find())
						{
							i++;
							while (i<length && token.charAt(i)!=' ')
							{
								pos=i-1;
								c=token.charAt(i);
								s=Character.toString(c);
								token2=token2.concat(s);
								i++;
							}

						
							
							if (token2.contains("AD"))
							{
								track=0;
								if (token2.contains(",")||token2.contains(".")||token2.contains("?")||token2.contains("!"))
								{
								if (token2.contains(","))
								{
									track=1;
								token2=token2.replace(",","");
								}
								else if (token2.contains("."))
								{
									track=2;
								token2=token2.replace(".","");
								}
								else if (token2.contains("?"))
								{
									track=3;
								token2=token2.replace("?","");
								}
								else if (token2.contains("!"))
								{
									track=4;
								token2=token2.replace("!","");
								}
								}
								
							int len=token1.length();
							if (len==1)
						    final_tok="000"+token1+"0101";
							else if (len==2)
								final_tok="00"+token1+"0101";
							else if (len==3)
								final_tok="0"+token1+"0101";
							else if (len==4)
								final_tok=token1+"0101";
				
							token2.replace("AD","");
							if (track==1)
								   final_tok=final_tok+",";
							   else if (track==2)
								   final_tok=final_tok+".";
							   else if (track==3)
								   final_tok=final_tok+"?";
							   else if (track==4)
								   final_tok=final_tok+"!";
							q.append(final_tok+" ");
							token1="";
							token2="";
							token3="";
							
							continue;
							}
							
							else if (token2.contains("BC"))
							{
								track=0;
								if (token2.contains(",")||token2.contains(".")||token2.contains("?")||token2.contains("!"))
								{
								if (token2.contains(","))
								{
									track=1;
								token2=token2.replace(",","");
								}
								else if (token2.contains("."))
								{
									track=2;
								token2=token2.replace(".","");
								}
								else if (token2.contains("?"))
								{
									track=3;
								token2=token2.replace("?","");
								}
								else if (token2.contains("!"))
								{
									track=4;
								token2=token2.replace("!","");
								}
								}
								
							int len=token1.length();
							if (len==1)
								final_tok="-000"+token1+"0101";
							else if (len==2)
								final_tok="-00"+token1+"0101";
							else if (len==3)
								final_tok="-0"+token1+"0101";
							else if (len==4)
								final_tok="-"+token1+"0101";
			
							token2.replace("BC","");
							if (track==1)
								   final_tok=final_tok+",";
							   else if (track==2)
								   final_tok=final_tok+".";
							   else if (track==3)
								   final_tok=final_tok+"?";
							   else if (track==4)
								   final_tok=final_tok+"!";
							q.append(final_tok+" ");
							token1="";
							token2="";
							token3="";
							
							continue;

							
							}
							else i=l;
			
							
						}
						
     					String year6 = YEAR2;
						Pattern ryear6 = Pattern.compile(year6);
						Matcher myear6 = ryear6.matcher(token1);
						
						
						if (token1.contains("-") && myear6.find())
						{
							track=0;
							if (token1.contains(",")||token1.contains(".")||token1.contains("?")||token1.contains("!"))
							{
							if (token1.contains(","))
							{
								track=1;
							token1=token1.replace(",","");
							}
							else if (token1.contains("."))
							{
								track=2;
							token1=token1.replace(".","");
							}
							else if (token1.contains("?"))
							{
								track=3;
							token1=token1.replace("?","");
							}
							else if (token1.contains("!"))
							{
								track=4;
							token1=token1.replace("!","");
							}
							}
							final_tok=token1.substring(0,4)+"0101-"+token1.substring(0,2)+token1.substring(5,7)+"0101";
							if (track==1)
								   final_tok=final_tok+",";
							   else if (track==2)
								   final_tok=final_tok+".";
							   else if (track==3)
								   final_tok=final_tok+"?";
							   else if (track==4)
								   final_tok=final_tok+"!";
							q.append(final_tok+" ");
							token1="";
							token2="";
							token3="";
							continue;
						}
						
						else if (myear6.find())
							{
								final_tok=token1.concat("0101");
								q.append(final_tok+" ");
								token1="";
								token2="";
								token3="";
								
								continue;
							}
						track=0;												
						String time2 = TIME2;
						Pattern rtime2 = Pattern.compile(time2);
						Matcher mtime2 = rtime2.matcher(token1);
						if (mtime2.find())
						{
						    int pmindex=-1;
					   if (token1.contains("AM") || token1.contains("am"))
					   {    
						   track=0;
						   if (token1.contains(",")||token1.contains(".")||token1.contains("?")||token1.contains("!"))
							{
							if (token1.contains(","))
							{
								track=1;
							token1=token1.replace(",","");
							}
							else if (token1.contains("."))
							{
								track=2;
							token1=token1.replace(".","");
							}
							else if (token1.contains("?"))
							{
								track=3;
							token1=token1.replace("?","");
							}
							else if (token1.contains("!"))
							{
								track=4;
							token1=token1.replace("!","");
							}
							}
						   
						    if (token1.indexOf("AM")!=-1)
						    {
						    pmindex = token1.indexOf("AM");
						    }
						    else if (token1.indexOf("am")!=-1)
						    {
						    pmindex = token1.indexOf("am");
						    }
					        int separator=token1.indexOf(":");
					        int hour = Integer.parseInt(token1.substring(0, separator));
					        int minute = Integer.parseInt(token1.substring(separator+1,pmindex));
						    
					       if (hour<10)
					       {
					        if (minute<10)
					        token1="0"+hour+":0"+minute;
					        else
					        token1="0"+hour+":"+minute;
						   }
					       else				       
					       if (hour==12)
					       {
					        if (minute<10)
					        token1="00"+":0"+minute;
					        else
					        token1="00"+":"+minute;
						   }
					       else
					       {
					        if (minute<10)
					        token1=hour+":0"+minute;
					        else
					        token1=hour+":"+minute;
						   }
					       
					       final_tok=token1.concat(":00");
						
					       if (track==1)
							   final_tok=final_tok+",";
						   else if (track==2)
							   final_tok=final_tok+".";
						   else if (track==3)
							   final_tok=final_tok+"?";
						   else if (track==4)
							   final_tok=final_tok+"!";
							q.append(final_tok+" ");
							token1="";
							token2="";
							token3="";
							
							continue;
						}    
					   if (token1.contains("PM") || token1.contains("pm"))
					    {
				
						   track=0;
						   if (token1.contains(",")||token1.contains(".")||token1.contains("?")||token1.contains("!"))
							{
							if (token1.contains(","))
							{
								track=1;
							token1=token1.replace(",","");
							}
							else if (token1.contains("."))
							{
								track=2;
							token1=token1.replace(".","");
							}
							else if (token1.contains("?"))
							{
								track=3;
							token1=token1.replace("?","");
							}
							else if (token1.contains("!"))
							{
								track=4;
							token1=token1.replace("!","");
							}
							}
						   
						    if (token1.indexOf("PM")!=-1)
						    {
						        pmindex = token1.indexOf("PM");
						    }
						    else if (token1.indexOf("pm")!=-1)
						    {
						    	pmindex = token1.indexOf("pm");
						    }
						        int separator=token1.indexOf(":");
						        int hour = Integer.parseInt(token1.substring(0, separator));
						        int minute = Integer.parseInt(token1.substring(separator+1,pmindex));
						        hour = hour+12;
						        if (hour==24 )
					    		{
					    			hour=hour-12;
					    		}
						        if (minute<10)
						        token1=hour+":0"+minute;
						        else
						        token1=hour+":"+minute;						        							
	
						    
							final_tok=token1.concat(":00");
		
							if (track==1)
								   final_tok=final_tok+",";
							   else if (track==2)
								   final_tok=final_tok+".";
							   else if (track==3)
								   final_tok=final_tok+"?";
							   else if (track==4)
								   final_tok=final_tok+"!";
							q.append(final_tok+" ");
							token1="";
							token2="";
							token3="";
							
							continue;
						 }
						}
						
					//	else
					//{   
						String time = TIME;
						Pattern rtime = Pattern.compile(time);
						Matcher mtime = rtime.matcher(token1);
						
						if (mtime.find())
						{
								
								i++;
								while (i<length && token.charAt(i)!=' ')
								{
									pos=i-token1.length();
									c=token.charAt(i);
									s=Character.toString(c);
									token2=token2.concat(s);
									i++;
								}
								
								//System.out.println("Token2 : " + token2);
								
							if (token2.startsWith("am") || token2.startsWith("AM"))
							{
								track=0;
								if (token2.contains(",")||token2.contains(".")||token2.contains("?")||token2.contains("!"))
								{
								if (token2.contains(","))
								{
									track=1;
								token1=token2.replace(",","");
								}
								else if (token2.contains("."))
								{
									track=2;
								token2=token2.replace(".","");
								}
								else if (token2.contains("?"))
								{
									track=3;
								token2=token2.replace("?","");
								}
								else if (token2.contains("!"))
								{
									track=4;
								token2=token2.replace("!","");
								}
								}
								
								token2=token2.replace("am","");
							    token2=token2.replace("AM","");
							    int separator=token1.indexOf(":");
						        int hour = Integer.parseInt(token1.substring(0, separator));
						        int minute = Integer.parseInt(token1.substring(separator+1,separator+3));
						   //     final_tok=token1.concat(":00");
						       /* if (hour==12)
					    		{
						        	if (minute<10)
					    			final_tok="00:0"+minute+"00";
						        	else
						        	final_tok="00:"+minute+"00";	
					    		}*/
						        if (hour<10)
						        {
						        	if (minute<10)
					    			final_tok="0"+hour+":0"+minute+":00";
						        	else
						        	final_tok="0"+hour+":"+minute+":00";	
					    		}
						        else if (hour==12)
						        {
						        	if (minute<10)
						    		final_tok="00"+":0"+minute+":00";
							        else
							        final_tok="00:"+minute+":00";
						        }
						        else
						        	final_tok=token1+":00";	
						        
						        if (track==1)
									   final_tok=final_tok+",";
								   else if (track==2)
									   final_tok=final_tok+".";
								   else if (track==3)
									   final_tok=final_tok+"?";
								   else if (track==4)
									   final_tok=final_tok+"!";
			
							    q.append(final_tok+" ");
							    token1="";
								token2="";
								token3="";
								
								continue;
							}
		
							else if (token2.startsWith("pm") || token2.startsWith("PM"))
							{
							    track=0;
						        int separator=token1.indexOf(":");
						        int hour = Integer.parseInt(token1.substring(0, separator));
						        int minute = Integer.parseInt(token1.substring(separator+1,separator+3));
						        hour = hour+12;
						        if (hour==24 )
					    		{
					    			hour=hour-12;
					    		}
						        
						    	if (token2.contains("pm"))
								{						    		
								token2=token2.replace("pm","");
								}
								else if (token2.contains("PM"))
								{
							    token2=token2.replace("PM","");
								}
						        
						        if (minute<10)
							    token1=hour+":0"+minute;
							    else
							    token1=hour+":"+minute;
							    
							    final_tok=token1.concat(":00");
							    if (track==1)
									   final_tok=final_tok+",";
								   else if (track==2)
									   final_tok=final_tok+".";
								   else if (track==3)
									   final_tok=final_tok+"?";
								   else if (track==4)
									   final_tok=final_tok+"!";
							    
							    q.append(final_tok+" ");
							    token1="";
								token2="";
								token3="";
				
							    continue;
							   
							}
						}

					//}									
							final_tok=token1;
							q.append(final_tok+" ");
							token1="";
							token2="";
							token3="";
							
							
							if (token.matches("\\d{4}�\\d{2}"))
							{
								final_tok=token.substring(0,4)+"0101�"+token.substring(0,2)+token.substring(5,7)+"0101";
								q.append(final_tok+" ");
							    token1="";
								token2="";
								token3="";
								continue;
							}
								
						}				

					final_tok=token1;
					q.append(token1+" ");
					String updated = q.toString();
					updated = updated.trim();
					if(old_token.trim().endsWith("."))
					{
						if(!(updated.trim().endsWith(".")))
							updated = updated + ".";
					}
					stream.previous();
					stream.set(updated);
					stream.next();
				}					
			}
	}

}
