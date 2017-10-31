package com.mcc.fox.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ChatBot {

	@Id
	public String id;
	public String trainFilePath;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTrainFilePath() {
		return trainFilePath;
	}
	public void setTrainFilePath(String trainFilePath) {
		this.trainFilePath = trainFilePath;
	}
	
	
}
