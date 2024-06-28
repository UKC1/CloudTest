import './Main.scss';
import Cards from "./../../../../components/card/Cards";
import {NavLink} from "react-router-dom";
import axios from "axios";
import React, {useEffect, useRef, useState} from "react";
import {Tabs} from "rsuite";
import {AddButton} from "./../../../../components/button/Button";

export default function Main() {
    const [foodData, setFoodData] = useState([]);
    const [foodHasMore, setFoodHasMore] = useState(true);
    const [foodPage, setFoodPage] = useState(0); // 현재 페이지 번호를 상태로 관리

    const [myTownData, setMyTownData] = useState([]);
    const [myTownHasMore, setMyTownHasMore] = useState(true);
    const [myTownPage, setMyTownPage] = useState(0); // 현재 페이지 번호를 상태로 관리

    const [loading, setLoading] = useState(false);
    const [panel, setPanel] = useState([
        { key: '1', title: '나눔거래' },
        { key: '2', title: '나의동네' }
    ]);
    const initialLoadComplete = useRef(false);

    const [isTop, setIsTop] = useState(false);

    useEffect(() => {
        if (!initialLoadComplete.current && !loading) {
            fetchData('/api/foods', foodData, setFoodData, foodHasMore, setFoodHasMore, foodPage, setFoodPage);
            fetchData('/api/myTowns', myTownData, setMyTownData, myTownHasMore, setMyTownHasMore, myTownPage, setMyTownPage);
            initialLoadComplete.current = true; // 초기 로드 완료 표시
        }
    }, []);

    const fetchData = async (url, data, setData, hasMore, setHasMore, page, setPage ) => {
        if(!hasMore || loading) return;
        console.log('데이터 요청 시작...');

        setLoading(true);
        try{
            const response = await axios.get(url, {
                params: { page, size: 5 }
            });
            console.log('받아온 데이터 로그에 출력:', response.data.content);
            if(response.data.content.length === 0){
                setHasMore(false);
            }else{
                setData(prev => [...prev, ...response.data.content]);
                setPage(prevPage => prevPage + 1); // 다음 페이지 번호로 업데이트
            }
        }catch (error){
            console.log('Falied to fetch data:', error);
        }finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        const tabsContent = document.querySelector('.rs-tabs-content'); // CSS 선택자로 접근
        if(tabsContent) {

            setIsTop(true);

            console.log('스크롤 이벤트 리스너 등록');
            const checkScroll = () => {
                const isAtTop = tabsContent.scrollTop === 0;
                setIsTop(isAtTop);
                console.log("top", tabsContent.scrollTop);
            }

            tabsContent.addEventListener('scroll', checkScroll);

            return () => {
                console.log("스크롤 이벤트 리스너 해제");
                tabsContent.removeEventListener('scroll', checkScroll);
            }
        }
    }, []);

    return (
        <section className={'main'} >
            <Tabs className={'main_tab'} defaultActiveKey="1" appearance="subtle">
                {panel.map((tab) => (
                    <Tabs.Tab className={'tab'} eventKey={tab.key} title={tab.title} key={tab.key}>
                        <div className={'tab_content'}>
                            {loading && <div>Loading...</div>}
                            {tab.key === '1' ?
                                foodData.map((food, index) => (
                                    <NavLink to={`foods/${food.foodId}`} key={`${food.foodId}-${index}`}>
                                        <Cards item={food}/>
                                    </NavLink>
                                )) : myTownData.map((myTown, index) => (
                                    <NavLink to={`myTowns/${myTown.myTownId}`} key={`${myTown.myTownId}-${index}`}>
                                        <Cards item={myTown}/>
                                    </NavLink>
                                ))}
                            {!foodHasMore && !myTownHasMore && <div>No more data available.</div>}
                            {tab.key === '1' && foodHasMore && !loading && (
                                <button className={'more'} onClick={() => fetchData('/api/foods', foodData, setFoodData, foodHasMore, setFoodHasMore, foodPage, setFoodPage)}>더보기</button>
                            )}
                            {tab.key === '2' && myTownHasMore && !loading && (
                                <button className={'more'} onClick={() => fetchData('/api/myTowns', myTownData, setMyTownData, myTownHasMore, setMyTownHasMore, myTownPage, setMyTownPage)}>더보기</button>
                            )}
                        </div>
                    </Tabs.Tab>
                ))}
            </Tabs>
            <NavLink to={'/main/add'}>
                <AddButton className={isTop ? 'height' : ''}/>
            </NavLink>
        </section>
    );
}