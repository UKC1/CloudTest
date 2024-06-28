import './Search.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faAngleLeft} from "@fortawesome/free-solid-svg-icons";
import {NavLink, useNavigate} from "react-router-dom";
import {faCircleXmark} from "@fortawesome/free-solid-svg-icons/faCircleXmark";
import {useState} from "react";
import axios from "axios";
import Cards from "../../../components/card/Cards";
import {useSelector} from "react-redux";

export default function Search() {
    const navigate = useNavigate();
    const [inputValue, setInputValue] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const foodData = useSelector((state) => state.food.value);
    const handleClear = () => {
        setInputValue('');
        setSearchResults([]); // 검색 결과도 초기화
    }
    const handleInput = (event) => {
        setInputValue(event.target.value);
    }

    // 검색을 실행하는 함수
    const handleSearch = async () => {
        if (!inputValue) return; // 입력 값이 없다면 검색하지 않음
        try {
            const response = await axios.get(`/api/foods/search/by-category?query=${encodeURIComponent(inputValue)}`);
            setSearchResults(response.data); // 검색 결과를 상태에 저장
        } catch (error) {
            console.error('Search failed:', error);
            setSearchResults([]); // 에러 발생 시 검색 결과 초기화
        }
    }

    return (
       <div className={'search_wrap'}>
        <div className={'search_address'}>
            <div className={'prev'}>
                <button onClick={() => navigate(-1)}>
                    <span><FontAwesomeIcon icon={faAngleLeft}/></span>
                </button>
            </div>
            <div className={'search'}>
                <input
                    type={'text'}
                    value={inputValue}
                    placeholder={'검색어를 입력해주세요'}
                    onChange={handleInput}
                    onKeyPress={event => event.key === 'Enter' && handleSearch()} // Enter 키를 누르면 검색 실행
                />
                {inputValue && (
                    <button onClick={handleClear}>
                        <FontAwesomeIcon icon={faCircleXmark}/>
                    </button>
                )}
            </div>
        </div>
        {/* 검색 결과를 리스트 형태로 표시 (간단한 예시) */}
        <div className="search-results">
            {searchResults.map(food => (
                <NavLink to={`/main/foods/${food.foodId}`} key={`${food.foodId}`}>
                    <Cards item={food}/>
                </NavLink>
            ))}
        </div>
    </div>
)
    ;
}