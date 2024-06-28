import React, {useCallback, useEffect, useRef, useState} from 'react';
import axios from 'axios';
import {useLocation, useNavigate} from "react-router-dom";
import './MainForm.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faAngleRight, faCamera, faXmark} from "@fortawesome/free-solid-svg-icons";
import {Swiper, SwiperSlide} from "swiper/react";
import {faCircleXmark} from "@fortawesome/free-solid-svg-icons/faCircleXmark";
import {DatePicker} from "rsuite";
import {FaCalendarDay} from 'react-icons/fa';
import {format} from "date-fns";
import {SquareButton} from "./../../../../components/button/Button";
import Select from "./../../../../components/select/Select";
import Drawers from "./../../../../components/drawer/Drawers";
import {useDispatch, useSelector} from "react-redux";
import {addImage, clearFood, removeImage, removeImageUrl, setFood, setLocationDetails} from "./../../../../redux/foodSlice";
import PlaceSearch from "./../../../../components/address/PlaceSearch";


const MainForm = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const { state }  = useLocation();
    const swiperRef = useRef(null);
    const foodData = useSelector(state => state.food.value)
    const initialData = state?.foodData || foodData;
    const images = useSelector(state => state.food.value.images || []);
    const [errors, setErrors] = useState({});
    const [selectedLocation, setSelectedLocation] = useState(null);
    const [allImages, setAllImages] = useState([]); // 통합 이미지 배열
    const maxImageCount = 8;
    useEffect(() => {
        if (initialData) {
            dispatch(setFood(initialData));
        }

        setAllImages([
            ...(initialData.imageUrls || []).map((url, index) => ({ url, file: null, source: 'url', originalIndex: index })),
            ...(initialData.images || []).map((img, index) => ({ ...img, source: 'new', originalIndex: index }))
        ]);
        const newFoodData = {
            ...initialData,
            images: [], // 새 이미지 파일 데이터용 배열 초기화
            imageUrls: initialData.imageUrls || [] // 기존 이미지 URL 데이터
            // images: initialData.imageUrls ? initialData.imageUrls.map(url => ({url, file: null})) : []
        };

        dispatch(setFood(newFoodData));
    },[dispatch]);

    const handleChange = useCallback((e) => {
        const {name, value} = e.target;
        dispatch(setFood({...foodData, [name]: value}));
    },[dispatch, foodData]);
    // const handleImageChange = (e) => {
    //     if (e.target.files) {
    //         const fileImages = Array.from(e.target.files).map(file => ({
    //             file,
    //             url: URL.createObjectURL(file),
    //         }));
    //         fileImages.forEach(image =>{
    //             dispatch(addImage(image));
    //         });
    //     }
    // };
    const handleImageChange = (e) => {
        if (e.target.files) {
            const newFiles = Array.from(e.target.files).map(file => ({
                file,
                url: URL.createObjectURL(file),
                source: 'new'
            }));

            // 이미지 개수 제한 검사
            if (allImages.length + newFiles.length > maxImageCount) {
                alert(`최대 ${maxImageCount}개의 이미지만 업로드 가능합니다.`);
                return;
            }

            // 중복 이미지 검사 및 추가
            const filteredFiles = newFiles.filter(newFile => {
                return !allImages.some(image => image.url === newFile.url);
            });

            if (filteredFiles.length === 0) {
                alert("이미 선택된 이미지입니다.");
                return;
            }

            setAllImages(prev => [...prev, ...filteredFiles]);
            filteredFiles.forEach(image => {
                dispatch(addImage(image));
            });

            // 입력 필드 리셋
            e.target.value = '';
        }
    };

    // const handleRemoveImage = (index) => {
    //     dispatch(removeImage(index));
    //     if(swiperRef.current && swiperRef.current.swiper) {
    //         swiperRef.current.swiper.update();
    //     }
    // };
    const handleRemoveImage = (index) => {
        const imageToRemove = allImages[index];
        setAllImages(prev => prev.filter((_, i) => i !== index));

        if (imageToRemove.source === 'new') {
            const newImagesIndex = foodData.images.findIndex(img => img.identifier === imageToRemove.identifier);
            if (newImagesIndex !== -1) dispatch(removeImage(newImagesIndex));
        } else if (imageToRemove.source === 'url') {
            const imageUrlIndex = foodData.imageUrls.findIndex(url => url === imageToRemove.url);
            if (imageUrlIndex !== -1) dispatch(removeImageUrl(imageUrlIndex));
        }

        if (swiperRef.current && swiperRef.current.swiper) {
            swiperRef.current.swiper.update();
        }
    };
    const handleDateChange = (name, dateValue) => {
        const formattedDate = dateValue ? format(dateValue, 'yyyy-MM-dd') : '';
        dispatch(setFood({ ...foodData, [name]: formattedDate }));
    };

    const handleLocationSelect = (locationInfo) => {
        console.log('MainForm에서 선택된 장소:', locationInfo);
        dispatch(setLocationDetails(locationInfo)); // 전체 위치 정보 저장
        dispatch(setFood({ ...foodData, location: locationInfo.name, locationDetails: locationInfo })); // 위치 이름과 위치 정보를 모두 저장
    };

    const validateForm = () => {
        const newErrors = {};
        if (!foodData.title) newErrors.title = '제목을 입력해주세요.';
        if (!foodData.description) newErrors.description = '내용을 입력해주세요';
        if (!foodData.eatByDate) newErrors.eatByDate = '소비기한을 입력해주세요';
        if (!foodData.makeByDate) newErrors.makeByDate = '제조일날짜을 입력해주세요';
        if (!foodData.category) newErrors.category = '카테고리를 선택해주세요';
        if (!foodData.location) newErrors.location = '지역을 선택해주세요';
        if (!foodData.locationDetails.lat) newErrors.locationDetails = '위치을 선택해주세요';
        // if (foodData.imageUrls.length === 0 && !foodData.images) newErrors.images = '최소 하나의 이미지를 등록해주세요';
        if (allImages.length === 0 ) newErrors.images = '최소 하나의 이미지를 등록해주세요';

        // allImages.forEach((image, index) => {
        //     console.log('image.file:', image.file, 'image.url:', image.url);
        //     console.log('image.file === null:', image.file === null);
        //     console.log('image.url === null:', image.url === null);
        //     if (image.file === null && image.url === null) newErrors.images = '이미지를 추가해주세요';
        // })
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    }


    useEffect(() => {
        console.log('Redux로부터 업데이트된 food data:', foodData);
    }, [foodData]); // foodData가 변경될 때마다 실행
    useEffect(() => {
        console.log('뭐가 들어있을까용?', allImages);
    }, [allImages]); // foodData가 변경될 때마다 실행


    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validateForm()) {

            if (errors.location) {
                alert(errors.location);
            }
            if (errors.locationDetails) {
                alert(errors.locationDetails);
            }
            if (errors.images) {
                alert(errors.images);
            }
            return;
        }

        const formData = new FormData();

        formData.append('title', foodData.title);
        formData.append('description', foodData.description);
        formData.append('makeByDate', format(foodData.makeByDate, 'yyyy-MM-dd'));
        formData.append('eatByDate', format(foodData.eatByDate, 'yyyy-MM-dd'));
        formData.append('category', foodData.category);
        formData.append('location', foodData.location);
        formData.append('latitude', foodData.locationDetails.lat);
        formData.append('longitude', foodData.locationDetails.lng);
        // // 이미지 파일 체크 및 추가
        // foodData.images.forEach((image, index) => {
        //     if (image.file) { // 파일이 실제로 존재하는지 확인
        //         formData.append(`images[${index}]`, image.file);
        //         console.log(`Image added to formData: images[${index}]`, image.file);
        //     }
        // });
        // 새 이미지 파일 데이터 처리
        foodData.images.forEach((image, index) => {
            if (image.file) {
                formData.append(`images[${index}]`, image.file);
            }
        });
        // 기존 이미지 URL 데이터 처리
        foodData.imageUrls.forEach((url, index) => {
            formData.append(`imageUrls[${index}]`, url);
        });


        console.log("FormData 내용 검사:");
        for (let [key, value] of formData.entries()) {
            console.log(`${key}:`, value);
        }



        const method = foodData.foodId ? 'PUT' : 'POST';
        const url = foodData.foodId ? `/api/foods/${foodData.foodId}` : '/api/foods';
        const token = localStorage.getItem('jwt');

        try {
            const response = await axios({
                method: method,
                url: url,
                data: formData,
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            console.log('서버 응답', response.data);
            navigate('/main');
        } catch (error) {
            console.error('Error submitting food data:', error);
            alert('제출 중 오류가 발생했습니다: ' + (error.response?.data?.message || error.message));
        }
    };

    return (
        <div className="form_wrap">
            <form onSubmit={handleSubmit}>
                <div className="form_group">
                    <div className={"image_wrap"}>
                        <label htmlFor="image">
                            <span className="camera_icon">
                                <FontAwesomeIcon icon={faCamera}/>
                            </span>
                            <span className={'txt'}>이미지 ({allImages.length}/8)</span>
                            <input
                                className="image_input"
                                type="file"
                                id="image"
                                onChange={handleImageChange}
                                multiple
                            />
                        </label>
                    </div>
                    <Swiper
                        ref={swiperRef}
                        className="preview_swiper"
                        slidesPerView={'auto'}
                        spaceBetween={10}
                        centeredSlides={false}
                    >
                        {allImages && allImages.map((image, index) => (
                            <SwiperSlide key={index}>
                                <div>
                                    <img src={image.url} alt={`Preview ${index}`}/>
                                    <button className={'close'} type="button" onClick={() => handleRemoveImage(index)}>
                                        <FontAwesomeIcon icon={faCircleXmark}/>
                                    </button>
                                </div>
                            </SwiperSlide>
                        ))}
                    </Swiper>
                </div>

                <div className="form_group dates_wrap">
                    <div className={'date'}>
                        <p>제조일</p>
                        <DatePicker
                            className={'date_picker'}
                            caretAs={FaCalendarDay}
                            oneTap
                            name="makeByDate"
                            id="makeByDate"
                            // value={food.makeByDate ? new Date(food.makeByDate) : null}
                            // value={new Date(foodData.makeByDate)}
                            value={foodData.makeByDate ? new Date(foodData.makeByDate) : null}
                            onChange={value => handleDateChange('makeByDate', value)}
                        />
                        {errors.makeByDate && <div className="error">{errors.makeByDate}</div>}
                    </div>

                    <div className={'date'}>
                        <p>소비기한</p>
                        <DatePicker
                            className={'date_picker'}
                            caretAs={FaCalendarDay}
                            oneTap
                            name="eatByDate"
                            id="eatByDate"
                            value={foodData.eatByDate ? new Date(foodData.eatByDate) : null}
                            // value={new Date(foodData.eatByDate)}

                            onChange={value => handleDateChange('eatByDate', value)}
                        />
                        {errors.eatByDate && <div className="error">{errors.eatByDate}</div>}
                    </div>
                </div>

                <div className="form_group">
                    <label htmlFor="title" className={'a11y-hidden'}>제목</label>
                    <input
                        className={'title'}
                        name="title"
                        id="title"
                        value={foodData.title || ''}
                        onChange={handleChange}
                        placeholder="글 제목"
                    />
                    {errors.title && <div className="error">{errors.title}</div>}
                </div>
                <div className="form_group">
                    <label htmlFor="description" className={'a11y-hidden'}>내용</label>
                    <textarea
                        className={'description'}
                        name="description"
                        id="description"
                        value={foodData.description || ''}
                        onChange={handleChange}
                        placeholder="내용을 입력해주세요"
                    />
                    {errors.description && <div className="error">{errors.description}</div>}
                </div>

                <div className="form_group">
                    <Select
                        name="category"
                        id="category"
                        value={foodData.category || ''}
                        debounce={300}
                        onChange={(value) => {
                            handleChange({
                                target: {
                                    name: "category",
                                    value: value
                                }
                            });
                        }}
                    />
                </div>
                <div className={'form_group'}>
                    <Drawers
                        trigger={
                            <div className={'location_wrap'}>
                                <input
                                    className={'location'}
                                    type="text"
                                    id={'location'}
                                    name={'location'}
                                    // value={foodData.location}
                                    value={foodData.location || ''}
                                    placeholder="원하는 장소를 입력해주세요"
                                    readOnly
                                />
                                <FontAwesomeIcon className={'arrow'} icon={faAngleRight}/>
                            </div>

                        }
                        drawerContent={<PlaceSearch onLocationSelect={handleLocationSelect}/>}
                        onLocationSelect={handleLocationSelect}
                        isPlaceDrawer={true}
                    />

                </div>

                <div className="form_group btn_wrap">
                    <SquareButton className={'submit_button'} name={'등록하기'}/>
                </div>
            </form>
        </div>
    );
};
export default MainForm;