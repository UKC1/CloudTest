import './Register.scss'
import React, {useEffect, useRef, useState} from "react";
import {useDebounce} from "../../../hook/useDebounce";
import axios from "axios";
import AddressDrawer from "../../../components/drawer/AddressDrawer";
import {useNavigate} from "react-router-dom";
export default function Register() {
    const initVal = useRef({
        nickname: '',
        password: '',
        passwordConfirm: '',
        location: '',
        mobileNumber: '',
        authNumber: ''
    });

    const [Val, setVal] = useState(initVal.current);
    const [nicknameValid, setNicknameValid] = useState(false);
    const [nicknameMessage, setNicknameMessage] = useState("");
    const [Error, setError] = useState({});
    const navigate = useNavigate();
    const [serverAuthNumber, setServerAuthNumber] = useState("");
    const [verificationStatus, setVerificationStatus] = useState(false);
    const [authButtonLabel, setAuthButtonLabel] = useState('인증번호 전송');
    const [authClassName, setAuthClassName] = useState('ghost_btn');
    const [BtnDisabled, setBtnDisabled] = useState(true);
    const [countdown, setCountdown] = useState(null);
    const [Feedback, setFeedback] = useState(Val);
    const DebounceVal = useDebounce(Val);
    const timerRef = useRef();

    useEffect(() => {
        if (Val.nickname.length > 0 && Val.nickname.length < 2) {
            setNicknameMessage("2글자 이상 입력하세요");
        } else if (Val.nickname.length >= 2 && !nicknameValid) {
            setNicknameMessage("닉네임 중복 검사하세요");
        } else if (nicknameValid) {
            setNicknameMessage("유효한 닉네임입니다");
        } else {
            setNicknameMessage("");
        }
    }, [Val.nickname, nicknameValid]);

    useEffect(() => {
        const validation = validateFields(DebounceVal);
        setFeedback(validation);
        setBtnDisabled(!isFormValid());
    }, [DebounceVal, verificationStatus]);


    const handleChange = (event) => {
        const { name, value } = event.target;

        if (name === 'nickname') {
            setNicknameValid(false);
        }

        if (name === 'mobileNumber' || name === 'authNumber') {
            if (!value.match(/^\d*$/)) {
                event.preventDefault();
                return;
            }
        }

        setVal(prevVal => ({
            ...prevVal,
            [name]: value
        }));

        setError(prevErrors => ({
            ...prevErrors,
            [name]: check({ ...Val, [name]: value })[name]
        }));
    };

    const validateFields = (values) => {
        let feedbacks = {};
        let passwordErrors = check(values);

        // feedbacks.nickname = {
        //     valid: values.nickname.length >= 2 && nicknameValid,
        //     message: values.nickname.length >= 2 || nicknameValid ? "사용가능한 닉네임 입니다." : "닉네임 인증해주세요"
        // };
        feedbacks.password = {
            valid: !passwordErrors.password,
            message: passwordErrors.password || "비밀번호가 유효합니다."
        };
        feedbacks.passwordConfirm = {
            valid: values.password === values.passwordConfirm,
            message: values.password === values.passwordConfirm ? "비밀번호가 일치합니다." : "비밀번호가 일치하지 않습니다."
        };
        feedbacks.mobileNumber = {
            valid: values.mobileNumber.match(/^\d{10,11}$/),
            message: values.mobileNumber.match(/^\d{10,11}$/) ? "전화번호가 유효합니다." : "전화번호 형식이 올바르지 않습니다."
        };
        return feedbacks;
    };
    const handleLocationSelect = (selectedLocation) => {
        setVal(prevVal => ({
            ...prevVal,
            location: selectedLocation
        }));
    }
    // 중복 닉네임 체크
    const handleCheckNickName = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.get('/api/user/checkNickName', {params: {nickname: Val.nickname}});
            if (response.data.isUnique) {
                alert('사용 가능한 닉네임입니다');
                setNicknameValid(true);
            } else {
                alert('이미 사용중인 닉네임입니다');
                setNicknameValid(false);
            }

        } catch (error) {
            alert('서버와 연결이 안됐습니다.');
        }
    }

    // 휴대폰 번호 인증 요청
    const handleSendAuthNumber = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post('/api/user/memberPhoneCheck', {mobileNumber: Val.mobileNumber});
            const checkNum = response.data.checkNum;
            if (checkNum !== undefined) {
                alert('6자리 인증번호를 발송했습니다');
                setServerAuthNumber(checkNum);
                setAuthButtonLabel('인증번호 재전송');
                setAuthClassName('text_btn');
                startTimer();
            } else {
                alert('서버 응답에 인증번호가 없습니다');
            }
        } catch (error) {
            console.log('Auth Number Send Failed', error);
        }
    }

    const startTimer = () => {
        setCountdown(180);
        clearInterval(timerRef.current);
        timerRef.current = setInterval(() => {
            setCountdown(prevCount => {
                if (prevCount <= 1) {
                    clearInterval(timerRef.current);
                    setAuthButtonLabel('인증번호 전송');
                    setAuthClassName('ghost_btn');
                    return null;
                }
                return prevCount - 1;
            });
        }, 1000);
    };

    useEffect(() => {
        if (countdown === 0) {
            clearInterval(timerRef.current);
            setAuthButtonLabel('인증번호 전송');

        }
    }, [countdown]);

    useEffect(() => {
        return () => clearInterval(timerRef.current);
    }, []);


    const formatTime = (time) => {
        const minutes = Math.floor(time / 60);
        const seconds = time % 60;
        return `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
    };
    // 인증번호 확인
    const checkVerificationCode = () => {
        if (serverAuthNumber === Val.authNumber) {
            alert('인증 성공하였습니다.');
            setVerificationStatus(true);
        } else {
            alert('인증 실패하였습니다. 다시 입력해주세요.');
            setVerificationStatus(false);
        }
    }
    // 회원가입 인증요청
    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post('/api/user/register', Val);
            if (response.status === 200) {
                alert('회원가입 성공');
                navigate('/login');
            } else {
                alert('회원가입 실패');
            }
        } catch (error) {
            console.log('Register Failed', error);
        }
    }
    const check = (value) => {
        const txt = /[a-zA-Z0-9가-힇]/g;
        const num = /[0-9]/;
        const spc = /[!@#$%^&*()_+]/;
        const error = {};

        //text 인증로직
        // if (value.nickname.length < 2) {
        //     error.nickname = '최소 2글자 이상 입력하세요';
        // }
        // 비밀번호 유효성 검사
        if (value.password.length < 8 || !txt.test(value.password) || !num.test(value.password) || !spc.test(value.password)) {
            error.password = '특수문자, 영문, 숫자 포함해서 8글자 이상 입력하세요';
        }
        //password 인증로직
        if (value.password !== value.passwordConfirm) {
            error.passwordConfirm = '비밀번호가 같지 않습니다'
        }
        return error;

    }
    const isFormValid = () => {
        return (
            Val.nickname.length >= 2 &&
            Val.password.length >= 8 &&
            Val.password === Val.passwordConfirm &&
            Val.mobileNumber.match(/^\d{10,11}$/) &&
            Val.authNumber.length === 6 &&
            verificationStatus
        );
    };
    useEffect(() => {
        setError(check(DebounceVal));
        setBtnDisabled(!isFormValid());
    }, [DebounceVal]);
    return (
        <form className={"register"} onSubmit={handleSubmit}>
            <div className={'field'}>
                <div className={'input_wrap'}>
                    <label htmlFor={"nickname"} className={"a11y-hidden"}>닉네임</label>
                    <input
                        type="text"
                        id={"nickname"}
                        name={"nickname"}
                        value={Val.nickname}
                        onChange={handleChange}
                        placeholder="닉네임을 입력하세요"
                    />
                    <button type="button" onClick={handleCheckNickName}>
                        <span>중복체크</span>
                    </button>
                </div>
                <div className={nicknameValid ? "success" : "error"}>
                    {nicknameMessage}
                </div>
            </div>

            <div className={'field'}>
                <label htmlFor={"password"} className={"a11y-hidden"}>비밀번호</label>
                <input
                    type="password"
                    id={"password"}
                    name={"password"}
                    value={Val.password}
                    onChange={handleChange}
                    placeholder="비밀번호를 입력하세요"
                />
                <div className={Feedback.password.valid ? "success" : "error"}>
                    {Val.password && Feedback.password.message}
                </div>
            </div>

            <div className={'field'}>
                <label htmlFor={"passwordCheck"} className={"a11y-hidden"}>비밀번호 확인</label>
                <input
                    type="password"
                    id={"passwordConfirm"}
                    name={"passwordConfirm"}
                    value={Val.passwordConfirm}
                    onChange={handleChange}
                    placeholder="비밀번호를 확인해주세요"
                />
                <div className={Feedback.passwordConfirm.valid ? "success" : "error"}>
                    {Val.passwordConfirm && Feedback.passwordConfirm.message}
                </div>
            </div>

            <div className={'field'}>
                <label htmlFor="location" className={"a11y-hidden"}>주소</label>
                <AddressDrawer
                    title="주소 검색"
                    placeholder="주소를 입력해주세요"
                    onLocationSelect={handleLocationSelect}
                />
            </div>

            <div className={'field'}>
                <div className={'input_wrap'}>
                    <label htmlFor="phoneNumber" className={"a11y-hidden"}>휴대폰 번호</label>
                    <input
                        type="text"
                        id="mobileNumber"
                        name="mobileNumber"
                        value={Val.mobileNumber}
                        maxLength={11}
                        onChange={handleChange}
                        placeholder="휴대폰 번호를 입력해주세요"
                    />
                    <button
                        type="button"
                        className={authClassName}
                        onClick={handleSendAuthNumber}
                    >
                        <span className={'resend'}>{authButtonLabel}</span>
                    </button>
                </div>
            </div>

            <div className={'field'}>
                <div className={'input_wrap'}>
                    <label htmlFor="authNumber" className={"a11y-hidden"}>인증번호</label>
                    <input
                        type="text"
                        id="authNumber"
                        name="authNumber"
                        value={Val.authNumber}
                        maxLength={6}
                        onChange={handleChange}
                        placeholder="인증번호 입력"
                    />
                    {countdown && <span className="timer">{formatTime(countdown)}</span>}
                    <button type="button" onClick={checkVerificationCode}>
                        <span>인증번호 확인</span>
                    </button>
                </div>
            </div>
            <div className={'field btn_wrap'}>
                <button disabled={BtnDisabled}>
                    <span>회원가입</span>
                </button>
            </div>
        </form>
    )

}