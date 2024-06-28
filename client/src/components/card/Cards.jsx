import {Box, Card, Flex, Text} from '@radix-ui/themes';
import './Cards.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHeart} from "@fortawesome/free-regular-svg-icons";
import { faHeart as faSolidHeart } from "@fortawesome/free-solid-svg-icons";
import Badge from "../badge/Badge";
import { formatDistanceToNow, parseISO } from 'date-fns';
import { ko } from 'date-fns/locale';
import {useState} from "react";

export default function Cards({item}) {

    const {imageUrls, title, createdAt, description, category, likes, price, maxTO} = item;
    const [isFavorited, setIsFavorited] = useState(item.isFavorite);
    function formatTimeAgo(dateStr) {
        // console.log('Date String:', dateStr); // Check what you actually receive as input

        if (!dateStr) {
            console.error('Invalid or undefined date string');
            return 'Date unavailable'; // Or some default/fallback value
        }
        const date = parseISO(dateStr);  // 서버에서 'yyyy-MM-dd' 형식의 문자열로 받은 날짜를 Date 객체로 변환
        return formatDistanceToNow(date, { addSuffix: true, locale: ko });  // 현재 시간과의 차이를 자연스럽게 표현
    }
    return(
        <Flex className={'card_wrap'} gap="3" direction="column">
            <Box className={'card_box'}>
                <Card className={'card'} size="1">
                    <Flex className={'card_layout'} gap="3" align="center">
                        <div className={'img_wrap'}>
                            <img
                                src={imageUrls?.[0] || './../img/cookshare.svg'}
                                alt={title}
                            />
                        </div>
                        <Box className={'content'}>
                            <div className={'text_wrap'}>
                                <div className={'title'}>
                                    <h4>{title}</h4>
                                    <p className={'date'}>
                                        {createdAt && <span className={'date'}>{formatTimeAgo(createdAt)}</span>}
                                    </p>
                                </div>
                                <div className={'description'}>
                                    <p>{description}</p>
                                    <p>
                                        <span>#{category}</span>
                                    </p>
                                </div>
                            </div>
                            <div className={'icon_wrap'}>
                                <Badge food={item}/>
                                <p className={'like'}>
                                    <FontAwesomeIcon icon={isFavorited ? faSolidHeart : faHeart} color={isFavorited ? 'red' : 'grey'}/>
                                    <span>{likes}</span>
                                </p>
                            </div>
                        </Box>
                    </Flex>
                </Card>
            </Box>
        </Flex>
    )
}