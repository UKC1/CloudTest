import React, {useEffect, useState} from 'react';
import useKakaoLoader from './useKakaoLoader';
import './PlaceSearch.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faLocationDot, faMagnifyingGlass} from "@fortawesome/free-solid-svg-icons";
import {useDebounce} from "../../hook/useDebounce";
const PlaceSearch = ({onLocationSelect}) => {
    const [inputText, setInputText] = useState('');
    const [places, setPlaces] = useState([]);
    const [title, setTitle] = useState('');

    useKakaoLoader();

    const debouncedInputText = useDebounce(inputText);


    const searchPlaces = (query) => {
        if (!query) {
            setPlaces([]);
            setTitle('');
            return;
        }
        if (window.kakao && window.kakao.maps && window.kakao.maps.services) {
            const ps = new window.kakao.maps.services.Places();
            ps.keywordSearch(inputText, (data, status) => {
                if (status === window.kakao.maps.services.Status.OK) {
                    setPlaces(data);
                    setTitle('장소결과');
                } else {
                    setPlaces([]);
                    setTitle('검색 결과가 없습니다.');
                    // alert('검색 결과가 없습니다.');
                }
            });
        } else {
            console.error("Kakao Maps libraries are not loaded yet.");
        }
    };

    useEffect(() => {
        searchPlaces(debouncedInputText);
    }, [debouncedInputText]);
    const handlePlaceClick = (place) => {
        const { y, x, place_name } = place;
        const locationInfo = {
            lat: parseFloat(y),
            lng: parseFloat(x),
            name: place_name
        };
        console.log("플레이스서치Selected location for MapView:", locationInfo);  // 로그 출력 추가
        setInputText(place_name);
        onLocationSelect(locationInfo);
    };

    const highlightText = (text, highlight) => {
        if (!highlight) return text;
        const parts = text.split(new RegExp(`(${highlight})`, 'gi'));
        return parts.map((part, index) =>
            part.toLowerCase() === highlight.toLowerCase() ? (
                <span key={index} className="highlight">
                    {part}
                </span>
            ) : (
                part
            )
        );
    };

    return (
        <div className="place-search-wrap">
            <div className="input-wrap">
                <input
                    type="text"
                    value={inputText}
                    onChange={e => setInputText(e.target.value)}
                    placeholder="장소를 검색해주세요"
                />
                <button onClick={searchPlaces}>
                    <FontAwesomeIcon className={'search'} icon={faMagnifyingGlass}/>
                </button>
            </div>
            <ul>
            <p className={'title'}>{title}</p>
            {places.map((place, index) => (
                    <li key={index}
                        onClick={() => handlePlaceClick(place)}
                        className={place.place_name.includes(inputText) && inputText !== '' ? 'matching-text' : ''}
                    >
                        <span className={'icon'}>
                            <FontAwesomeIcon className={'dot'} icon={faLocationDot}/>
                        </span>
                        <div className={'info'}>
                            <p>{highlightText(place.place_name, inputText)}</p>
                            <span className={'address'}>{place.address_name}</span>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default PlaceSearch;
