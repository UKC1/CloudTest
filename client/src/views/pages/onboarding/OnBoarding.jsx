import './OnBoarding.scss';
import {Swiper, SwiperSlide} from "swiper/react";
import 'swiper/css';
import 'swiper/css/pagination';
import {Pagination} from "swiper/modules";
import {useRef} from "react";
import {NavLink} from "react-router-dom";
import {SquareButton} from "../../../components/button/Button";

export default function OnBoarding() {
    const swiperRef = useRef(null);
    const skipToLastSlide = () => {
        if (swiperRef.current) {
            swiperRef.current.slideTo(swiperRef.current.slides.length - 1);
        }
    };
    return (
        <div className={'onBoarding'} >
            <Swiper className={'swiper'}
                    modules={[ Pagination]}
                    spaceBetween={50}
                    navigation
                    pagination={{ clickable: true }}
                    onSwiper={(swiper) => { swiperRef.current = swiper; }}
                    breakpoints={{
                        480: {
                            slidesPerView: 1,
                        },
                    }}
            >
                <SwiperSlide className={'swiper_slide'}>
                    <header>
                        <div>
                            <button onClick={skipToLastSlide}>건너뛰기</button>
                        </div>
                    </header>
                    <div className={'img_box'}>
                        <img src={'../../img/onboarding01.svg'} alt="onboarding image 1"/>
                    </div>
                    <div className={'desc'}>
                        <h2>혼자 감당하기에는 너무 많은 음식</h2>
                        <div className={'txt'}>
                            <p>마트에서 이것 저것 많이 사면</p>
                            <p>처치 곤란인 적이 많지 않은가요?</p>
                        </div>
                    </div>
                </SwiperSlide>
                <SwiperSlide className={'swiper_slide'}>
                    <header>
                        <div>
                            <button onClick={skipToLastSlide}>건너뛰기</button>
                        </div>
                    </header>
                    <div className={'img_box'}>
                        <img src={'../../img/onboarding02.svg'} alt="onboarding image 2"/>
                    </div>
                    <div className={'desc'}>
                        <h2>1인 가구들의 고충을 해결</h2>
                        <div className={'txt'}>
                            <p>대량으로 요리해서 매일 같은 음식을</p>
                            <p>먹고 있지는 않은가요?</p>
                        </div>
                    </div>
                </SwiperSlide>
                <SwiperSlide className={'swiper_slide'}>
                    <div className={'img_box'}>
                        <img src={'../../img/onboarding03.svg'} alt="onboarding image 3"/>
                    </div>
                    <div className={'desc'}>
                        <h2>사람들과 음식을 나누어 보세요</h2>
                        <div className={'txt'}>
                            <p>음식 물물교환 시스템</p>
                            <p>대량의 양의 음식 나눔 가능</p>
                        </div>
                    </div>
                    <div className={'btn_wrap'}>
                        <NavLink to={'/login'}>
                            <SquareButton name={'시작하기'}/>
                        </NavLink>
                    </div>
                </SwiperSlide>
            </Swiper>
        </div>
    )
}
