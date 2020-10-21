package java_tutorial_2;
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
import org.lemurproject.galago.core.retrieval.Retrieval;
//import org.lemurproject.galago.core.retrieval.RetrievalFactory;
import org.lemurproject.galago.core.retrieval.ScoredDocument;
import org.lemurproject.galago.core.retrieval.query.Node;
import org.lemurproject.galago.core.retrieval.query.StructuredQuery;
import org.lemurproject.galago.core.index.disk.DiskIndex;
import org.lemurproject.galago.core.index.IndexPartReader;
import org.lemurproject.galago.core.index.KeyIterator;
import org.lemurproject.galago.core.index.IndexPartReader;
import org.lemurproject.galago.core.index.KeyIterator;
import org.lemurproject.galago.core.index.disk.DiskIndex;
import org.lemurproject.galago.core.retrieval.iterator.CountIterator;
import org.lemurproject.galago.core.retrieval.processing.ScoringContext;
import org.lemurproject.galago.utility.ByteUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.TreeSet;
import java.io.File;
public class Task1 {
    public static void main(String[] args) throws Exception {

        // create a search engine based on the params file.
        String jsonConfigFile = "src\\java_tutorial_2\\search.params";
        Parameters globalParams = Parameters.parseFile(jsonConfigFile);
		String pathIndexBase = "D:\\school\\homework\\IR_TM\\tutorial_galago_install\\CS_6550_galago_tutorial\\index\\";
		 Retrieval retrieval = RetrievalFactory.instance(pathIndexBase, Parameters.create());
//        org.lemurproject.galago.core.retrieval.Retrieval retrieval = RetrievalFactory.instance(globalParams);

        // load queries from file
        String queryFile = globalParams.getAsString("queryFile");
//        String rankFile = globalParams.getAsString("rankFile");
        String [] models = {
//                "#combine:part=postings.krovetz()", // QL with stemming
                "postings" //QL without stemming
//                "#combine:", //QL without stemming
        };
        String [] smoothingMethods = {
            "#dirichlet:mu=2000:",
//            "#jm:lambda=0.5:",
//            "#jm:lambda=1.0"
        };

        // run the queries
        int d=0;
        for (String model : models){
            for (String smoothing: smoothingMethods){
            	d+=1;
                BufferedReader reader = new BufferedReader(new FileReader(queryFile));
               
//                BufferedWriter writer = new BufferedWriter(new FileWriter(rankFile + model + smoothing));
                BufferedWriter writer = new BufferedWriter(new FileWriter("txt"+d+".txt"));
                String line;
                while ((line = reader.readLine()) != null) {
                    String [] elems = line.split("\t");
                    String qid = elems[0];
                    String query = elems[1];
                    runQuery(globalParams, qid, query, retrieval, writer, model, smoothing, pathIndexBase);
                }
//                System.out.println("finished ");
//                System.out.println(model+smoothing);
                reader.close();
                writer.close();
            }
        }
    }
    private static void count_word(String pathIndexBase, String term, String model,String docno)throws Exception {
		 File pathPosting = new File( new File( pathIndexBase ), model);
		 DiskIndex index = new DiskIndex( pathIndexBase );
		 IndexPartReader posting = DiskIndex.openIndexPart( pathPosting.getAbsolutePath() );
		 KeyIterator vocabulary = posting.getIterator();
//		 System.out.println(term);
		 if ( vocabulary.skipToKey( ByteUtil.fromString( term ) ) && term.equals( vocabulary.getKeyString() ) ) {
			    // get an iterator for the term's posting list
			    CountIterator iterator = (CountIterator) vocabulary.getValueIterator();
			    ScoringContext sc = new ScoringContext();
			    
			    while ( !iterator.isDone() ) {
			        // Get the current entry's document id.
			        // Note that you need to assign the value of sc.document,
			        // otherwise count(sc) and others will not work correctly.
			        sc.document = iterator.currentCandidate();
			        
			        String docno_cur = index.getName( sc.document ); // get the docno (external ID) of the current document
//			        System.out.println(docno_cur);
			        if (docno_cur.equals(docno)){
			        int lenghth=index.getLength(sc.document);
			        int freq = iterator.count( sc ); // get the frequency of the term in the current document
			        System.out.printf( "%-20s%-15s%-15s%-10s\n", term, lenghth, docno, freq );
//			        System.out.println(lenghth);
//			        System.out.printf( lenghth);
			       break;
			    }
			    iterator.movePast( iterator.currentCandidate() ); }// jump to the entry right after the current one}
			}		 
			posting.close();
			index.close();}
    private static void runQuery(Parameters p, String qid, String query, Retrieval retrieval, BufferedWriter writer, String model, String smoothing,String pathIndexBase) throws Exception {
        //Parameters p = new Parameters();
        //p.set("startAt", 0); // set the start point for document retrieval. 0 means retrieving from all documents.
        p.set("requested", 2); // set the maximum number of document retrieved for each query.
        //        p.set(key, value);
        p.set("scorer", "jm");
        p.set("lambda", 0.5);

        String query_orig=query;
        if (model.length() > 0) {
            if (smoothing.length() > 0) {
                String[] terms = query.split(" ");
                query = "";
                for (String t : terms) {
                    query +="#extents:part="+model+":"+ t + "() ";
                }
//                System.out.println(query+"*******");
            }
            query = "#combine" + "(" + query + ")"; // apply the retrieval model to the query if exists
//            System.out.println(query+"*******1");
        }
        Node root = StructuredQuery.parse(query);       // turn the query string into a query tree
        System.out.println(root.toString());
        Node transformed = retrieval.transformQuery(root, p);  // apply traversals
        System.out.println(transformed.toString());
        List<ScoredDocument> results = retrieval.executeQuery(transformed, p).scoredDocuments; // issue the query!
        System.out.println("****************");
        for(ScoredDocument sd:results){ // print results

        	String docno=sd.getName();
//        	long docid=retrieval.getDocumentId( docno );
//        	Document.DocumentComponents dc = new Document.DocumentComponents( false, false, true );
//        	Document doc = retrieval.getDocument( docno, dc );
//        	System.out.println(doc.);
        	String[] terms = query_orig.split(" ");
        	System.out.println("*************");
//        	System.out.println(sd.score);
        	System.out.println(sd.toString());
        	System.out.printf( "%-20s%-15s%-15s%-10s\n", "Word","DOC_Length", "DOCNO", "FREQ" );
        	for (String t : terms){
        	count_word(pathIndexBase,t,model,docno);
        	}
        	System.out.println("*************");
            writer.write(sd.toTRECformat(qid));
            writer.write("\n");
        }

    }
}

























