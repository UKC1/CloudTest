import { Drawer } from 'rsuite';
import {cloneElement, useState} from "react";
import './Drawers.scss';

const Drawers = ({title, trigger, drawerContent, onLocationSelect, isPlaceDrawer}) => {
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
    const triggerElement = cloneElement(trigger, { onClick: handleOpen });
    const drawerContentWithProps = cloneElement(drawerContent, {
        onLocationSelect: (selectedAddress) => {
            onLocationSelect(selectedAddress);
            handleClose();
        }
    });

    const drawerClassName = isPlaceDrawer ? 'drawer place-drawer' : 'drawer';

    return (
        <div>
            {triggerElement}

            <Drawer className={drawerClassName} placement={placement} open={open} onClose={() => setOpen(false)}>
                <Drawer.Header>
                    <Drawer.Title>{title}</Drawer.Title>
                </Drawer.Header>
                <Drawer.Body>
                    {drawerContentWithProps}
                </Drawer.Body>
            </Drawer>
        </div>
    );
};

export default Drawers;

