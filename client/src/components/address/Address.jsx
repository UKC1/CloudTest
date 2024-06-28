import React, { useEffect, useRef, useState } from 'react';
import useKakaoLoader from "./useKakaoLoader"

const Address = ({ onLocationSelect }) => {
    const [address, setAddress] = useState('');
    // const kakaoKey = process.env.REACT_APP_KAKAO_KEY;
    const postcodeWrapper  = useRef(null);
    useKakaoLoader()

    useEffect(() => {
        if(!window.daum || !window.daum.Postcode) {
            const script = document.createElement('script');
            script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
            document.head.appendChild(script);
            script.onload = () => {
                console.log('Daum Map scripts loaded');
                initializePostcode();
            };
            script.onerror = error => {
                console.error('Failed to load Daum Map scripts', error);
            };
        } else {
            initializePostcode();
        }
    }, []);
    const initializePostcode = () => {
        if(!postcodeWrapper.current.hasChildNodes()){
            new window.daum.Postcode({
                oncomplete: function(data) {
                    const addr = data.address;
                    setAddress(addr); //주소 상태 업데이트 하는 부분
                    onLocationSelect(addr); // Register 컴포넌트로 주소 데이터 전달
                },
                width: '100%',
                height: '100%'
            }).embed(postcodeWrapper.current);
        }
    };

    return (
        <div>
            <div ref={postcodeWrapper} style={{ height: '500px' }}></div>
        </div>
    );
};

export default Address;