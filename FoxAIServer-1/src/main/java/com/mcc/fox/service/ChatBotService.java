package com.mcc.fox.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcc.fox.model.ChatBot;
import com.mcc.fox.repository.ChatBotRepository;
import com.mcc.fox.request.model.ChatRequestModel;
import com.mcc.rifat.ir.Document;
import com.mcc.rifat.ir.SearchEngine;
import com.mcc.rifat.model.ConversationJsonModel;
import com.mcc.rifat.utility.JsonService;


@Service
public class ChatBotService {

	@Autowired
	private ChatBotRepository chatBotRepository;
	

	public boolean addChatBot(ChatBot chatBot) {
		File trainFile = new File(chatBot.trainFilePath);
		JsonService jsonService = new JsonService();
		ConversationJsonModel conversationJsonModel = new ConversationJsonModel();
		jsonService.writeJsonFile(trainFile, conversationJsonModel);
		
		chatBot = chatBotRepository.save(chatBot);
		if ( chatBot != null) {
			return true;
		} else {
			return false;
		}
	}

	public ChatBot getChatBot(String id) {
		return chatBotRepository.findById(id);
	}
	
	public void addTrainData(ArrayList<ArrayList<String>> trainData, String chatBotId) {
		ChatBot chatBot = getChatBot(chatBotId);
		
		JsonService jsonService = new JsonService();
		ConversationJsonModel  conversationJsonModel = jsonService.readConversaionJsonFile(new File(chatBot.trainFilePath));
		
		for (ArrayList<String> conversation : trainData) {
			conversationJsonModel.conversations.add(conversation);
		}
		jsonService.writeJsonFile(new File(chatBot.trainFilePath), conversationJsonModel);
	}
	
	
	public String getNextMessage(ChatRequestModel chatRequestModel) {
		
		ChatBot chatBot = getChatBot(chatRequestModel.chatBotId);
		JsonService jsonService = new JsonService();
		ConversationJsonModel conversationJsonModel = jsonService.readConversaionJsonFile(new File(chatBot.trainFilePath));
		SearchEngine searchEngine = new SearchEngine(conversationJsonModel);
		
		Document queryDoc = new Document(chatRequestModel.dialogues);
		Document relevantDoc = searchEngine.search(queryDoc, 0.80);
		return relevantDoc.getRawSentences().get(chatRequestModel.dialogues.size());
	}
	
}
