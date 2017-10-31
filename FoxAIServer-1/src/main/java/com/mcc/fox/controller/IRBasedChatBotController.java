package com.mcc.fox.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mcc.fox.model.ChatBot;
import com.mcc.fox.request.model.ChatRequestModel;
import com.mcc.fox.request.model.TrainChatBotRequestModel;
import com.mcc.fox.service.ChatBotService;
import com.mcc.fox.utility.CustomConfigStrings;

@RestController
@RequestMapping("/irchatbot")
public class IRBasedChatBotController {

	@Autowired
	private ChatBotService chatBotService;// = new ChatBotService(chatBotRepository);
	
	
	@RequestMapping("/create-chatbot")
	public boolean createChatBot(String id) {
		ChatBot chatBot = new ChatBot();
		chatBot.id = id;
		chatBot.trainFilePath = CustomConfigStrings.BASE_PATH_OF_TRAIN_FILES+id+".json";
		return chatBotService.addChatBot(chatBot);
	}
	
	@RequestMapping(value ="/train-chatbot", method= RequestMethod.POST)
	public TrainChatBotRequestModel trainChatBot(@RequestBody TrainChatBotRequestModel trainChatBotRequestModel) {
		
		chatBotService.addTrainData(trainChatBotRequestModel.trainData, trainChatBotRequestModel.chatBotId);
		return trainChatBotRequestModel;
	}
	
	@RequestMapping("/chat")
	public String chat(@RequestBody ChatRequestModel chatRequestModel) {
		return chatBotService.getNextMessage(chatRequestModel);
	}
	
}
