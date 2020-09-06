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
			
			String pathIndexBase = "D:\\school\\homework\\IR_TM\\tutorial_galago_install\\CS_6550_galago_tutorial\\index";
			Retrieval retrieval = RetrievalFactory.instance( pathIndexBase, Parameters.create() );
			Node n = new Node();
			n.setOperator( "lengths" );
			n.getNodeParameters().set( "part", "lengths" );
			
			FieldStatistics stats = retrieval.getCollectionStatistics( n );
			
			System.out.printf( "%-15s%-20s\n", "Internal ID", "External ID (docno)" );
			Set<String> docnos = new TreeSet<>();
			
			// Now we iteratively read and print out the internal and external IDs for documents in the index.
			for ( long docid = stats.firstDocId; docid <= stats.lastDocId; docid++ ) {
				String docno = retrieval.getDocumentName( (long) docid );
				docnos.add( docno );
				System.out.printf( "%-15d%-20s\n", docid, docno );
			}
			
			// We can locate a document's internal ID by its external ID (docno) as well.
			// The following loop iterates through the docnos of documents and find their internal IDs.
			System.out.printf( "%-20s%-15s\n", "External ID (docno)", "Internal ID" );
			for ( String docno : docnos ) {
				long docid = retrieval.getDocumentId( docno );
				System.out.printf( "%-20s%-15d\n", docno, docid );
			}
			
			retrieval.close();

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
}