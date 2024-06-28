import { createSlice } from '@reduxjs/toolkit';

export const foodSlice = createSlice({
    name: 'food',
    initialState: {
        foods: [],
        value: {
            title: '',
            description: '',
            location: '',
            locationDetails: {
                name: '',
                lat: 37.5665,
                lng: 126.9780
            },
            makeByDate: '',
            eatByDate: '',
            createdAt: '',
            category: '한식',
            images: [],
            imageUrls: [],  // URL 목록 추가
            giver: {
                nickName: '',
            }
        }
    },
    reducers: {
        setFood: (state, action) => {
            state.value = action.payload;
        },
        setLocationDetails: (state, action) => {
            state.value.locationDetails = action.payload;
        },
        addImage: (state, action) => {
            state.value.images.push({
                file: action.payload.file,
                url: action.payload.url || URL.createObjectURL(action.payload.file),
                isNew: true  // 새 이미지 표시
            });
        },
        removeImage: (state, action) => {
            state.value.images = state.value.images.filter((_, index) => index !== action.payload);
        },
        clearFood: (state) => {
            state.value = {
                title: '',
                description: '',
                location: '',
                locationDetails: {
                    name: '',
                    lat: 37.5665,
                    lng: 126.9780
                },
                makeByDate: '',
                eatByDate: '',
                createdAt: '',
                category: '',
                images: [],
                imageUrls: [],  // 초기화
                giver:{
                    nickName: '',
                }
            };
        },
        setImageUrls: (state, action) => {
            state.value.imageUrls = action.payload;
        },
        addImageUrl: (state, action) => {
            state.value.imageUrls.push(action.payload);
        },
        removeImageUrl: (state, action) => {
            state.value.imageUrls = state.value.imageUrls.filter((url, index) => index !== action.payload);
        },
    },
});

export const { setFood, clearFood, addImage, removeImage, setLocationDetails, setImageUrls, addImageUrl, removeImageUrl } = foodSlice.actions;

export default foodSlice.reducer;