package com.mcc.fox.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.mcc.fox.model.ChatBot;

@Repository
public interface ChatBotRepository extends CrudRepository<ChatBot, Long>{

	public ChatBot findById(String id);
}
