import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { BrowserRouter } from 'react-router-dom';
import {Provider} from "react-redux";
import store from "./redux/store";
import axios from "axios";
const rootElement = document.getElementById('root');
const root = ReactDOM.createRoot(rootElement);

// Axios 인터셉터 설정
axios.interceptors.request.use(
    config => {
        // 회원가입과 휴대폰 인증 요청에서는 Authorization 헤더를 추가하지 않음
        const noAuthRequired = ['/api/user/login', '/api/user/register', 'api/user/memberPhoneCheck', '/api/user/checkNickName'];

        if (noAuthRequired.some(path => config.url.endsWith(path))) {
            return config;
        }

        const token = localStorage.getItem('jwt');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);


root.render(
    <React.StrictMode>
        <Provider store={store}>
            <BrowserRouter>
                <App />
            </BrowserRouter>
        </Provider>
    </React.StrictMode>
);
