import {createSlice} from "@reduxjs/toolkit";

export const addressSlice = createSlice({
    name: 'address',
    initialState: {
        currentAddress: '가산동',
    },
    reducers:{
        setAddress: (state, action) => {
            state.currentAddress = action.payload;
        }
    }
})
export const {setAddress} = addressSlice.actions;
export default addressSlice.reducer;
