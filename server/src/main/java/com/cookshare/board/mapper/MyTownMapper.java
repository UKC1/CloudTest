package com.cookshare.board.mapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.cookshare.board.dto.MyTownDTO;
import com.cookshare.domain.Category;
import com.cookshare.domain.MyTown;
import com.cookshare.domain.MyTownImage;

@Mapper(componentModel = "spring", implementationName = "MyTownMapperImpl")
public interface MyTownMapper {

	@Mapping(target = "myTownId", source = "myTown.myTownId")
	@Mapping(target = "imageUrls", expression = "java(mapImagePaths(myTownImages))")
	@Mapping(target = "category", source = "category.name")
	@Mapping(target = "createdAt", source = "myTown.createdAt", qualifiedByName = "timestampToLocalDateTime")
	@Mapping(target = "expiresAt", source = "myTown.expiresAt", qualifiedByName = "timestampToLocalDateTime")
	@Mapping(target = "title", source = "myTown.title")
	@Mapping(target = "description", source = "myTown.description")
	@Mapping(target = "location", source = "myTown.location")
	@Mapping(target = "latitude", source = "myTown.latitude")
	@Mapping(target = "longitude", source = "myTown.longitude")
	MyTownDTO convertToMyTownDTO(MyTown myTown, List<MyTownImage> myTownImages, Category category);

	default List<String> mapImagePaths(List<MyTownImage> myTownImages) {
		if (myTownImages == null) return Collections.emptyList();
		return myTownImages.stream()
			.flatMap(image -> image.getImagePaths().stream())
			.collect(Collectors.toList());
	}

	@Named("timestampToLocalDate")
	default LocalDate timestampToLocalDate(Timestamp timestamp) {
		return timestamp != null ? timestamp.toLocalDateTime().toLocalDate() : null;
	}

	@Mapping(target = "myTownId", ignore = true, source = "myTownDTO.myTownId")
	@Mapping(target = "title", source = "myTownDTO.title")
	@Mapping(target = "description", source = "myTownDTO.description")
	@Mapping(target = "location", source = "myTownDTO.location")
	@Mapping(target = "latitude", source = "myTownDTO.latitude")
	@Mapping(target = "longitude", source = "myTownDTO.longitude")
	@Mapping(target = "price", source = "myTownDTO.price")
	@Mapping(target = "createdAt", expression = "java(java.sql.Timestamp.from(java.time.Instant.now()))")
	@Mapping(target = "expiresAt", expression = "java(java.sql.Timestamp.from(java.time.Instant.now()))")
	@Mapping(target = "updatedAt", expression = "java(java.sql.Timestamp.from(java.time.Instant.now()))")
	@Mapping(target = "category", ignore = true)
	MyTown convertToMyTown(MyTownDTO myTownDTO);

	@Named("timestampToLocalDateTime")
	default LocalDateTime timestampToLocalDateTime(Timestamp timestamp) {
		return timestamp != null ? timestamp.toLocalDateTime() : null;
	}
	@Named("localDateToTimestamp")
	default Timestamp localDateToTimestamp(LocalDate date) {
		return date == null ? null : Timestamp.valueOf(date.atStartOfDay());
	}

	@Named("convertToMyTownImage")
	default MyTownImage convertToMyTownImage(MyTownDTO MyTownDTO, MyTown MyTown) {
		if (MyTownDTO.getImageUrls() == null) return null;
		return MyTownImage.builder()
			.imagePaths(MyTownDTO.getImageUrls())
			.myTown(MyTown)
			.createdAt(Timestamp.from(Instant.now()))
			.updatedAt(Timestamp.from(Instant.now()))
			.build();
	}

	@Named("convertToCategory")
	default Category convertToCategory(String categoryName) {
		Category category;
		category = Category.builder()
			.name(categoryName)
			.createdAt(Timestamp.from(Instant.now()))
			.updatedAt(Timestamp.from(Instant.now()))
			.build();
		return category;
	}
}
