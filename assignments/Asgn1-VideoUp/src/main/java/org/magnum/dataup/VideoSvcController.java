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
package org.magnum.dataup;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletResponse;

import org.magnum.dataup.exception.*;
import org.magnum.dataup.exception.VideoNotFoundException;
import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.magnum.dataup.model.VideoStatus.VideoState;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import retrofit.client.Response;
import retrofit.mime.TypedFile;

@Controller
public class VideoSvcController {

	private ConcurrentHashMap<String, Video> videos = new ConcurrentHashMap<String, Video>();
	
	@RequestMapping(value=Constants.VIDEO_SVC_PATH, method=RequestMethod.GET)
	public @ResponseBody Collection<Video> getVideoList() {
		return (Collection<Video>) videos.values();
	}

	@RequestMapping(value=Constants.VIDEO_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody Video addVideo(@RequestBody Video v) {
		if (v == null || 
			(v.getTitle() == null || v.getTitle().length() == 0) ) {
			throw new InvalidVideoDataException();
		} else { 
			MaxVideoId.getInstance();
			long id = MaxVideoId.getMaxId();
			v.setId(id);
			String key = Long.toString(id);
			v.setDataUrl(Constants.VIDEO_SVC_PATH + "/" + key + "/data");
			videos.put(key, v); 
			return v;
		}
	}

	@RequestMapping(value=Constants.VIDEO_DATA_PATH, method=RequestMethod.POST)
	public @ResponseBody VideoStatus setVideoData(@PathVariable("id") long id, @RequestPart(Constants.DATA_PARAMETER) MultipartFile videoData) {
		if (!videoData.isEmpty()) {
			String key = Long.toString(id);
			if (videos.containsKey(key)) {
				Video video = videos.get(key);
				try (InputStream in = videoData.getInputStream()) {
					VideoFileManager videoFileMgr = VideoFileManager.get();
					videoFileMgr.saveVideoData(video, in);
					return new VideoStatus(VideoState.READY);
				} catch (IOException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			} else 
				// Not found HTTP error
				throw new VideoNotFoundException(id);
		} else 
			// Empty video data
			throw new InvalidVideoDataException();
		return null;
	}

	@RequestMapping(value=Constants.VIDEO_DATA_PATH, method=RequestMethod.GET)
	public void getData(@PathVariable("id") long id, HttpServletResponse response) {
		String key = Long.toString(id);
		if (videos.containsKey(key)) {
			try {
				Video video = videos.get(key);
				VideoFileManager videoFileMgr = VideoFileManager.get();
				if (videoFileMgr.hasVideoData(video)) {
			        response.setContentType(video.getContentType());
					videoFileMgr.copyVideoData(video, response.getOutputStream());
					response.flushBuffer();
				} else
					// No video data
					throw new VideoNotFoundException(id);
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				throw new InvalidVideoDataException();
			}
		} else
			// Not found HTTP error
			throw new VideoNotFoundException(id);
	}


}
