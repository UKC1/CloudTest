import { Drawer } from 'rsuite';
import {useState} from "react";
import Address from "../address/Address";
const AddressDrawer = ({title, placeholder}) => {
    const [open, setOpen] = useState(false);
    const [placement, setPlacement] = useState('bottom');
    const [address, setAddress] = useState('');

    const handleOpen = () => {
        document.body.classList.add('modal_open');
        setOpen(true);
    }
    const handleClose = () => {
        document.body.classList.remove('modal_open');
        setOpen(false);
    }

    const handleLocationSelect = (selectAddress) => {
        setAddress(selectAddress);
        handleClose();
    }
    return (
        <div>
            <input
                type="text"
                placeholder={placeholder}
                value={address}
                onClick={handleOpen}
                readOnly
            />

            <Drawer placement={placement} open={open} onClose={() => setOpen(false)}>
                <Drawer.Header>
                    <Drawer.Title>{title}</Drawer.Title>
                </Drawer.Header>
                <Drawer.Body>
                    <Address onLocationSelect={handleLocationSelect}/>
                </Drawer.Body>
            </Drawer>
        </div>
    );
};

export default AddressDrawer;

