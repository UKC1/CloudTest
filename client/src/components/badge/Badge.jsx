import './Badge.scss';
export default function Badge({food}) {
    const badgeClass = food.status === '나눔중' ? 'badge_done' : '';
    return (
        <p className={`badge ${badgeClass}`}>{food.status}</p>
    );
}
