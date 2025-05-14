package com.academyx.batch.dto;

public class UserDTO {

	 private Long userId;
	    private String name;

	    public UserDTO(Long userId, String name) {
	        this.userId = userId;
	        this.name = name;
	    }

	    // Getters and Setters
	    public Long getUserId() {
	        return userId;
	    }

	    public void setUserId(Long userId) {
	        this.userId = userId;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }
}
