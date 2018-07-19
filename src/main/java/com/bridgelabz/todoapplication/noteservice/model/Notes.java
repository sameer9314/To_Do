package com.bridgelabz.todoapplication.noteservice.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import io.swagger.annotations.ApiModelProperty;

@Document(collection = "notes")
@Service
public class Notes implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String title;
	private String description;
	@ApiModelProperty(hidden = true)
	private String creationDate;
	@ApiModelProperty(hidden = true)
	private String userId;
	@ApiModelProperty(hidden = true)
	private String lastModified;
	
	private String archive;
	private List<String> label;
	private String pinned;
	
	public String getPinned() {
		return pinned;
	}

	public void setPinned(String pinned) {
		this.pinned = pinned;
	}

	public String getArchive() {
		return archive;
	}

	public void setArchive(String archive) {
		this.archive = archive;
	}

//	public String getLabel() {
//		return label;
//	}
//
//	public void setLabel(String label) {
//		this.label = label;
//	}
	
	public Notes() {
		super();
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Notes [id=" + id + ", title=" + title + ", description=" + description + ", creationDate="
				+ creationDate + ", userId=" + userId + ", lastModified=" + lastModified + ", archive=" + archive
				+ ", label=" + label + ", pinned=" + pinned + "]";
	}

	

	
}
