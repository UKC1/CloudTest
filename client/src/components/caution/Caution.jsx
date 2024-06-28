export default function Caution({items}) {
    return(
        <div className={'caution'}>
            <ul>
                {items.map((items, index) => (
                    <li key={index}>
                        <strong>{items.title}</strong> {items.description}
                    </li>
                ))}
            </ul>
        </div>
    )
}