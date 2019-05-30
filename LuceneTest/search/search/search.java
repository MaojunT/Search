package search;

public class search {
	  public static void main(String[] args) throws Exception {
		  
		String source = "D:\\Projects\\CS4315\\LuceneTest\\source\\lyrics.xls";  
		LuceneSearch ls = new LuceneSearch();
		ls.parse(source);
		new Gui(ls);
	  }
}
