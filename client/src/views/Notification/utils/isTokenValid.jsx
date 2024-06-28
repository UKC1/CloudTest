import {jwtDecode} from "jwt-decode";


export const isTokenValid = () => {
    const token = localStorage.getItem('jwt');
    if (!token) {
        return false;
    }

    try {
        jwtDecode(token);
        return true;
    } catch (e) {
        console.error("토큰 디코딩 실패:", e.message);
        return false;
    }
};