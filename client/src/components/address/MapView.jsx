import React, {useEffect, useState} from 'react';
import useKakaoLoader from './useKakaoLoader';
import { Map, MapMarker } from "react-kakao-maps-sdk";

const MapView = ({ selectedLocation }) => {
    useKakaoLoader();
    const [mapCenter, setMapCenter] = useState({
        lat: 37.5665,
        lng: 126.9780
    });
    const [markerPosition, setMarkerPosition] = useState({
        lat: 37.5665, // 기본 마커 위치
        lng: 126.9780
    });

    // const [markerPosition, setMarkerPosition] = useState(null);

    // 선택된 위치가 변경될 때마다 지도 중심과 마커 위치 업데이트
    useEffect(() => {
        console.log('MapView에서 받은 selectedLocation:', selectedLocation); // 디버깅 로그 추가
        if (selectedLocation && 'lat' in selectedLocation && 'lng' in selectedLocation) {
            console.log('맵뷰Updating map center and marker:', selectedLocation);
            setMapCenter(selectedLocation);
            setMarkerPosition(selectedLocation);
        }else{
            console.error('안되는거야?Invalid location received:', selectedLocation);
        }
    }, [selectedLocation]);


    return (
        <Map
            center={mapCenter}
            style={{ width: "100%", height: "350px" }}
            level={3} // 지도 확대 레벨
        >
            {selectedLocation && 'lat' in selectedLocation && 'lng' in selectedLocation ? (
                <MapMarker
                    position={markerPosition}
                    image={{
                        src: "/img/locationdot.svg",
                        size: { width: 34, height: 39 },
                        options: { offset: { x: 17, y: 39 } }
                    }}
                />
            ): (
                <div>지도 위치를 가져올 수 없습니다.</div>
            )}
        </Map>
    );
};

export default MapView;

