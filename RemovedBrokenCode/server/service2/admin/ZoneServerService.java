package ru.imagebook.server.service2.admin;

import org.springframework.web.multipart.MultipartFile;

public interface ZoneServerService {
	void uploadAndParseFile(MultipartFile file);
}
