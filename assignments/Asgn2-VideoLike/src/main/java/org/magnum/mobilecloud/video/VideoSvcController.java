/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.magnum.mobilecloud.video;

import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

import org.magnum.mobilecloud.exception.InvalidVideoDataException;
import org.magnum.mobilecloud.exception.VideoNotFoundException;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class VideoSvcController {
	
	public static final String TITLE_PARAMETER = "title";
	public static final String DURATION_PARAMETER = "duration";
	public static final String TOKEN_PATH = "/oauth/token";
	// The path where we expect the VideoSvc to live
	public static final String VIDEO_SVC_PATH = "/video";
	// The path to search videos by title
	public static final String VIDEO_TITLE_SEARCH_PATH = VIDEO_SVC_PATH + "/search/findByName";
	// The path to search videos by title
	public static final String VIDEO_DURATION_SEARCH_PATH = VIDEO_SVC_PATH + "/search/findByDurationLessThan";
	
	@Autowired
	private VideoRepository videos;

	@PreAuthorize("hasRole(user)")
	@RequestMapping(value=VIDEO_SVC_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Video> getVideoList() {
		Collection<Video> videoList = (Collection<Video>) videos.findAll();
		return videoList;
	}

	@PreAuthorize("hasRole(user)")
	@RequestMapping(value=VIDEO_SVC_PATH + "/{id}", method=RequestMethod.GET)
	public @ResponseBody Video getVideoById(@PathVariable("id") long id) {
		return videos.findOne(id);
	}

	@PreAuthorize("hasRole(user)")
	@RequestMapping(value=VIDEO_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody Video addVideo(@RequestBody Video v) {
		videos.save(v);
		return v;
	}

	@PreAuthorize("hasRole(user)")
	@RequestMapping(value=VIDEO_SVC_PATH + "/{id}/like", method=RequestMethod.POST)
	public @ResponseBody String likeVideo(@PathVariable("id") long id, Principal p) {
		if (!videos.exists(id))
			throw new VideoNotFoundException(id);
		else {
			Video v = videos.findOne(id);
			String username = p.getName();
			List<String> likedBy =  v.getLikedBy();
			if (likedBy.indexOf(username) == -1) {
				likedBy.add(username);
				v.setLikedBy(likedBy);
				v.setLikes(likedBy.size());
				videos.save(v);
			} else {
				throw new InvalidVideoDataException();
			}
		}
		return null;
	}

	@PreAuthorize("hasRole(user)")
	@RequestMapping(value=VIDEO_SVC_PATH + "/{id}/unlike", method=RequestMethod.POST) 
	public @ResponseBody String unlikeVideo(@PathVariable("id") long id, Principal p) {
		if (!videos.exists(id))
			throw new VideoNotFoundException(id);
		else {
			Video v = videos.findOne(id);
			String username = p.getName();
			List<String> likedBy =  v.getLikedBy();
			if (likedBy.indexOf(username) != -1) {
				likedBy.remove(username);
				v.setLikedBy(likedBy);
				v.setLikes(likedBy.size());
				videos.save(v);
			}
		}
		return null;
	}

	@PreAuthorize("hasRole(user)")
	@RequestMapping(value=VIDEO_TITLE_SEARCH_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Video> findByTitle(@RequestParam(TITLE_PARAMETER) String title) {
		return videos.findByName(title);
	}

	@PreAuthorize("hasRole(user)")
	@RequestMapping(value=VIDEO_DURATION_SEARCH_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Video> findByDurationLessThan(@RequestParam(DURATION_PARAMETER) long duration) {
		return videos.findByDurationLessThan(duration);
	}

	@PreAuthorize("hasRole(user)")
	@RequestMapping(value=VIDEO_SVC_PATH + "/{id}/likedby", method=RequestMethod.GET)
	public @ResponseBody Collection<String> getUsersWhoLikedVideo(@PathVariable("id") long id) {
		if (!videos.exists(id))
			throw new VideoNotFoundException(id);
		{
			Video v = videos.findOne(id);
			List<String> likedBy =  v.getLikedBy();
			return likedBy;
		}
	}
	
}
