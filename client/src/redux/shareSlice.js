import {createSlice} from "@reduxjs/toolkit";

export const shareSlice = createSlice({
    name: 'share',
    initialState: {
        value: {
            title: '나는 제목이야',
            description: '나는 내용이다',
            location: '',
            locationDetails: {
                name: '',
                lat: 37.5665,
                lng: 126.9780
            },
            createdAt: '',
            price: '백마원',
            maxTO: '10명',
            giver: {
                nickName: ''
            }
        }
    },
    reducers: {
        setShare: (state, action) => {
            state.value = action.payload;
        }
    }
})
export const {setShare} = shareSlice.actions

export default shareSlice.reducer