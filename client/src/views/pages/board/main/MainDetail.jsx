import './MainDetail.scss';
import { Avatar } from "rsuite";
import React, {useEffect, useRef, useState} from "react";
import axios from "axios";
import {NavLink, useNavigate, useParams} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHeart} from "@fortawesome/free-regular-svg-icons";
import Caution from "./../../../../components/caution/Caution";
import CautionData from "./../../../../data/CautionData";
import MapView from "./../../../../components/address/MapView";
import {IconButton, SquareButton} from "./../../../../components/button/Button";
import {faAngleRight, faHeart as faSolidHeart} from "@fortawesome/free-solid-svg-icons";
import {useDispatch, useSelector} from "react-redux";
import {clearFood, setFood} from "./../../../../redux/foodSlice";
import { formatDistanceToNow, parseISO } from 'date-fns';
import { ko } from 'date-fns/locale';
import {jwtDecode} from "jwt-decode";
import {Swiper, SwiperSlide} from "swiper/react";



export default function MainDetail() {
    const {id} = useParams();
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const foodData = useSelector((state) => state.food.value);
    const [isFavorited, setIsFavorited] = useState(false);
    const [favoriteCount, setFavoriteCount] = useState(foodData.likes || 0);
    // const [selectedLocation, setSelectedLocation] = useState({ lat: 37.5665, lng: 126.9780, name: '' });
    const token = localStorage.getItem('jwt');
    const decoded = jwtDecode(token);
    const userId = decoded.mobileNumber;
    const selectedLocation = useSelector((state) => state.food.value.locationDetails);
    // const images = useSelector(state => state.food.value.images || []);
    const swiperRef = useRef(null);
    const [allImages, setAllImages] = useState([]);

    useEffect(() => {
        if(foodData.imageUrls && foodData.imageUrls.length > 0){
            setAllImages(foodData.imageUrls);
        }
        console.log("Selected location in MainDetail:", selectedLocation);
    }, [selectedLocation, foodData.imageUrls]);
    // const handleLocationSelect = (location) => {
    //     setSelectedLocation(location);
    // }
    const handleChat = () => {
        console.log("버튼 클릭 확인, 사용자 확인:", userId, "수혜자 확인:", foodData.giver.mobileNumber);
        if (window.confirm("사용자와 채팅하시겠습니까?")) {
            console.log("채팅 시작 확인, 방 생성 시도");
            axios.post('/api/chat/createRoom', {
                firstUserMobileNumber: foodData.giver.mobileNumber,
                secondUserMobileNumber: userId,
                foodId: foodData.foodId
            })
                .then(response => {
                    const newChatRoomId = response.data.urlIdentifier;
                    console.log("채팅방 ID:", newChatRoomId);
                    if (newChatRoomId) {
                        navigate(`/chat/GetChat/${newChatRoomId}`);
                    } else {
                        console.error("Chat room ID is not provided in the response");
                    }
                })
                .catch(error => {
                    console.error("Failed to create chat room:", error);
                });
        }
    };

    function formatTimeAgo(dateTimeStr) {
        if (!dateTimeStr) {
            console.error('Invalid or undefined datetime string');
            return '시간 정보 없음';
        }
        const dateTime = parseISO(dateTimeStr);  // ISO 문자열을 Date 객체로 변환
        return formatDistanceToNow(dateTime, { addSuffix: true, locale: ko });  // 현재 시간과의 차이 표시
    }

    const toggleFavorite = async () => {
        try {
            const method = isFavorited ? 'delete' : 'post'; // 현재 찜 상태에 따라 메서드 결정
            const response = await axios({
                method: method,
                url: `/api/favorites/${foodData.foodId}`,
                data: {
                    // userId: food.giver.userId, // 현재 로그인한 사용자 ID
                    isFavorite: !isFavorited
                },
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('jwt')}`
                }
            });
            setIsFavorited(!isFavorited); // 찜 상태 토글
            setFavoriteCount(prev => isFavorited ? prev - 1 : prev + 1); // 찜 횟수 조정
            console.log(isFavorited);
            console.log(response);

        } catch (error) {
            console.error('Error toggling favorite:', error);
        }
    };


    useEffect(()=>{
            const fetchFoodsData = async () => {
                console.log('푸드 아이디:', id);
                try{
                    const response = await axios.get(`/api/foods/${id}`);
                    console.log('받아온 데이터 로그에 출력:', response.data);
                    const foodWithLocation = {
                        ...response.data,
                        locationDetails: {
                            lat: response.data.latitude,
                            lng: response.data.longitude,
                            name: response.data.location
                        },
                        // images: response.data.imageUrls ? response.data.imageUrls.map(url => ({url, file: null })) : []
                    };
                    dispatch(setFood(foodWithLocation));
                    setIsFavorited(response.data.isFavorite);
                    setFavoriteCount(response.data.likes);
                }catch (error){
                    dispatch(clearFood());
                    console.log('Falied to fetch data:', error);
                }
            };
            fetchFoodsData();

            return () =>{
                dispatch(clearFood());
            }
    },[id, dispatch]);

    if (!foodData || !foodData.giver || !foodData.giver.mobileNumber) {
        return <div>Loading...</div>; // 데이터가 로드되기를 기다리는 동안 로딩 표시
    }

    return (
        <section className={'main_detail'}>
            <div className={'img_wrap'}>
                <Swiper
                    ref={swiperRef}
                    className="preview_swiper"
                    slidesPerView={'auto'}
                    spaceBetween={10}
                    centeredSlides={false}
                >
                    {allImages && allImages.map((url, index) => (
                        <SwiperSlide key={index}>
                            <div>
                                <img src={url} alt={`Preview ${index}`}/>
                            </div>
                        </SwiperSlide>
                    ))}
                </Swiper>
            </div>

            <div className={'content_wrap'}>
                <div className={'user_wrap'}>
                    <Avatar className={'avatar'} circle/>
                    <div className={'user_info'}>
                        <p className={'nick_name'}>{foodData.giver?.nickName || '닉네임 자리입니다만'}</p>
                        <p className={'location'}>{foodData.location}</p>
                    </div>
                </div>
                <div className={'title_wrap'}>
                    <h5>{foodData.title}</h5>
                    {foodData.createdAt && <p className={'date'}>{formatTimeAgo(foodData.createdAt)}</p>}
                </div>
                <div className={'dates_wrap'}>
                    <p><span>소비기한</span>{foodData.eatByDate}</p>
                </div>
                <div className={'description_wrap'}>
                    <p>{foodData.description}</p>
                </div>
                <div className={'caution_wrap'}>
                    <Caution items={CautionData}/>
                </div>
                <div className={'map_wrap'}>
                    <div className={'title_wrap'}>
                        <h6>나눔 희망장소</h6>
                        <FontAwesomeIcon icon={faAngleRight} />
                    </div>
                    {console.log("디테일 Passing selected location to MapView:", selectedLocation)}
                    <MapView selectedLocation={selectedLocation}/>
                </div>

                <div className={'actions_wrap'}>
                    <IconButton
                        icon={isFavorited ? faSolidHeart : faHeart}
                        onClick={toggleFavorite}
                        style={{ color: isFavorited ? 'red' : 'grey' }}
                    />
                    {/*<NavLink to={'/main'}>*/}
                    <SquareButton name={'채팅하기'} onClick={handleChat} />
                        {/*<ChatButton foodId={foodData.foodId} giverId={foodData.giver.mobileNumber} />*/}
                    {/*</NavLink>*/}
                </div>
            </div>
        </section>
    )
}