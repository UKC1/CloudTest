import {NavLink, useNavigate} from "react-router-dom";
import './Menu.scss';
import {useDispatch, useSelector} from "react-redux";
import {clearFood} from "../../redux/foodSlice";
import axios from "axios";

export default function Menu() {
    const food = useSelector((state) => state.food.value);
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const handleClearFood = async () => {
        // if (window.confirm("이 항목을 정말 삭제하시겠습니까?")) {
            try {
                await axios.delete(`/api/foods/${food.foodId}`);
                dispatch(clearFood());
                navigate('/main');
            } catch (error) {
                console.error('Error deleting the food:', error);
                alert("삭제 중 오류가 발생했습니다.");
            }
        // }
    };
    return(
        <div className={'menu'}>
            <ul>
                <li className={'menu_list'}>
                    <NavLink
                        to={{
                            pathname: `/main/update/${food.foodId}`,
                            state: {food}
                        }}
                    >
                        수정
                    </NavLink>
                </li>
                <li className={'menu_list'}>
                    <NavLink
                        onClick={handleClearFood}
                    >
                        삭제
                    </NavLink>
                </li>
            </ul>
        </div>
    )
}