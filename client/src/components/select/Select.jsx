import { InputPicker } from 'rsuite';
import './Select.scss';

const data = ['한식', '중식', '양식', '일식'].map(
    item => ({ label: item, value: item })
);

const Select = ({ name, id, value, onChange }) => (
    <>
        <InputPicker
            name={name}
            id={id}
            data={data}
            value={value}
            onChange={onChange}
            block
            placement={"topStart"}
        />
    </>
);
export default Select;
