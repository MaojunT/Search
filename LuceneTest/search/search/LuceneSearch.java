package search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LuceneSearch {
	
	Directory index;
	WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
	IndexWriterConfig config = new IndexWriterConfig(analyzer);
	
    public void parse(String filePath) throws Exception {
    	deleteIndex();
		final String INDEX_DIRECTORY = "D:\\Projects\\CS4315\\LuceneTest\\index";
		index = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
    	Sheet contentSheet = Workbook.getWorkbook(new File(filePath)).getSheet(
    			0);
    	indexDocs(contentSheet);

    }
    
    void indexDocs(Sheet contentSheet) throws CorruptIndexException,
    IOException {
		//IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter w = new IndexWriter(index, config);
		
		for (int i = 1; i < contentSheet.getRows(); i++) {
			Document document = new Document();
			
			Cell[] rankCell = contentSheet.getColumn(0);
			String rank = rankCell[i].getContents();
    		Field rankField = new TextField("rank", rank, Store.YES);
   
			Cell[] songCell = contentSheet.getColumn(1);
			String song = songCell[i].getContents();
    		Field songField = new TextField("song", song, Store.YES);

			Cell[] artistCell = contentSheet.getColumn(2);
			String artist = artistCell[i].getContents();
    		Field artistField = new TextField("artist", artist, Store.YES);
    		
			Cell[] yearCell = contentSheet.getColumn(3);
			String year = yearCell[i].getContents();
    		Field yearField = new TextField("year", year, Store.YES);
    		
			Cell[] lyricCell = contentSheet.getColumn(4);
			String lyric = lyricCell[i].getContents();
    		Field lyricField = new TextField("lyric", lyric, Store.YES);
		    
    		Cell[] sourceCell = contentSheet.getColumn(5);
			String source = sourceCell[i].getContents();
    		Field sourceField = new TextField("source", source, Store.YES);
    		
    		
    		document.add(rankField);
    		document.add(songField);
    		document.add(artistField);
    		document.add(yearField);
    		document.add(lyricField);
    		document.add(sourceField);
    		System.out.println(document);
    		w.addDocument(document);
		
		}
		w.close();
	}

	
	public StringBuffer search(String s, String selectedItem) throws Exception {
		//deleteIndex();
    	
    
    	//File f = new File("D:\\Projects\\CS4315\\LuceneTest\\source");
    	//File[] listFiles = f.listFiles();
    
    	//for (File file : listFiles) {
    		//Document document = new Document();

    		//String file_name = file.getName();
    		//Field fileNameField = new TextField("fileName", file_name, Store.YES);

    		//long file_size = FileUtils.sizeOf(file);
    		//Field fileSizeField = new LongPoint("fileSize", file_size);

    		//String file_path = file.getPath();
    		//Field filePathField = new StoredField("filePath", file_path);
   

    		//String file_content = FileUtils.readFileToString(file, "UTF-8");
    		//Field fileContentField = new TextField("fileContent", file_content, Store.YES);
    		//document.add(fileNameField);
    		//document.add(fileSizeField);
    		//document.add(new StoredField("fileSize", file_size));
    	//	document.add(filePathField);
    	//	document.add(fileContentField);
        
    	//	w.addDocument(document);
    	//}
    
    	//w.close();
   
    
		QueryParser queryParser = new QueryParser(selectedItem, analyzer);
		queryParser.setDefaultOperator(QueryParser.Operator.AND);
		Query  query = queryParser.parse(s);
 

    	final String INDEX_DIRECTORY = "D:\\Projects\\CS4315\\LuceneTest\\index";
    	Directory index = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
	
    	IndexReader reader = DirectoryReader.open(index);
    	IndexSearcher searcher = new IndexSearcher(reader);
    	TopDocs topDocs = searcher.search(query, 100);
    	
    	Formatter formatter = new SimpleHTMLFormatter();
    	QueryScorer scorer = new QueryScorer(query);
    	Highlighter highlighter = new Highlighter(formatter, scorer);
    	Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 50);
    	highlighter.setTextFragmenter(fragmenter);
    	
    	ScoreDoc[] scoreDocs = topDocs.scoreDocs;
     
    	StringBuffer result = new StringBuffer();
    	result.append("Found " + scoreDocs.length + ".<br><br>");
    	//System.out.println("Found" + scoreDocs.length + ".");
    	for (ScoreDoc scoreDoc : scoreDocs) {
    		int doc = scoreDoc.doc;
    		Document document = searcher.doc(doc);

    		String rank = document.get("rank");
    		//System.out.println(fileName);
    		result.append("Rank: " + rank + "<br>");

    		String song = document.get("song");
    		//System.out.println(fileContent);
    		result.append("Song:  <a href='http://"+doc+"'>" + song + "</a><br>");

    		String artist = document.get("artist");
    		//System.out.println(fileSize);
    		result.append("Artist: " + artist + "<br>");

    		String year = document.get("year");
    		//System.out.println(filePath);
    		result.append("Year: " + year + "<br>");
    		
    		String lyric = document.get("lyric");
    		if (lyric != null) {
    			TokenStream tokenStream = analyzer.tokenStream("lyric",new StringReader(lyric));
    			String[] frags = highlighter.getBestFragments(tokenStream,lyric, 2);
    			for (String frag : frags) {
    	            result.append(frag + "<br>");
    			}
    		}
			//TokenStream stream = TokenSources.getAnyTokenStream(reader, doc, "lyric", analyzer);
    		//tring[] frags = highlighter.getBestFragments(stream, lyric, 10);
    		//result.append("Lyric: " + lyric + "\n");
            //for (String frag : frags)
            //{
            //    System.out.println("=======================");
             //   result.append(frag + "\n");
            //}
    		
    		String source = document.get("source");
    		result.append("Sources: " + source + "<br>");
    		result.append("-------------------------------------------------------------------------<br>");
    		//System.out.println("-----------------");
    	}
    	reader.close();

		return result;
  }
	
	public StringBuffer searchDetail(int t) throws Exception {
    	final String INDEX_DIRECTORY = "D:\\Projects\\CS4315\\LuceneTest\\index";
    	Directory index = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
	
    	IndexReader reader = DirectoryReader.open(index);
    	IndexSearcher searcher = new IndexSearcher(reader);

    	
    	StringBuffer result = new StringBuffer();
    	//result.append("Found " + scoreDocs.length + ".<br><br>");
    	
    	Document document = searcher.doc(t);


    	String rank = document.get("rank");
    	result.append("Rank: " + rank + "<br>");

    	String song = document.get("song");
    	result.append("Song: " + song + "<br>");

    	String artist = document.get("artist");
    	result.append("Artist: " + artist + "<br>");

    	String year = document.get("year");
    	result.append("Year: " + year + "<br>");
    		
    	String lyric = document.get("lyric");
    	result.append(lyric + "<br>");
    		
    	String source = document.get("source");
    	result.append("Sources: " + source + "<br>");
    	result.append("-------------------------------------------------------------------------<br>");
  
    	reader.close();

		return result;
	}

	private static IndexWriter getIndexWriter() throws Exception {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		final String INDEX_DIRECTORY = "D:\\Projects\\CS4315\\LuceneTest\\index";
		Directory index = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
	    
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter w = new IndexWriter(index, config);
		return w;
	}

	private static void  deleteIndex() throws Exception {
		IndexWriter indexWriter = getIndexWriter();
		indexWriter.deleteAll();
		indexWriter.close();
	}


	public static List<String> tokenizeString(Analyzer analyzer, String string) {
	    List<String> result = new ArrayList<String>();
	    try {
	      TokenStream stream  = analyzer.tokenStream(null, new StringReader(string));
	      stream.reset();
	      while (stream.incrementToken()) {
	        result.add(stream.getAttribute(CharTermAttribute.class).toString());
	      }
	    } catch (IOException e) {
	      // not thrown b/c we're using a string reader...
	      throw new RuntimeException(e);
	    }
	    return result;
	  }
}
