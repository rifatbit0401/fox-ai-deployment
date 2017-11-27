package com.example.demo;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LuceneService {

    private String indexDir;

    public LuceneService(String indexDir){
        this.indexDir = indexDir;
    }

    public void constructIndex(List<QAPair>qaPairList) throws IOException {

        FSDirectory directory = FSDirectory.open(new File(indexDir));
        IndexWriter indexWriter = new IndexWriter(directory, new StandardAnalyzer(Version.LUCENE_30),true, IndexWriter.MaxFieldLength.UNLIMITED);
        for (QAPair qaPair : qaPairList) {
            Document document = new Document();
            Field questionField = new Field("question", qaPair.question, Field.Store.YES, Field.Index.ANALYZED);
            Field answerField = new Field("answer", qaPair.answer, Field.Store.YES, Field.Index.ANALYZED);

            document.add(questionField);
            document.add(answerField);
            indexWriter.addDocument(document);
        }

        indexWriter.optimize();
        indexWriter.close();
        directory.close();

    }

    public String search(String queryStr) throws IOException, ParseException {
        FSDirectory directory = FSDirectory.open(new File(indexDir));
        IndexSearcher indexSearcher = new IndexSearcher(directory);
        QueryParser queryParser = new QueryParser(Version.LUCENE_30, "question",
                new StandardAnalyzer(Version.LUCENE_30));
        Query query = queryParser.parse(queryStr);
        TopDocs hits = indexSearcher.search(query, 1000000);
        return indexSearcher.doc(hits.scoreDocs[0].doc).get("answer");

    }


}
