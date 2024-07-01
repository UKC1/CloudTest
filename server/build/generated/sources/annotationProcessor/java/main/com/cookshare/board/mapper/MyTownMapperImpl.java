package com.cookshare.board.mapper;

import com.cookshare.board.dto.MyTownDTO;
import com.cookshare.domain.Category;
import com.cookshare.domain.MyTown;
import com.cookshare.domain.MyTown.MyTownBuilder;
import com.cookshare.domain.MyTownImage;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-01T17:43:56+0900",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 17 (Oracle Corporation)"
)
@Component
public class MyTownMapperImpl implements MyTownMapper {

    @Override
    public MyTownDTO convertToMyTownDTO(MyTown myTown, List<MyTownImage> myTownImages, Category category) {
        if ( myTown == null && myTownImages == null && category == null ) {
            return null;
        }

        MyTownDTO myTownDTO = new MyTownDTO();

        if ( myTown != null ) {
            myTownDTO.setMyTownId( myTown.getMyTownId() );
            myTownDTO.setCreatedAt( timestampToLocalDateTime( myTown.getCreatedAt() ) );
            myTownDTO.setExpiresAt( timestampToLocalDateTime( myTown.getExpiresAt() ) );
            myTownDTO.setTitle( myTown.getTitle() );
            myTownDTO.setDescription( myTown.getDescription() );
            myTownDTO.setLocation( myTown.getLocation() );
            myTownDTO.setLatitude( myTown.getLatitude() );
            myTownDTO.setLongitude( myTown.getLongitude() );
            if ( myTown.getPrice() != null ) {
                myTownDTO.setPrice( myTown.getPrice() );
            }
            myTownDTO.setStatus( myTown.getStatus() );
            myTownDTO.setMaxTO( myTown.getMaxTO() );
        }
        if ( category != null ) {
            myTownDTO.setCategory( category.getName() );
        }
        myTownDTO.setImageUrls( mapImagePaths(myTownImages) );

        return myTownDTO;
    }

    @Override
    public MyTown convertToMyTown(MyTownDTO myTownDTO) {
        if ( myTownDTO == null ) {
            return null;
        }

        MyTownBuilder myTown = MyTown.builder();

        myTown.title( myTownDTO.getTitle() );
        myTown.description( myTownDTO.getDescription() );
        myTown.location( myTownDTO.getLocation() );
        myTown.latitude( myTownDTO.getLatitude() );
        myTown.longitude( myTownDTO.getLongitude() );
        myTown.price( myTownDTO.getPrice() );
        myTown.status( myTownDTO.getStatus() );
        myTown.maxTO( myTownDTO.getMaxTO() );

        myTown.createdAt( java.sql.Timestamp.from(java.time.Instant.now()) );
        myTown.expiresAt( java.sql.Timestamp.from(java.time.Instant.now()) );
        myTown.updatedAt( java.sql.Timestamp.from(java.time.Instant.now()) );

        return myTown.build();
    }
}
