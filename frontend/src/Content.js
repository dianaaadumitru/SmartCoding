
import ItemList from './ItemList';



const Content = ({ items, handleCheck, handleDelete }) => {



    return (
        <main>
            {items.length ? (
                <ItemList
                    items={items}
                    handleCheck={handleCheck}
                    handleDelete={handleDelete}
                />
            ) : (
                <p>empty list</p>
            )
            }
        </main>
    )
}

export default Content;