import Address from "../../../components/address/Address";
import {useState} from "react";
import {useDispatch} from "react-redux";
import {setAddress} from "../../../redux/addressSlice";
import {useNavigate} from "react-router-dom";
import './SearchAddress.scss';

export default function SearchAddress() {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [addressLocal, setAddressLocal] = useState('');
    const handleLocationSelect = (selectAddress) => {
        console.log('Selected address:', selectAddress); // 값 확인
        console.log('Action object:', setAddress(selectAddress)); // 액션 객체 확인
        setAddressLocal(selectAddress);
        dispatch(setAddress(selectAddress));
        navigate('/main');
    }
    return (
        <div className={'search_address_wrap'}>
            <Address onLocationSelect={handleLocationSelect}/>
        </div>
    )
}