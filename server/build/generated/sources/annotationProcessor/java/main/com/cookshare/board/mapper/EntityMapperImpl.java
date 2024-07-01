package com.cookshare.board.mapper;

import com.cookshare.board.dto.FoodDTO;
import com.cookshare.domain.Category;
import com.cookshare.domain.FavoriteFood;
import com.cookshare.domain.Food;
import com.cookshare.domain.FoodImage;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-01T17:43:56+0900",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 17 (Oracle Corporation)"
)
@Component
public class EntityMapperImpl implements EntityMapper {

    @Override
    public FoodDTO convertToFoodDTO(Food food, List<FoodImage> foodImages, Category category, FavoriteFood favoriteFood) {
        if ( food == null && foodImages == null && category == null && favoriteFood == null ) {
            return null;
        }

        FoodDTO foodDTO = new FoodDTO();

        if ( food != null ) {
            foodDTO.setFoodId( food.getFoodId() );
            foodDTO.setMakeByDate( timestampToLocalDate( food.getMakeByDate() ) );
            foodDTO.setEatByDate( timestampToLocalDate( food.getEatByDate() ) );
            foodDTO.setCreatedAt( timestampToLocalDateTime( food.getCreatedAt() ) );
            foodDTO.setTitle( food.getTitle() );
            foodDTO.setDescription( food.getDescription() );
            foodDTO.setGiver( food.getGiver() );
            foodDTO.setLocation( food.getLocation() );
            foodDTO.setLatitude( food.getLatitude() );
            foodDTO.setLongitude( food.getLongitude() );
            foodDTO.setLikes( food.getLikes() );
            foodDTO.setStatus( food.getStatus() );
            foodDTO.setReceiver( food.getReceiver() );
        }
        if ( category != null ) {
            foodDTO.setCategory( category.getName() );
        }
        if ( favoriteFood != null ) {
            foodDTO.setIsFavorite( favoriteFood.getIsFavorite() );
        }
        foodDTO.setImageUrls( mapImagePaths(foodImages) );

        return foodDTO;
    }

    @Override
    public Food convertToFood(FoodDTO foodDTO) {
        if ( foodDTO == null ) {
            return null;
        }

        Food food = new Food();

        food.setLocation( foodDTO.getLocation() );
        food.setLatitude( foodDTO.getLatitude() );
        food.setLongitude( foodDTO.getLongitude() );
        food.setGiver( foodDTO.getGiver() );
        food.setMakeByDate( localDateToTimestamp( foodDTO.getMakeByDate() ) );
        food.setEatByDate( localDateToTimestamp( foodDTO.getEatByDate() ) );
        food.setStatus( foodDTO.getStatus() );
        food.setTitle( foodDTO.getTitle() );
        food.setDescription( foodDTO.getDescription() );

        food.setLikes( 0 );
        food.setCreatedAt( java.sql.Timestamp.from(java.time.Instant.now()) );
        food.setUpdatedAt( java.sql.Timestamp.from(java.time.Instant.now()) );

        return food;
    }
}
