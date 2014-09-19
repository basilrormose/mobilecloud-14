//package org.magnum.mobilecloud.video.repository;
//
//import javax.persistence.*;
//
//import com.google.common.base.Objects;
//
//@Entity
//public class LikedBy {
//	
//	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
//	private long id;
//	
//	private long videoId;
//	private String username;
//	
//	public LikedBy() {
//	}
//	
//	public LikedBy(long videoId, String username) {
//		super();
//		this.videoId = videoId;
//		this.username = username;
//	}
//	
//	public long getId() {
//		return this.id;
//	}
//	
//	public void setId(long id) {
//		this.id = id;
//	}
//	
//	public long getVideoId() {
//		return videoId;
//	}
//
//	public void setVideoId(long videoId) {
//		this.videoId = videoId;
//	}
//	
//	public String getUsername() {
//		return username;
//	}
//	
//	public void setUsername(String username) {
//		this.username = username; 
//	}
//	
//	@Override
//	public int hashCode() {
//		// Google Guava provides great utilities for hashing
//		return Objects.hashCode(videoId, username);
//	}
//	
//	@Override
//	public boolean equals(Object obj) {
//		if (obj instanceof LikedBy) {
//			LikedBy other = (LikedBy) obj;
//			// Google Guava provides great utilities for equals too!
//			return Objects.equal(videoId, other.videoId)
//					&& Objects.equal(username, other.username);
//		} else {
//			return false;
//		}
//	}
//}
