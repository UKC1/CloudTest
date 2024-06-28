import { createSlice } from '@reduxjs/toolkit';

const initialState = {
    imageFiles: [],    // 여기에는 File 객체의 메타데이터만 저장
    imageUrls: []      // 업로드된 파일의 URL을 저장
};

export const imageSlice = createSlice({
    name: 'images',
    initialState,
    reducers: {
        addImage: (state, action) => {
            const { file } = action.payload;
            const imageMetaData = {
                id: file.id,
                url: file.url,
                type: file.type,
                lastModified: file.lastModified
            };
            const imageUrl = URL.createObjectURL(file);
            state.imageFiles.push(imageMetaData);  // 메타데이터 저장
            state.imageUrls.push(imageUrl);        // URL 저장
        },
        removeImage: (state, action) => {
            state.imageFiles = state.imageFiles.filter((_, index) => index !== action.payload);
            state.imageUrls = state.imageUrls.filter((_, index) => index !== action.payload);
        },
        clearImages: (state) => {
            state.imageFiles = [];
            state.imageUrls = [];
        }
    }
});

export const { addImage, removeImage, clearImages } = imageSlice.actions;
export default imageSlice.reducer;
