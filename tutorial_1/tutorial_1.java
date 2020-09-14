package tutorial_1;
import org.lemurproject.galago.core.index.LengthsReader;
import org.lemurproject.galago.core.index.disk.DiskLengthsReader;
import org.lemurproject.galago.core.retrieval.Retrieval;
import org.lemurproject.galago.core.retrieval.RetrievalFactory;
import org.lemurproject.galago.core.retrieval.iterator.LengthsIterator;
import org.lemurproject.galago.core.retrieval.processing.ScoringContext;
import org.lemurproject.galago.core.retrieval.query.Node;
import org.lemurproject.galago.core.retrieval.query.StructuredQuery;
import org.lemurproject.galago.core.index.stats.FieldStatistics;
import org.lemurproject.galago.core.index.stats.NodeStatistics;
import org.lemurproject.galago.utility.Parameters;
import org.lemurproject.galago.core.parse.Document;
import org.lemurproject.galago.core.parse.Tag;
import java.util.Set;
import java.util.TreeSet;
import java.io.File;
public class tutorial_1 {
	
	public static void main( String[] args ) {
		try {
			
			String pathIndexBase = "D:\\school\\homework\\IR_TM\\tutorial_galago_install\\CS_6550_galago_tutorial\\index";  //path to index folder
			Retrieval retrieval = RetrievalFactory.instance(pathIndexBase, Parameters.create());
			
//			Extracting Document Statistics
	        Node n = new Node();
		    n.setOperator("lengths");
	        n.getNodeParameters().set("part", "lengths");
		    FieldStatistics stat = retrieval.getCollectionStatistics(n);   
		    long documentCount = stat.documentCount;
		    long maxLength = stat.maxLength;
		    long minLength = stat.minLength;
		    double avgLength = stat.avgLength;
		    long firstDocId = stat.firstDocId;
		    long lastDocId = stat.lastDocId;
		   
	        System.out.printf ("Max length       : %d \n", maxLength);
	        System.out.printf ("Min length       : %d \n", minLength);
	        System.out.printf ("Average length   : %f \n", avgLength);
	        System.out.printf ("Document Count   : %d \n", documentCount);
	        System.out.printf ("First Document ID: %d \n", firstDocId);
	        System.out.printf ("Last Document ID : %d \n", lastDocId); 		    

//			Extracting Document Statistics in text filed.	        
		    String field = "text";
		    Node fieldNode = StructuredQuery.parse( "#lengths:" + field + ":part=lengths()" );
		    FieldStatistics fieldStats = retrieval.getCollectionStatistics( fieldNode );   
		    double average_len= fieldStats.avgLength;
		    long max_length=fieldStats.maxLength;
		    long corpusLength = fieldStats.collectionLength;
		    long corpuscount=fieldStats.documentCount;		     
		    System.out.printf ("average length in text field   : %f \n", average_len);
		    System.out.printf ("corpus Length and corpus count in text field: %d %d \n", corpusLength ,corpuscount);
		    System.out.printf ("max length in text field   : %d \n", max_length);	        
	        
		    
//			Extracting Term Statistics.
            String query = "apple";
	        Node node = StructuredQuery.parse( "#text:" + query + ":part=field." + field + "()" );
	        node.getNodeParameters().set("queryType", "count");
	        node = retrieval.transformQuery(node, Parameters.create());

            NodeStatistics stat_node = retrieval.getNodeStatistics(node);
            long maxCount = stat_node.maximumCount;
            long nodeDocCount = stat_node.nodeDocumentCount;
            long nodeFrequency = stat_node.nodeFrequency;
	        System.out.println("Maximum Count      : " + maxCount);
	        System.out.println("Node Document Count: " + nodeDocCount);
	        System.out.println("Node Freq          : " + nodeFrequency);	        
	        
	        

		    
		    
// 			iterate to find the id of the document with the minimal document length. 		    
		    File fileLength = new File( new File( pathIndexBase ), "lengths" );
		    LengthsReader indexLength = new DiskLengthsReader( fileLength.getAbsolutePath() );
		    LengthsIterator iterator = (LengthsIterator) indexLength.getIterator( fieldNode );
			ScoringContext sc = new ScoringContext();
			int length_min=100000;
			long id_min=sc.document;
			String docno_min="";
			while ( !iterator.isDone() ) {
				sc.document = iterator.currentCandidate();
				String docno = retrieval.getDocumentName( (long) sc.document );
				int length = iterator.length( sc );
				if (length<=length_min)
				{length_min=length;
				docno_min=docno;
				id_min=sc.document;
				}
				iterator.movePast( iterator.currentCandidate() );
			}
			System.out.printf( "min_length document inner id,docno, length   : %-10d%-15s%-10d\n", id_min, docno_min, length_min);
			indexLength.close();
			
			
// 			iterate to find the id of the document with the minimal document length. 			
			Document.DocumentComponents dc = new Document.DocumentComponents( false, false, true );
			Set<String> unique_vocab=new TreeSet<>();
			for(long docid=stat.firstDocId;docid<=stat.lastDocId;docid++) {
				Document doc = retrieval.getDocument( retrieval.getDocumentName( docid ), dc );
				for ( Tag tag : doc.tags ) { // doc.tags return a list of document fields
					if ( tag.name.equals( field ) ) { // iteratively checking and find the field you hope to access
						// keep a copy of the start and end position of the field in the document.
						for ( int position = tag.begin; position < tag.end; position++ ) {
							String term1 = doc.terms.get( position );
							if (term1.length()<10) {
							unique_vocab.add(term1);}	
						}
					}
				}				
			}
			System.out.printf ("number of unique words with length less than 10: %d \n", unique_vocab.size());

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
}