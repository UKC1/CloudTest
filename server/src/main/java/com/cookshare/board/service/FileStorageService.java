package com.cookshare.board.service;

import com.cookshare.board.exception.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageService {

	@Value("${file.upload-dir}")
	private String uploadDir;
	private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);

	public List<String> storeFiles(List<MultipartFile> files) throws FileStorageException {
		List<String> urls = new ArrayList<>();
		for (MultipartFile file : files) {
			String fileName = StringUtils.cleanPath(System.currentTimeMillis() + "_" + file.getOriginalFilename());
			Path targetLocation = Paths.get(uploadDir).resolve(fileName);

			try {
				Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
				log.info("File stored successfully: {}", fileName);
				urls.add(createFileDownloadUri(fileName));
			} catch (IOException ex) {
				log.error("Could not store file {}: {}", fileName, ex.getMessage());
				throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
			}
		}
		return urls;
	}

	private String createFileDownloadUri(String fileName) {
		return ServletUriComponentsBuilder.fromCurrentContextPath()
			.path("/download/")
			.path(fileName)
			.toUriString();
	}
}