import {configureStore} from "@reduxjs/toolkit";
import addressReducer from "./addressSlice";
import foodReducer from "./foodSlice";
import imageReducer from "./imageSlice";
import shareReducer from "./shareSlice";
export const store = configureStore({
    reducer: {
        address: addressReducer,
        food: foodReducer,
        image: imageReducer,
        share: shareReducer
    },
    // middleware: (getDefaultMiddleware) =>
    //     getDefaultMiddleware({
    //         serializableCheck: {
    //             ignoredActions: ['food/addImage'], // 직렬화 검사에서 무시할 액션
    //             ignoredPaths: ['food.value.images'], // 직렬화 검사에서 무시할 상태 경로
    //         },
    //     }),
})
export default store;


