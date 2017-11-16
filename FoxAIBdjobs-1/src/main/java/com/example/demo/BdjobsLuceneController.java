package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bdjobsai")
public class BdjobsLuceneController {

	LuceneService luceneService = new LuceneService(ConfigString.LUCENE_INDEX_DIR);
	
	@RequestMapping("/index")
	public String index() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();

        TypeReference type =  new TypeReference<ArrayList<ArrayList<String>>>() {
        };
        List<List<String>>data = mapper.readValue(new File(ConfigString.BDJOBS_QA_JSON_FILE_PATH),type);

        System.out.print("done");
        List<QAPair>qaPairs = new ArrayList<QAPair>();

        for (List<String>item: data) {
            QAPair qaPair = new QAPair();
            qaPair.answer = item.get(1);
            qaPair.question= item.get(0);
            qaPairs.add(qaPair);
        }

        //LuceneService luceneService = new LuceneService("/home/rifat/Desktop/lucene test/index");
        luceneService.constructIndex(qaPairs);
        return "done";
	}
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public String search(String question) {
		try {
			return luceneService.search(question);
		} catch (Exception e) {
			return "please specify your query";
		} 
	}
	
	
	
	
}
